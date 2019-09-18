package com.jackchampagne;

public class Planet {

    public static double G = 1000;

    public int size;
    public int mass;
    public double x, y;
    public double dx, dy;

    public double ax, ay;

    public Planet(int mass, int xPos, int yPos, double xVel, double yVel) {
        this.mass = mass;
        this.x = xPos;
        this.y = yPos;
        this.dx = xVel;
        this.dy = yVel;

        this.ax = 0;
        this.ay = 0;

        if (mass < 200) {
            this.size = 2;
        } else {
            this.size = (int) (mass / 100.0);
        }
    }

    public void applyKinematics(double dT) {
        this.x += dx * dT;
        this.y += dy * dT;
    }

    double r;
    public void calcAccel(double dT) {
        this.ax = 0;
        this.ay = 0;
        for (Planet planet : Simulation.planets){
            if (planet != this) {
                r = distance(this.x, this.y, planet.x, planet.y);
                // Calculate force
                this.ax +=  G * planet.mass * (planet.x - this.x) / Math.pow(r, 3);
                this.ay +=  G * planet.mass * (planet.y - this.y) / Math.pow(r, 3);
            }
        }
        this.dx += ax * dT;
        this.dy += ay * dT;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}