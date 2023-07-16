package com.vox.deployment.config;

import jakarta.annotation.PostConstruct;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FtpClientConfiguration {
    @Value("${ftp.server.hostname}")
    private String hostname;

    @Value("${ftp.server.port}")
    private int port;

    @Value("${ftp.server.username}")
    private String username;

    @Value("${ftp.server.password}")
    private String password;

    @Bean
    public FTPClient ftpClient() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultTimeout(5000);
        return ftpClient;
    }

    @PostConstruct
    public void initializeFtpClient() throws IOException {
        FTPClient ftpClient = ftpClient();
        ftpClient.connect(hostname, port);
        ftpClient.login(username, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }
}
