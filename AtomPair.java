/**
 * This class is meant to represent an Atom Pair. In this class
 * we are are going to be taking 2 atoms as a parameter, and comparing if 
 * the distance between the two (with no Radius). Also we are also comparing if
 * they are equal to each other
 */
public class AtomPair {
    private Atom A;
    private Atom B;
    
    /**
     * This contructor takes 2 Atoms. 
     * @param A, first atom
     * @param B, second atom.
     */ 
    public AtomPair(Atom A, Atom B) {
        this.A = A;
        this.B = B;
    }
    
    /**
     * This method allows us to get the atom A, that will be used for calculations
     */
    public Atom getA() {
        return A;
    }
    
    /**
     * This method allows us to get the atomB, that will be used for calculations
     */
    public Atom getB() {
        return B;
    }
    
    /**
     * This method allows us to get the distance between the pair. In this distance
     * method we are only calculating the distance between the atoms but not 
     * including their radii because it is not relevant in the distance we
     * want to measure.
     */ 
    public double distance() {
        double distance = A.getCenter().distance(B.getCenter())
                        - A.getRadius() - B.getRadius();
        return distance;
    }
    
    /**
     * This method compares 2 pairs by their distance.
     */ 
    public boolean equals(Object other) {
        if (other == null || !(other instanceof AtomPair)) {
            return false;
        }
        AtomPair otherAtomPair = (AtomPair)other;
        return ((A.equals(otherAtomPair.A) && B.equals(otherAtomPair.B))
            || (A.equals(otherAtomPair.B) && B.equals(otherAtomPair.A)));
    }
    
    public String toString() {
        return "<" + A.getCenter() + ", " + B.getCenter() + ">";
    }
}