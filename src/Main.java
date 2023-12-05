import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class Frame extends JFrame {
    private final SolarSystemPanel panel;
    public Frame() {
        setTitle("Solar System Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1000, 850);
        setVisible(true);
        ImageIcon image = new ImageIcon("");
        setIconImage(image.getImage());
        getContentPane().setBackground(new Color(0, 0, 0));

        panel = new SolarSystemPanel();
        add(panel);
    }

    class SolarSystemPanel extends JPanel {
        private final List<Planet> planets;

        public SolarSystemPanel() {
            planets = new ArrayList<>();
            initializePlanets();
        }

        private void initializePlanets() {
            //Sun
            Planet sun = new Planet(0, 0, 30, Color.YELLOW, 1.98892e30);
            sun.sun = true;

            // Mercury
            Planet mercury = new Planet(0.387 * Planet.AU, 0, 8, Color.GRAY, 3.30e23);
            mercury.setYVel(-47.4 * 1000);

            // Venus
            Planet venus = new Planet(0.723 * Planet.AU, 0, 14, Color.WHITE, 4.8685e24);
            venus.setYVel(-35.02 * 1000);

            // Earth
            Planet earth = new Planet(-Planet.AU, 0, 16, Color.BLUE, 5.9742e24);
            earth.setYVel(29.783 * 1000);

            // Mars
            Planet mars = new Planet(-1.524 * Planet.AU, 0, 12, Color.RED, 6.39e23);
            mars.setYVel(24.077 * 1000);

            planets.add(sun);
            planets.add(mercury);
            planets.add(venus);
            planets.add(earth);
            planets.add(mars);

            Timer timer = new Timer(16, e -> repaint());
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            Graphics2D g2d = (Graphics2D) g;

            for (Planet planet : planets) {
                planet.updatePosition(planets);
                planet.draw(g2d, getWidth(), getHeight());
            }
        }
    }
}

class Planet {
    public static final double AU = 149.6e6 * 1000;
    public static final double G = 6.67428e-11;
    public static final double SCALE = 250 / AU; // 1AU = 100 pixels
    public static final double TIMESTEP = 3600 * 24; // 1 day

    private double x, y;
    private final int radius;
    private final Color color;
    private final double mass;

    private final List<Point2D.Double> orbit;
    public boolean sun;
    private double distanceToSun;

    private double xVel, yVel;

    public Planet(double x, double y, int radius, Color color, double mass) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.mass = mass;

        this.orbit = new ArrayList<>();
        this.sun = false;
        this.distanceToSun = 0;

        this.xVel = 0;
        this.yVel = 0;
    }

    public void draw(Graphics2D g2d, int width, int height) {
        int x = (int) (this.x * SCALE + width / 2);
        int y = (int) (this.y * SCALE + height / 2);

        g2d.setColor(this.color);

        if (orbit.size() > 2) {
            for (int i = 0; i < orbit.size() - 1; i++) {
                Point2D.Double point1 = orbit.get(i);
                Point2D.Double point2 = orbit.get(i + 1);
                int x1 = (int) (point1.x * SCALE + width / 2);
                int y1 = (int) (point1.y * SCALE + height / 2);
                int x2 = (int) (point2.x * SCALE + width / 2);
                int y2 = (int) (point2.y * SCALE + height / 2);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

        if (!this.sun) {
            g2d.setColor(Color.WHITE);
            String distanceText = String.format("       %.2f AU", this.distanceToSun / AU);
            g2d.drawString(distanceText, x, y);
        }
    }

    public Point2D.Double attraction(Planet other) {
        double distanceX = other.x - this.x;
        double distanceY = other.y - this.y;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (other.sun) {
            this.distanceToSun = distance;
        }

        if (distance < 1e-10) {
            distance = 1e-10;
        }

        double forceMagnitude = Planet.G * this.mass * other.mass / (distance * distance);

        double forceX = forceMagnitude * distanceX / distance;
        double forceY = forceMagnitude * distanceY / distance;

        return new Point2D.Double(forceX, forceY);
    }

    public void updatePosition(List<Planet> planets) {
        double totalFx = 0;
        double totalFy = 0;

        for (Planet planet : planets) {
            if (this == planet) {
                continue;
            }

            Point2D.Double force = attraction(planet);
            totalFx += force.x;
            totalFy += force.y;
        }

        this.xVel += totalFx / this.mass * TIMESTEP;
        this.yVel += totalFy / this.mass * TIMESTEP;

        this.x += this.xVel * TIMESTEP;
        this.y += this.yVel * TIMESTEP;
        this.orbit.add(new Point2D.Double(this.x, this.y));

        if (orbit.size() > 680) {
            orbit.remove(0);
        }
    }

    public void setYVel(double yVel) {
        this.yVel = yVel;
    }
}


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Frame();
            }
        });
    }
}