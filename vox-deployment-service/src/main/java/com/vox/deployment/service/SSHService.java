package com.vox.deployment.service;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@Service
public class SSHService {
    @Value("${ssh.server.hostname}")
    private String hostname;

    @Value("${ssh.server.port}")
    private int port;

    @Value("${ssh.server.username}")
    private String username;

    @Value("${ssh.server.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(SSHService.class);

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void deploy(String appName, String remoteFilePath) throws IOException {
        String command = "java -jar " + remoteFilePath + "deployment.jar" + "\n";
        try{
            executeCommand(command);
            logger.info("Deployment of " + appName + " microservice to " + hostname + " complete");
        } catch (IOException e) {
            logger.error("Error deploying " + appName + " microservice to " + hostname);
            throw new RuntimeException(e);
        }
    }

    public void stop(String appName, String remoteDirectory) throws IOException {
        String command = "pgrep -lf " + remoteDirectory + "deployment.jar" + "\n";
        try {
            executeCommand(command);
            logger.info("Stopping of " + appName + " microservice at " + hostname + " complete");
        } catch (IOException e) {
            logger.error("Error stopping " + appName + " microservice at " + hostname);
            throw new RuntimeException(e);
        }
    }

    private void executeCommand(String command) throws IOException {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        long defaultTimeoutSeconds = 5L;
        try (ClientSession session = client.connect(username, hostname, port)
                .verify(defaultTimeoutSeconds, TimeUnit.SECONDS).getSession()) {
            session.addPasswordIdentity(password);
            session.auth().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);

            try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
                 ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
                channel.setOut(responseStream);
                try {
                    channel.open().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
                    try (OutputStream pipedIn = channel.getInvertedIn()) {
                        pipedIn.write(command.getBytes());
                        pipedIn.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
                            TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));
                    String responseString = new String(responseStream.toByteArray());
                    logger.info(responseString);
                } finally {
                    channel.close(false);
                }
            }
        } finally {
            client.stop();
        }
    }
}
