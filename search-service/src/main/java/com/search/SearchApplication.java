package com.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableDiscoveryClient
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
//		Receiver r= new Receiver();

        SpringApplication.run(SearchApplication.class, args);
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip + " HEEH");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


}
