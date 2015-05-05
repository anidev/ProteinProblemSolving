/**
 * Intro to Problem Solving - Onufriev
 * Final Project
 * 
 * Language: Java
 * 
 * Authors:
 * Rupin Bhalla, Anirudh Bagde, Gene Kim, Catherine Ta.
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

/**
 * This class is the ProteinCavaties Class. This is the main part of the code,
 * we implement all our methods, and we find the cavity points in this class
 */ 
public class ProteinCavities {
    // Parameters
    private String inFilename;
    private String outFilename;
    private double probeSphereRadius;
    private double resolution;
    private Scanner atomInput;

    // Algorithm fields
    private List<Atom> atoms;
    private Set<AtomPair> validatedPairs;
    private List<Point> voidList;
    private List<Point> cavityList;
    
    /**
     * The constructor instantiates the list of atoms, atom pairs, and validated
     * pairs.
     */
    ProteinCavities(String inFilename, String outFilename,
                           double probeSphereRadius, double resolution) {
        this.inFilename = inFilename;
        this.outFilename = outFilename;
        this.probeSphereRadius = probeSphereRadius;
        this.resolution = resolution;

        // Initialize lists
        atoms = new ArrayList<Atom>();
        validatedPairs = new HashSet<AtomPair>();
        voidList = new LinkedList<Point>();
        cavityList = new LinkedList<Point>();
    }
    
    /**
     * Run all algorithm methods to process data and find cavity points.
     */
    public void run() {
        System.out.println("- Running algorithm -");
        System.out.println("Reading input file (" + inFilename + ")...");
        readFile();
        System.out.println("Forming atom pairs...");
        findingValidPairs();
        System.out.println("\\- Created " + validatedPairs.size() + " pairs");
        System.out.println("Searching for void points...");
        CRUSADEforVoidPoints();
        System.out.println("\\- Found " + voidList.size() + " void points");
        System.out.println("Identifying cavity points...");
        determineCavityPoints();
        System.out.println("\\- Identified " + cavityList.size() + " cavity points");
        System.out.println("Writing to output file (" + outFilename + ")...");
        outputFile();
        System.out.println("\\- Done");
    }
    

    /**
     * Reads protein data from a file. Each line in the file represents a single
     * atom of the protein, along with its coordinates and radius. The format
     * for the file is given in the project description PDF. This method should 
     * populate the atoms list with Atom objects for each atom in the file.
     */
    public void readFile() {
        File atomData = new File(inFilename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(atomData);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        while (scanner.hasNextLine()) {
            // Each line is divided into columns
            // X,Y,Z are columns 6,7,8, radius is column 10
            String line = scanner.nextLine();
            String[] splitLines = line.split("\\s+"); // Split by spaces
            double xCoord = Double.parseDouble(splitLines[5]);
            double yCoord = Double.parseDouble(splitLines[6]);
            double zCoord = Double.parseDouble(splitLines[7]);
            double foundRadius = Double.parseDouble(splitLines[9]);
            
            Point center = new Point(xCoord, yCoord, zCoord);
            double radius =  foundRadius;
            Atom atom = new Atom(center, radius);
            atoms.add(atom);
        }
    }
    
    /**
     * Creates a direct copy of the input file, but with cavity points at the end of the file.
     */
    public void outputFile() {
        // Open input and output files
        File atomInput = new File(inFilename);
        File atomOutput = new File(outFilename);
        FileWriter filewriter = null;
        Scanner scanner = null;
            
        try {
            scanner = new Scanner(atomInput);
            filewriter = new FileWriter(atomOutput);
        } 
        catch(FileNotFoundException e ) {
            e.printStackTrace();
            System.exit(1);
        }
        catch(IOException b) {
            b.printStackTrace();
            System.exit(1);
        }
       
        // First copy all of input file to output file
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
               filewriter.write(line);
               filewriter.write(System.getProperty("line.separator"));
            }
            catch(IOException b) {
                b.printStackTrace();
                System.exit(1);
            }
        }
        // Then add all cavity points to output file
        Iterator<Point> iterator = cavityList.iterator();
        int i = 0;
        while(iterator.hasNext()) {
            Point cavity = iterator.next();
            try {
                filewriter.write("ATOM  1000" + i + "  MC  CAV  500" + i + "      ");
                filewriter.write(cavity.getX() + "   " + cavity.getY() + "   " + cavity.getZ() + "   1.00 " + probeSphereRadius);
                filewriter.write(System.getProperty("line.separator"));  //breaks line.
            }
            catch(IOException b) {
                b.printStackTrace();
                System.exit(1);
            }
            i++;
        }
        // Close output streams
        try {
            scanner.close();
            filewriter.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Tests to see if there is enough space for a probe sphere at the given
     * point. If it returns true, it is guaranteed that a probe sphere at the
     * given point does not overlap any atom in the protein.
     * @param probe Point for probe sphere
     * @return True if probe sphere can fit at the given point.
     */
    public boolean probeTest(Point probe) {
        boolean probeClear = true;
        // Check probe point against all atoms
        for (int i = 0; i < atoms.size(); i++) {
            Atom atom = atoms.get(i);
            double distance = probe.distance(atom.getCenter()); // calculates distance between probe point and atom point.
            
            // if the distance between the centers is smaller than the sum of the radii of the spheres, probeClear = false.
            if (distance < atom.getRadius() + probeSphereRadius) {
                probeClear = false;
                break;
            }
        }
        return probeClear;
    }
    
    /**
     * This method will find the valid pairs and check that the distance between
     * the pairs is greater than the probe sphere size. So in order to do this, 
     * we compare each atom with every atom, and put each pair in a list. Then 
     * after having that list, we will compare each atomPair's distance to the 
     * size of the probe sphere. If the probe sphere distance is smaller than 
     * the atomPair's distance, then it is valid and we put the pair into the 
     * validatedPairs List.
     */
    public void findingValidPairs() {
        // TODO Check for duplicate pairs
        for(int i = 0; i < atoms.size(); i++) {
            for(int j = 0; j < atoms.size(); j++) {
                if(i == j) {
                    continue;
                }
                AtomPair pair = new AtomPair(atoms.get(i), atoms.get(j));
                // Checks distance is big enough, and makes sure flip of pair
                // is not already in the list
                // validatedPairs.contains is O(1) because it uses HashSet
                if (pair.distance() > probeSphereRadius * 2
                    && !validatedPairs.contains(pair)) {
                    validatedPairs.add(pair);
                }
            }
        }
    }
    
    /**
     * This method creates vectors between all the atoms in the atomPairs list
     * and checks if a void point can be placed along increments of the
     * resolution length.
     * 
     * For our holy crusade for void points, we are creating a method that 
     * creates vectors between all the atoms in the atomPairs list and checks
     * if a void point can be placed along increments of the resolution length.
     * If a void point can not be put we remove it from our validatedPairs List,
     * but if a void point can be put there, then we will put the specific pair
     * into our voidList. Then only will our holy crusade be complete.
     * All hail the holy Cavity Point.
     */
    public int output = 0;
    public void CRUSADEforVoidPoints() {
        Iterator<AtomPair> iterator = validatedPairs.iterator();
        // Check each pair
        while(iterator.hasNext()) {
            LinkedList<Point> tempList = new LinkedList<Point>();
            AtomPair pair = iterator.next();
            Vector vector = new Vector(pair.getA(), 
                pair.getB(), probeSphereRadius);
                
            
            boolean isRemoved = false;
            // Step along vector
            while(!vector.arrivedAtTarget() && !isRemoved) {
                // test the probe sphere
                if(probeTest(vector.getCurrent())) {
                    tempList.add(vector.getCurrent());
                    vector.step(resolution);
                }
                else {
                    isRemoved = true;
                    iterator.remove();
                }
            }
            // If entire pair succeeded, then add the points along the vector
            if(!isRemoved) {
                voidList.addAll(tempList);
            }
        }
    }
    
    /**
     * This method determines if the void points are cavity points.
     */ 
    public void determineCavityPoints() {
        LinkedList<Point> untestedList = new LinkedList<Point>(voidList);
        LinkedList<Point> chainList = new LinkedList<Point>();
        Queue<Point> testingList = new LinkedList<Point>();
        while(untestedList.peekFirst() != null) {
            // Clear lists
            chainList.clear();
            testingList.clear();
            // Start of chain
            Point startPoint = untestedList.pollFirst();
            chainList.add(startPoint);
            testingList.add(startPoint);
            boolean chainExposed = false;
            
            // Find points for rest of the chain
            // Checks each point in testingList, adding more as necessary
            while(testingList.peek() != null) {
                Point testPoint = testingList.poll();
                // Check if other void points are close enough to be in chain
                Iterator<Point> untestedIterator = untestedList.iterator();
                while(untestedIterator.hasNext()) {
                    Point voidPoint = untestedIterator.next();
                    // Threshold is slightly over resolution to give wiggle room
                    if(testPoint.distance(voidPoint) < resolution * 1.5) {
                        // Add to chain, queue for subsequent testing, and
                        // remove from the untested list
                        chainList.add(voidPoint);
                        testingList.add(voidPoint);
                        untestedIterator.remove();
                        // If this one point is on the edge, the entire chain
                        // is exposed, and none of the void points in the chain
                        // are cavity points.
                        // testPoint is the previous point.
                        if(onEdge(testPoint, voidPoint)) {
                            chainExposed = true;
                        }
                    }
                }
            }
            // If not a single point in the chain was on the edge, then the
            // entire chain represents a cavity within the protein.
            if(!chainExposed) {
                cavityList.addAll(chainList);
            }
        }
    }
    
    /**
     * Returns true if point is on the edge, which is true when the given point
     * has no void points or atoms nearby.
     */
    public boolean onEdge(Point previous, Point current) {
        if(previous == null) {
            return false;
        }
        
        Point unit = Utils.unitVector(previous, current);

        // Use unit vector to decide where to place probe sphere 
        
        double distance = 3 * probeSphereRadius; // 1.5 probe spheres should be good
        
        double scaledX = distance * unit.getX();
        double scaledY = distance * unit.getY();
        double scaledZ = distance * unit.getZ();
        
        Point[] testpoints = new Point[] {
                                new Point(current.getX() + scaledX,
                                          current.getY() + scaledY,
                                          current.getZ() + scaledZ),
                                new Point(previous.getX() - scaledX,
                                          previous.getY() - scaledY,
                                          previous.getZ() - scaledZ)
                            };

        // compare if testPoint overlaps a void point
        // if it does, then return false not on the edge
        // if it doesn't, then return true, last point was on the edge
        // Tests point in front of current point as well as behind previous point
        
        for(int i = 0; i < testpoints.length; i++) {
            Point testpoint = testpoints[i];
            boolean result = true;
            for(Point voidPoint : voidList) {
                if(testpoint.distance(voidPoint) < probeSphereRadius) {
                    result = false;
                }
            }
            if(result) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Main method. Arguments are -i, -o, -probe, and -resolution
     */ 
    public static void main(String[] args) {
        if(args.length == 0) {
            printUsage();
            return;
        }
        String inFilename = "";
        String outFilename = "";
        double probeSphereRadius = 1.4;
        double resolution = 0.25;
        for(int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.equals("--help") || arg.equals("-h")) {
                printUsage();
                return;
            } else if(arg.equals("-i")) {
                inFilename = args[++i];
            } else if(arg.equals("-o")) {
                outFilename = args[++i];
            } else if(arg.equals("-probe")) {
                probeSphereRadius = Double.parseDouble(args[++i]);
            } else if(arg.equals("-resolution")) {
                resolution = Double.parseDouble(args[++i]);
            }
        }

        if(inFilename.equals("") || outFilename.equals("")) {
            System.out.println("Please specify an input and output filename.");
            return;
        }

        System.out.println("- Algorithm Parameters -");
        System.out.println("Input filename: " + inFilename);
        System.out.println("Output filename: " + outFilename);
        System.out.println("Probe radius: " + probeSphereRadius);
        System.out.println("Resolution: " + resolution);
        System.out.println("");

        ProteinCavities cavities = new ProteinCavities(inFilename, outFilename,
                                                       probeSphereRadius,
                                                       resolution);
        cavities.run();
    }
    
    public static void printUsage() {
        System.out.print("Usage: ProteinCavities -i <input> -o <output> ");
        System.out.println("-probe <ρ_w> -resolution <resolution>");
    }
}