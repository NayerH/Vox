package com.search.repository;

import com.search.entity.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PostRepository extends ElasticsearchRepository<Post, String> {

}