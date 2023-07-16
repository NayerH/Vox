package com.discovery.service;

import com.loadbalancer.v1.docker.DockerClientWrapper;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class EurekaRegistry {

    private final DockerClientWrapper dockerClientWrapper;
    ArrayList<BackendServer> backendServers;

    @Autowired
    public EurekaRegistry(DockerClientWrapper dockerClientWrapper) {
        this.dockerClientWrapper = dockerClientWrapper;
        this.backendServers = new ArrayList<BackendServer>();
    }


    @Scheduled(fixedDelay = 10000L)
    @EventListener(ApplicationReadyEvent.class)
    public void getRunningInstances() {
        ArrayList<BackendServer> currentBackendServers = new ArrayList<BackendServer>();
        PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
        Applications applications = registry.getApplications();
        applications.getRegisteredApplications().forEach((registeredApplication) -> {
            registeredApplication.getInstances().forEach((instance) -> {
                BackendServer backendServer = new BackendServer(instance.getHostName(), instance.getPort(), instance.getId(), instance.getAppName().toLowerCase());
                currentBackendServers.add(backendServer);
                if (!backendServers.contains(backendServer)) {
                    System.out.println("entered !contains");
                    backendServers.add(backendServer);
                    try {
                        dockerClientWrapper.addServerCmd(backendServer.getBackend(), backendServer.getServer(), backendServer.getHost(), backendServer.getPort());
                        dockerClientWrapper.upServerCmd(backendServer.getBackend(), backendServer.getServer());
                    } catch (Exception e) {
                        System.out.println("Error " + e);
                    }
                    System.out.println(backendServers);
                }
            });
        });
        for (int i = 0; i < backendServers.size(); i++) {
            BackendServer backendServer = backendServers.get(i);
            if (!currentBackendServers.contains(backendServer)) {
                try {
                    dockerClientWrapper.drainServerCmd(backendServer.getBackend(), backendServer.getServer());
                    backendServers.remove(i);
                    i--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}