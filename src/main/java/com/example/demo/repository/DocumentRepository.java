package com.example.demo.repository;

import com.example.demo.document.Document;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;

public interface DocumentRepository extends ReactiveElasticsearchRepository<Document, String>, DocumentRepositoryCustom {
}
