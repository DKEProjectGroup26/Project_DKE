 // This is the same RadGraph.java but modified with the Vertex and Graph classes
// and the calculate and solveWithCalculate (works for all but 3 graphs)
// read the comments for more info
// it also prints some information about the graph and all the vertices with their colors if it managed to solve it

// IMPORTANT: this file assumes that the graphs are in a folder called testing_graphs
// if your graphs are in the same folder as the file, remove testing_graphs/ on line 47

// the solving magic happens on line 186


import java.io.*;
import java.util.*;

class ColEdge {
    int u;
    int v;
}
		
public class ReadAndSolve {
		
    public final static boolean DEBUG = false; //true;
		
    public final static String COMMENT = "//";
		
    public static void main( String args[] ) {
        // This is for the command line
        /*
            if (args.length < 1) {
                System.out.println("Error! No filename specified.");
                System.exit(0);
			}
            
			String inputfile = args[0];
        */
        
        // This is for testing
        
        // still unsolvable graphs: 6  (/20)
        int graphNumber = 1;
        
        System.out.println("Attempting graph number " +  graphNumber);
        System.out.println();
        
        String inputfile = "testing_graphs/graph" +  (graphNumber < 10 ? "0" : "") + graphNumber + ".txt";
        
        boolean seen[] = null;
        
        //! n is the number of vertices in the graph
        int n = -1;
        
        //! m is the number of edges in the graph
        int m = -1;
        
        //! e will contain the edges of the graph
        ColEdge e[] = null;
        
        try { 
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);
            
            String record = new String();
            
            //! THe first few lines of the file are allowed to be comments, staring with a // symbol.
            //! These comments are only allowed at the top of the file.
            
            //! -----------------------------------------
            while ((record = br.readLine()) != null) {
                if (record.startsWith("//")) continue;
                break; // Saw a line that did not start with a comment -- time to start reading the data in!
            }
	        
            if (record.startsWith("VERTICES = ")) {
                n = Integer.parseInt( record.substring(11) );					
                if(DEBUG) System.out.println(COMMENT + " Number of vertices = " + n);
            }
            
            seen = new boolean[n+1];	
            
            record = br.readLine();
            
            if (record.startsWith("EDGES = ")) {
                m = Integer.parseInt( record.substring(8) );					
                if(DEBUG) System.out.println(COMMENT + " Expected number of edges = " + m);
            }
            
            e = new ColEdge[m];	
            
            for (int d=0; d<m; d++) {
                if(DEBUG) System.out.println(COMMENT + " Reading edge " + (d + 1));
                record = br.readLine();
                String data[] = record.split(" ");
                if (data.length != 2) {
                    System.out.println("Error! Malformed edge line: " + record);
                    System.exit(0);
                }
                e[d] = new ColEdge();
                
                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);
                
                seen[ e[d].u ] = true;
                seen[ e[d].v ] = true;
                
                if(DEBUG) System.out.println(COMMENT + " Edge: " +  e[d].u + " " +e[d].v);
            }
            
            String surplus = br.readLine();
            if (surplus != null)
                if(surplus.length() >= 2)
                    if(DEBUG)
                        System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '" + surplus+ "'");						
            
        } catch (IOException ex) {
            // catch possible io errors from readLine()
            System.out.println("Error! Problem reading file " +  inputfile);
            System.exit(0);
        }
        
        for (int x=1; x<=n; x++)
            if (seen[x] == false)
                if(DEBUG)
                    System.out.println(COMMENT + " Warning: vertex " +  x + " didn't appear in any edge : it will be considered a disconnected vertex on its own.");

        //! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
        //! e[1] will be the second edge...
        //! (and so on)
        //! e[m-1] will be the last edge
        //! 
        //! there will be n vertices in the graph, numbered 1 to n
        
        
        
        //! INSERT YOUR CODE HERE!
        
        
        // NOTES TO SELF:
        // GRAPH 06 HAS 343 UNBOUND VERTICES!!!
        // THEY NEED TO BE DEALT WITH AS THEY CAN CREATE INFINITE LOOPS
        // (GRAPH 06 LOOPS FOREVER)
        // ALSO, IF THE GRAPH IS SEPARATED INTO 2 OR MORE PARTS
        
        // discard unbound
        // ideally separate into another graph and solve apart
        // fix getVertex
        
        // create Graph object
        Graph graph = new Graph(n, e); // inputs: (number of vertices, edges)
        
        // print info
        printGraphInfo(graph);
        
        // split graph
        // System.out.println("splitting...");
        // ArrayList<Graph> subGraphs = graph.split(); // these still have the same vertex objects!
        // System.out.println("done");
        // System.out.println(subGraphs.size() + " sub graphs");
        // System.out.println();
        
        
        // set all unbound vertices to color 1
        // unbound vertices don't have neighbors so it doesn't matter what color they are
        try {
            for (Vertex vertex : graph.vertices)
                if (vertex.neighbors.size() == 0)
                    vertex.setColor(1);
        } catch (ColorConflict exception) {
            System.out.println("something went very wrong");
            System.exit(1);
        }
        
        // find biggest clique
        
        
        
        int maxClique = graph.maxClique(2);
        System.out.println(maxClique + "-clique found");
        System.out.println();
        
        // int maxCliqueFound = maxClique(graph);
        // System.out.println(maxCliqueFound + " max clique size found");
        // System.out.println();
        
        int minBound = maxClique; // or something else
        
        // calculate solution
        Graph solution = solveWithCalculate(graph, minBound);
        
        // check the solution
        System.out.println();
        if (solution.isCorrect())
            System.out.println("THE SOLUTION HAS BEEN CHECKED AND IS CORRECT");
        else {
            System.out.println("THE SOLUTION IS WRONG");
            System.exit(1);
        }
        
        // print the solution
        printSolution(solution);
    } // end of main(...)
    
    // this method uses calculate(...) with numbers of colors
    // from 0 upwards until the graph can be solved
    static Graph solveWithCalculate(Graph graph, int minBound) {
        
        // experimental
        int unboundFilled = 0;
        try {
            for (Vertex vertex : graph.vertices)
                if (vertex.neighbors.size() == 0) {
                    vertex.setColor(1);
                    unboundFilled++;
                }
       } catch (ColorConflict e) {
           System.out.println("something went very wrong");
           System.exit(1);
       }
       
       System.out.println(unboundFilled + " unbound vertices solved");
       System.out.println();
        
        
        // see if it's better to go min -> max or max -> min
        for (int numberOfColors = minBound;; numberOfColors++) {
            
            long startTime = System.nanoTime();
            
            System.out.println("trying with " +  numberOfColors + " color" + (numberOfColors == 1 ? "" : "s"));
            
            graph.setColors(numberOfColors);
            Graph maybeSolved = calculate(graph);
            
            long nanoTaken = System.nanoTime() - startTime;
            System.out.println("  " + Math.round(nanoTaken / 1e6) / 1e3 + "s taken");
            
            if (maybeSolved != null)
                return maybeSolved;
        }
    }
    
    // this method requires the graph to have colors set (graph.setColors(...))
    // assisted brute force with cloning
    static Graph calculate(Graph graph) {return calculate(graph, 0);}
    static Graph calculate(Graph graph, int depth) {
        
        // System.out.println("called (depth: " + depth + ")");
        
        
        if (graph.isSolved()) return graph;
        
        boolean anyWithNeighbors = false;
        for (Vertex vertex : graph.vertices)
            if (vertex.neighbors.size() > 0) {
                anyWithNeighbors = true;
                break;
            }
        
        for (Vertex vertex : graph.vertices) {
            if (vertex.solved) continue;
            if (anyWithNeighbors && vertex.neighbors.size() == 0) continue;
            
            for (int color : vertex.allowedColors) {
                
                boolean solved = false;
                try {
                    Graph clone = graph.clone();
                    clone.getVertex(vertex.value).setColor(color);
                    return calculate(clone, depth + 1); // won't return if ColorConflict is thrown
                } catch(ColorConflict e) {
                    // ignore
                }
            }
        }
        return null;
    }
    
    static void printGraphInfo(Graph graph) {
        System.out.println("Graph Info:");
        
        System.out.println();
        
        System.out.println(graph.vertices.size() + " vertices");
        System.out.println(graph.rawEdges.size() + " edges");
        System.out.println();
        
        int minEpV = -1; // min edges per vertex
        int maxEpV = -1; // max edges per vertex
        int unbound = 0; // vertices with 0 edges
        int hanging = 0; // vertices with 1 edge
        for (Vertex vertex : graph.vertices) {
            if (vertex.neighbors == null) {
                System.out.println("WHAT");
                System.out.flush();
                System.exit(1);
            }
            int size = vertex.neighbors.size();
            if (minEpV == -1 || minEpV > size) minEpV = size;
            if (maxEpV == -1 || maxEpV < size) maxEpV = size;
            if (size == 0) unbound++;
            if (size == 1) hanging++;
        }
        
        System.out.println(minEpV + " edges min");
        System.out.println(maxEpV + " edges max");
        System.out.println();
        System.out.println(unbound + " unbound vertices");
        System.out.println(hanging + " hanging vertices");
        System.out.println();
    }
    
    static void printSolution(Graph solution) {
        System.out.println();
        if (solution == null) {
            System.out.println("error: calculation failed (solution is null)");
            System.exit(1);
        } else {
            System.out.println("SOLVED WITH " +  solution.numberOfColors + " COLORS");
            System.out.println("(vertex -> color)");
            
            for (Vertex vertex : solution.vertices)
                System.out.println(vertex.value + " -> " +  vertex.color);
        }
    }
    
    static boolean isGood(Graph graph) {
        for (Vertex vertex : graph.vertices)
            for (Vertex neighbor : vertex.neighbors)
                if (vertex.color == neighbor.color)
                    return false;
        return true;
    }
}

// really just alternate names for a basic Exceptions
class ColorConflict extends Exception {/* ignore */}
class CliqueTimeout extends Exception {/* ignore */}


class Vertex {
    
    int value = -1; // vertex number (1, 2, 3, ...)
    int color = -1; // color (1, 2, 3, ...) (-1 if not set yet)
    ArrayList<Vertex> neighbors = null; // references to Vertex object that share edges with this object
    ArrayList<Integer> allowedColors = null; // allowed colors (only set when graph.setColors(...) is called)
    boolean solved = false; // if color is -1 then false else true
    
    // standard constructor
    public Vertex(int inputValue) {
        value = inputValue;
    }
    
    // clone constructor (this is used by the enclosing Graph object)
    public Vertex(int myValue, int myColor, ArrayList<Integer> myAllowed) {
        value = myValue;
        color = myColor;
        
        if (myAllowed != null) {
            // if graph.setColors(...) hasn't been called, allowedColors is null
            
            // allowedColors = new ArrayList<Integer>();
            // for (int allowed : myAllowed)
                // allowedColors.add(allowed);
            allowedColors = new ArrayList<Integer>(myAllowed);
        }
        
        solved = color != -1;
        // neighbors are filled in by enclosing Graph object
    }
    
    // this is to be called by the enclosing graph object
    public void makeNeighbors(ArrayList<ColEdge> edges, ArrayList<Vertex> allVertices) {
        neighbors = new ArrayList<Vertex>();
        
        for (ColEdge edge : edges) {
            int selfInEdge = -1;
            if (edge.u == value) selfInEdge = 0;
            else if (edge.v == value) selfInEdge = 1;
            else continue;
            
            for (Vertex vertex : allVertices) {
                if (selfInEdge == 0 && vertex.value == edge.v || selfInEdge == 1 && vertex.value == edge.u) {
                    neighbors.add(vertex);
                    break;
                }
            }
        }
    }
    
    // to be called by Graph
    // sets color
    // removes color as an option for all neighbors
    public void setColor(int inputColor) throws ColorConflict {
        for (Vertex neighbor : neighbors)
            if (neighbor.color == inputColor)
                throw new ColorConflict();
        
        color = inputColor;
        
        allowedColors = null;
        solved = true;
        
        for (Vertex neighbor : neighbors)
            neighbor.disallow(color);
    }
    
    // to be called by Graph
    // removes color from allowed colors
    // throws error if no options left
    // sets color if only 1 option left
    public void disallow(int inputColor) throws ColorConflict {
        if (inputColor == color)
            throw new ColorConflict();
        
        if (solved) return;
        
        int index = allowedColors.indexOf(inputColor);
        
        if (index == -1) return;
        
        allowedColors.remove(index);
        
        if (allowedColors.size() == 0) {
            throw new ColorConflict();
        } else if (allowedColors.size() == 1) {
            int onlyColor = allowedColors.get(0);
            
            setColor(onlyColor);
        }
    }
    
    // just calls clone constructor
    public Vertex clone() {
        return new Vertex(value, color, allowedColors);
    }
}

class Graph {
    
    ArrayList<Vertex> vertices = null; // list of Vertex objects (values ordered from 1 on)
    ArrayList<ColEdge> rawEdges = null; // the ColEdge objects given by reading the file (but in an ArrayList)
    int numberOfColors = -1; // this is initialized separately by graph.setColors(...)
    
    // stardard constructor
    public Graph(int numberOfVertices, ColEdge[] inputEdges) {
        vertices = new ArrayList<Vertex>();
        for (int i = 0; i < numberOfVertices; i++)
            vertices.add(new Vertex(i + 1)); // vertices start at 1!
        
        rawEdges = new ArrayList<ColEdge>();
        for (ColEdge edge : inputEdges)
            rawEdges.add(edge);
        
        makeVertexNeighbors();
    }
    
    // splitting constructor
    public Graph(ArrayList<Vertex> selectedVertices, Graph originalGraph) {
        // vertices = new ArrayList<Vertex>();
        // for (Vertex vertex : selectedVertices)
            // vertices.add(vertex); // will have neighbors already (not cloning)
        vertices = new ArrayList<Vertex>(selectedVertices);
        
        // select the edges that apply
        rawEdges = new ArrayList<ColEdge>();
        for (ColEdge edge : originalGraph.rawEdges) {
            
            boolean uMatch = false,
                    vMatch = false;
            
            for (Vertex vertex : vertices) {
                if (vertex.value == edge.u) uMatch = true;
                else if (vertex.value == edge.v) vMatch = true;
                else continue;
                
                if (uMatch && vMatch) {
                    rawEdges.add(edge);
                    break;
                }
            }
        }
        
        numberOfColors = originalGraph.numberOfColors;
    }
    
    // clone constructor
    public Graph(ArrayList<Vertex> myVertices, ArrayList<ColEdge> myEdges, int myColors) {
        vertices = new ArrayList<Vertex>();
        for (Vertex vertex : myVertices)
            vertices.add(vertex.clone());
        
        rawEdges = myEdges;
        
        numberOfColors = myColors;
        
        makeVertexNeighbors();
    }
    
    // tell all vertices to fill in their neighbors
    public void makeVertexNeighbors() {
        for (Vertex vertex : vertices)
            vertex.makeNeighbors(rawEdges, vertices);
    }
    
    // tells all vertices how many colors you're working with (to solve the graph)
    public void setColors(int howMany) {
        numberOfColors = howMany;
        
        for (Vertex vertex : vertices) {
            vertex.allowedColors = new ArrayList<Integer>();
            for (int i = 0; i < numberOfColors; i++)
                vertex.allowedColors.add(i + 1); // colors start at 1!
        }
    }
    
    public boolean isSolved() {
        for (Vertex vertex : vertices)
            if (!vertex.solved)
                return false;
        
        return true;
    }
    
    // get vertex by value
    // IMPORTANT: this assumes that vertex order has not changed since initialization
    // public Vertex getVertex(int vertexValue) {
        // return vertices.get(vertexValue - 1);
    // }
    public Vertex getVertex(int value) {
        for (Vertex vertex : vertices)
            if (vertex.value == value)
                return vertex;
        return null;
    }
    
    // just calls clone constructor
    public Graph clone() {
        return new Graph(vertices, rawEdges, numberOfColors);
    }
    
    public boolean hasClique(int size, float timeOut) throws CliqueTimeout {
        return hasClique(size, new ArrayList<Vertex>(), System.nanoTime(), timeOut);
    }
    
    public boolean hasClique(int size, ArrayList<Vertex> clique, long startTime, float timeOut) throws CliqueTimeout {
        
        long timeTaken = System.nanoTime() - startTime;
        float secsTaken = (float) (timeTaken / 1e9);
        if (secsTaken >= timeOut) throw new CliqueTimeout();
        
        vLoop: for (Vertex vertex : vertices) {
            if (vertex.neighbors.size() < size - 1)
                continue;
            
            for (Vertex check : clique)
                if (!vertex.neighbors.contains(check))
                    continue vLoop;
            
            ArrayList<Vertex> newClique = new ArrayList<Vertex>(clique);
            newClique.add(vertex);
            
            if (newClique.size() == size) {
                for (Vertex v0 : newClique)
                    for (Vertex v1 : newClique)
                    if (v0 != v1 && !v0.neighbors.contains(v1)) {
                        System.out.println("error: something went very wrong while checking clique of size " + size);
                        System.exit(1);
                    }
                
                return true;
            }
            boolean success = hasClique(size, newClique, startTime, timeOut);
            if (success) return success;
        }
        return false;
    }
    
    public int maxClique(int startFrom) {
        int maxSize = 0;
        int skipped = 0;
        int maxSkip = 0;
        for (int size = startFrom;; size++) {
            try {
                if (hasClique(size, 1)) {
                    maxSize = size;
                } else
                    break;
                skipped = 0;
            } catch (CliqueTimeout e) {
                skipped++;
                if (skipped >= 4)
                    return maxSize;
            }
        }
        return maxSize;
    }
    
    public boolean isCorrect() {
        for (Vertex vertex : vertices) {
            if (!vertex.solved) {
                System.out.println("error: checking unsolved graph");
                System.exit(1);
            } else
                for (Vertex neighbor : vertex.neighbors)
                    if (vertex.color == neighbor.color)
                        return false;
        }
        return true;
    }
}
