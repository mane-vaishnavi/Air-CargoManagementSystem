package com.shivray.cargo.util;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public final class Validation {

    private static final Pattern EMAIL =
            Pattern.compile(
                    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            );

    private static final Pattern PHONE =
            Pattern.compile("^[6-9]\\d{9}$");

    private static final Pattern PNR =
            Pattern.compile("^SIC-[A-Z0-9]{8}$");

    public static void required(String v, String n) {
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    n + " is required."
            );
        }
    }

    public static void email(String v) {
        if (!EMAIL.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "Enter a valid email address."
            );
        }
    }

    public static void phone(String v) {
        if (!PHONE.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "Enter a valid 10-digit Indian mobile number."
            );
        }
    }

    public static void pnr(String v) {
        if (!PNR.matcher(v).matches()) {
            throw new IllegalArgumentException(
                    "Invalid PNR format."
            );
        }
    }

    public static void positive(double v, String n) {
        if (v <= 0) {
            throw new IllegalArgumentException(
                    n + " must be greater than zero."
            );
        }
    }

    public static void future(LocalDateTime d) {
        if (d.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Date/time must be in the future."
            );
        }
    }

    private Validation() {
    }
}