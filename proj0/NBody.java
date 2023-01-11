public class NBody {
    private static final String BACKGROUND = "images/starfield.jpg";

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        int time = 0;
        while (time <= T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                double tempXForce = planets[i].calcNetForceExertedByX(planets);
                double tempYForce = planets[i].calcNetForceExertedByY(planets);
                xForces[i] = tempXForce;
                yForces[i] = tempYForce;
            }
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, BACKGROUND);
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }

    public static double readRadius(String fileName) {
        In in = new In(fileName);
        double radius = 0;
        in.readLine();
        radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int size = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[size];
        int i = 0;
        while (i <= size - 1) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String img = in.readString();
            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, img);
            i++;
        }
        return planets;
    }
}
