import java.util.Arrays;

public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    static final double G = 6.67e-11;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double dy = p.yyPos - this.yyPos;
        return Math.sqrt(Math.pow(dx, 2d) + Math.pow(dy, 2d));
    }

    public double calcForceExertedBy(Planet p) {
        double r = calcDistance(p);
        return G * this.mass * p.mass / Math.pow(r, 2d);
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        return calcForceExertedBy(p) * dx / calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        return calcForceExertedBy(p) * dy / calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        planets = Arrays.stream(planets).filter(planet -> !planet.equals(this)).toArray(Planet[]::new);
        double netX = 0;
        for (Planet planet : planets) {
            netX += calcForceExertedByX(planet);
        }
        return netX;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        planets = Arrays.stream(planets).filter(planet -> !planet.equals(this)).toArray(Planet[]::new);
        double netY = 0;
        for (Planet planet : planets) {
            netY += calcForceExertedByY(planet);
        }
        return netY;
    }

    public void update(double dt, double fX, double fY) {
        double aX = fX / mass;
        xxVel += aX * dt;
        xxPos += xxVel * dt;
        double aY = fY / mass;
        yyVel += aY * dt;
        yyPos += yyVel * dt;

    }
    public void draw() {
        StdDraw.picture(xxPos,yyPos,"images/"+imgFileName);
    }

}
