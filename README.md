# Vox

This is a scalable web application for a news media outlet built using Java Springboot, using the microservices architecture.

### VOX/Posts

This mini-service is part of a larger app that allows users to view, comment on, and create posts. It is designed to be a user-friendly, secure, and scalable platform for creating and sharing content.

The mini-service allows users to:

* View posts: Users can see the content of posts, such as text, images, and videos. They can also see who created the post, when it was created, and how many times it has been viewed.
* Comment on posts: Users can add their own thoughts and opinions to posts. Comments can be public or private, and they can be replied to by other users.
* Create and delete posts: Authors can add new posts to the platform, and they can also remove posts that they no longer want to be visible.

The mini-service is a powerful tool that can be used to create and share content with others. It is easy to use, secure, and scalable, making it a great choice for anyone who wants to build a community around their content.


### Tools

This mini-service is built using the following tools:

* Spring Boot
* Redis
* MongoDB
* RabbitMQ

### Endpoints

This mini-service exposes the following endpoints:

* `api/v1/posts/all` - Get all posts
* `api/v1/posts/get/{id}` - Get a single post by ID
* `api/v1/posts/add` - Create a new post
* `api/v1/posts/delete/{id}` - Delete a post by ID
* `api/v1/posts/categories` - Get all post categories
* `api/v1/posts/categories/top` - Get the top 10 post categories
* `api/v1/posts/update/{id}` - Update a post by ID
* `api/v1/posts/categories/{category}` - Get all posts in a category
* `api/v1/posts/comments/{postId}` - Get all comments for a post
* `api/v1/posts/comments/{postId}/{commentId}` - Get a single comment by ID
* `api/v1/media/add` - Upload media

### Properties

The following properties can be used to configure the mini-service:

* `spring.cache.views` - The number of views to cache
* `mongodb.post.database` - The name of the MongoDB database for posts
* `mongodb.post.auto-index-creation` - Whether to automatically create indexes for posts
* `mongodb.post.threading.enabled` - Whether to enable threading for posts
* `mongodb.post.username` - The username for the MongoDB database
* `mongodb.post.password` - The password for the MongoDB database
* `mongodb.post.uri` - The URI for the MongoDB database
* `mongodb.media.database` - The name of the MongoDB database for media
* `mongodb.media.auto-index-creation` - Whether to automatically create indexes for media
* `mongodb.media.threading.enabled` - Whether to enable threading for media
* `mongodb.media.username` - The username for the MongoDB database
* `mongodb.media.password` - The password for the MongoDB database
* `mongodb.media.uri` - The URI for the MongoDB database
* `spring.rabbitmq.host` - The host for the RabbitMQ server
* `spring.rabbitmq.port` - The port for the RabbitMQ server
* `spring.rabbitmq.username` - The username for the RabbitMQ server
* `spring.rabbitmq.password` - The password for the RabbitMQ server
* `spring.cache.type` - The type of cache to use
* `redis.timeout` - The timeout for Redis connections
* `redis.maximumActiveConnectionCount` - The maximum number of active Redis connections
* `spring.redis.cloud.host` - The host for the Redis Cloud server
* `spring.redis.cloud.port` - The port for the Redis Cloud server
* `spring.redis.cloud.password` - The password for the Redis Cloud server
* `spring.redis.port` - The port for the Redis server
* `spring.redis.host` - The host for the Redis server
* `redis.sessiondata.ttl` - The TTL for session data in seconds
* `logging.config` - The path to the logback configuration file
* `spring.servlet.multipart.max-file-size` - The maximum file size for multipart requests
* `spring.servlet.multipart.max-request-size` - The maximum request size for multipart requests

### Dependencies

The following dependencies are required to build and run the mini-service:

* **Spring Boot Starter AMQP**

This dependency provides support for using the Spring AMQP framework, which is a lightweight, high-performance messaging framework that provides a consistent programming model for sending and receiving messages between applications.

* **Spring Boot Starter Data JPA**

This dependency provides support for using the Spring Data JPA framework, which is an extension of the Spring Data family that provides support for using the Java Persistence API (JPA) to access relational databases.

* **Spring Boot Starter Data MongoDB**

This dependency provides support for using the Spring Data MongoDB framework, which is an extension of the Spring Data family that provides support for using MongoDB as a data store.

* **Spring Boot Starter Data Redis Reactive**

This dependency provides support for using the Spring Data Redis Reactive framework, which is an extension of the Spring Data family that provides support for using Redis as a data store in a reactive manner.

* **Spring Boot Starter Web**

This dependency provides support for building web applications using Spring MVC.

* **Spring Boot Starter Test**

This dependency provides support for testing Spring Boot applications.

* **Reactor Test**

This dependency provides support for testing reactive applications.

* **Spring Rabbit Test**

This dependency provides support for testing Spring AMQP applications.

* **Spring Boot Starter Data Redis**

This dependency provides support for using the Spring Data Redis framework, which is an extension of the Spring Data family that provides support for using Redis as a data store.

* **Jedis**

This dependency provides a Java client library for Redis.

* **ModelMapper**

This dependency provides a Java object mapping library that can be used to map between different object graphs.

* **Mongo Java Driver**

This dependency provides a Java driver for MongoDB.

* **Lombok**

This dependency provides a Java annotation processor that can be used to generate boilerplate code, such as getters, setters, and toString methods.

### Contribution

To contribute to this project, please clone the GitHub repository:

```
git clone https://github.com/NayerH/Vox.git
```

The files related to this mini-service are inside the package `com.vox.post`.

### How to install the dependencies and run the project

1. Install Java 17.
2. Install Maven.
3. Clone the GitHub repository:

```
git clone https://github.com/NayerH/Vox.git
```

4. Change directory to the project root:

```
cd Vox
```

5. Run the following command to install the dependencies:

```
mvn install
```

6. Run the following command to start the project:

```
mvn spring-boot:run
```

The project will be started on port 8080.
