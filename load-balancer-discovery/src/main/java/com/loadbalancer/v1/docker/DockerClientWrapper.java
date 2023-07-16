package com.loadbalancer.v1.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class DockerClientWrapper {

    private final DockerClient dockerClient;
    private String host;


    public DockerClientWrapper(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
        try {
            String host = this.executeCommand("show servers state").split("\n")[2].split(" ")[4];
            this.host = host;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String addServerCmd(String backend, String server, int port) throws InterruptedException {
        System.out.println(host);
        StringBuilder sb = new StringBuilder();
        sb.append("add server ");
        sb.append(backend);
        sb.append("/");
        sb.append(server);
        sb.append(" ");
        sb.append(this.host).append(":").append(port);
        return this.executeCommand(sb.toString());
    }

    public String addServerCmd(String backend, String server, String host, int port) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        sb.append("add server ");
        sb.append(backend);
        sb.append("/");
        sb.append(server);
        sb.append(" ");
        sb.append(host);
        return this.executeCommand(sb.toString());
    }

    public String upServerCmd(String backend, String server) throws InterruptedException {
        return executeCommand("set server " + backend + "/"+ server + " state ready health UP");
    }
    public String drainServerCmd(String backend, String server) throws InterruptedException {
        return executeCommand("set server " + backend + "/"+ server + " state drain");
    }
    public String Command(String command) {
        StringBuilder sb = new StringBuilder();
        sb.append("echo \"");
        sb.append(command);
        sb.append("\" | socat stdio tcp4-connect:127.0.0.1:9999\n");
        return sb.toString();
    }

    public String executeCommand(String command) throws InterruptedException {
        String Command = Command(command);
        System.out.println(Command);
        InputStream stdin = new ByteArrayInputStream(Command.getBytes());
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd("load_balancer_discovery-web-1")
                .withAttachStdout(true)
                .withAttachStdin(true)
                .withCmd("/bin/sh")
                .exec();

        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .withTty(true)
                .withStdIn(stdin)
                .exec(new ExecStartResultCallback(stdout, System.err))
                .awaitCompletion(5, TimeUnit.SECONDS);
        return stdout.toString();
    }

}
