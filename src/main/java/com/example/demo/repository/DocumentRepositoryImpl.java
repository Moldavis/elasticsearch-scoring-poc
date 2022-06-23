package com.example.demo.repository;

import com.example.demo.document.Document;
import com.example.demo.repository.model.SearchPhrase;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class DocumentRepositoryImpl implements DocumentRepositoryCustom {

	private ReactiveElasticsearchOperations elasticsearchOperations;

	public DocumentRepositoryImpl(ReactiveElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Override
	public Flux<Document> findMostRelevantBySearchPhrase(SearchPhrase searchPhrase) {
		NativeSearchQueryBuilder queryBuilder = buildQuery(searchPhrase);
		return elasticsearchOperations.search(
						queryBuilder.build(),
						Document.class,
						IndexCoordinates.of("documents")
				).collectList()
				.flatMapMany(searchHits -> {
					double highestScore = searchHits.stream()
							.mapToDouble(SearchHit::getScore)
							.max()
							.orElse(-1);
					return Flux.fromIterable(searchHits)
							.filter(hit -> hit.getScore() == highestScore)
							.map(SearchHit::getContent);
				});
	}

	private NativeSearchQueryBuilder buildQuery(SearchPhrase searchPhrase) {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

		queryBuilder
				.withQuery(
						boolQuery()
								.must(termQuery(Document.Fields.firstField, searchPhrase.getFirstField()))
								.must(termQuery(Document.Fields.secondField, searchPhrase.getSecondField()))
								.must(termQuery(Document.Fields.thirdField, searchPhrase.getThirdField()))
				);

		return queryBuilder;
	}
}
