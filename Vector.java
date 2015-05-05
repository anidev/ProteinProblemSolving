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
 * This class represents a vector between two atoms, starting and ending probe
 * radius away from the surface of both atoms.
 */
public class Vector {
    private AtomPair pair;
    private Point current;
    private Point target;
    private Point unit;
    private double probeRadius;
    
    /**
     * Construct a vector from the given atoms and probe radius.
     * @param A, first atom
     * @param B, second atom
     * @param probeRadius, radius of probe spehre
     */
    public Vector(Atom A, Atom B, double probeRadius) {
        this.probeRadius = probeRadius;
        this.pair = new AtomPair(A, B);
        this.current = A.getCenter();
        this.unit = calcUnitVector();
        this.target = calcTargetPoint();
        this.step(A.getRadius());
        this.step(probeRadius);
    }
    
    public Atom getA() {
        return pair.getA();
    }
    
    public Atom getB() {
        return pair.getB();
    }
    
    /**
     * Returns the distance between the surface of the atoms, as reported by
     * AtomPair.distance(). Note however that the vector itself starts and
     * stops stepping when probe radius away from the surface of atoms, which
     * is the value of AtomPair.distance() minus probeRadius*2.
     * @return Distance between atom surfaces.
     */
    public double distance() {
        return pair.distance();
    }
    
    public AtomPair getPair() {
        return pair;
    }
    
    /**
     * Return the point along the vector that this object is currently at.
     * @return Current position on vector
     */
    public Point getCurrent() {
        return current;
    }
    
    /**
     * Return the unit vector as a Point object, where x,y,z represents i,j,k.
     * @return Unit vector from atom A to atom B
     */
    public Point getUnit() {
        return unit;
    }
    
    /**
     * Return the target point for the vector. See @see{#getDistanceToTarget}.
     * @return Target point
     */
     public Point getTarget() {
         return target;
     }
    
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Vector)) {
            return false;
        }
        Vector otherVector = (Vector)other;
        return this.getPair().equals(otherVector.getPair())
            && current.equals(otherVector.getCurrent());
    }
    
    /**
     * Increment along this vector by the specified length. The current
     * position of the vector can be retrieed with getCurrent().
     * @param length How far to step along vector
     */
    public void step(double length) {
        double newX = current.getX() + unit.getX() * length;
        double newY = current.getY() + unit.getY() * length;
        double newZ = current.getZ() + unit.getZ() * length;
        current = new Point(newX, newY, newZ);
    }
    
    /**
     * Returns distance to target. Target is defined as the point that is
     * probe radius away from the surface of atom B. Returned distance will
     * be negative if 
     */
    public double getDistanceToTarget() {
        return current.distance(target);
    }
    
    /**
     * Specifies if the current position of the vector is at or beyond
     * the target of the vector. Target is defined in @see{getDistanceToTarget}
     * @return True if at or beyond target point
     */
    public boolean arrivedAtTarget() {
        double distance = getDistanceToTarget();
        Point a = pair.getA().getCenter();
        return Utils.fequals(distance, 0.0) // at target
            || current.distance(a) > target.distance(a); // beyond
    }
    
    /**
     * Calculate and return the unit vector from atom A to atom B. Unit vector
     * is returned as a Point object, with x,y,z representing i,j,k.
     * @return Calculated unit vector
     */
     private Point calcUnitVector() {
        Point a = pair.getA().getCenter();
        Point b = pair.getB().getCenter();
        return Utils.unitVector(a, b);
     }
    
    /**
     * Calculate and return the target point. Target point is defined in
     * @see{#getDistanceToTarget}.
     * @return Calculated target point
     */
    private Point calcTargetPoint() {
        Point b = pair.getB().getCenter();
        double length = pair.getB().getRadius() + probeRadius;
        double newX = b.getX() - unit.getX() * length;
        double newY = b.getY() - unit.getY() * length;
        double newZ = b.getZ() - unit.getZ() * length;
        return new Point(newX, newY, newZ);
    }
}