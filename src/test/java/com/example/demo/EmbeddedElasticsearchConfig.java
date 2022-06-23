package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Profile({"test"})
@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.example.demo")
public class EmbeddedElasticsearchConfig extends AbstractReactiveElasticsearchConfiguration {
	@Value("${elasticsearch.cluster.name:docker-cluster}")
	private String clusterName;

	@Value("${elasticsearch.host:127.0.0.1}")
	private String host;

	@Override
	public ReactiveElasticsearchClient reactiveElasticsearchClient() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(host + ":9200")
				.build();

		return ReactiveRestClients.create(clientConfiguration);
	}

	@Bean
	public ElasticsearchOperations elasticsearchOperations() {
		return new ElasticsearchRestTemplate(
				RestClients.create(
								ClientConfiguration.builder()
										.connectedTo(host + ":9200")
										.build()
						)
						.rest()
		);
	}
}
