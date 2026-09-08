package com.shivray.cargo.ui;

import com.shivray.cargo.api.LiveApi;

import com.shivray.cargo.dao.CargoDAO;

import com.shivray.cargo.model.Session;

import com.shivray.cargo.reports.PdfReports;

import com.shivray.cargo.util.Validation;

import javax.swing.*;

import javax.swing.border.EmptyBorder;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.table.DefaultTableModel;

import java.awt.*;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.time.LocalTime;

import java.time.YearMonth;

import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;

import java.util.List;

public class DashboardFrame extends JFrame {

    private boolean isAdmin() {
        return Session.role != null && Session.role.equalsIgnoreCase("ADMIN");
    }

    private final CargoDAO dao = new CargoDAO();

    private final CardLayout cards = new CardLayout();

    private final JPanel content = new JPanel(cards);

    private final JLabel pageTitle = new JLabel("Dashboard");

    private final JLabel userLabel = new JLabel();

    private final Color NAVY = new Color(15, 23, 42);

    private final Color BLUE = new Color(37, 99, 235);

    private final Color BG = new Color(246, 248, 252);

    private final Color TEXT = new Color(30, 41, 59);

    // Subtle dashboard colors

    private final Color LIGHT_BLUE = new Color(210, 228, 252);

    private final Color LIGHT_GREEN = new Color(210, 244, 235);

    private final Color LIGHT_ORANGE = new Color(255, 226, 198);

    private final Color LIGHT_PURPLE = new Color(228, 220, 252);

    private final Color LIGHT_RED = new Color(255, 218, 224);

    private final Color LIGHT_CYAN = new Color(208, 239, 252);

    private final Color BORDER = new Color(191, 203, 218);





    // =========================================================

    // CONSTRUCTOR

    // =========================================================

    public DashboardFrame() {

        setTitle("Shivray International - Air Cargo Management System");

        setSize(1380, 850);

        setMinimumSize(new Dimension(1180, 720));

        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(BG);

        setLayout(new BorderLayout());

        add(sidebar(), BorderLayout.WEST);

        add(mainArea(), BorderLayout.CENTER);

        showPage("Dashboard");

    }





    // =========================================================

    // SIDEBAR

    // =========================================================

    private JPanel sidebar() {

        JPanel side = new JPanel(new BorderLayout());

        side.setPreferredSize(new Dimension(245, 850));

        side.setBackground(NAVY);

        side.setBorder(

                new EmptyBorder(20, 14, 18, 14)

        );





        // Brand

        JPanel brand = new JPanel(new BorderLayout(10, 0));

        brand.setOpaque(false);

        JLabel icon = new JLabel("✈", SwingConstants.CENTER);

        icon.setFont(

                new Font("SansSerif", Font.BOLD, 28)

        );

        icon.setForeground(Color.WHITE);

        icon.setPreferredSize(new Dimension(42, 42));





        JLabel name = new JLabel("SHIVRAY INTERNATIONAL");

        name.setFont(

                new Font("SansSerif", Font.BOLD, 14)

        );

        name.setForeground(Color.WHITE);





        JLabel sub = new JLabel("Air Cargo Management");

        sub.setFont(

                new Font("SansSerif", Font.PLAIN, 11)

        );

        sub.setForeground(

                new Color(148, 163, 184)

        );





        JPanel titles = new JPanel(

                new GridLayout(2, 1)

        );

        titles.setOpaque(false);

        titles.add(name);

        titles.add(sub);





        brand.add(icon, BorderLayout.WEST);

        brand.add(titles, BorderLayout.CENTER);

        side.add(brand, BorderLayout.NORTH);





        // Menu

        JPanel menu = new JPanel();

        menu.setOpaque(false);

        menu.setBorder(

                new EmptyBorder(28, 0, 0, 0)

        );

        menu.setLayout(

                new BoxLayout(menu, BoxLayout.Y_AXIS)

        );





        String[][] items = isAdmin() ? new String[][] {
                {"⌂", "Dashboard"},
                {"✈", "Cargo Flights"},
                {"▣", "Shippers"},
                {"▤", "Cargo Booking"},
                {"◉", "Shipment Tracking"},
                {"×", "Cancel & Refund"},
                {"▥", "Air Waybill"},
                {"▦", "Reports"},
                {"◌", "Live API"},
                {"↪", "Logout"}
        } : new String[][] {
                {"⌂", "Dashboard"},
                {"▣", "Shippers"},
                {"▤", "Cargo Booking"},
                {"◉", "Shipment Tracking"},
                {"▥", "Air Waybill"},
                {"▦", "Reports"},
                {"◌", "Live API"},
                {"↪", "Logout"}
        };





        for (String[] item : items) {

            JButton b =

                    navButton(item[0], item[1]);

            menu.add(b);

            menu.add(

                    Box.createVerticalStrut(5)

            );

        }





        side.add(menu, BorderLayout.CENTER);





        return side;

    }





    // =========================================================

    // NAVIGATION BUTTON

    // =========================================================

    private JButton navButton(

            String glyph,

            String text

    ) {

        JButton b =

                new JButton(

                        "  " + glyph + "   " + text

                );

        b.setHorizontalAlignment(

                SwingConstants.LEFT

        );

        b.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        13

                )

        );

        b.setForeground(

                new Color(191, 203, 218)

        );

        b.setBackground(NAVY);

        b.setBorder(

                new EmptyBorder(

                        12, 12, 12, 8

                )

        );

        b.setFocusPainted(false);

        b.setOpaque(true);

        b.setMaximumSize(

                new Dimension(

                        Integer.MAX_VALUE,

                        45

                )

        );

        b.addActionListener(e -> {
            if ("Logout".equals(text)) {
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                showPage(text);
            }
        });

        return b;

    }





    // =========================================================

    // MAIN AREA

    // =========================================================

    private JPanel mainArea() {

        JPanel root =

                new JPanel(new BorderLayout());

        root.setOpaque(false);

        root.add(

                topbar(),

                BorderLayout.NORTH

        );





        content.setOpaque(false);

        content.setBorder(

                new EmptyBorder(

                        20, 24, 24, 24

                )

        );





        content.add(

                dashboard(),

                "Dashboard"

        );

        content.add(

                flights(),

                "Cargo Flights"

        );

        content.add(

                shippers(),

                "Shippers"

        );

        content.add(

                booking(),

                "Cargo Booking"

        );

        content.add(

                tracking(),

                "Shipment Tracking"

        );

        content.add(

                cancel(),

                "Cancel & Refund"

        );

        content.add(

                awb(),

                "Air Waybill"

        );

        content.add(

                reports(),

                "Reports"

        );

        content.add(

                api(),

                "Live API"

        );





        root.add(

                content,

                BorderLayout.CENTER

        );

        return root;

    }





    // =========================================================

    // TOP BAR

    // =========================================================

    private JPanel topbar() {

        JPanel p =

                new JPanel(new BorderLayout());

        p.setBackground(Color.WHITE);

        p.setBorder(

                new EmptyBorder(

                        18, 24, 16, 24

                )

        );





        pageTitle.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        22

                )

        );

        pageTitle.setForeground(TEXT);

        p.add(

                pageTitle,

                BorderLayout.WEST

        );





        userLabel.setText(

                "●  " +

                Session.fullName +

                "   |   " +

                Session.role

        );

        userLabel.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        13

                )

        );

        userLabel.setForeground(

                new Color(71, 85, 105)

        );

        p.add(

                userLabel,

                BorderLayout.EAST

        );





        return p;

    }





    // =========================================================

    // SHOW PAGE

    // =========================================================

    private boolean isAdminOnlyPage(String name) {
        return "Cargo Flights".equals(name) || "Cancel & Refund".equals(name);
    }

    private void showPage(String name) {
        if (isAdminOnlyPage(name) && !isAdmin()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Access denied. This module is available only to Admin.",
                    "Access Restricted",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        pageTitle.setText(name);
        cards.show(content, name);
    }


        // DASHBOARD

    // =========================================================

    private JPanel dashboard() {

        JPanel p =

                new JPanel(

                        new BorderLayout(0, 18)

                );

        p.setOpaque(false);





        // Heading

        JPanel heading =

                new JPanel(new BorderLayout());

        heading.setOpaque(false);





        JPanel welcomePanel =

                new JPanel(

                        new GridLayout(2, 1, 0, 2)

                );

        welcomePanel.setOpaque(false);





        JLabel welcome =

                new JLabel(

                        "Welcome back, " +

                        Session.fullName

                );

        welcome.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        24

                )

        );

        welcome.setForeground(TEXT);





        JLabel welcomeSub =

                new JLabel(

                        "Manage your air cargo operations efficiently."

                );

        welcomeSub.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        12

                )

        );

        welcomeSub.setForeground(

                new Color(100, 116, 139)

        );





        welcomePanel.add(welcome);

        welcomePanel.add(welcomeSub);





        JLabel date =

                new JLabel(

                        LocalDateTime

                                .now()

                                .toLocalDate()

                                .toString()

                );

        date.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        12

                )

        );

        date.setForeground(

                new Color(100, 116, 139)

        );





        heading.add(

                welcomePanel,

                BorderLayout.WEST

        );

        heading.add(

                date,

                BorderLayout.EAST

        );





        p.add(

                heading,

                BorderLayout.NORTH

        );





        // Body

        JPanel body =

                new JPanel(

                        new BorderLayout(0, 18)

                );

        body.setOpaque(false);





        // Stats

        JPanel stats =

                new JPanel(

                        new GridLayout(1, 4, 14, 14)

                );

        stats.setOpaque(false);





        if (isAdmin()) {

            stats.add(

                    statCard(

                            "Cargo Flights",

                            "Schedule & Capacity",

                            "✈",

                            "Cargo Flights",

                            LIGHT_BLUE,

                            new Color(37, 99, 235)

                    )

            );

        } else {

            stats.add(

                    statCard(

                            "Air Waybill",

                            "Shipment Documents",

                            "▥",

                            "Air Waybill",

                            LIGHT_CYAN,

                            new Color(8, 145, 178)

                    )

            );

        }





        stats.add(

                statCard(

                        "Shippers",

                        "Exporter Master Data",

                        "▣",

                        "Shippers",

                        LIGHT_GREEN,

                        new Color(5, 150, 105)

                )

        );





        stats.add(

                statCard(

                        "Cargo Booking",

                        "Create & Manage PNR",

                        "▤",

                        "Cargo Booking",

                        LIGHT_ORANGE,

                        new Color(234, 88, 12)

                )

        );





        stats.add(

                statCard(

                        "Shipment Tracking",

                        "Live Movement History",

                        "◉",

                        "Shipment Tracking",

                        LIGHT_PURPLE,

                        new Color(124, 58, 237)

                )

        );





        body.add(

                stats,

                BorderLayout.NORTH

        );





        // Quick Operations

        JPanel actions =

                cardPanel(

                        new BorderLayout(14, 14)

                );

        actions.setBorder(

                new EmptyBorder(

                        20, 20, 20, 20

                )

        );





        actions.add(

                sectionTitle(

                        "Quick Operations",

                        "Frequently used cargo operations"

                ),

                BorderLayout.NORTH

        );





        JPanel quick =

                new JPanel(

                        new GridLayout(isAdmin() ? 2 : 2, isAdmin() ? 3 : 2, 12, 12)

                );

        quick.setOpaque(false);





        String[] qs = isAdmin()
                ? new String[] {
                        "Add Cargo Flight",
                        "Register Shipper",
                        "Create Cargo Booking",
                        "Track Shipment",
                        "Cancel & Refund",
                        "Generate Reports"
                }
                : new String[] {
                        "Register Shipper",
                        "Create Cargo Booking",
                        "Track Shipment",
                        "Generate Reports"
                };

        String[] pages = isAdmin()
                ? new String[] {
                        "Cargo Flights",
                        "Shippers",
                        "Cargo Booking",
                        "Shipment Tracking",
                        "Cancel & Refund",
                        "Reports"
                }
                : new String[] {
                        "Shippers",
                        "Cargo Booking",
                        "Shipment Tracking",
                        "Reports"
                };

        String[] icons = isAdmin()
                ? new String[] { "✈", "▣", "▤", "◉", "↻", "▦" }
                : new String[] { "▣", "▤", "◉", "▦" };

        Color[] backgrounds = isAdmin()
                ? new Color[] { LIGHT_BLUE, LIGHT_GREEN, LIGHT_ORANGE, LIGHT_PURPLE, LIGHT_RED, LIGHT_CYAN }
                : new Color[] { LIGHT_GREEN, LIGHT_ORANGE, LIGHT_PURPLE, LIGHT_CYAN };

        Color[] iconColors = isAdmin()
                ? new Color[] {
                        new Color(37, 99, 235),
                        new Color(5, 150, 105),
                        new Color(234, 88, 12),
                        new Color(124, 58, 237),
                        new Color(225, 29, 72),
                        new Color(8, 145, 178)
                }
                : new Color[] {
                        new Color(5, 150, 105),
                        new Color(234, 88, 12),
                        new Color(124, 58, 237),
                        new Color(8, 145, 178)
                };

        for (int i = 0; i < qs.length; i++) {

            quick.add(

                    operationCard(

                            icons[i],

                            qs[i],

                            pages[i],

                            backgrounds[i],

                            iconColors[i]

                    )

            );

        }





        actions.add(

                quick,

                BorderLayout.CENTER

        );





        body.add(

                actions,

                BorderLayout.CENTER

        );





        p.add(

                body,

                BorderLayout.CENTER

        );





        return p;

    }





    // =========================================================

    // STAT CARD

    // =========================================================

    private JPanel statCard(

            String title,

            String subtitle,

            String glyph,

            String page,

            Color cardColor,

            Color iconColor

    ) {

        JPanel c =

                cardPanel(

                        new BorderLayout(12, 0)

                );





        c.setBorder(

                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(

                                BORDER

                        ),

                        new EmptyBorder(

                                14, 14, 14, 14

                        )

                )

        );





        JPanel iconPanel =

                new JPanel(

                        new GridBagLayout()

                );

        iconPanel.setBackground(

                cardColor

        );

        iconPanel.setPreferredSize(

                new Dimension(48, 48)

        );





        JLabel g =

                new JLabel(

                        glyph,

                        SwingConstants.CENTER

                );

        g.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        22

                )

        );

        g.setForeground(iconColor);

        iconPanel.add(g);





        c.add(

                iconPanel,

                BorderLayout.WEST

        );





        JPanel txt =

                new JPanel(

                        new GridLayout(2, 1, 0, 3)

                );

        txt.setOpaque(false);





        JLabel t =

                new JLabel(title);

        t.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        14

                )

        );

        t.setForeground(TEXT);





        JLabel s =

                new JLabel(subtitle);

        s.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        11

                )

        );

        s.setForeground(

                new Color(100, 116, 139)

        );





        txt.add(t);

        txt.add(s);





        c.add(

                txt,

                BorderLayout.CENTER

        );





        c.setCursor(

                Cursor.getPredefinedCursor(

                        Cursor.HAND_CURSOR

                )

        );





        c.addMouseListener(

                new java.awt.event.MouseAdapter() {

                    @Override

                    public void mouseEntered(

                            java.awt.event.MouseEvent e

                    ) {

                        c.setBackground(

                                cardColor

                        );

                    }





                    @Override

                    public void mouseExited(

                            java.awt.event.MouseEvent e

                    ) {

                        c.setBackground(

                                Color.WHITE

                        );

                    }





                    @Override

                    public void mouseClicked(

                            java.awt.event.MouseEvent e

                    ) {

                        showPage(page);

                    }

                }

        );





        return c;

    }





    // =========================================================

    // QUICK OPERATION CARD

    // =========================================================

    private JButton operationCard(

            String glyph,

            String title,

            String page,

            Color background,

            Color iconColor

    ) {

        JButton b = new JButton();

        b.setLayout(

                new BorderLayout(10, 0)

        );

        b.setBackground(background);

        b.setBorder(

                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(

                                BORDER

                        ),

                        new EmptyBorder(

                                10, 12, 10, 12

                        )

                )

        );

        b.setFocusPainted(false);

        b.setOpaque(true);

        b.setCursor(

                Cursor.getPredefinedCursor(

                        Cursor.HAND_CURSOR

                )

        );





        JPanel iconPanel =

                new JPanel(

                        new GridBagLayout()

                );

        iconPanel.setBackground(

                Color.WHITE

        );

        iconPanel.setPreferredSize(

                new Dimension(42, 42)

        );





        JLabel icon =

                new JLabel(

                        glyph,

                        SwingConstants.CENTER

                );

        icon.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        19

                )

        );

        icon.setForeground(iconColor);

        iconPanel.add(icon);





        JPanel textPanel =

                new JPanel(

                        new GridLayout(2, 1, 0, 2)

                );

        textPanel.setOpaque(false);





        JLabel titleLabel =

                new JLabel(title);

        titleLabel.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        12

                )

        );

        titleLabel.setForeground(TEXT);





        JLabel subLabel =

                new JLabel(

                        getOperationSubtitle(title)

                );

        subLabel.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        10

                )

        );

        subLabel.setForeground(

                new Color(71, 85, 105)

        );





        textPanel.add(titleLabel);

        textPanel.add(subLabel);





        JLabel arrow =

                new JLabel("›");

        arrow.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        22

                )

        );

        arrow.setForeground(iconColor);





        b.add(

                iconPanel,

                BorderLayout.WEST

        );

        b.add(

                textPanel,

                BorderLayout.CENTER

        );

        b.add(

                arrow,

                BorderLayout.EAST

        );





        b.addActionListener(

                e -> showPage(page)

        );





        return b;

    }





    private String getOperationSubtitle(

            String title

    ) {

        switch (title) {

            case "Add Cargo Flight":

                return "Schedule & manage flights";

            case "Register Shipper":

                return "Add exporter / shipper details";

            case "Create Cargo Booking":

                return "Create and manage PNR";

            case "Track Shipment":

                return "View shipment movement";

            case "Cancel & Refund":

                return "Manage cancellations";

            case "Generate Reports":

                return "View and export PDF reports";

            default:

                return "Cargo operation";

        }

    }





    // =========================================================

    // CARGO FLIGHTS

    // =========================================================

    private JPanel flights() {

        JPanel p =

                modulePanel(

                        "Cargo Flight Schedule",

                        "Manage routes, cargo capacity and flight status."

                );





        String[] cols = {

                "ID",

                "Code",

                "Flight",

                "Source",

                "Destination",

                "Departure",

                "Arrival",

                "Capacity KG",

                "Available KG",

                "Status"

        };





        DefaultTableModel m =

                model(cols);

        JTable table =

                table(m);





        JTextField[] f =

                fields(6);





        String[] labels = {

                "Flight Code",

                "Flight Name",

                "Source",

                "Destination",

                "Departure (yyyy-MM-dd HH:mm)",

                "Arrival (yyyy-MM-dd HH:mm)"

        };





        JPanel form =

                formGrid(labels, f);





        JTextField cap =

                new JTextField("10000");





        addField(

                form,

                "Capacity KG",

                cap

        );





        JButton add =

                primaryButton("+ Add Flight");

        JButton refresh =

                outlineButton("↻ Refresh");





        JPanel actions =

                new JPanel(

                        new FlowLayout(

                                FlowLayout.RIGHT,

                                8,

                                0

                        )

                );

        actions.setOpaque(false);

        actions.add(refresh);

        actions.add(add);





        JPanel north =

                new JPanel(

                        new BorderLayout(0, 12)

                );

        north.setOpaque(false);

        north.add(

                form,

                BorderLayout.CENTER

        );

        north.add(

                actions,

                BorderLayout.SOUTH

        );





        p.add(

                north,

                BorderLayout.NORTH

        );

        p.add(

                tableScroll(table),

                BorderLayout.CENTER

        );





        add.addActionListener(e -> {

            try {

                for (int i = 0; i < f.length; i++) {

                    Validation.required(

                            f[i].getText(),

                            labels[i]

                    );

                }





                Validation.positive(

                        Double.parseDouble(

                                cap.getText()

                        ),

                        "Capacity"

                );





                dao.addFlight(

                        f[0].getText(),

                        f[1].getText(),

                        f[2].getText(),

                        f[3].getText(),

                        f[4].getText(),

                        f[5].getText(),

                        Double.parseDouble(

                                cap.getText()

                        )

                );





                load(

                        m,

                        dao.flights()

                );





                info(

                        "Flight added successfully."

                );





            } catch (Exception x) {

                err(x);

            }

        });





        refresh.addActionListener(

                e -> refresh(

                        m,

                        () -> dao.flights()

                )

        );





        refresh(

                m,

                () -> dao.flights()

        );





        return p;

    }





    // =========================================================

    // SHIPPERS

    // =========================================================

    private JPanel shippers() {

        JPanel p =

                modulePanel(

                        "Exporter / Shipper Management",

                        "Maintain verified shipper and exporter master data."

                );





        String[] cols = {

                "ID",

                "Code",

                "Company",

                "Contact",

                "Email",

                "Phone",

                "City",

                "Country"

        };





        DefaultTableModel m =

                model(cols);

        JTable table =

                table(m);





        String[] labels = {

                "Shipper Code",

                "Company Name",

                "Contact Name",

                "Email",

                "Phone",

                "Address",

                "City",

                "Country",

                "GST No",

                "IEC No"

        };





        JTextField[] f =

                fields(labels.length);





        JPanel form =

                formGrid(labels, f);





        JButton add =

                primaryButton(

                        "+ Register Shipper"

                );

        JButton refresh =

                outlineButton(

                        "↻ Refresh"

                );





        JPanel a =

                new JPanel(

                        new FlowLayout(

                                FlowLayout.RIGHT,

                                8,

                                0

                        )

                );

        a.setOpaque(false);

        a.add(refresh);

        a.add(add);





        JPanel north =

                new JPanel(

                        new BorderLayout(0, 12)

                );

        north.setOpaque(false);

        north.add(

                form,

                BorderLayout.CENTER

        );

        north.add(

                a,

                BorderLayout.SOUTH

        );





        p.add(

                north,

                BorderLayout.NORTH

        );

        p.add(

                tableScroll(table),

                BorderLayout.CENTER

        );





        add.addActionListener(e -> {

            try {

                for (int i = 0; i < f.length; i++) {

                    Validation.required(

                            f[i].getText(),

                            labels[i]

                    );

                }





                Validation.email(

                        f[3].getText()

                );

                Validation.phone(

                        f[4].getText()

                );





                dao.addShipper(

                        f[0].getText(),

                        f[1].getText(),

                        f[2].getText(),

                        f[3].getText(),

                        f[4].getText(),

                        f[5].getText(),

                        f[6].getText(),

                        f[7].getText(),

                        f[8].getText(),

                        f[9].getText()

                );





                load(

                        m,

                        dao.shippers()

                );





                info(

                        "Shipper registered successfully."

                );





            } catch (Exception x) {

                err(x);

            }

        });





        refresh.addActionListener(

                e -> refresh(

                        m,

                        () -> dao.shippers()

                )

        );





        refresh(

                m,

                () -> dao.shippers()

        );





        return p;

    }





    // =========================================================

    // CARGO BOOKING

    // =========================================================

    private JPanel booking() {

        JPanel p =

                modulePanel(

                        "Cargo Booking Management",

                        "Create cargo reservations with automatic PNR, charge calculation and capacity validation."

                );





        JPanel form =

                cardPanel(

                        new GridBagLayout()

                );

        form.setBorder(

                new EmptyBorder(

                        24, 28, 24, 28

                )

        );





        GridBagConstraints g =

                new GridBagConstraints();

        g.insets =

                new Insets(

                        7, 7, 7, 7

                );

        g.fill =

                GridBagConstraints.HORIZONTAL;

        g.weightx = 1;





        JTextField ship =

                new JTextField("1");

        JTextField flight =

                new JTextField("1");

        JTextField type =

                new JTextField("General Cargo");

        JTextField desc =

                new JTextField();

        JTextField wt =

                new JTextField();

        JTextField l =

                new JTextField("10");

        JTextField w =

                new JTextField("10");

        JTextField h =

                new JTextField("10");

        JTextField val =

                new JTextField("0");





        JTextField[] fs = {

                ship,

                flight,

                type,

                desc,

                wt,

                l,

                w,

                h,

                val

        };





        String[] ls = {

                "Shipper ID",

                "Flight ID",

                "Cargo Type",

                "Description",

                "Weight (KG)",

                "Length (CM)",

                "Width (CM)",

                "Height (CM)",

                "Declared Value (INR)"

        };





        for (int i = 0; i < fs.length; i++) {

            g.gridx = 0;

            g.gridy = i;

            form.add(

                    label(ls[i]),

                    g

            );





            g.gridx = 1;

            form.add(

                    fs[i],

                    g

            );

        }





        JLabel charge =

                new JLabel(

                        "Freight is calculated automatically: weight × ₹85 + declared value × 0.25%"

                );

        charge.setForeground(

                new Color(71, 85, 105)

        );





        g.gridx = 0;

        g.gridy = fs.length;

        g.gridwidth = 2;





        form.add(

                charge,

                g

        );





        JButton b =

                primaryButton(

                        "CREATE CARGO BOOKING"

                );





        g.gridy++;





        form.add(

                b,

                g

        );





        b.addActionListener(e -> {

            try {

                for (int i = 0; i < fs.length; i++) {

                    Validation.required(

                            fs[i].getText(),

                            ls[i]

                    );

                }





                double weight =

                        Double.parseDouble(

                                wt.getText()

                        );





                Validation.positive(

                        weight,

                        "Weight"

                );





                String pnr =

                        dao.book(

                                Long.parseLong(

                                        ship.getText()

                                ),

                                Long.parseLong(

                                        flight.getText()

                                ),

                                type.getText(),

                                desc.getText(),

                                weight,

                                Double.parseDouble(

                                        l.getText()

                                ),

                                Double.parseDouble(

                                        w.getText()

                                ),

                                Double.parseDouble(

                                        h.getText()

                                ),

                                Double.parseDouble(

                                        val.getText()

                                )

                        );





                info(

                        "Booking created successfully.\nPNR: "

                                + pnr

                );





            } catch (Exception x) {

                err(x);

            }

        });





        p.add(

                form,

                BorderLayout.CENTER

        );





        return p;

    }





    // =========================================================

    // SHIPMENT TRACKING

    // =========================================================

    private JPanel tracking() {

        JPanel p =

                modulePanel(

                        "Shipment Tracking",

                        "Search PNR history and add operational tracking events."

                );





        /*

         * IMPORTANT:

         * Earlier FlowLayout was causing the buttons

         * to become a thin blue strip.

         *

         * Now we use GridBagLayout with a separate

         * button row.

         */

        JPanel form =

                cardPanel(

                        new GridBagLayout()

                );





        form.setBorder(

                new EmptyBorder(

                        18, 24, 18, 24

                )

        );





        GridBagConstraints g =

                new GridBagConstraints();

        g.insets =

                new Insets(

                        7, 8, 7, 8

                );

        g.fill =

                GridBagConstraints.HORIZONTAL;

        g.weightx = 1;





        // Fields

        JTextField pnr =

                new JTextField(14);

        JTextField status =

                new JTextField(

                        "IN_TRANSIT",

                        14

                );

        JTextField loc =

                new JTextField(16);

        JTextField rem =

                new JTextField(25);





        // Buttons

        JButton search =

                primaryButton(

                        "Track PNR"

                );

        JButton update =

                primaryButton(

                        "+ Add Event"

                );





        // =====================================================

        // ROW 1

        // PNR + Status

        // =====================================================

        g.gridy = 0;





        g.gridx = 0;

        g.weightx = 0;

        form.add(

                label("PNR"),

                g

        );





        g.gridx = 1;

        g.weightx = 1;

        form.add(

                pnr,

                g

        );





        g.gridx = 2;

        g.weightx = 0;

        form.add(

                label("Status"),

                g

        );





        g.gridx = 3;

        g.weightx = 1;

        form.add(

                status,

                g

        );





        // =====================================================

        // ROW 2

        // Location + Remarks

        // =====================================================

        g.gridy = 1;





        g.gridx = 0;

        g.weightx = 0;

        form.add(

                label("Location"),

                g

        );





        g.gridx = 1;

        g.weightx = 1;

        form.add(

                loc,

                g

        );





        g.gridx = 2;

        g.weightx = 0;

        form.add(

                label("Remarks"),

                g

        );





        g.gridx = 3;

        g.weightx = 1;

        form.add(

                rem,

                g

        );





        // =====================================================

        // ROW 3 - BUTTONS

        // =====================================================

        JPanel buttonPanel =

                new JPanel(

                        new FlowLayout(

                                FlowLayout.RIGHT,

                                10,

                                4

                        )

                );

        buttonPanel.setOpaque(false);





        buttonPanel.add(search);

        buttonPanel.add(update);





        g.gridy = 2;

        g.gridx = 0;

        g.gridwidth = 4;

        g.weightx = 1;





        form.add(

                buttonPanel,

                g

        );





        // =====================================================

        // TABLE

        // =====================================================

        String[] c = {

                "Status",

                "Location",

                "Remarks",

                "Event Time"

        };





        DefaultTableModel m =

                model(c);





        JTable t =

                table(m);





        p.add(

                form,

                BorderLayout.NORTH

        );





        p.add(

                tableScroll(t),

                BorderLayout.CENTER

        );





        // =====================================================

        // TRACK PNR BUTTON

        // =====================================================

        search.addActionListener(e -> {

            try {

                Validation.required(

                        pnr.getText(),

                        "PNR"

                );

                Validation.pnr(

                        pnr.getText()

                );





                load(

                        m,

                        dao.tracking(

                                pnr.getText()

                        )

                );





            } catch (Exception x) {

                err(x);

            }

        });





        // =====================================================

        // ADD TRACKING EVENT BUTTON

        // =====================================================

        update.addActionListener(e -> {

            try {

                Validation.pnr(

                        pnr.getText()

                );





                Validation.required(

                        status.getText(),

                        "Status"

                );





                Validation.required(

                        loc.getText(),

                        "Location"

                );





                dao.addTracking(

                        pnr.getText(),

                        status.getText(),

                        loc.getText(),

                        rem.getText()

                );





                load(

                        m,

                        dao.tracking(

                                pnr.getText()

                        )

                );





                info(

                        "Tracking event added successfully."

                );





            } catch (Exception x) {

                err(x);

            }

        });





        return p;

    }





    // =========================================================

    // CANCEL & REFUND

    // =========================================================

    private JPanel cancel() {

        JPanel p =

                modulePanel(

                        "Cancellation & Refund",

                        "Cancel a shipment safely and process the configured refund transaction."

                );





        JPanel box =

                cardPanel(

                        new GridBagLayout()

                );





        box.setBorder(

                new EmptyBorder(

                        35, 55, 35, 55

                )

        );





        GridBagConstraints g =

                new GridBagConstraints();

        g.insets =

                new Insets(

                        10, 10, 10, 10

                );

        g.fill =

                GridBagConstraints.HORIZONTAL;

        g.weightx = 1;





        JTextField pnr =

                new JTextField();

        JTextField reason =

                new JTextField();





        g.gridx = 0;

        g.gridy = 0;

        box.add(

                label("PNR"),

                g

        );





        g.gridx = 1;

        box.add(

                pnr,

                g

        );





        g.gridx = 0;

        g.gridy++;





        box.add(

                label("Cancellation Reason"),

                g

        );





        g.gridx = 1;

        box.add(

                reason,

                g

        );





        JButton b =

                primaryButton(

                        "CANCEL SHIPMENT & PROCESS REFUND"

                );





        g.gridx = 1;

        g.gridy++;





        box.add(

                b,

                g

        );





        b.addActionListener(e -> {

            try {

                Validation.pnr(

                        pnr.getText()

                );





                Validation.required(

                        reason.getText(),

                        "Reason"

                );





                double r =

                        dao.cancel(

                                pnr.getText(),

                                reason.getText()

                        );





                info(

                        String.format(

                                "Shipment cancelled successfully.\nRefund processed: ₹ %.2f",

                                r

                        )

                );





            } catch (Exception x) {

                err(x);

            }

        });





        p.add(

                box,

                BorderLayout.NORTH

        );





        return p;

    }





    // =========================================================

    // AIR WAYBILL

    // =========================================================

    private JPanel awb() {

        JPanel p =

                modulePanel(

                        "Air Waybill & Documentation",

                        "Generate shipment documentation linked to the cargo booking."

                );





        JPanel box =

                cardPanel(

                        new GridBagLayout()

                );





        box.setBorder(

                new EmptyBorder(

                        30, 55, 30, 55

                )

        );





        GridBagConstraints g =

                new GridBagConstraints();

        g.insets =

                new Insets(

                        9, 9, 9, 9

                );

        g.fill =

                GridBagConstraints.HORIZONTAL;

        g.weightx = 1;





        JTextField pnr =

                new JTextField();

        JTextField cons =

                new JTextField();

        JTextField pieces =

                new JTextField("1");

        JTextField notes =

                new JTextField();





        String[] labs = {

                "PNR",

                "Consignee",

                "Total Pieces",

                "Notes"

        };





        JTextField[] fs = {

                pnr,

                cons,

                pieces,

                notes

        };





        for (int i = 0; i < fs.length; i++) {

            g.gridx = 0;

            g.gridy = i;





            box.add(

                    label(labs[i]),

                    g

            );





            g.gridx = 1;





            box.add(

                    fs[i],

                    g

            );

        }





        JButton b =

                primaryButton(

                        "GENERATE AIR WAYBILL"

                );





        g.gridx = 1;

        g.gridy = fs.length;





        box.add(

                b,

                g

        );





        b.addActionListener(e -> {

            try {

                Validation.pnr(

                        pnr.getText()

                );





                Validation.required(

                        cons.getText(),

                        "Consignee"

                );





                int pc =

                        Integer.parseInt(

                                pieces.getText()

                        );





                if (pc <= 0) {

                    throw new IllegalArgumentException(

                            "Total pieces must be positive."

                    );

                }





                long id =

                        dao.createAwb(

                                pnr.getText(),

                                cons.getText(),

                                pc,

                                notes.getText()

                        );





                info(

                        "AWB created successfully.\n"

                                + "Database ID: "

                                + id

                                + "\nUse Reports to generate PDFs."

                );





            } catch (Exception x) {

                err(x);

            }

        });





        p.add(

                box,

                BorderLayout.NORTH

        );





        return p;

    }





    // =========================================================

    // REPORTS

    // =========================================================

    private JPanel reports() {

        JPanel p =

                modulePanel(

                        "Reports & Analytics",

                        "Generate daily, monthly and status-based shipment reports as PDF."

                );





        JPanel grid =

                cardPanel(

                        new GridLayout(

                                0, 3, 14, 14

                        )

                );





        grid.setBorder(

                new EmptyBorder(

                        22, 22, 22, 22

                )

        );





        String[] names = {

                "All Shipments",

                "Confirmed / Booked",

                "In Transit",

                "Delivered",

                "Cancelled",

                "Monthly Shipments",

                "Daily Shipments",

                "Revenue Report",

                "Shipper Report",

                "Flight Cargo Report",

                "Refund Report",

                "AWB Report"

        };





        for (String n : names) {

            JButton b =

                    outlineButton(

                            "▦  " + n

                    );





            b.setHorizontalAlignment(

                    SwingConstants.LEFT

            );





            grid.add(b);





            b.addActionListener(e -> {

                try {

                    String where = "";





                    if (n.contains("Cancelled")) {

                        where =

                                "WHERE b.status='CANCELLED'";

                    } else if (

                            n.contains("In Transit")

                    ) {

                        where =

                                "WHERE b.status='IN_TRANSIT'";

                    } else if (

                            n.contains("Delivered")

                    ) {

                        where =

                                "WHERE b.status='DELIVERED'";

                    } else if (

                            n.contains("Confirmed")

                    ) {

                        where =

                                "WHERE b.status='BOOKED'";

                    } else if (

                            n.contains("Monthly")

                    ) {

                        where =

                                "WHERE YEAR(b.booking_date)="

                                + "YEAR(CURDATE()) "

                                + "AND MONTH(b.booking_date)="

                                + "MONTH(CURDATE())";

                    } else if (

                            n.contains("Daily")

                    ) {

                        where =

                                "WHERE DATE(b.booking_date)=CURDATE()";

                    }





                    PdfReports.generate(

                            n,

                            dao.reports(where)

                    );





                    info(

                            "PDF generated in the reports folder."

                    );





                } catch (Exception x) {

                    err(x);

                }

            });

        }





        p.add(

                grid,

                BorderLayout.CENTER

        );





        return p;

    }





    // =========================================================

    // LIVE API

    // =========================================================

    private JPanel api() {

        JPanel p =

                modulePanel(

                        "Live API Integration",

                        "Fetch live exchange-rate data using Java HttpClient."

                );





        JPanel box =

                cardPanel(

                        new BorderLayout(0, 14)

                );





        box.setBorder(

                new EmptyBorder(

                        22, 22, 22, 22

                )

        );





        JPanel top =

                new JPanel(

                        new FlowLayout(

                                FlowLayout.LEFT

                        )

                );

        top.setOpaque(false);





        JButton b =

                primaryButton(

                        "↻ Fetch Live EUR Rates"

                );





        top.add(b);





        box.add(

                top,

                BorderLayout.NORTH

        );





        JTextArea out =

                new JTextArea();





        out.setEditable(false);





        out.setFont(

                new Font(

                        "Monospaced",

                        Font.PLAIN,

                        12

                )

        );





        out.setMargin(

                new Insets(

                        12, 12, 12, 12

                )

        );





        box.add(

                new JScrollPane(out),

                BorderLayout.CENTER

        );





        p.add(

                box,

                BorderLayout.CENTER

        );





        b.addActionListener(e -> {

            try {

                out.setText(

                        new LiveApi()

                                .exchangeRates("EUR")

                );





            } catch (Exception x) {

                out.setText(

                        "API request failed:\n"

                                + x.getMessage()

                );

            }

        });





        return p;

    }





    // =========================================================

    // MODULE PANEL

    // =========================================================

    private JPanel modulePanel(

            String title,

            String subtitle

    ) {

        JPanel p =

                new JPanel(

                        new BorderLayout(0, 16)

                );

        p.setOpaque(false);





        p.add(

                sectionTitle(

                        title,

                        subtitle

                ),

                BorderLayout.NORTH

        );





        return p;

    }





    // =========================================================

    // SECTION TITLE

    // =========================================================

    private JPanel sectionTitle(

            String title,

            String subtitle

    ) {

        JPanel p =

                new JPanel(

                        new GridLayout(2, 1)

                );

        p.setOpaque(false);





        JLabel a =

                new JLabel(title);

        a.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        20

                )

        );

        a.setForeground(TEXT);





        JLabel b =

                new JLabel(subtitle);

        b.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        12

                )

        );

        b.setForeground(

                new Color(100, 116, 139)

        );





        p.add(a);

        p.add(b);





        return p;

    }





    // =========================================================

    // CARD PANEL

    // =========================================================

    private JPanel cardPanel(

            LayoutManager lm

    ) {

        JPanel p =

                new JPanel(lm);

        p.setBackground(

                Color.WHITE

        );

        return p;

    }





    // =========================================================

    // LABEL

    // =========================================================

    private JLabel label(

            String s

    ) {

        JLabel l =

                new JLabel(s);

        l.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        12

                )

        );

        l.setForeground(

                new Color(51, 65, 85)

        );





        return l;

    }





    private JLabel section(

            String s

    ) {

        return label(s);

    }





    // =========================================================

    // PRIMARY BUTTON

    // =========================================================

    private JButton primaryButton(

            String s

    ) {

        JButton b =

                new JButton(s);

        b.setBackground(BLUE);

        b.setForeground(

                Color.WHITE

        );

        b.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        12

                )

        );

        b.setFocusPainted(false);

        b.setBorder(

                new EmptyBorder(

                        10, 16, 10, 16

                )

        );





        return b;

    }





    // =========================================================

    // OUTLINE BUTTON

    // =========================================================

    private JButton outlineButton(

            String s

    ) {

        JButton b =

                new JButton(s);

        b.setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        12

                )

        );

        b.setFocusPainted(false);

        b.setBorder(

                new EmptyBorder(

                        10, 14, 10, 14

                )

        );





        return b;

    }





    // =========================================================

    // DATE & TIME PICKER

    // =========================================================

    private JPanel dateTimeEditor(JTextField field) {

        JPanel panel = new JPanel(new BorderLayout(4, 0));

        panel.setOpaque(false);

        panel.add(field, BorderLayout.CENTER);

        JButton dateButton = new JButton("📅");

        JButton timeButton = new JButton("◷");

        stylePickerButton(dateButton);

        stylePickerButton(timeButton);

        dateButton.setToolTipText("Select date");

        timeButton.setToolTipText("Select time");

        dateButton.addActionListener(e -> chooseDate(field));

        timeButton.addActionListener(e -> chooseTime(field));

        JPanel buttons = new JPanel(new GridLayout(1, 2, 3, 0));

        buttons.setOpaque(false);

        buttons.add(dateButton);

        buttons.add(timeButton);

        panel.add(buttons, BorderLayout.EAST);

        return panel;

    }

    private void stylePickerButton(JButton button) {

        button.setFont(new Font("SansSerif", Font.PLAIN, 14));

        button.setForeground(new Color(51, 65, 85));

        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setBorder(BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(new Color(191, 203, 218)),

                new EmptyBorder(2, 8, 2, 8)

        ));

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(42, 32));

    }

    private void chooseDate(JTextField target) {

        LocalDate initial = parseDate(target.getText());

        final JDialog dialog = new JDialog(this, "Select Date", true);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dialog.setSize(330, 310);

        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(8, 8));

        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        root.setBackground(Color.WHITE);

        final YearMonth[] month = {YearMonth.from(initial)};

        final LocalDate[] selected = {initial};

        JPanel header = new JPanel(new BorderLayout(8, 0));

        header.setOpaque(false);

        JButton previous = new JButton("‹");

        JButton next = new JButton("›");

        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);

        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        monthLabel.setForeground(TEXT);

        previous.setFocusPainted(false);

        next.setFocusPainted(false);

        header.add(previous, BorderLayout.WEST);

        header.add(monthLabel, BorderLayout.CENTER);

        header.add(next, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);

        JPanel calendar = new JPanel(new GridLayout(7, 7, 3, 3));

        calendar.setOpaque(false);

        root.add(calendar, BorderLayout.CENTER);

        Runnable refreshCalendar = () -> {

            calendar.removeAll();

            String name = month[0].getMonth().toString();

            monthLabel.setText(name.substring(0, 1) + name.substring(1).toLowerCase() + " " + month[0].getYear());

            String[] weekdays = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};

            for (String day : weekdays) {

                JLabel label = new JLabel(day, SwingConstants.CENTER);

                label.setFont(new Font("SansSerif", Font.BOLD, 11));

                label.setForeground(new Color(100, 116, 139));

                calendar.add(label);

            }

            LocalDate first = month[0].atDay(1);

            int offset = first.getDayOfWeek().getValue() - 1;

            for (int i = 0; i < offset; i++) calendar.add(new JLabel(""));

            for (int day = 1; day <= month[0].lengthOfMonth(); day++) {

                final int dayValue = day;

                JButton dayButton = new JButton(String.valueOf(day));

                dayButton.setFocusPainted(false);

                dayButton.setFont(new Font("SansSerif", Font.PLAIN, 12));

                dayButton.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

                dayButton.setBackground(Color.WHITE);

                LocalDate date = month[0].atDay(dayValue);

                if (date.equals(selected[0])) {

                    dayButton.setBackground(new Color(219, 234, 254));

                    dayButton.setForeground(new Color(30, 64, 175));

                }

                dayButton.addActionListener(e -> {

                    setDatePart(target, month[0].atDay(dayValue));

                    dialog.dispose();

                });

                calendar.add(dayButton);

            }

            calendar.revalidate();

            calendar.repaint();

        };

        previous.addActionListener(e -> { month[0] = month[0].minusMonths(1); refreshCalendar.run(); });

        next.addActionListener(e -> { month[0] = month[0].plusMonths(1); refreshCalendar.run(); });

        refreshCalendar.run();

        dialog.setContentPane(root);

        dialog.setVisible(true);

    }

    private void chooseTime(JTextField target) {

        LocalTime initial = parseTime(target.getText());

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(new EmptyBorder(12, 12, 4, 12));

        GridBagConstraints g = new GridBagConstraints();

        g.insets = new Insets(6, 6, 6, 6);

        JSpinner hour = new JSpinner(new SpinnerNumberModel(initial.getHour(), 0, 23, 1));

        JSpinner minute = new JSpinner(new SpinnerNumberModel(initial.getMinute(), 0, 59, 1));

        hour.setPreferredSize(new Dimension(90, 32));

        minute.setPreferredSize(new Dimension(90, 32));

        g.gridx = 0; g.gridy = 0; panel.add(new JLabel("Hour (00-23)"), g);

        g.gridx = 1; panel.add(hour, g);

        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Minute (00-59)"), g);

        g.gridx = 1; panel.add(minute, g);

        int result = JOptionPane.showConfirmDialog(this, panel, "Select Time", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            setTimePart(target, LocalTime.of((Integer) hour.getValue(), (Integer) minute.getValue()));

        }

    }

    private LocalDate parseDate(String value) {

        if (value != null && value.length() >= 10) {

            try { return LocalDate.parse(value.substring(0, 10)); }

            catch (DateTimeParseException ignored) { }

        }

        return LocalDate.now();

    }

    private LocalTime parseTime(String value) {

        if (value != null && value.length() >= 16) {

            try { return LocalTime.parse(value.substring(11, 16), DateTimeFormatter.ofPattern("HH:mm")); }

            catch (DateTimeParseException ignored) { }

        }

        return LocalTime.now().withSecond(0).withNano(0);

    }

    private void setDatePart(JTextField target, LocalDate date) {

        LocalTime time = parseTime(target.getText());

        target.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time.format(DateTimeFormatter.ofPattern("HH:mm")));

    }

    private void setTimePart(JTextField target, LocalTime time) {

        LocalDate date = parseDate(target.getText());

        target.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + time.format(DateTimeFormatter.ofPattern("HH:mm")));

    }

    // =========================================================

    // FIELDS

    // =========================================================

    private JTextField[] fields(

            int n

    ) {

        JTextField[] f =

                new JTextField[n];





        for (int i = 0; i < n; i++) {

            f[i] =

                    new JTextField();

        }





        return f;

    }





    // =========================================================

    // FORM GRID

    // =========================================================

    private JPanel formGrid(

            String[] labels,

            JTextField[] fields

    ) {

        JPanel p =

                new JPanel(

                        new GridLayout(

                                0, 4, 8, 8

                        )

                );

        p.setOpaque(false);





        for (int i = 0; i < labels.length; i++) {

            p.add(

                    label(labels[i])

            );

            if (labels[i].startsWith("Departure") || labels[i].startsWith("Arrival")) {

                p.add(dateTimeEditor(fields[i]));

            } else {

                p.add(fields[i]);

            }

        }





        return p;

    }





    private void addField(

            JPanel p,

            String name,

            JTextField f

    ) {

        p.add(

                label(name)

        );

        p.add(f);

    }





    // =========================================================

    // TABLE MODEL

    // =========================================================

    private DefaultTableModel model(

            String[] cols

    ) {

        return new DefaultTableModel(

                cols,

                0

        ) {

            @Override

            public boolean isCellEditable(

                    int r,

                    int c

            ) {

                return false;

            }

        };

    }





    // =========================================================

    // TABLE

    // =========================================================

    private JTable table(

            DefaultTableModel m

    ) {

        JTable t =

                new JTable(m);





        t.setRowHeight(28);





        t.setFont(

                new Font(

                        "SansSerif",

                        Font.PLAIN,

                        12

                )

        );





        t.getTableHeader().setFont(

                new Font(

                        "SansSerif",

                        Font.BOLD,

                        12

                )

        );





        t.getTableHeader().setBackground(

                new Color(241, 245, 249)

        );





        t.setShowVerticalLines(false);





        t.setGridColor(

                new Color(226, 232, 240)

        );





        t.setAutoCreateRowSorter(true);





        DefaultTableCellRenderer r =

                new DefaultTableCellRenderer();





        r.setBorder(

                new EmptyBorder(

                        0, 8, 0, 8

                )

        );





        t.setDefaultRenderer(

                Object.class,

                r

        );





        return t;

    }





    // =========================================================

    // TABLE SCROLL

    // =========================================================

    private JScrollPane tableScroll(

            JTable t

    ) {

        JScrollPane s =

                new JScrollPane(t);





        s.setBorder(

                BorderFactory.createLineBorder(

                        new Color(

                                226, 232, 240

                        )

                )

        );





        return s;

    }





    // =========================================================

    // LOAD TABLE DATA

    // =========================================================

    private void load(

            DefaultTableModel m,

            List<Object[]> rows

    ) {

        m.setRowCount(0);





        for (Object[] r : rows) {

            m.addRow(r);

        }

    }





    // =========================================================

    // REFRESH

    // =========================================================

    private void refresh(

            DefaultTableModel m,

            DataLoader loader

    ) {

        try {

            load(

                    m,

                    loader.load()

            );





        } catch (Exception x) {

            err(x);

        }

    }





    // =========================================================

    // DATA LOADER

    // =========================================================

    private interface DataLoader {

        List<Object[]> load()

                throws Exception;

    }





    // =========================================================

    // INFORMATION MESSAGE

    // =========================================================

    private void info(

            String s

    ) {

        // Custom success dialog with a green check mark

        JLabel message = new JLabel(

                "<html><div style='text-align:center;'>" +

                "<font color='#15803D' size='6'>✓</font><br>" +

                "<font color='#1E293B' size='4'>" +

                s.replace("\n", "<br>") +

                "</font></div></html>"

        );





        JOptionPane.showMessageDialog(

                this,

                message,

                "Shivray International - Success",

                JOptionPane.PLAIN_MESSAGE

        );





    }





    // =========================================================

    // ERROR MESSAGE

    // =========================================================

    private void err(

            Exception e

    ) {

        JOptionPane.showMessageDialog(

                this,

                e.getMessage() == null

                        ? e.toString()

                        : e.getMessage(),

                "Operation Failed",

                JOptionPane.ERROR_MESSAGE

        );

    }

}