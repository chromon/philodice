package com.philodice.core.Entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerEntity {

    @Value("${server.address}")
    private String hostAddress;

    @Value("${server.port}")
    private int port;

    public String getLink() {
        return "http://" +
                this.getHostAddress() +
                ":" +
                this.getPort() +
                "/";
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
