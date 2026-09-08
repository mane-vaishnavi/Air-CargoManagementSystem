package com.shivray.cargo.dao;

import com.shivray.cargo.config.DB;

import java.sql.*;
import java.util.*;

public class CargoDAO {

    // =========================
    // FLIGHTS
    // =========================
    public List<Object[]> flights() throws SQLException {

        return rows(
                "SELECT id, flight_code, flight_name, source, destination, " +
                "DATE_FORMAT(departure,'%Y-%m-%d %H:%i'), " +
                "DATE_FORMAT(arrival,'%Y-%m-%d %H:%i'), " +
                "cargo_capacity_kg, cargo_available_kg, status " +
                "FROM cargo_flights ORDER BY departure",
                10
        );
    }


    // =========================
    // SHIPPERS
    // =========================
    public List<Object[]> shippers() throws SQLException {

        return rows(
                "SELECT id, shipper_code, company_name, contact_name, email, " +
                "phone, city, country " +
                "FROM shippers ORDER BY id DESC",
                8
        );
    }


    // =========================
    // BOOKINGS
    // =========================
    public List<Object[]> bookings() throws SQLException {

        return rows(
                "SELECT b.id, b.pnr, b.booking_no, s.company_name, " +
                "f.flight_code, b.cargo_type, b.weight_kg, " +
                "b.freight_charge, b.booking_date, b.status " +
                "FROM cargo_bookings b " +
                "JOIN shippers s ON s.id = b.shipper_id " +
                "JOIN cargo_flights f ON f.id = b.flight_id " +
                "ORDER BY b.id DESC",
                10
        );
    }


    // =========================
    // SHIPMENT TRACKING
    // =========================
    public List<Object[]> tracking(String pnr) throws SQLException {

        return rows(
                "SELECT t.status, t.location, t.remarks, " +
                "DATE_FORMAT(t.event_time,'%Y-%m-%d %H:%i') " +
                "FROM shipment_tracking t " +
                "JOIN cargo_bookings b ON b.id = t.booking_id " +
                "WHERE b.pnr = ? " +
                "ORDER BY t.event_time",
                4,
                pnr
        );
    }


    // =========================
    // REPORTS
    // =========================
    public List<Object[]> reports(String where) throws SQLException {

        return rows(
                "SELECT b.pnr, b.booking_no, s.company_name, " +
                "f.flight_code, b.weight_kg, b.freight_charge, " +
                "b.booking_date, b.status " +
                "FROM cargo_bookings b " +
                "JOIN shippers s ON s.id = b.shipper_id " +
                "JOIN cargo_flights f ON f.id = b.flight_id " +
                where +
                " ORDER BY b.booking_date DESC",
                8
        );
    }


    // =========================
    // ADD FLIGHT
    // =========================
    public long addFlight(
            String code,
            String name,
            String src,
            String des,
            String dep,
            String arr,
            double cap
    ) throws SQLException {

        String q =
                "INSERT INTO cargo_flights(" +
                "flight_code, flight_name, source, destination, " +
                "departure, arrival, cargo_capacity_kg, cargo_available_kg" +
                ") VALUES(?,?,?,?,?,?,?,?)";

        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(
                        q,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            s.setString(1, code);
            s.setString(2, name);
            s.setString(3, src);
            s.setString(4, des);
            s.setString(5, dep);
            s.setString(6, arr);
            s.setDouble(7, cap);
            s.setDouble(8, cap);

            s.executeUpdate();

            return key(s);
        }
    }


    // =========================
    // ADD SHIPPER
    // =========================
    public long addShipper(
            String code,
            String company,
            String contact,
            String email,
            String phone,
            String address,
            String city,
            String country,
            String gst,
            String iec
    ) throws SQLException {

        String q =
                "INSERT INTO shippers(" +
                "shipper_code, company_name, contact_name, email, phone, " +
                "address, city, country, gst_no, iec_no" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(
                        q,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            String[] a = {
                    code,
                    company,
                    contact,
                    email,
                    phone,
                    address,
                    city,
                    country,
                    gst,
                    iec
            };

            for (int i = 0; i < a.length; i++) {
                s.setString(i + 1, a[i]);
            }

            s.executeUpdate();

            return key(s);
        }
    }


    // =========================
    // CREATE CARGO BOOKING
    // =========================
    public String book(
            long shipper,
            long flight,
            String type,
            String desc,
            double weight,
            double l,
            double w,
            double h,
            double value
    ) throws SQLException {

        String p =
                com.shivray.cargo.util.IdGenerator.pnr();

        String bk =
                com.shivray.cargo.util.IdGenerator.booking();


        String q =
                "INSERT INTO cargo_bookings(" +
                "pnr, booking_no, shipper_id, flight_id, cargo_type, " +
                "description, weight_kg, length_cm, width_cm, height_cm, " +
                "declared_value, freight_charge" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";


        try (Connection c = DB.get()) {

            c.setAutoCommit(false);

            try (PreparedStatement s = c.prepareStatement(q)) {

                // -------------------------
                // Booking details
                // -------------------------
                s.setString(1, p);
                s.setString(2, bk);
                s.setLong(3, shipper);
                s.setLong(4, flight);
                s.setString(5, type);
                s.setString(6, desc);
                s.setDouble(7, weight);
                s.setDouble(8, l);
                s.setDouble(9, w);
                s.setDouble(10, h);
                s.setDouble(11, value);


                // -------------------------
                // Freight calculation
                // Weight × 85
                // Declared Value × 0.25%
                // -------------------------
                double charge =
                        weight * 85 + value * 0.0025;

                s.setDouble(12, charge);

                s.executeUpdate();


                // -------------------------
                // Update available capacity
                // -------------------------
                try (
                        PreparedStatement u = c.prepareStatement(
                                "UPDATE cargo_flights " +
                                "SET cargo_available_kg = cargo_available_kg - ? " +
                                "WHERE id = ? " +
                                "AND cargo_available_kg >= ?"
                        )
                ) {

                    u.setDouble(1, weight);
                    u.setLong(2, flight);
                    u.setDouble(3, weight);

                    if (u.executeUpdate() != 1) {

                        throw new SQLException(
                                "Insufficient cargo capacity."
                        );
                    }
                }


                // -------------------------
                // Initial Tracking Entry
                // -------------------------
                try (
                        PreparedStatement t = c.prepareStatement(
                                "INSERT INTO shipment_tracking(" +
                                "booking_id, status, location, remarks" +
                                ") " +
                                "SELECT b.id, 'BOOKED', f.source, 'Booking created' " +
                                "FROM cargo_bookings b " +
                                "JOIN cargo_flights f " +
                                "ON f.id = b.flight_id " +
                                "WHERE b.pnr = ?"
                        )
                ) {

                    t.setString(1, p);

                    t.executeUpdate();
                }


                // -------------------------
                // Commit transaction
                // -------------------------
                c.commit();

                return p;

            } catch (Exception e) {

                c.rollback();

                throw e;

            } finally {

                c.setAutoCommit(true);
            }
        }
    }


    // =========================
    // CANCEL BOOKING + REFUND
    // =========================
    public double cancel(
            String pnr,
            String reason
    ) throws SQLException {

        String q =
                "SELECT id, freight_charge, status, flight_id, weight_kg " +
                "FROM cargo_bookings " +
                "WHERE pnr = ? FOR UPDATE";


        try (Connection c = DB.get()) {

            c.setAutoCommit(false);

            try {

                long id;
                double fare;
                double weight;
                long flight;
                String status;


                // -------------------------
                // Find booking
                // -------------------------
                try (PreparedStatement s = c.prepareStatement(q)) {

                    s.setString(1, pnr);

                    try (ResultSet r = s.executeQuery()) {

                        if (!r.next()) {

                            throw new SQLException(
                                    "PNR not found"
                            );
                        }

                        id = r.getLong(1);
                        fare = r.getDouble(2);
                        status = r.getString(3);
                        flight = r.getLong(4);
                        weight = r.getDouble(5);
                    }
                }


                // -------------------------
                // Check already cancelled
                // -------------------------
                if ("CANCELLED".equals(status)) {

                    throw new SQLException(
                            "Shipment is already cancelled."
                    );
                }


                // -------------------------
                // Refund = 80% of freight
                // -------------------------
                double refund = fare * 0.80;


                // -------------------------
                // Update booking status
                // -------------------------
                try (
                        PreparedStatement s = c.prepareStatement(
                                "UPDATE cargo_bookings " +
                                "SET status='CANCELLED' " +
                                "WHERE id=?"
                        )
                ) {

                    s.setLong(1, id);
                    s.executeUpdate();
                }


                // -------------------------
                // Restore cargo capacity
                // -------------------------
                try (
                        PreparedStatement s = c.prepareStatement(
                                "UPDATE cargo_flights " +
                                "SET cargo_available_kg = " +
                                "cargo_available_kg + ? " +
                                "WHERE id=?"
                        )
                ) {

                    s.setDouble(1, weight);
                    s.setLong(2, flight);

                    s.executeUpdate();
                }


                // -------------------------
                // Cancellation record
                // -------------------------
                long cid;

                try (
                        PreparedStatement s = c.prepareStatement(
                                "INSERT INTO cancellations(" +
                                "booking_id, cancellation_no, reason, refund_amount" +
                                ") VALUES(?,?,?,?)",
                                Statement.RETURN_GENERATED_KEYS
                        )
                ) {

                    s.setLong(1, id);

                    s.setString(
                            2,
                            com.shivray.cargo.util.IdGenerator.cancel()
                    );

                    s.setString(3, reason);
                    s.setDouble(4, refund);

                    s.executeUpdate();

                    cid = key(s);
                }


                // -------------------------
                // Refund record
                // -------------------------
                try (
                        PreparedStatement s = c.prepareStatement(
                                "INSERT INTO refunds(" +
                                "cancellation_id, refund_reference, amount" +
                                ") VALUES(?,?,?)"
                        )
                ) {

                    s.setLong(1, cid);

                    s.setString(
                            2,
                            com.shivray.cargo.util.IdGenerator.refund()
                    );

                    s.setDouble(3, refund);

                    s.executeUpdate();
                }


                // -------------------------
                // Commit
                // -------------------------
                c.commit();

                return refund;


            } catch (Exception e) {

                c.rollback();

                throw e;

            } finally {

                c.setAutoCommit(true);
            }
        }
    }


    // =========================
    // ADD TRACKING EVENT
    // =========================
    public void addTracking(
            String pnr,
            String status,
            String loc,
            String remarks
    ) throws SQLException {


        String q =
                "INSERT INTO shipment_tracking(" +
                "booking_id, status, location, remarks" +
                ") " +
                "SELECT id, ?, ?, ? " +
                "FROM cargo_bookings " +
                "WHERE pnr = ?";


        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(q)
        ) {

            s.setString(1, status);
            s.setString(2, loc);
            s.setString(3, remarks);
            s.setString(4, pnr);


            if (s.executeUpdate() != 1) {

                throw new SQLException(
                        "PNR not found."
                );
            }
        }


        // Update booking status
        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(
                        "UPDATE cargo_bookings " +
                        "SET status=? " +
                        "WHERE pnr=?"
                )
        ) {

            s.setString(1, status);
            s.setString(2, pnr);

            s.executeUpdate();
        }
    }


    // =========================
    // CREATE AIR WAYBILL
    // =========================
    public long createAwb(
            String pnr,
            String consignee,
            int pieces,
            String notes
    ) throws SQLException {


        String q =
                "INSERT INTO air_waybills(" +
                "booking_id, awb_no, origin, destination, consignee, " +
                "total_pieces, notes" +
                ") " +
                "SELECT b.id, ?, f.source, f.destination, ?, ?, ? " +
                "FROM cargo_bookings b " +
                "JOIN cargo_flights f " +
                "ON f.id = b.flight_id " +
                "WHERE b.pnr = ?";


        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(
                        q,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            s.setString(
                    1,
                    com.shivray.cargo.util.IdGenerator.awb()
            );

            s.setString(2, consignee);
            s.setInt(3, pieces);
            s.setString(4, notes);
            s.setString(5, pnr);


            if (s.executeUpdate() != 1) {

                throw new SQLException(
                        "PNR not found."
                );
            }


            return key(s);
        }
    }


    // =========================
    // GENERIC ROW FETCH
    // =========================
    private List<Object[]> rows(
            String q,
            int n,
            Object... p
    ) throws SQLException {


        List<Object[]> out =
                new ArrayList<>();


        try (
                Connection c = DB.get();
                PreparedStatement s = c.prepareStatement(q)
        ) {


            // Set parameters
            for (int i = 0; i < p.length; i++) {

                s.setObject(
                        i + 1,
                        p[i]
                );
            }


            try (ResultSet r = s.executeQuery()) {

                while (r.next()) {

                    Object[] x =
                            new Object[n];


                    for (int i = 0; i < n; i++) {

                        x[i] =
                                r.getObject(i + 1);
                    }


                    out.add(x);
                }
            }
        }


        return out;
    }


    // =========================
    // GENERATED KEY
    // =========================
    private long key(
            PreparedStatement s
    ) throws SQLException {


        try (ResultSet r =
                     s.getGeneratedKeys()) {


            if (!r.next()) {

                throw new SQLException(
                        "No generated key"
                );
            }


            return r.getLong(1);
        }
    }
}