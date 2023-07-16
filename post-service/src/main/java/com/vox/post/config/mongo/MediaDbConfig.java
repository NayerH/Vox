package com.vox.post.config.mongo;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.vox.post.repository.media"}, mongoTemplateRef = MediaDbConfig.MONGO_TEMPLATE)
public class MediaDbConfig {
    protected static final String MONGO_TEMPLATE = "mediaMongoTemplate";
}
