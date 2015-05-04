/**
 * This class represents an atom.
 */
public class Atom {
    private Point center;
    private double radius;
    
    /**
     * The constructor takes in a center point and a radius.
     * @param center The center point of the atom.
     * @param radius The radius of the atom.
     */
    public Atom(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }
    
    /**
     * This method compares two Atom objects to check if they are equal.
     * @param other The other Atom object to compare.
     * @return Returns true if the two Atom objects are equal, false otherwise.
     */
    public boolean equals(Object other) {
        if(other == null || !(other instanceof Atom)) {
            return false;
        }
        Atom otherAtom = (Atom)other;
        return (this.getCenter().equals(otherAtom.getCenter())
            && Utils.fequals(this.getRadius(), otherAtom.getRadius()));
    }
    
    /**
     * This method gets the center point of atom.
     * @return Returns the center point of the atom.
     */
    public Point getCenter() {
        return center;
    }
    
    /**
     * This method gets the radius of the atom.
     * @return Returns the radius of the atom.
     */
    public double getRadius() {
        return radius;
    }
    
    public String toString() {
        return "{" + center.toString() + ", " + radius + "}";
    }
}