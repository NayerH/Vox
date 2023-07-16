package com.search.es.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import org.springframework.data.elasticsearch.config.EnableElasticsearchAuditing;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;
import java.util.List;

//import org.elasticsearch.client.Node
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.search.repository")
@EnableElasticsearchAuditing
@ComponentScan(basePackages = {"com.search.repository"})
public class EsConfig extends ElasticsearchConfiguration {

    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .build();
    }

//    @Bean
//    public ElasticsearchOperations template() {
//        return new ElasticsearchTemplate();
//    }
}

