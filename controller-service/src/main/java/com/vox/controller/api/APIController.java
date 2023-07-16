package com.vox.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.vox.controller.service.ControllerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@org.springframework.web.bind.annotation.RestController
public class APIController {
    @Autowired
    RestTemplate restTemplate;
    private ControllerService controllerService;

    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    public APIController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @PostMapping("/setMQ")
    public void setMQ(@RequestBody JsonNode message) {
        controllerService.setMQ(message);
    }

    @PostMapping("/setMaxThreadCount")
    public void setMaxThreadCount(@RequestBody JsonNode message) {
        controllerService.setMaxThreadCount(message);
    }

    @PostMapping("/setMaxDbConnectionsCount")
    public void setMaxDbConnectionsCount(@RequestBody JsonNode message) {
        controllerService.setMaxDbConnectionsCount(message);
    }

    @PostMapping("/setErrorReportingLevel")
    public void setErrorReportingLevel(@RequestBody JsonNode message) {
        controllerService.setErrorReportingLevel(message);
    }

    @PostMapping("/addCommand")
    public void addCommand(@RequestBody JsonNode message){
        controllerService.addCommand(message);
    }

    @PostMapping("/deleteCommand")
    public void deleteCommand(@RequestBody JsonNode message) {
        controllerService.deleteCommand(message);
    }

    @PostMapping("/updateCommand")
    public void updateCommand(@RequestBody JsonNode message) {
        controllerService.updateCommand(message);
    }

    @PostMapping("/updateClass")
    public void updateClass(@RequestBody JsonNode message) {
        controllerService.updateClass(message);
    }

    @PostMapping("/freeze")
    public void freeze(@RequestBody JsonNode message) {
        controllerService.freeze(message);
    }

    @PostMapping("/continue")
    public void continueService(@RequestBody JsonNode message) {
        controllerService.continueService(message);
    }

//    @GetMapping("/health/post/response")
    @Scheduled(cron = "0 * * * * *")
    public void healthPostResponse() {
        String url = "http://localhost:8080/actuator/metrics/http.server.requests";
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode body = response.getBody();
        ResponseEntity<String> action = controllerService.checkPostRT(body);
        logger.info(action.getBody());
    }

//    @GetMapping("/health/post")
    @Scheduled(cron = "0 * * * * *")
    public void healthPostStatus() {
        String url = "http://localhost:8080/actuator/health";
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode body = response.getBody();
        ResponseEntity<String> action = controllerService.checkPostStatus(body);
        logger.info(action.getBody());
    }
}
