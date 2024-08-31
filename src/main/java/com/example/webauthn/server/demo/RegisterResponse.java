package com.example.webauthn.server.demo;

public class RegisterResponse {

    private String credentialIdString;
    private String authenticatorString;

    public String getCredentialIdString() {
        return credentialIdString;
    }

    public String getAuthenticatorString() {
        return authenticatorString;
    }

    public void setCredentialIdString(String credentialIdString) {
        this.credentialIdString = credentialIdString;
    }

    public void setAuthenticatorString(String authenticatorString) {
        this.authenticatorString = authenticatorString;
    }

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "credentialIdString='" + credentialIdString + '\'' +
                ", authenticatorString='" + authenticatorString + '\'' +
                '}';
    }
}
