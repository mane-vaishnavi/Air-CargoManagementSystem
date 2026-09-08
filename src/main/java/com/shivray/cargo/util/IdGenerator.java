package com.shivray.cargo.util;

import java.util.UUID;

public final class IdGenerator {

    private static String s() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    public static String pnr() {
        return "SIC-" + s();
    }

    public static String booking() {
        return "BK-" + s();
    }

    public static String awb() {
        return "AWB-" + s() + s().substring(0, 4);
    }

    public static String cancel() {
        return "CAN-" + s();
    }

    public static String refund() {
        return "REF-" + s();
    }

    private IdGenerator() {
    }
}