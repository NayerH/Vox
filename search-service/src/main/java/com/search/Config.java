//package com.search;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.client.erhlc.RestClients;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//import org.springframework.stereotype.Component;
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.search.repository")
//@ComponentScan(basePackages = { "com.search" })
//public class Config extends AbstractElasticsearchConfiguration {
//
//    @Bean
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo("localhost:9200")
//                .build();
//
//        return RestClients.create(clientConfiguration)
//                .rest();
//    }
//}
//
