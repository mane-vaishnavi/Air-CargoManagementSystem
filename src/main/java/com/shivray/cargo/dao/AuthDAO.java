package com.shivray.cargo.dao;

import com.shivray.cargo.config.DB;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;

public class AuthDAO {

    public String[] login(String u, String p) throws SQLException {

    	
        String sql = "SELECT u.id, u.username, u.password_hash, u.full_name, r.name "
                   + "FROM users u "
                   + "JOIN roles r ON r.id = u.role_id "
                   + "WHERE u.username = ? AND u.status = 'ACTIVE'";

        try (
            Connection c = DB.get();
            PreparedStatement s = c.prepareStatement(sql)
        ) {
            s.setString(1, u);

            try (ResultSet r = s.executeQuery()) {

                if (!r.next() ||
                    !(r.getString(3).equals(p) ||
                      r.getString(3).equals(sha(p)))) {
                    return null;
                }

                return new String[]{
                    r.getString(1),
                    r.getString(2),
                    r.getString(5),
                    r.getString(4)
                };
            }
        }
    }

    private static String sha(String s) {
        try {
            byte[] b = MessageDigest
                    .getInstance("SHA-256")
                    .digest(s.getBytes(StandardCharsets.UTF_8));

            StringBuilder x = new StringBuilder();

            for (byte z : b) {
                x.append(String.format("%02x", z));
            }

            return x.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}