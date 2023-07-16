package com.vox.deployment.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FtpService {
    @Value("${ftp.server.hostname}")
    private String hostname;

    @Value("${ftp.server.port}")
    private int port;

    @Value("${ftp.server.username}")
    private String username;

    @Value("${ftp.server.password}")
    private String password;
    private final FTPClient ftpClient;
    private static final Logger logger = LoggerFactory.getLogger(FtpService.class);

    public FtpService(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public void sendJarFile(String appName, String remoteDirectory) {
        String filePath = "src/main/resources/data/" + appName + "/deployment.jar";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            ftpClient.disconnect();
            ftpClient.connect(hostname, port);
            boolean isLoggedin = ftpClient.login(username, password);
            logger.info("FTP: Is logged in? " + isLoggedin);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean res = ftpClient.storeFile(remoteDirectory + "/deployment.jar", fis);
            logger.info("FTP: File sent successfully? " + res);
            fis.close();
        } catch (IOException e) {
            logger.error("Error sending " + appName + " microservice to FTP server");
            throw new RuntimeException(e);
        }
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}

