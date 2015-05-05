/**
 * Java Sun Compiler
 * Rupin Bhalla, Anirudh Bagde, Gene Kim, Catherine Ta.
 */

/**
 * These are our Basic Tests for the algorithm
 */ 
public class BasicTests {
    public static void main(String[] args) {
        //TEST FOR POINT (works as expected)
        System.out.println("TEST FOR POINT CLASS");
        Point p1 = new Point(0, 0 ,0);
        Point p2 = new Point(1, 1, 1);
        Point p3 = new Point(0, 0, 0);
        String test = "";
        System.out.println("P1: " + p1);//0 0 0
        System.out.println("P2: " + p2); //1 1 1
        System.out.println("Distance between P1 & P2: " + p1.distance(p2)); //sqrt 3
        System.out.println("P1 and P2 are equal: " + p1.equals(p2)); //false
        System.out.println("P1 and P3 are equal: " + p1.equals(p3)); //true
        System.out.println("P1 and test are equal: " + p1.equals(test)); //false
        // Test hashes: p1 == p3, p1 != p2
        System.out.println("P1 hash: " + p1.hashCode());
        System.out.println("P2 hash: " + p2.hashCode());
        System.out.println("P3 hash: " + p3.hashCode());
        System.out.println("");
        
        //TEST FOR ATOM (works as expected)
        System.out.println("TEST FOR ATOM CLASS");
        Atom a1 = new Atom(p1, 1);
        Atom a2 = new Atom(p2, 1);
        Atom a3 = new Atom(p3, 1);
        System.out.println("A1 CENTER POINT: " + a1.getCenter()); //0 0 0
        System.out.println("A1 RADIUS: " + a1.getRadius()); //1
        System.out.println("A1 and A2 are equal: " + a1.equals(a2)); //false
        System.out.println("A1 and A3 are equal: " + a1.equals(a3)); //true
        System.out.println("A1 and test are equal: " + a1.equals(test)); //false
        // Test hashes: a1 == a3, a1 != a2
        System.out.println("A1 hash: " + a1.hashCode());
        System.out.println("A2 hash: " + a2.hashCode());
        System.out.println("A3 hash: " + a3.hashCode());
        System.out.println("");
        
        //TEST FOR ATOMPAIR (works as expected)
        System.out.println("TEST FOR ATOMPAIR CLASS");
        a2 = new Atom(new Point(5, 5, 5), 1);
        AtomPair ap1 = new AtomPair(a1, a2);
        AtomPair ap2 = new AtomPair(a1, a3);
        AtomPair ap3 = new AtomPair(a2, a1);
        System.out.println("AP1 A: " + ap1.getA().getCenter()); //0 0 0
        System.out.println("AP1 B: " + ap1.getB().getCenter()); //5 5 5
        System.out.println("Distance AP1: " + ap1.distance()); //sqrt 75 - 2
        System.out.println("Distance AP2: " + ap2.distance()); //-2
        System.out.println("AP1 and AP2 are equal: " + ap1.equals(ap2)); //false
        System.out.println("AP1 and AP3 are equal: " + ap1.equals(ap3)); //true
        System.out.println("AP1 and test are equal: " + ap1.equals(test)); //false
        // Test hashes: ap1 == ap3, ap1 != ap2
        System.out.println("AP1 hash: " + ap1.hashCode());
        System.out.println("AP2 hash: " + ap2.hashCode());
        System.out.println("AP3 hash: " + ap3.hashCode());
        System.out.println("");
        
        //TEST FOR VECTOR
        System.out.println("TEST FOR VECTOR CLASS");
        Vector v1 = new Vector(a2, a1, 1.0);
        System.out.println("V1 A: " + v1.getA().getCenter()); //5 5 5
        System.out.println("V1 B: " + v1.getB().getCenter()); //0 0 0
        System.out.println("V1 arrived: " + v1.arrivedAtTarget()); // false
        System.out.println("V1 Unit: " + v1.getUnit()); // -1/sqrt(3) = -0.58
        // Should be a2 radius + probe radius away from a2: (5*sqrt(3)-2)/sqrt(3)
        System.out.println("V1 Current: " + v1.getCurrent()); // 3.85
        // Should be a1 radius + probe radius from a1
        System.out.println("V1 Target: " + v1.getTarget()); // 2/sqrt(3) = 1.15
        v1.step(3);
        // Stepped an additional 3 units from current: (5*sqrt(3)-5)/sqrt(3)
        System.out.println("V1 Current(+3): " + v1.getCurrent()); // 2.11
        System.out.println("V1 arrived: " + v1.arrivedAtTarget()); // false
        v1.step(3); // Step another 3, should have passed target by now
        System.out.println("V1 arrived: " + v1.arrivedAtTarget()); // true
    }
}