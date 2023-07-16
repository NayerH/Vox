package com.search.service;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.search.entity.Person;
import com.search.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class PersonService {


    private final PersonRepository repository;
    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public void save(final Person person) {
        repository.save(person);
    }

    public Person findById(final String id) {
        return repository.findById(id).orElse(null);
    }

//findBymongoId // findbymongo ID and update  // findmongoID and dlete
    public List<Person> findAll() {
        Iterator<Person> it = repository.findAll().iterator();
        List<Person> res = new ArrayList<>();
        while (it.hasNext()) {
            Person p = it.next();
            res.add(p);
        }
        return res;
    }

    public List<Person> findFuzzy(final String name) {

        FuzzyQuery fq = QueryBuilders
                .fuzzy()
                .field("name").value(name)
                .fuzziness("2")
                .build();

        SearchRequest sr = new SearchRequest.Builder().query(fq._toQuery()).build();
        List<Person> res = new ArrayList<>();

        try {

            SearchResponse<Person> response = elasticsearchClient.search(sr, Person.class);
            HitsMetadata<Person> hits = response.hits();
            for (Hit<Person> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    public List<Person> getSuggestions(String prefix) {
        WildcardQuery fq = QueryBuilders.wildcard().field("title").value(prefix).wildcard(prefix + "*").build();


        SearchRequest searchRequest = new SearchRequest.Builder().query(fq._toQuery()).build();
        List<Person> res = new ArrayList<>();

        try {

            SearchResponse<Person> response = elasticsearchClient.search(searchRequest, Person.class);
            HitsMetadata<Person> hits = response.hits();
            for (Hit<Person> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }


}
