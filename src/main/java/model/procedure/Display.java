package model.procedure;

import javax.swing.JFrame;

import model.Board;

import java.awt.*;
import javax.swing.*;

public class Display {

    private JFrame frame;
    private Canvas canvas;
    private int height, width;
    public static final int GRIDSIZE = 100;

    public Display() {
        frame = new JFrame("4 - gewinnt");
        height = (Board.HEIGHT) * GRIDSIZE + 1;
        width = (Board.WIDTH) * GRIDSIZE + 1;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setBackground(new Color(42, 55, 71));
        frame.add(canvas);
        frame.pack();
    }

    public int getHeight() {
        if (frame == null) return 1080; 
        return frame.getHeight();
    }
    
    public int getWidth() {
        if (frame == null) return 1920; 
        return frame.getWidth();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getJFrame() {
        return frame;
    }
}