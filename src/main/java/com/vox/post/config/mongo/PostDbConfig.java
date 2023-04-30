package com.vox.post.config.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.vox.post.repository.posts"}, mongoTemplateRef = PostDbConfig.MONGO_TEMPLATE)
public class PostDbConfig {
    protected static final String MONGO_TEMPLATE = "postMongoTemplate";
}
