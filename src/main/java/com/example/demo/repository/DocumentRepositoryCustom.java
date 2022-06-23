package com.example.demo.repository;

import com.example.demo.document.Document;
import com.example.demo.repository.model.SearchPhrase;
import reactor.core.publisher.Flux;

public interface DocumentRepositoryCustom {
	Flux<Document> findMostRelevantBySearchPhrase(SearchPhrase searchPhrase);
}
