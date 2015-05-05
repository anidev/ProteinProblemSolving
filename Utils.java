/**
 * Intro to Problem Solving - Onufriev
 * Final Project
 * 
 * Language: Java
 * 
 * Authors:
 * Rupin Bhalla, Anirudh Bagde, Gene Kim, Catherine Ta.
 */

/**
 * General utility functions go here.
 */
public class Utils {
    public static final double EPSILON = 0.000001;
    
    public static boolean fequals(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }
    
    /**
     * Return unit vector from a to b.
     */
    public static Point unitVector(Point a, Point b) {
        double magnitude = a.distance(b);
        double unitX = (b.getX() - a.getX()) / magnitude;
        double unitY = (b.getY() - a.getY()) / magnitude;
        double unitZ = (b.getZ() - a.getZ()) / magnitude;
        return new Point(unitX, unitY, unitZ);
    }
}