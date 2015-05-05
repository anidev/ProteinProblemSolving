/**
 * Java Sun Compiler
 * Rupin Bhalla, Anirudh Bagde, Gene Kim, Catherine Ta.
 */

/**
 * This class represents a point.
 */
public class Point {
    private double x;
    private double y;
    private double z;
    
    /**
     * The constructor takes in three doubles for the x, y, and z coordinates
     * of the point.
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @param z The z coordinate of the point.
     */
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * This method calculates the distance between two Point objects.
     * @param other The other Point obejct to find the distance between.
     * @return Returns the distance between the two Point objects.
     */
    public double distance(Point other) {
        double xSubtract = x - other.getX();
        double ySubtract = y - other.getY();
        double zSubtract = z - other.getZ();
        double xSquared = xSubtract*xSubtract;
        double ySquared = ySubtract*ySubtract;
        double zSquared = zSubtract*zSubtract;
        return Math.sqrt(xSquared + ySquared + zSquared);
    }
    
    /**
     * This method gets the x coordinate of the point.
     * @return Returns the x coordinate of the point.
     */
    public double getX() {
        return x;
    }
    
    /**
     * This method gets the y coordinate of the point.
     * @return Returns the y coordinate of the point.
     */
    public double getY() {
        return y;
    }
    
    /**
     * This method gets the z coordinate of the point.
     * @return Returns the z coordinate of the point.
     */
    public double getZ() {
        return z;
    }
    
    /**
     * This method compares two Point objects to check if they are equal.
     * @param other The other Point object to compare.
     * @return Returns true if the two Point objects are equal, false otherwise.
     */
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Point)) {
            return false;
        }
        Point otherPoint = (Point)other;
        return (Utils.fequals(this.getX(), otherPoint.getX()) 
            && Utils.fequals(this.getY(), otherPoint.getY())
            && Utils.fequals(this.getZ(), otherPoint.getZ()));
    }
    
    /**
     * The hash code for this class is computed by rounding the double x,y,z
     * values to 2 decimal places.
     */
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + (int)(x * 100);
        result = prime * result + (int)(y * 100);
        result = prime * result + (int)(z * 100);
        return result;
    }
    
    public String toString() {
        return "(" + x + " " + y + " " + z + ")";
    }
}
  