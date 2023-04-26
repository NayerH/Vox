package com.vox.post.config;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private Environment env;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private String port;
    @Value("${spring.data.mongodb.database}")
    private String db;
    @Value("${spring.data.mongodb.username}")
    private String user;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Autowired
    public MongoConfig(Environment env) {
        this.env = env;
    }
    @Override
    public MongoClient mongoClient() {
        String uriString = "mongodb://" + user + ":" + password + "@" + host + ":" + port + "/" + db;
        ConnectionString connectionString = new ConnectionString(uriString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .writeConcern(WriteConcern.ACKNOWLEDGED.withWTimeout(0L, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(settings);
    }

    @Override
    protected String getDatabaseName() {
        return db;
    }
    public void setHost() {
        this.host = env.getProperty("spring.data.mongodb.uri");
    }
    public void setPort() {
        this.port = env.getProperty("spring.data.mongodb.port");
    }
    public void setDb() {
        this.db = env.getProperty("spring.data.mongodb.database");
    }
    public void setUser() {
        this.user = env.getProperty("spring.data.mongodb.username");
    }
    public void setPassword() {
        this.password = env.getProperty("spring.data.mongodb.password");
    }
}
