package com.vox.post.controller;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.vox.post.config.couchbase.CouchbaseProperties;
import com.vox.post.model.MediaFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.vox.post.config.couchbase.CollectionNames.MEDIA;

@RestController
@RequestMapping(path = "api/v1/media")
public class MediaController {

    private Cluster cluster;
    private Collection mediaCollection;
    private CouchbaseProperties couchbaseProperties;
    private Bucket bucket;

    public MediaController(Cluster cluster, Bucket bucket, CouchbaseProperties couchbaseProperties) {
        System.out.println("Initializing profile controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.mediaCollection = bucket.collection(MEDIA);
        this.couchbaseProperties = couchbaseProperties;
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<MediaFile> save(@RequestBody final MediaFile mediaFile) {
        System.out.println("Saving media file: " + mediaFile);
        MediaFile savedMediaFile = mediaFile.getMediaFile();
        try{
            mediaCollection.insert(savedMediaFile.getId(), savedMediaFile);
            return ResponseEntity.ok(savedMediaFile);
        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<MediaFile>> get() {
        System.out.println("Getting media files ");
        String qryString = "SELECT p.* FROM `"+couchbaseProperties.getBucketName()+"`.`_default`.`"+MEDIA +"` p ";
        System.out.println("Query="+qryString);
        try{
            List<MediaFile> mediaFiles = cluster.query(qryString, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS)).rowsAs(MediaFile.class);
            return ResponseEntity.ok(mediaFiles);
        }catch (Exception e){
            return ResponseEntity.status(500).body(null);
        }
    }


}
