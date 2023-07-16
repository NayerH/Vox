package com.search.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.search.controller.PostController;
import com.search.entity.Post;
import com.search.entity.Tag;
import com.search.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Component
@EnableRabbit
@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository repository;
    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public void save(final Post post) {
        repository.save(post);
    }

    public Post findById(final String id) {
        return repository.findById(id).orElse(null);
    }
    public void deleteByMongoId(String mongoId){
        Post post = findByMongoId(mongoId);
        repository.deleteById(post.getId());
    }
    public void updateByMongoId(Post newPost){
        Post oldPost = findByMongoId(newPost.getMongoId());
        if(newPost.getContent()!=null){
            oldPost.setContent(newPost.getContent());
//        }  if(newPost.getTags()!=null){
//            oldPost.setTags(newPost.getTags());
        }  if(newPost.getViews()!=null){
            oldPost.setViews(newPost.getViews());
        }if(newPost.getTitle()!=null){
            oldPost.setTitle(newPost.getTitle());
        }
        deleteByMongoId(newPost.getMongoId());
        repository.save(oldPost);


    }

    public Post findByMongoId(final String mongoId) {
        MatchQuery matchQuery = QueryBuilders.match().field("mongoId").query(mongoId).build();

        SearchRequest sr = new SearchRequest.Builder().query(matchQuery._toQuery()).build();

        Post post = null;
        try {

            SearchResponse<Post> response = elasticsearchClient.search(sr, Post.class);
            HitsMetadata<Post> hits = response.hits();
            for (Hit<Post> hit : hits.hits()) {
                post = hit.source();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    public List<Post> findAll() {
        Iterator<Post> it = repository.findAll().iterator();
        List<Post> res = new ArrayList<>();
        while (it.hasNext()) {
            Post p = it.next();
            res.add(p);
        }
        return res;
    }

    public List<Post> autocomplete(String prefix) {
        List<String> fields = new ArrayList<>();
        fields.add("title");
        fields.add("content");
        MultiMatchQuery multi = QueryBuilders.multiMatch().fields(fields).operator(Operator.Or).query(prefix).type(TextQueryType.PhrasePrefix).build();


        SearchRequest sr = new SearchRequest.Builder().query(multi._toQuery()).build();
        List<Post> res = new ArrayList<>();

        try {

            SearchResponse<Post> response = elasticsearchClient.search(sr, Post.class);
            HitsMetadata<Post> hits = response.hits();
            for (Hit<Post> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<Post> fullText(String prefix) {
        WildcardQuery fq = QueryBuilders.wildcard().field("title").value(prefix).wildcard("*" + prefix + "*").build();


        SearchRequest sr = new SearchRequest.Builder().query(fq._toQuery()).build();
        List<Post> res = new ArrayList<>();

        try {

            SearchResponse<Post> response = elasticsearchClient.search(sr, Post.class);
            HitsMetadata<Post> hits = response.hits();
            for (Hit<Post> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<Post> multiplefields(String s) {
        List<String> fields = new ArrayList<>();
        fields.add("title");
        fields.add("content");
        //fields.add("tags");
        MultiMatchQuery multi;
        if (s.length() == 1) {
            multi = QueryBuilders.multiMatch().fields(fields).operator(Operator.Or).query(s).type(TextQueryType.PhrasePrefix).build();
        } else if (s.length() == 2) {
            multi = QueryBuilders.multiMatch().fields(fields).operator(Operator.Or).query("*" + s + "*").fuzziness("1").build();
        } else {
            multi = QueryBuilders.multiMatch().fields(fields).operator(Operator.Or).query("*" + s + "*").fuzziness("2").build();
        }

        SearchRequest sr = new SearchRequest.Builder().query(multi._toQuery()).build();
        List<Post> res = new ArrayList<>();

        try {

            SearchResponse<Post> response = elasticsearchClient.search(sr, Post.class);
            HitsMetadata<Post> hits = response.hits();
            for (Hit<Post> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Post> findFuzzy(final String title) {

        FuzzyQuery fq = QueryBuilders
                .fuzzy()
                .field("title").value(title)
                .fuzziness("3")
                .build();

        SearchRequest sr = new SearchRequest.Builder().query(fq._toQuery()).build();
        List<Post> res = new ArrayList<>();

        try {

            SearchResponse<Post> response = elasticsearchClient.search(sr, Post.class);
            HitsMetadata<Post> hits = response.hits();
            for (Hit<Post> hit : hits.hits()) {
                res.add(hit.source());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
    public void insertFromQueue(JsonNode actualObj,String id) {
        Post p = new Post();
        p.setId( UUID.randomUUID().toString());
        p.setMongoId(id);
        p.setContent(actualObj.get("content").textValue());
        p.setTitle(actualObj.get("title").textValue());
        p.setViews(Long.parseLong(actualObj.get("views").toString()));
        p.setTag(actualObj.get("category").textValue());
        this.save(p);
    }



    public void updateFromQueue(JsonNode actualObj, String id) {
        Post p = this.findByMongoId(id);
        if ( p==null){
            LOGGER.warn("Post with id {} was not found", id);
            return;
        }
        if (actualObj.get("content") != null)
            p.setContent(actualObj.get("content").textValue());
        if (actualObj.get("title") != null)
            p.setTitle(actualObj.get("title").textValue());
        if (actualObj.get("category") != null)
            p.setTag(actualObj.get("category").textValue());

        this.save(p);
    }
}

