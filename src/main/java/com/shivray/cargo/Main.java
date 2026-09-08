package com.shivray.cargo;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.shivray.cargo.ui.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        try {
            FlatIntelliJLaf.setup();
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(
                () -> new LoginFrame().setVisible(true)
        );
    }
}