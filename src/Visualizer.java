import edu.princeton.cs.algs4.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Visualizer implements MouseListener, MouseMotionListener {

    private PhotoEnhancer pe;
    private Picture p;

    private int originalMouseX;
    private int originalMouseY;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        originalMouseX = e.getX();
        originalMouseY = e.getY() - 44;

        Picture p = pe.picture();
        int[] bounds = pe.findBounds(originalMouseX, originalMouseY);

        paintBounds(p, bounds, new Point(originalMouseX, originalMouseY));
        System.out.println(originalMouseX);
        System.out.println(originalMouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int currentMouseX = e.getX();
        int currentMouseY = e.getY() - 44;

        int[] bounds = pe.findBounds(currentMouseX, currentMouseY);

        paintBounds(p, bounds, new Point(currentMouseX, currentMouseY));
        System.out.println(currentMouseX);
        System.out.println(currentMouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    JFrame frame;

    public Visualizer(PhotoEnhancer pe) {
        this.pe = pe;
        p = pe.picture();
    }

    public void visualize() {
        show(p);
        pe.picture().save("output.png");
    }

    private void paintBounds(Picture p, int[] bounds, Point point) {
        double originalEnergy = pe.energy(point.x, point.y);
//        double originalEnergy = pe.energy(originalMouseX, originalMouseY);
        for (int row = bounds[0]; row < bounds[1]; row++) {
            for (int col = bounds[2]; col < bounds[3]; col++) {
                // 4/26: added if statement.
                if (pe.energy(col, row) < originalEnergy) {
                    Color color = pe.findAverageColor(col, row);
                    p.set(col, row, color);
                }
            }
        }
        System.out.println("Color set");
    }

    public void show(Picture img) {
        if (frame == null) {
            frame = new JFrame();

            // mouseListeners
            frame.addMouseListener(this);
            frame.addMouseMotionListener(this);

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(img);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);

            frame.setContentPane(img.getJLabel());
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Output");
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        // draw
        frame.setContentPane(img.getJLabel());
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: PhotoEnhancer [filename]");
            return;
        }

        Picture samplePicture = new Picture(args[0]);
        PhotoEnhancer pe = new PhotoEnhancer(samplePicture);
        Visualizer scv = new Visualizer(pe);
        scv.show(samplePicture);

        while (true) {
            scv.visualize();
            scv.show(pe.picture());
        }
    }
}
