package com.example.webauthn.server.demo;

public class Attestation {
    private String fmt;
    private AttestationAttStmt attStmt;
//    private byte[] authData;

    public String getFmt() {
        return fmt;
    }

    public AttestationAttStmt getAttStmt() {
        return attStmt;
    }
//
//    public byte[] getAuthData() {
//        return authData;
//    }

    @Override
    public String toString() {
        return "Attestation{" +
                "fmt='" + fmt + '\'' +
                ", attStmt=" + attStmt +
//                ", authData=" + Arrays.toString(authData) +
                '}';
    }
}
