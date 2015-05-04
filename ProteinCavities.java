import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

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
    public ProteinCavities(String inFilename, String outFilename,
                           double probeSphereRadius, double resolution) {
        this.inFilename = inFilename;
        this.outFilename = outFilename;
        this.probeSphereRadius = probeSphereRadius;
        this.resolution = resolution;

        atoms = new ArrayList<Atom>();
        validatedPairs = new HashSet<AtomPair>();
        voidList = new LinkedList<Point>();
    }
    
    /**
     * Run all algorithm methods to process data and find cavity points.
     */
    public void run() {
        System.out.println("- Running algorithm -");
        System.out.println("Reading input file (" + inFilename + ")...");
        readFile();
        //fakeAtoms();
        System.out.println("Forming atom pairs...");
        findingValidPairs();
        System.out.println("\\- Created " + validatedPairs.size() + " pairs");
        System.out.println("Searching for void points...");
        CRUSADEforVoidPoints();
        System.out.println("\\- Found " + voidList.size() + " void points");
    }
    
    /**
     * Populates fake atom points since file reading code is not yet ready.
     */
     public void fakeAtoms() {
         atoms.add(new Atom(new Point(0, 0, 0), 1));
         atoms.add(new Atom(new Point(5, 0, 0), 1));
         atoms.add(new Atom(new Point(2.5, 2.5, 0), 1));
         atoms.add(new Atom(new Point(2.5, -2.5, 0), 1));
         atoms.add(new Atom(new Point(2.5, 0, 2.5), 1));
         atoms.add(new Atom(new Point(2.5, 0, -2.5), 1));
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
        
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            /**
            double xCoord = Double.parseDouble(line.substring(31, 37));
            double yCoord = Double.parseDouble(line.substring(39, 45));
            double zCoord = Double.parseDouble(line.substring(47, 53));
            double foundRadius = Double.parseDouble(line.substring(55, 59));
            **/
            
            String[] splitLines = line.split("\\s+");
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
     * 
     * */
     public void outputFile()
     {
         //not started
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
        for (int i = 0; i < atoms.size(); i++)
        {
            double distance = probe.distance(atoms.get(i).getCenter()); // calculates distance between probe point and atom point.
            
            if (distance < atoms.get(i).getRadius() + probeSphereRadius) // if the distance between the centers is smaller than the sum of the radii of the spheres, probeClear = false.
            {
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
     * into our voidList. Then only will our holy crusde be complete.
     * All hail the holy Cavity Point.
     */
    public void CRUSADEforVoidPoints() {
        Iterator<AtomPair> iterator = validatedPairs.iterator();

        while(iterator.hasNext()) {
            LinkedList<Point> tempList = new LinkedList<Point>();
            AtomPair pair = iterator.next();
            Vector vector = new Vector(pair.getA(), 
                pair.getB(), probeSphereRadius);
                
            
            boolean isRemoved = false;
                
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
            
            if(!isRemoved) {
                voidList.addAll(tempList);
            }
        }
    }
    
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
                    if(testPoint.distance(voidPoint) < resolution + 0.001) {
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
            if(chainExposed) {
                cavityList.addAll(chainList);
            }
        }
    }
    
    public boolean onEdge(Point previous, Point current) 
    {
        if(previous == null)
        {
            return false;
        }
        
        double magnitude = previous.distance(current);
        double unitX = (current.getX() - previous.getX()) / magnitude;
        double unitY = (current.getY() - previous.getY()) / magnitude;
        double unitZ = (current.getZ() - previous.getZ()) / magnitude;
        
        // 1 probe sphere distance
        // Use unit vector to decide where to place probe sphere 
        
        double distance = 2 * probeSphereRadius;
        
        double scaledX = distance * unitX;
        double scaledY = distance * unitY;
        double scaledZ = distance * unitZ;
        
        Point testpoint = new Point(current.getX() + scaledX, current.getY() 
            + scaledY, current.getZ() + scaledZ);
            
        
        // compare if testPoint overlapps a void point
        // if it does, then return false not on the edge
        // if it doesn't, then return true, last point was on the edge
        
        
        
        
        
        
        
           
        
        
            
            
        //Point a = pair.getA().getCenter();
        //Point b = pair.getB().getCenter();
        //double magnitude = a.distance(b);
        //double unitX = (b.getX() - a.getX()) / magnitude;
        //double unitY = (b.getY() - a.getY()) / magnitude;
        //double unitZ = (b.getZ() - a.getZ()) / magnitude;
        //return new Point(unitX, unitY, unitZ);
        
        
        return false; // this line is here just it compiles, fix it later
    }
    
    
    /**
     * This method is meant to say how Ani is super smart and is a GOD Among us
     * all
     */ 
    public static void main(String[] args) {
        if(args.length == 0) {
            printUsage();
            return;
        }
        String inFilename = "";
        String outFilename = "";
        double probeSphereRadius = 1.7;
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
