package com.example.webauthn.server.demo;

public class Challenge {

    private String challenge;

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "challenge='" + challenge + '\'' +
                '}';
    }
}
