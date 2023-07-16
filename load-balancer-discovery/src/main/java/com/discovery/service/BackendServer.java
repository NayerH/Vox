package com.discovery.service;


public class BackendServer {

    private String host;
    private int port;
    private String server;
    private String backend;

    public BackendServer(String host, int port, String server, String backend) {
        this.host = host;
        this.port = port;
        this.server = server;
        this.backend = backend;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }


    @Override
    public boolean equals(Object obj) {
      BackendServer backendServer = (BackendServer) obj;
      return (this.host.equals(backendServer.host) && this.backend.equals(backendServer.backend)&& this.port==(backendServer.port)
      && this.server.equals(backendServer.server));
    }

    @Override
    public String toString() {
        return "BackendServer{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", server='" + server + '\'' +
                ", backend='" + backend + '\'' +
                '}';
    }
}
