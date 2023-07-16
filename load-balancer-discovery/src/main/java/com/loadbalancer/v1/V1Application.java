package com.loadbalancer.v1;


import com.loadbalancer.v1.docker.DockerClientWrapper;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@EnableScheduling
@EnableEurekaServer
@SpringBootApplication
@ComponentScan("com")
public class V1Application {

    private final DockerClientWrapper dockerClientWrapper;


    @Autowired
    public V1Application(DockerClientWrapper dockerClientWrapper) {
        this.dockerClientWrapper = dockerClientWrapper;
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(V1Application.class, args);
        DockerClientWrapper dockerClientWrapper = context.getBean(DockerClientWrapper.class);


    }


}

