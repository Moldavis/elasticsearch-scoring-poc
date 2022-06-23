package com.example.demo;

import com.example.demo.document.Document;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.model.SearchPhrase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Import(EmbeddedElasticsearchConfig.class)
public class DocumentScoringTest {
	@Autowired
	private DocumentRepository repository;
	private static final String FIRST_FIELD = "first";
	private static final String SECOND_FIELD = "second";
	private static final String THIRD_FIELD = "third";

	@Test
	public void can_find_two_documents_with_identical_score() {
		repository.deleteAll().block();
		repository.saveAll(
				List.of(
						Document.builder()
								.firstField(FIRST_FIELD)
								.secondField(SECOND_FIELD)
								.thirdField(THIRD_FIELD)
								.build(),
						Document.builder()
								.firstField(FIRST_FIELD)
								.secondField(SECOND_FIELD)
								.thirdField(THIRD_FIELD)
								.build()
				)
		).collectList().block();

		repository.findMostRelevantBySearchPhrase(
						SearchPhrase.builder()
								.firstField(FIRST_FIELD)
								.secondField(SECOND_FIELD)
								.thirdField(THIRD_FIELD)
								.build()
				).collectList()
				.as(StepVerifier::create)
				.consumeNextWith(documents ->
						assertThat(
								documents,
								hasSize(2)
						)
				)
				.verifyComplete();
	}
}
