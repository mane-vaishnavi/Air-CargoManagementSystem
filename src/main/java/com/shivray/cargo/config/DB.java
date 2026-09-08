package com.shivray.cargo.config;

import java.io.*;
import java.sql.*;
import java.util.*;

public final class DB {

    private static final Properties P = new Properties();

    static {
        try (
            InputStream in = DB.class
                    .getClassLoader()
                    .getResourceAsStream("app.properties")
        ) {
            P.load(in);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Cannot load app.properties",
                    e
            );
        }
    }

    public static Connection get() throws SQLException {

        return DriverManager.getConnection(
                P.getProperty("db.url"),
                P.getProperty("db.user"),
                P.getProperty("db.password")
        );
    }

    public static void close(AutoCloseable... c) {

        for (AutoCloseable x : c) {

            try {

                if (x != null) {
                    x.close();
                }

            } catch (Exception ignored) {
            }
        }
    }
}