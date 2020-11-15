package com.command.transfer.chord.com.chord.data;


import com.command.transfer.chord.com.chord.tool.SHA1;

public class Address {
    private String ip;
    private int port;
    private String addr;
    private long hashCode;

    Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.addr = ip + ":" + port;
        this.hashCode = SHA1.GetHash(this.addr);
    }

    Address(String addr) {
        String[] s = addr.split(":");
        this.ip = s[0];
        this.port = Integer.parseInt(s[1]);
        this.addr = addr;
        this.hashCode = SHA1.GetHash(this.addr);
    }

    public String GetString() {
        return this.addr;
    }

    public String GetIP() {
        return this.ip;
    }

    public int GetPort() {
        return port;
    }

    public long HashCode() {
        return this.hashCode;
    }
}
