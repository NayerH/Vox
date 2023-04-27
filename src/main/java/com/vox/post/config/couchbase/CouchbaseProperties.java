package com.vox.post.config.couchbase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseProperties {
    @Value("${spring.couchbase.connection-string}")
    private String connectionString;
    @Value("${spring.couchbase.user}")
    private String username;
    @Value("${spring.couchbase.password}")
    private String password;
    @Value("${spring.couchbase.bucket-name}")
    private String bucketName;

    public String getConnectionString() {
        return connectionString;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getBucketName() {
        return bucketName;
    }

}
