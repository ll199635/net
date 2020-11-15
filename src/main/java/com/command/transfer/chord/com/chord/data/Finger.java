package com.command.transfer.chord.com.chord.data;

public class Finger {
    public long local;
    public Address addr;
    public boolean isFind;

    Finger(long local, Address addr, boolean isFind) {
        this.local = local;
        this.addr = addr;
        this.isFind = isFind;
    }
}
