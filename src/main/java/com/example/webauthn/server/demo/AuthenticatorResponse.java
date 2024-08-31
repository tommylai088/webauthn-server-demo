package com.example.webauthn.server.demo;

public class AuthenticatorResponse {
    private String clientDataJSON;
    private String attestationObject;

    private String authenticatorData;
    private String signature;
    private String userHandle;

    @Override
    public String toString() {
        return "AuthenticatorResponse{" +
                "clientDataJSON='" + clientDataJSON + '\'' +
                ", attestationObject='" + attestationObject + '\'' +
                ", authenticatorData='" + authenticatorData + '\'' +
                ", signature='" + signature + '\'' +
                ", userHandle='" + userHandle + '\'' +
                '}';
    }

    public String getSignature() {
        return signature;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public String getAuthenticatorData() {
        return authenticatorData;
    }

    public String getClientDataJSON() {
        return clientDataJSON;
    }

    public void setClientDataJSON(String clientDataJSON) {
        this.clientDataJSON = clientDataJSON;
    }

    public String getAttestationObject() {
        return attestationObject;
    }

    public void setAttestationObject(String attestationObject) {
        this.attestationObject = attestationObject;
    }

}
