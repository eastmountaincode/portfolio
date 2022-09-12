import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Scanner;

public class InputReader {
    public InputReader() {};

    // CLRS PG. 716
    public static double calculateResidualCapacity(Node u, Node v, Edge[][] adjacencyMatrix) {
        double flow;
        double capacity;
        if (adjacencyMatrix[u.getID()][v.getID()].getCapacity() > 0) {
            capacity = adjacencyMatrix[u.getID()][v.getID()].getCapacity();
            flow = adjacencyMatrix[u.getID()][v.getID()].getFlow();
            return capacity - flow;
        }
        else if (adjacencyMatrix[v.getID()][u.getID()].getCapacity() > 0) {
            flow = adjacencyMatrix[v.getID()][u.getID()].getFlow();
            return flow;
        }
        else {
            return 0;
        }
    }

    public static void displayResidualGraph(Edge[][] adjacencyMatrix, Node[] nodeList) {
        Edge[][] resGraph = new Edge[adjacencyMatrix.length][adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                resGraph[i][j] = new Edge(calculateResidualCapacity(nodeList[i], nodeList[j], adjacencyMatrix));
            }
        }
        PrintGraph(resGraph);
    }

    public Edge[][] produceGraph(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        // Find the vertex with the highest index to determine |V|
        int maxVert = 0;
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\\s+");
            if (Integer.valueOf(line[0]) > maxVert) {
                maxVert = Integer.valueOf(line[0]);
            }
            else if (Integer.valueOf(line[1]) > maxVert) {
                maxVert = Integer.valueOf(line[1]);
            }
        }
        // Initialize newGraph and fill it with null values.
        Edge[][] newGraph = new Edge[maxVert][maxVert];
        int i;
        int j;
        for (i = 0; i < maxVert; i++) {
            for(j = 0; j < maxVert; j++) {
                newGraph[i][j] = new Edge(0);
            }
        }
        //PrintGraph(newGraph);

        // get i, j, and w(i, j) values
        scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\\s+");
            i = Integer.valueOf(line[0]) - 1;
            j = Integer.valueOf(line[1]) - 1;
            double w = Double.valueOf(line[2]);
            newGraph[i][j] = new Edge(w);
        }
        //PrintGraph(newGraph);



        return newGraph;
    }

    public static void PrintGraph(Edge[][] graph) {
        for (Edge[] edges : graph) {
            System.out.println(Arrays.toString(edges));
        }
        System.out.println();
    }

    public static void InitializePreflow(Edge[][] adjacencyMatrix, Node[] nodeList) {
        // lines 1-5 are already accounted for...starting at line 6...
        // NOTE: code requires that source is always the first item in the node list
        nodeList[0].setHeight(nodeList.length);

//        // My own thing: initialize source's excess to be equal to the capacity of all its outgoing edges
//        int initialExcess = 0;
//        for (int v = 0; v < adjacencyMatrix.length; v++) {
//            initialExcess += adjacencyMatrix[0][v].getCapacity();
//        }
//        nodeList[0].setExcess(initialExcess);

        for (int v = 1; v < adjacencyMatrix.length; v++) {
            // Set the flow for all the edges outgoing from s to their capacity
            // Zero capacity means no edge between s and v, so we make sure that's not the case
            if (adjacencyMatrix[0][v].getCapacity() != 0) {
                adjacencyMatrix[0][v].setFlow(adjacencyMatrix[0][v].getCapacity());
                nodeList[v].setExcess(adjacencyMatrix[0][v].getCapacity());
                nodeList[0].setExcess(nodeList[0].getExcess() - adjacencyMatrix[0][v].getCapacity());
            }

        }
    }

    public static void relabel(Node u, Edge[][] adjacencyMatrix, Node[] nodeList) {
        // Find v with minimum height such that (u, v) is an edge
        // NOTE: a Node's ID will be its index into the adjacencyMatrix
        int minHeight = Integer.MAX_VALUE;
        Boolean atLeastOneEdge = false;
        for (int v = 0; v < adjacencyMatrix.length; v++) {
            // If capacity is not 0, meaning there IS an edge...
            if (calculateResidualCapacity(u, nodeList[v], adjacencyMatrix) > 0) {
                if (nodeList[v].getHeight() < minHeight) {
                    minHeight = nodeList[v].getHeight();
                    atLeastOneEdge = true;
                }
            }
        }
        if (atLeastOneEdge) {
            System.out.println("Raising height of node " + u.getID());
            u.setHeight(1 + minHeight);
        }
    }

    public static void push(Node u, Node v, Edge[][] adjacencyMatrix, Node[] nodeList) {
        double deltaFlow = Math.min(u.getExcess(), calculateResidualCapacity(u, v, adjacencyMatrix));
        if (adjacencyMatrix[u.getID()][v.getID()].getCapacity() > 0) {
            adjacencyMatrix[u.getID()][v.getID()].setFlow(adjacencyMatrix[u.getID()][v.getID()].getFlow() + deltaFlow);
        }
        else {
            adjacencyMatrix[v.getID()][u.getID()].setFlow(adjacencyMatrix[v.getID()][u.getID()].getFlow() - deltaFlow);
        }
        u.setExcess(u.getExcess() - deltaFlow);
        v.setExcess(v.getExcess() + deltaFlow);
    }

    public static Boolean tryToFind(Edge[][] adjacencyMatrix, Node[] nodeList) {
        // Try to find a valid push operation
        for (Node node: nodeList) {
            // find an overflowing node
            if (node.getExcess() > 0) {
                // now, try to find a node2 such that c(node, node2) > 0...
                for (Node node2: nodeList) {
                    if (calculateResidualCapacity(node, node2, adjacencyMatrix) > 0) {
                        if (node.getHeight() == node2.getHeight() + 1) {
                            System.out.println("STATUS BEFORE:");
                            System.out.println("PUSHING FROM " + node.toString());
                            System.out.println("PUSHING TO " + node2.toString());
                            System.out.println("r(u, v) is " + calculateResidualCapacity(node, node2, adjacencyMatrix));
                            push(node, node2, adjacencyMatrix, nodeList);
                            System.out.println("STATUS AFTER:");
                            System.out.println("PUSHED FROM " + node.toString());
                            System.out.println("PUSHED TO " + node2.toString());
                            System.out.println("r(u, v) is " + calculateResidualCapacity(node, node2, adjacencyMatrix));
                            System.out.println();
                            return true;
                        }
                    }
                }
            }
        }

        // Try to find a valid relabel operation
        for (Node node: nodeList) {
            // If we try to relabel the sink, we're done, get out of this loop
            if (node.equals(nodeList[adjacencyMatrix.length - 1])) {
                break;
            }

            // find an overflowing node...
            if (node.getExcess() > 0) {
                Boolean validCondition = true;
                for (Node node2: nodeList) {
                    // if there is an RESIDUAL edge...
                    if (calculateResidualCapacity(node, node2, adjacencyMatrix) > 0) {
                        // verify that u.h <= v.h... otherwise set validCondition to false
                        if (node.getHeight() > node2.getHeight()) {
                            validCondition = false;
                        }
                    }
                }

                if (validCondition) {
                    System.out.println("STATUS BEFORE:");
                    System.out.println("RELABELING: " + node.toString());
                    relabel(node, adjacencyMatrix, nodeList);
                    System.out.println("STATUS AFTER:");
                    System.out.println("RELABELING: " + node.toString());
                    System.out.println();
                    return true;
                }
            }
        }
        // if we get here, we couldn't find a push or relabel operation.
        return false;
    }




    public static void main(String[] args) throws FileNotFoundException {
        // Read in input file and create adjacency graph
        InputReader inputReader = new InputReader();
        File file = new File("src/foundInternetFlow.txt");
        Edge[][] adjacencyMatrix = inputReader.produceGraph(file);

        // Create vertex list / node list
        Node[] nodeList = new Node[adjacencyMatrix.length];
        for (int i = 0; i < nodeList.length; i++) {
            nodeList[i] = new Node();
            // ID is the index in adjacency matrix AND nodeList
            nodeList[i].setID(i);
        }
        // Initialize preflow
        InitializePreflow(adjacencyMatrix, nodeList);
        
        System.out.println("INITIAL NODE LIST AFTER PREFLOW INITIALIZATION");
        for (Node node: nodeList) {
            System.out.println(node.toString());
        }
        System.out.println();

        Boolean ThereIsAnotherOperation = true;
        while (ThereIsAnotherOperation) {
            ThereIsAnotherOperation = tryToFind(adjacencyMatrix, nodeList);

            System.out.println("PRINTING GRAPH DURING MAIN LOOP AFTER OPERATION");
            PrintGraph(adjacencyMatrix);

            System.out.println("PRINTING NODE LIST AFTER OPERATION");
            for (Node node: nodeList) {
                System.out.println(node.toString());
            }
            System.out.println();
            System.out.println("PRINTING RESIDUAL GRAPH AFTER OPERATION");
            displayResidualGraph(adjacencyMatrix, nodeList);

            System.out.println("###########################################");
            System.out.println();
        }
        System.out.println("MAX FLOW IS: " + nodeList[adjacencyMatrix.length -1 ].getExcess());





    }

}

