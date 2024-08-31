package com.example.webauthn.server.demo;

public class Device {

    private String deviceId;
    private String challenge;

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getChallenge() {
        return challenge;
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", challenge='" + challenge + '\'' +
                '}';
    }
}
