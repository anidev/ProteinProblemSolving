/**
 * General utility functions go here.
 */
public class Utils {
    public static final double EPSILON = 0.000001;
    
    public static boolean fequals(double d1, double d2) {
        return Math.abs(d1 - d2) < EPSILON;
    }
}