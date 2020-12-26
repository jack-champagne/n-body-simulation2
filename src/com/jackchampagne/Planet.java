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

    private static Simulation.INTEGRATOR integrator = Simulation.INTEGRATOR.EULER; // Defaults to euler's method

    public static void setIntegrator(Simulation.INTEGRATOR newIntegrator) {
        integrator = newIntegrator;
    }

    public void update(double dTime) {
        switch(integrator) {
            case EULER:
                eulerStep(dTime);
                break;
            case SYMPLECTIC:
                symplecticStep(dTime);
                break;
            default:
            case LEAPFROG:
                leapfrogStep(dTime);
                break;
        }
    }

    // The eulerStep method uses the forwards euler method of approximating differential equation to update the locations of the planets.
    private void eulerStep(double dTime){
    }

    // The symplecticStep uses a simplectic method of integrating to approximate orbits and their energy conserving nature
    private void symplecticStep(double dTime) {
    }

    // The leapfrogStep uses leapfrog integration in order to approximate orbits and their energy conserving nature
    private boolean updatePos = true;
    private void leapfrogStep(double dTime) {
        if (updatePos) {
            this.dPos(dTime);
            updatePos = false;
        } else {
            this.dVel(dTime);
            updatePos = true;
        }
    }

    public void dPos(double dT) {
        if (this.mass >= 4000) {
            return;
        }

        this.x += dx * dT;
        this.y += dy * dT;
    }

    private void dVel(double dT) {
        calcAccelByForces();

        this.dx += this.ax * dT;
        this.dy += this.ay * dT;
    }

    private double r;
    public void calcAccelByForces() {
        double nextax = 0;
        double nextay = 0;
        for (Planet planet : Simulation.planets){
            if (planet != this) {
                r = distance(this.x, this.y, planet.x, planet.y);
                // Calculate force
                nextax +=  (G/100.0) * planet.mass * (planet.x - this.x) / Math.pow(r, 2);
                nextay +=  (G/100.0) * planet.mass * (planet.y - this.y) / Math.pow(r, 2);
            }
        }
        this.ax = nextax;
        this.ay = nextay;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}