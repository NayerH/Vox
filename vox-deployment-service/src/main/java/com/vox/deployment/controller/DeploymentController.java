package com.vox.deployment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.vox.deployment.service.FtpService;
import com.vox.deployment.service.SSHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Service
@RestController
public class DeploymentController {
    private final FtpService ftpService;
    private final SSHService sshService;
    private static final Logger logger = LoggerFactory.getLogger(DeploymentController.class);

    public DeploymentController(FtpService ftpService, SSHService sshService) {
        this.ftpService = ftpService;
        this.sshService = sshService;
    }

//    @RabbitListener(queues = "${rabbitmq.queue2.name}")
    @GetMapping("/send")
    public void send() { //JsonNode req
//        logger.info("Received request to deploy: " + req.toString());
        String appName = "post";
        String ip = "172.20.10.4";
        String remoteDirectory = "data/";
//        try {
//            appName = req.get("appName").asText();
//            ip = req.get("ip").asText();
//            remoteDirectory = req.get("remoteDirectory").asText();
//        } catch (Exception e) {
//            logger.error("Error typecasting inputs in deployment: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
        ftpService.setHostname(ip);
        ftpService.sendJarFile(appName, remoteDirectory);
        logger.info("Successfully sent " + appName + " microservice to FTP server");
    }

//    @RabbitListener(queues = "${rabbitmq.queue1.name}")
    @GetMapping("/deploy")
    public void deploy() { //JsonNode req
//        logger.info("Received request to deploy: " + req.toString());
        String appName = null;
        String ip = null;
        String remoteDirectory = null;
        boolean stop = true;
//        try {
//            appName = req.get("appName").asText();
//            ip = req.get("ip").asText();
//            remoteDirectory = req.get("remoteDirectory").asText();
//            stop = req.get("stop").asBoolean();
//        } catch (Exception e) {
//            logger.error("Error typecasting inputs in deployment: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
        appName = "post";
        ip = "172.20.10.4";
        remoteDirectory = "data/";
        sshService.setHostname(ip);
        if(!stop){
            try {
                sshService.deploy(appName, remoteDirectory);
                logger.info("Successfully Deployed " + appName + " to " + ip);
            } catch (IOException e) {
                logger.error("Error in SSH deploying " + appName + " to " + ip);
                throw new RuntimeException(e);
            }
        } else {
            try {
                sshService.stop(appName, remoteDirectory);
                logger.info("Successfully stopped " + appName + " at " + ip);
            } catch (IOException e) {
                logger.error("Error in SSH: stopping " + appName + " at " + ip);
                throw new RuntimeException(e);
            }
        }
    }
}
