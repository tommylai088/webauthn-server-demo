package com.example.webauthn.server.demo;

import java.util.ArrayList;
import java.util.Arrays;

public class AttestationAttStmt {
    private ArrayList x5c;

    private byte[] sig;

    public ArrayList getX5c() {
        return x5c;
    }

    public byte[] getSig() {
        return sig;
    }

    @Override
    public String toString() {
        return "AttestationAttStmt{" +
                "x5c=" + x5c +
                ", sig=" + Arrays.toString(sig) +
                '}';
    }
}
