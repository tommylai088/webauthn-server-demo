package com.example.webauthn.server.demo;

public class PublicKeyCredential {
    private String id;
    private String type;
    private String rawId;
    private AuthenticatorResponse response;

    private RegisterResponse authenticator;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRawId(String rawId) {
        this.rawId = rawId;
    }

    public void setResponse(AuthenticatorResponse response) {
        this.response = response;
    }

    public RegisterResponse getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(RegisterResponse authenticator) {
        this.authenticator = authenticator;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getRawId() {
        return rawId;
    }

    public AuthenticatorResponse getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "PublicKeyCredential{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", rawId='" + rawId + '\'' +
                ", response=" + response +
                ", authenticator=" + authenticator +
                '}';
    }

}
