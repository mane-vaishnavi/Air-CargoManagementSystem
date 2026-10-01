package com.shivray.cargo.ui;

import com.shivray.cargo.dao.AuthDAO;
import com.shivray.cargo.model.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField user =
            new JTextField("admin"); //admin---------------------------------

    private final JPasswordField pass =
            new JPasswordField("admin"); //admin------------------------------

    private final Color NAVY =
            new Color(15, 23, 42);

    private final Color BLUE =
            new Color(37, 99, 235);

    public LoginFrame() {

        setTitle(
                "Shivray International | Secure Login"
        );

        setSize(940, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                EXIT_ON_CLOSE
        );

        setResizable(false);

        build();
    }

    private void build() {

        JPanel root =
                new JPanel(
                        new GridLayout(1, 2)
                );

        root.setBackground(Color.WHITE);

        root.add(branding());
        root.add(loginPanel());

        add(root);
    }

    // =========================================================
    // BRANDING
    // =========================================================

    private JPanel branding() {

        JPanel p =
                new JPanel(
                        new BorderLayout()
                );

        p.setBackground(NAVY);

        p.setBorder(
                new EmptyBorder(
                        55,
                        48,
                        45,
                        48
                )
        );

        // -----------------------------------------------------
        // Top Branding
        // -----------------------------------------------------

        JPanel top =
                new JPanel();

        top.setOpaque(false);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel plane =
                new JLabel("✈");

        plane.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        58
                )
        );

        plane.setForeground(
                new Color(96, 165, 250)
        );

        plane.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        top.add(plane);

        top.add(
                Box.createVerticalStrut(18)
        );

        JLabel title =
                new JLabel(
                        "SHIVRAY INTERNATIONAL"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        title.setForeground(
                Color.WHITE
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        top.add(title);

        top.add(
                Box.createVerticalStrut(8)
        );

        JLabel sub =
                new JLabel(
                        "AIR CARGO MANAGEMENT SYSTEM"
                );

        sub.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        sub.setForeground(
                new Color(148, 163, 184)
        );

        sub.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        top.add(sub);

        top.add(
                Box.createVerticalStrut(25)
        );

        JLabel desc =
                new JLabel(
                        "<html>"
                        + "Secure cargo operations, shipment tracking"
                        + "<br>"
                        + "and enterprise documentation."
                        + "</html>"
                );

        desc.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        desc.setForeground(
                new Color(203, 213, 225)
        );

        desc.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        top.add(desc);

        p.add(
                top,
                BorderLayout.NORTH
        );

        // -----------------------------------------------------
        // Footer
        // -----------------------------------------------------

        JLabel footer =
                new JLabel(
                        "JDBC  •  MySQL  •  REST API  •  PDF Reports"
                );

        footer.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        footer.setForeground(
                new Color(100, 116, 139)
        );

        p.add(
                footer,
                BorderLayout.SOUTH
        );

        return p;
    }

    // =========================================================
    // LOGIN PANEL
    // =========================================================

    private JPanel loginPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                new Color(248, 250, 252)
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        // -----------------------------------------------------
        // Secure Login
        // -----------------------------------------------------

        JPanel secure =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                0,
                                0
                        )
                );

        secure.setOpaque(false);

        JLabel dot =
                new JLabel("●");

        dot.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        10
                )
        );

        dot.setForeground(
                new Color(22, 163, 74)
        );

        JLabel secureText =
                new JLabel(
                        "  SECURE LOGIN"
                );

        secureText.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        secureText.setForeground(
                new Color(71, 85, 105)
        );

        secure.add(dot);
        secure.add(secureText);

        secure.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(secure);

        content.add(
                Box.createVerticalStrut(18)
        );

        // -----------------------------------------------------
        // Welcome
        // -----------------------------------------------------

        JLabel welcome =
                new JLabel(
                        "Welcome back"
                );

        welcome.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        welcome.setForeground(
                new Color(15, 23, 42)
        );

        welcome.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(welcome);

        content.add(
                Box.createVerticalStrut(6)
        );

        JLabel hint =
                new JLabel(
                        "Sign in to continue to the cargo console"
                );

        hint.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        hint.setForeground(
                new Color(100, 116, 139)
        );

        hint.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(hint);

        content.add(
                Box.createVerticalStrut(30)
        );

        // -----------------------------------------------------
        // Username
        // -----------------------------------------------------

        content.add(
                fieldLabel("USERNAME")
        );

        content.add(
                Box.createVerticalStrut(8)
        );

        styleField(user);

        content.add(user);

        content.add(
                Box.createVerticalStrut(20)
        );

        
        // -----------------------------------------------------
        // Password
        // -----------------------------------------------------

        content.add(
                fieldLabel("PASSWORD")
        );

        content.add(
                Box.createVerticalStrut(8)
        );

        styleField(pass);

        content.add(pass);

        content.add(
                Box.createVerticalStrut(25)
        );

        // -----------------------------------------------------
        // Sign In
        // -----------------------------------------------------

        JButton login =
                loginButton();

        content.add(login);

        content.add(
                Box.createVerticalStrut(22)
        );

        // -----------------------------------------------------
        // Demo Accounts
        // -----------------------------------------------------

        JLabel demoTitle =
                new JLabel(
                        "Demo Accounts"
                );

        demoTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        demoTitle.setForeground(
                new Color(71, 85, 105)
        );

        demoTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(demoTitle);

        content.add(
                Box.createVerticalStrut(5)
        );

        JLabel demo =
                new JLabel(
                      //  "admin / admin     •     staff / staff"
                );

        demo.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        demo.setForeground(
                new Color(100, 116, 139)
        );

        demo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        content.add(demo);

        panel.add(
                content,
                gbc
        );

        login.addActionListener(
                e -> doLogin()
        );

        getRootPane().setDefaultButton(
                login
        );

        return panel;
    }

    // =========================================================
    // LOGIN BUTTON
    // =========================================================

    private JButton loginButton() {

        JButton b =
                new JButton(
                        "SIGN IN  →"
                );

        b.setBackground(BLUE);

        b.setForeground(
                Color.WHITE
        );

        b.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        b.setFocusPainted(false);

        b.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        b.setMaximumSize(
                new Dimension(
                        326,
                        46
                )
        );

        b.setPreferredSize(
                new Dimension(
                        326,
                        46
                )
        );

        b.setBorder(
                BorderFactory.createEmptyBorder(
                        12,
                        20,
                        12,
                        20
                )
        );

        b.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return b;
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private JLabel fieldLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        11
                )
        );

        label.setForeground(
                new Color(51, 65, 85)
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    // =========================================================
    // FIELD STYLE
    // =========================================================

    private void styleField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        field.setMaximumSize(
                new Dimension(
                        326,
                        44
                )
        );

        field.setPreferredSize(
                new Dimension(
                        326,
                        44
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(203, 213, 225)
                        ),
                        new EmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );
    }

    // =========================================================
    // LOGIN ACTION
    // =========================================================

    private void doLogin() {

        try {

            String[] x =
                    new AuthDAO().login(
                            user.getText().trim(),
                            new String(
                                    pass.getPassword()
                            )
                    );

            if (x == null) {

                throw new Exception(
                        "Invalid username or password."
                );
            }

            Session.userId =
                    Long.parseLong(x[0]);

            Session.username =
                    x[1];

            Session.role =
                    x[2];

            Session.fullName =
                    x[3];

            dispose();

            new DashboardFrame()
                    .setVisible(true);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}