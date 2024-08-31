package com.example.webauthn.server.demo;

public class ClientData {

    private String type;
    private String challenge;
    private String origin;

    public String getType() {
        return type;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "type='" + type + '\'' +
                ", challenge='" + challenge + '\'' +
                ", origin='" + origin + '\'' +
                '}';
    }
}