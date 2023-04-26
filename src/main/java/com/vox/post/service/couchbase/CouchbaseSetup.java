package com.vox.post.service.couchbase;

import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.IndexExistsException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;
import com.couchbase.client.java.query.QueryResult;
import com.vox.post.config.couchbase.CollectionNames;
import com.vox.post.config.couchbase.CouchbaseProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CouchbaseSetup implements CommandLineRunner {
    @Autowired
    private Bucket bucket;

    @Autowired
    private Cluster cluster;

    @Autowired
    private CouchbaseProperties props;

    @Override
    public void run(String... args) {
        try {
            cluster.queryIndexes().createPrimaryIndex(props.getBucketName());
            System.out.println("Created primary index" + props.getBucketName());
        } catch (Exception e) {
            System.out.println("Primary index already exists on bucket "+props.getBucketName());
        }

        CollectionManager collectionManager = bucket.collections();
        try {
            CollectionSpec spec = CollectionSpec.create(CollectionNames.MEDIA, bucket.defaultScope().name());
            collectionManager.createCollection(spec);
            System.out.println("Created collection '" + spec.name() + "' in scope '" + spec.scopeName() + "' of bucket '" + bucket.name() + "'");
            Thread.sleep(1000);
        } catch (CollectionExistsException e){
            System.out.println(String.format("Collection <%s> already exists", CollectionNames.MEDIA));
        } catch (Exception e) {
            System.out.println(String.format("Generic error <%s>",e.getMessage()));
        }

        try {
            final String query = "CREATE PRIMARY INDEX default_profile_index ON "+props.getBucketName()+"._default."+ CollectionNames.MEDIA;
            System.out.println(String.format("Creating default_profile_index: <%s>", query));
            final QueryResult result = cluster.query(query);
            for (JsonObject row : result.rowsAsObject()){
                System.out.println(String.format("Index Creation Status %s",row.getObject("meta").getString("status")));
            }
            System.out.println("Created primary index on collection " + CollectionNames.MEDIA);
            Thread.sleep(1000);
        } catch (IndexExistsException e){
            System.out.println(String.format("Collection's primary index already exists"));
        } catch (Exception e){
            System.out.println(String.format("General error <%s> when trying to create index ",e.getMessage()));
        }

        try {
            final QueryResult result = cluster.query("CREATE INDEX default_mediafile_title_index ON " + props.getBucketName() + "._default." + CollectionNames.MEDIA + "(title)");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(String.format("Failed to create secondary index on mediafile.title: %s", e.getMessage()));
        }

        System.out.println("Application is ready.");
    }
}
