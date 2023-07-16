package com.vox.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ControllerService {

    private final ArrayList<String> availableIPs;
    private final Map<String, Stack<String>> latestIPs;
    private Environment environment;
    private RabbitMQProducer rabbitMQProducer;


    public ControllerService(RabbitMQProducer rabbitMQProducer, Environment environment) {
        this.rabbitMQProducer = rabbitMQProducer;
        this.environment = environment;
        this.latestIPs = new HashMap<>();
        this.availableIPs = generateRandomIps();
    }

    public ArrayList<String> generateRandomIps() {
        ArrayList<String> ipList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            String ip = random.nextInt(256) + "." +
                    random.nextInt(256) + "." +
                    random.nextInt(256) + "." +
                    random.nextInt(256);
            ipList.add(ip);
        }

        return ipList;
    }

    public void setMQ(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing1.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void setMaxThreadCount(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing2.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void setMaxDbConnectionsCount(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing3.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }


    public void addCommand(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing4.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void deleteCommand(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing5.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void updateCommand(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing6.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void updateClass(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing7.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void freeze(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing8.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void continueService(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing9.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    public void setErrorReportingLevel(JsonNode message) {
        String routingKey = environment.getProperty("rabbitmq.routing10.key");
        rabbitMQProducer.sendMessage(routingKey, message);
    }

    //    Health Monitoring (Posts App)
    public ResponseEntity<String> checkPostRT(JsonNode body) {
        if (body == null) {
            return ResponseEntity.badRequest().body("Body is null");
        }
//        Get the avg response time from the body
        JsonNode measurements = body.get("measurements");
        double request_count = Double.parseDouble(measurements.get(0).get("value").asText());
        double total_time = Double.parseDouble(measurements.get(1).get("value").asText());
        double avg_time = (total_time * 1000) / request_count;

//        Get the threshold from the properties file
        String environmentPropertyAvg = environment.getProperty("health.avg.response.time");
        String environmentPropertyMin = environment.getProperty("health.min.response.time");
        assert environmentPropertyAvg != null;
        double threshold = Double.parseDouble(environmentPropertyAvg);
        assert environmentPropertyMin != null;
        double min_threshold = Double.parseDouble(environmentPropertyMin);

//        Compare the avg response time with the threshold
        if (avg_time < threshold && avg_time >= min_threshold) {
            return ResponseEntity.ok().body("Post App avg_response_time: " + avg_time + "ms :: Response Time is A OK");
        }
        if (avg_time < min_threshold) {
//            Get the latest instance from the stack and send a message to the deployment server
            Stack<String> stack = latestIPs.get("posts");
            String routingKey = environment.getProperty("rabbitmq.routing12.key");
//            If there are no instances created, do nothing
            if (stack.isEmpty()) {
                return ResponseEntity.ok().body("Post App avg_response_time: " + avg_time + "ms :: Response Time is A OK");
            } else {
                JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
                ObjectNode message = jsonNodeFactory.objectNode();
//                Get the latest instance from the stack and send a message to the deployment server
                String ip = stack.pop();
                availableIPs.add(ip);
                message.put("ip", ip);
                message.put("appName", "posts");
                message.put("remoteDirectory", "/");
                message.put("stop", true);
                rabbitMQProducer.sendMessage(routingKey, message);
                return ResponseEntity.ok().body("Post App avg_response_time: " + avg_time + "ms :: Deleting the latest instance");
            }
        }
        if (avg_time >= threshold) {
//        If the avg response time is greater than the threshold, send a message to the deployment server
            String routingKey = environment.getProperty("rabbitmq.routing11.key");
            if (!availableIPs.isEmpty()) {
                String ip = availableIPs.remove(0);
                Stack<String> stack;
                if (latestIPs.isEmpty()) {
                    stack = new Stack<>();
                } else {
                    stack = latestIPs.get("posts");
                }
                stack.push(ip);
                latestIPs.put("posts", stack);
                JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
                ObjectNode message = jsonNodeFactory.objectNode();
                message.put("ip", ip);
                message.put("appName", "posts");
                message.put("remoteDirectory", "/");
                message.put("stop", false);
                rabbitMQProducer.sendMessage(routingKey, message);
                return ResponseEntity.ok().body("Post App avg_response_time: " + avg_time + " ms :: Response Time is High -> Creating New Instance..." + message);
            }
            return ResponseEntity.status(500).body("No Available IPs to create new instance");
        }
        return null;
    }

    public ResponseEntity<String> checkPostStatus(JsonNode status) {
        if (status == null) {
            return ResponseEntity.badRequest().body("Body is null");
        }
        if (status.get("status").asText().equals("DOWN")) {
            String routingkey = environment.getProperty("rabbitmq.routing11.key");
            String ip = availableIPs.remove(0);
            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
            ObjectNode message = jsonNodeFactory.objectNode();
            message.put("ip", ip);
            message.put("appName", "posts");
            message.put("remoteDirectory", "/");
            message.put("stop", false);
            rabbitMQProducer.sendMessage(routingkey, message);
            return ResponseEntity.ok().body("Post App is DOWN -> Creating New Instance..." + message);
        }
        return ResponseEntity.ok().body("Post App is UP");
    }
}
