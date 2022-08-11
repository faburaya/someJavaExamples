package com.examples.swingtest;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.createWindow();
        });
    }

    public JFrame createWindow() {
        JLabel label = new JLabel("Dast ist eine Testanwedung mit Swing");
        JButton button = new JButton("Klick hier!");
        JFrame frame = new JFrame("Swing Testanwendung");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(label);
        frame.add(button);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
}
