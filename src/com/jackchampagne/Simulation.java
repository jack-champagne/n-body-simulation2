package com.jackchampagne;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Simulation extends JPanel implements Runnable {

    public static enum INTEGRATOR {EULER, SYMPLECTIC, LEAPFROG}

    public JFrame myFrame = new JFrame();
    private JCheckBox velocityVectorsCheckbox;
    private JCheckBox forceVectorsCheckbox;
    private JSlider timeWarpSlider;
    private JSlider gSlider;

    private final int WIDTH = 800;
    private final int HEIGHT = 800;

    public static ArrayList<Planet> planets = new ArrayList<Planet>();

    public Simulation() {
        //planets.add(new Planet(120, 400, 400, -5, -2));
        //planets.add(new Planet(100, 100, 100, 2, 2));
        //planets.add(new Planet(120, 200, 200, 4, 4));
        //planets.add(new Planet(20, 210, 210, -310, 310));
        planets.add(new Planet(500, 200, 200, -400, 400));
        planets.add(new Planet(5000, 400, 400, 80, -80));
        initFrame();
    }

    public void initFrame() {
        velocityVectorsCheckbox = new JCheckBox("Velocity Vectors");
        forceVectorsCheckbox = new JCheckBox("Force Vectors");
        JLabel timeSliderLabel = new JLabel("Time Warp: ");
        timeWarpSlider = new JSlider(-100, 100);    // Creates a slider with a minimum of -100 and a maximum of 100 but corresponds with -1x and 1x speed
        timeWarpSlider.setValue(1);

        JLabel gSliderLabel = new JLabel("G: ");
        gSlider = new JSlider(0, 10000);    // Creates a slider with a minimum of 0 and a maximum of 10000 for G
        gSlider.setValue(7500);

        JPanel uiPanel = new JPanel();
        uiPanel.setLayout(new FlowLayout());
        uiPanel.add(velocityVectorsCheckbox);
        uiPanel.add(forceVectorsCheckbox);
        uiPanel.add(timeSliderLabel);
        uiPanel.add(timeWarpSlider);
        uiPanel.add(gSliderLabel);
        uiPanel.add(gSlider);

        this.setSize(800, 800);

        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(WIDTH, HEIGHT);
        myFrame.setResizable(false);

        myFrame.setLayout(new BorderLayout());
        myFrame.add(this, BorderLayout.CENTER);
        myFrame.add(uiPanel, BorderLayout.NORTH);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);

        uiPanel.invalidate();
    }

    // TODO: Rewrite
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0,0, WIDTH, HEIGHT);
        for (Planet planet : planets) {
            g.setColor(Color.black);
            g.fillOval((int) planet.x - planet.size / 2, (int) planet.y - planet.size / 2, planet.size, planet.size);

            if (velocityVectorsCheckbox.isSelected()) {
                g.setColor(Color.red);
                g.drawLine((int) planet.x, (int) planet.y, (int) (planet.x + planet.dx * 0.1), (int) (planet.y + planet.dy * 0.1));
            }
            if (forceVectorsCheckbox.isSelected()) {
                g.setColor(Color.cyan);
                g.drawLine((int) planet.x, (int) planet.y, (int) (planet.x + planet.ax * 0.1), (int) (planet.y + planet.ay * 0.1));
            }

            if (planet.mass == 500) {
                double ge = -(1000 * 5000 * 500)/Math.sqrt((planet.x - 400) * (planet.x - 400) + (planet.y - 400) * (planet.y - 400));
                double ve = (1.0/2.0) * 500 * (planet.dx * planet.dx + planet.dy * planet.dy);
                g.setColor(Color.red);
                g.drawString(((Double)ve).toString(), 500, 40);
                g.drawString(((Double)ge).toString(), 500, 60);

                g.drawString(((Double)(ve + ge)).toString(), 500, 90);
            }
        }
    }

    private final int UPS = 100;
    private final int MILLIS_DELAY = 1000 / UPS;
    private long lastUpdate = System.currentTimeMillis();
    private long now;
    public void run() {
        Planet.setIntegrator(Simulation.INTEGRATOR.LEAPFROG);

        while (true) {
            now = System.currentTimeMillis();
            if (now - lastUpdate > MILLIS_DELAY) {
                this.repaint();
                tick();
                lastUpdate = System.currentTimeMillis();
            }
        }

    }

    public void tick() {
        for (Planet planet : planets) {
            double dTime = (timeWarpSlider.getValue() / 100.0) * (now - lastUpdate) / 1000.0;
            planet.update((timeWarpSlider.getValue() / 100.0) * ((double)MILLIS_DELAY / 1000.0));
        }

        Planet.G = gSlider.getValue();
    }

    public static void main(String[] args) {
        Simulation mySim = new Simulation();
        Thread simThread = new Thread(mySim);
        simThread.start();


    }
}
