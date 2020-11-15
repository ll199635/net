package com.command.transfer.chord.com.chord.tool;

import java.security.MessageDigest;

public class SHA1 {

    private static long GetFormattedText(byte[] bytes) {
        long bufLong = 0;
        for (int j = 0; j < Const.RING_LOC_DIGIT / 8; j++) {
            bufLong += ((long) (bytes[j] & 0xff) * (long) Math.pow(2, 8 * (Const.RING_LOC_DIGIT / 8 - j - 1)));
        }
        return bufLong;
    }

    public static long GetHash(String str) {
        if (str == null) {
            return -1;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return GetFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
