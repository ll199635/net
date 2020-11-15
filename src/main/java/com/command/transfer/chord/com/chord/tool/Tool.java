package com.command.transfer.chord.com.chord.tool;

public class Tool {
    public static long AddLocal(long a, long b) {
        long temp = a + b;
        if (temp >= Const.MAX_RING_LOC) {
            temp = temp - Const.MAX_RING_LOC + 1;
        }
        return temp;
    }
}
