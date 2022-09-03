package com.examples.swingtest;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class App {
    private static final Logger logger;

    static {
        logger = Logger.getLogger(App.class.getCanonicalName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.createWindow();
        });
    }

    private ImageIcon createIcon() {
        final String IMAGE_FILEPATH = "com/examples/swingtest/testimage.jpg";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(IMAGE_FILEPATH);
            byte[] imageData = inputStream.readAllBytes();
            return new ImageIcon(imageData);
        } catch (IOException ex) {
            logger.warning(String.format("Bild kann nicht aus '%s' geladen werden!", IMAGE_FILEPATH));
        }
        return new ImageIcon();
    }

    public JFrame createWindow() {
        JLabel label = new JLabel("Dast ist eine Testanwedung mit Swing");
        JLabel iconLabel = new JLabel(createIcon());
        JPanel panel = new JPanel();
        JLabel sizeLabel = new JLabel("Größe (%): ");
        JTextField sizeField = new JTextField();
        JLabel rotationLabel = new JLabel("Drehung (°): ");
        JTextField rotationField = new JTextField();
        JButton button = new JButton("Klick hier!");
        JFrame frame = new JFrame("Swing Testanwendung");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(iconLabel);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(rotationLabel);
        panel.add(rotationField);
        frame.add(button);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
}
