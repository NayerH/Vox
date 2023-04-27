package com.vox.post.config.couchbase;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

    @Autowired
    private CouchbaseProperties couchbaseProperties;

    @Bean(destroyMethod = "disconnect")
    public Cluster getCouchbaseCluster() {
        return Cluster.connect(couchbaseProperties.getConnectionString(), couchbaseProperties.getUsername(), couchbaseProperties.getPassword());
    }

    @Bean
    public Bucket getCouchbaseBucket(Cluster cluster) {

        // Creates the cluster if it does not exist yet
        if (!cluster.buckets().getAllBuckets().containsKey(couchbaseProperties.getBucketName())) {
            cluster.buckets().createBucket(
                    BucketSettings.create(couchbaseProperties.getBucketName())
                            .bucketType(BucketType.COUCHBASE)
                            .minimumDurabilityLevel(DurabilityLevel.NONE)
                            .ramQuotaMB(128));
        }
        return cluster.bucket(couchbaseProperties.getBucketName());
    }
}
