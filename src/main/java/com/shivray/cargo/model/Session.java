package com.shivray.cargo.model;

public final class Session {

    public static long userId;
    public static String username;
    public static String role;
    public static String fullName;

    private Session() {
    }

    public static boolean isAdmin() {

        return "ADMIN".equals(role);
    }
}