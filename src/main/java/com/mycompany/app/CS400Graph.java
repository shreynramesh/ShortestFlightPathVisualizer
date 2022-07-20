// --== CS400 File Header Information ==--
// Name: <Shrey Ramesh>
// Email: <snramesh@wisc.edu>
// Team: <AL>
// TA: <Yelun Bao>
// Lecturer: <Gary Dahl>
// Notes to Grader: <N/A>

package com.mycompany.app;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CS400Graph<T> implements GraphADT<T> {

    /**
     * Vertex objects group a data field with an adjacency list of weighted
     * directed edges that lead away from them.
     */
    protected class Vertex {
        public T data; // vertex label or application specific data
        public LinkedList<Edge> edgesLeaving;

        public Vertex(T data) {
            this.data = data;
            this.edgesLeaving = new LinkedList<>();
        }
    }

    /**
     * Edge objects are stored within their source vertex, and group together
     * their target destination vertex, along with an integer weight.
     */
    protected class Edge {
        public Vertex target;
        public int weight;

        public Edge(Vertex target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    protected Hashtable<T, Vertex> vertices; // holds graph verticies, key=data

    /**
     * Constructor
     * 
     */
    public CS400Graph() {
        vertices = new Hashtable<>();
    }

    /**
     * Insert a new vertex into the graph.
     * 
     * @param data the data item stored in the new vertex
     * @return true if the data can be inserted as a new vertex, false if it is
     *         already in the graph
     * @throws NullPointerException if data is null
     */
    public boolean insertVertex(T data) {
        if (data == null)
            throw new NullPointerException("Cannot add null vertex");
        if (vertices.containsKey(data))
            return false; // duplicate values are not allowed
        vertices.put(data, new Vertex(data));
        return true;
    }

    /**
     * Remove a vertex from the graph.
     * Also removes all edges adjacent to the vertex from the graph (all edges
     * that have the vertex as a source or a destination vertex).
     * 
     * @param data the data item stored in the vertex to remove
     * @return true if a vertex with *data* has been removed, false if it was not in
     *         the graph
     * @throws NullPointerException if data is null
     */
    public boolean removeVertex(T data) {
        if (data == null)
            throw new NullPointerException("Cannot remove null vertex");
        Vertex removeVertex = vertices.get(data);
        if (removeVertex == null)
            return false; // vertex not found within graph
        // search all vertices for edges targeting removeVertex
        for (Vertex v : vertices.values()) {
            Edge removeEdge = null;
            for (Edge e : v.edgesLeaving)
                if (e.target == removeVertex)
                    removeEdge = e;
            // and remove any such edges that are found
            if (removeEdge != null)
                v.edgesLeaving.remove(removeEdge);
        }
        // finally remove the vertex and all edges contained within it
        return vertices.remove(data) != null;
    }

    /**
     * Insert a new directed edge with a positive edge weight into the graph.
     * 
     * @param source the data item contained in the source vertex for the edge
     * @param target the data item contained in the target vertex for the edge
     * @param weight the weight for the edge (has to be a positive integer)
     * @return true if the edge could be inserted or its weight updated, false
     *         if the edge with the same weight was already in the graph
     * @throws IllegalArgumentException if either source or target or both are not
     *                                  in the graph,
     *                                  or if its weight is < 0
     * @throws NullPointerException     if either source or target or both are null
     */
    public boolean insertEdge(T source, T target, int weight) {
        if (source == null || target == null)
            throw new NullPointerException("Cannot add edge with null source or target");
        Vertex sourceVertex = this.vertices.get(source);
        Vertex targetVertex = this.vertices.get(target);
        if (sourceVertex == null || targetVertex == null)
            throw new IllegalArgumentException("Cannot add edge with vertices that do not exist");
        if (weight < 0)
            throw new IllegalArgumentException("Cannot add edge with negative weight");
        // handle cases where edge already exists between these verticies
        for (Edge e : sourceVertex.edgesLeaving)
            if (e.target == targetVertex) {
                if (e.weight == weight)
                    return false; // edge already exists
                else
                    e.weight = weight; // otherwise update weight of existing edge
                return true;
            }
        // otherwise add new edge to sourceVertex
        sourceVertex.edgesLeaving.add(new Edge(targetVertex, weight));
        return true;
    }

    /**
     * Remove an edge from the graph.
     * 
     * @param source the data item contained in the source vertex for the edge
     * @param target the data item contained in the target vertex for the edge
     * @return true if the edge could be removed, false if it was not in the graph
     * @throws IllegalArgumentException if either source or target or both are not
     *                                  in the graph
     * @throws NullPointerException     if either source or target or both are null
     */
    public boolean removeEdge(T source, T target) {
        if (source == null || target == null)
            throw new NullPointerException("Cannot remove edge with null source or target");
        Vertex sourceVertex = this.vertices.get(source);
        Vertex targetVertex = this.vertices.get(target);
        if (sourceVertex == null || targetVertex == null)
            throw new IllegalArgumentException("Cannot remove edge with vertices that do not exist");
        // find edge to remove
        Edge removeEdge = null;
        for (Edge e : sourceVertex.edgesLeaving)
            if (e.target == targetVertex)
                removeEdge = e;
        if (removeEdge != null) { // remove edge that is successfully found
            sourceVertex.edgesLeaving.remove(removeEdge);
            return true;
        }
        return false; // otherwise return false to indicate failure to find
    }

    /**
     * Check if the graph contains a vertex with data item *data*.
     * 
     * @param data the data item to check for
     * @return true if data item is stored in a vertex of the graph, false otherwise
     * @throws NullPointerException if *data* is null
     */
    public boolean containsVertex(T data) {
        if (data == null)
            throw new NullPointerException("Cannot contain null data vertex");
        return vertices.containsKey(data);
    }

    /**
     * Check if edge is in the graph.
     * 
     * @param source the data item contained in the source vertex for the edge
     * @param target the data item contained in the target vertex for the edge
     * @return true if the edge is in the graph, false if it is not in the graph
     * @throws NullPointerException if either source or target or both are null
     */
    public boolean containsEdge(T source, T target) {
        if (source == null || target == null)
            throw new NullPointerException("Cannot contain edge adjacent to null data");
        Vertex sourceVertex = vertices.get(source);
        Vertex targetVertex = vertices.get(target);
        if (sourceVertex == null)
            return false;
        for (Edge e : sourceVertex.edgesLeaving)
            if (e.target == targetVertex)
                return true;
        return false;
    }

    /**
     * Return the weight of an edge.
     * 
     * @param source the data item contained in the source vertex for the edge
     * @param target the data item contained in the target vertex for the edge
     * @return the weight of the edge (0 or positive integer)
     * @throws IllegalArgumentException if either sourceVertex or targetVertex or
     *                                  both are not in the graph
     * @throws NullPointerException     if either sourceVertex or targetVertex or
     *                                  both are null
     * @throws NoSuchElementException   if edge is not in the graph
     */
    public int getWeight(T source, T target) {
        if (source == null || target == null)
            throw new NullPointerException("Cannot contain weighted edge adjacent to null data");
        Vertex sourceVertex = vertices.get(source);
        Vertex targetVertex = vertices.get(target);
        if (sourceVertex == null || targetVertex == null)
            throw new IllegalArgumentException("Cannot retrieve weight of edge between vertices that do not exist");
        for (Edge e : sourceVertex.edgesLeaving)
            if (e.target == targetVertex)
                return e.weight;
        throw new NoSuchElementException("No directed edge found between these vertices");
    }

    /**
     * Return the number of edges in the graph.
     * 
     * @return the number of edges in the graph
     */
    public int getEdgeCount() {
        int edgeCount = 0;
        for (Vertex v : vertices.values())
            edgeCount += v.edgesLeaving.size();
        return edgeCount;
    }

    /**
     * Return the number of vertices in the graph
     * 
     * @return the number of vertices in the graph
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Check if the graph is empty (does not contain any vertices or edges).
     * 
     * @return true if the graph does not contain any vertices or edges, false
     *         otherwise
     */
    public boolean isEmpty() {
        return vertices.size() == 0;
    }

    /**
     * Path objects store a discovered path of vertices and the overall distance of
     * cost
     * of the weighted directed edges along this path. Path objects can be copied
     * and extended
     * to include new edges and verticies using the extend constructor. In
     * comparison to a
     * predecessor table which is sometimes used to implement Dijkstra's algorithm,
     * this
     * eliminates the need for tracing paths backwards from the destination vertex
     * to the
     * starting vertex at the end of the algorithm.
     */
    protected class Path implements Comparable<Path> {
        public Vertex start; // first vertex within path
        public int distance; // sumed weight of all edges in path
        public List<T> dataSequence; // ordered sequence of data from vertices in path
        public Vertex end; // last vertex within path

        /**
         * Creates a new path containing a single vertex. Since this vertex is both
         * the start and end of the path, it's initial distance is zero.
         * 
         * @param start is the first vertex on this path
         */
        public Path(Vertex start) {
            this.start = start;
            this.distance = 0;
            this.dataSequence = new LinkedList<>();
            this.dataSequence.add(start.data);
            this.end = start;
        }

        /**
         * This extension constructor makes a copy of the path passed into it as an
         * argument
         * without affecting the original path object (copyPath). The path is then
         * extended
         * by the Edge object extendBy.
         * 
         * @param copyPath is the path that is being copied
         * @param extendBy is the edge the copied path is extended by
         */
        public Path(Path copyPath, Edge extendBy) {
            // Copying over fields from copy path into this new Path object to make a deep
            // copy
            this.start = copyPath.start;
            this.distance = copyPath.distance;
            this.dataSequence = new LinkedList<>();
            for (T data : copyPath.dataSequence) { // Making another List reference to not interfere with original
                this.dataSequence.add(data);
            }
            this.end = copyPath.end;

            // Extending path by the Edge extendBy, adding to the overall distance of the
            // Path, and changing the end reference
            this.dataSequence.add(extendBy.target.data);
            this.distance += extendBy.weight;
            this.end = extendBy.target;

        }

        /**
         * Allows the natural ordering of paths to be increasing with path distance.
         * When path distance is equal, the string comparison of end vertex data is used
         * to break ties.
         * 
         * @param other is the other path that is being compared to this one
         * @return -1 when this path has a smaller distance than the other,
         *         +1 when this path has a larger distance that the other,
         *         and the comparison of end vertex data in string form when these
         *         distances are tied
         */
        public int compareTo(Path other) {
            int cmp = this.distance - other.distance;
            if (cmp != 0)
                return cmp; // use path distance as the natural ordering
            // when path distances are equal, break ties by comparing the string
            // representation of data in the end vertex of each path
            return this.end.data.toString().compareTo(other.end.data.toString());
        }
    }

    protected Path dijkstrasShortestPath(T start, T end) {
        PriorityQueue<Path> frontier = new PriorityQueue<>();

        if (start == null || end == null) {
            throw new NoSuchElementException("Cannot find the shortest path between null nodes");
        }

        // Finding vertices containing data if they exist
        Vertex startVertex = vertices.get(start);
        Vertex endVertex = vertices.get(end);

        if (startVertex == null || endVertex == null) {
            throw new NoSuchElementException(
                    "Cannot find the shortest path between nodes that do not exist in this graph");
        }

        ArrayList<T> vistedData = new ArrayList<>(); // Keeps track of nodes that have already been visted so they
                                                     // are not explored again.
        vistedData.add(start); // Have to visit this node first
        frontier.add(new Path(startVertex)); // Adding the start node as the first path in the frontier

        // Keeps looking for shorter paths until either the target node is found of the
        // frontier is empty. This means that no path exists.
        while (!frontier.isEmpty()) {
            Path chosenPath = frontier.remove(); // Chosing the Path with the shortest distance

            // Checking whether the chosenPath has reached the target
            if (chosenPath.end.equals(endVertex)) {
                return chosenPath;
            }

            for (Edge e : chosenPath.end.edgesLeaving) { // Adding all new-found Paths to the frontier unless we have
                                                         // already visited the node
                if (e != null && !vistedData.contains(e.target.data)) { // Checking if e has any leaving edges
                    frontier.add(new Path(chosenPath, e));
                } // else if (containsVertexInPath(frontier, e.target)) {
                  // compareAndFixFrontier(frontier, e.target, new Path(chosenPath, e));
                  // }
            }

            if (!frontier.isEmpty()) { // Adding the next chosenPath to the vistedData list as long as the frontier is
                                       // not empty
                vistedData.add(frontier.peek().end.data);
            }
        }

        throw new NoSuchElementException("Could not find a path between these two nodes");
    }

    // /**
    // * Looks through the frontier to find out whether the pathInQuestion is
    // cheaper
    // * than any other path to the same node.
    // *
    // * @param frontier A priority queue of paths that have been registered but
    // * not deemed as the cheapest path to a certain ndoe
    // * @param pathInQuestion the new Path that has been found from a certain edge
    // * @return true if a cheaper path to the node exists, false if a cheaper path
    // to
    // * the node does not exist
    // */
    // private boolean cheaperPath(PriorityQueue<CS400Graph<T>.Path> frontier, Path
    // pathInQuestion) {
    // if (frontier.isEmpty()) { // Cannot be a cheaper path if there is nothing in
    // the frontier
    // return false;
    // }

    // for (Path p : frontier) {
    // if (p.end.equals(pathInQuestion.end)) { // Checking if a path exists that
    // ends on the same node
    // if (p.compareTo(pathInQuestion) > 0) { // Checking if a cheaper path to that
    // node has been found
    // return true;
    // }
    // }
    // }

    // return false; // No path exists that ends on the same node
    // }

    /**
     * Returns the shortest path between start and end.
     * Uses Dijkstra's shortest path algorithm to find the shortest path.
     * 
     * @param start the data item in the starting vertex for the path
     * @param end   the data item in the destination vertex for the path
     * @return list of data item in vertices in order on the shortest path between
     *         vertex
     *         with data item start and vertex with data item end, including both
     *         start and end
     * @throws NoSuchElementException when no path from start to end can be found
     *                                including when no vertex containing start or
     *                                end can be found
     */
    public List<T> shortestPath(T start, T end) {
        return dijkstrasShortestPath(start, end).dataSequence;
    }

    /**
     * Returns the cost of the path (sum over edge weights) between start and end.
     * Uses Dijkstra's shortest path algorithm to find the shortest path.
     * 
     * @param start the data item in the starting vertex for the path
     * @param end   the data item in the end vertex for the path
     * @return the cost of the shortest path between vertex with data item start
     *         and vertex with data item end, including all edges between start and
     *         end
     * @throws NoSuchElementException when no path from start to end can be found
     *                                including when no vertex containing start or
     *                                end can be found
     */
    public int getPathCost(T start, T end) {
        return dijkstrasShortestPath(start, end).distance;
    }

    public static class BackendDeveloperTests {
        AirportGraph flightGraph;

        ArrayList<IAirport> airports;
        AirportPlaceholder a1;
        AirportPlaceholder a2;
        AirportPlaceholder a3;
        AirportPlaceholder a4;
        AirportPlaceholder a5;
        AirportPlaceholder a6;
        AirportPlaceholder a7;
        AirportPlaceholder a8;
        AirportPlaceholder a9;
        AirportPlaceholder a10;

        @BeforeEach
        public void createGraph() {
            flightGraph = new AirportGraph();

            // Creating airports
            airports = new ArrayList<>();
            a1 = new AirportPlaceholder("Pittsburgh International", "Pittsburgh", "USA", 40.49150085, -80.23290253);
            a2 = new AirportPlaceholder("Chicago O'Hare International Airport", "Chicago", "USA", 41.9786, -87.9048);
            a3 = new AirportPlaceholder("John F Kennedy International Airport", "New York City", "USA", 40.63980103,
                    -73.77890015);
            a4 = new AirportPlaceholder("Los Angeles International Airport", "Los Angeles", "USA", 33.94250107,
                    -118.4079971);
            a5 = new AirportPlaceholder("Sydney Kingsford Smith International Airport", "Sydney", "Australia",
                    -33.94609832763672, 151.177001953125);
            a6 = new AirportPlaceholder("Charles de Gaulle International Airport", "Paris", "France", 49.012798, 2.55);
            a7 = new AirportPlaceholder("Norman Manley International Airport", "Kingston", "Jamaica", 17.935699462890625,
                    -76.7874984741211);
            a8 = new AirportPlaceholder("Chennai International Airport", "Chennai", "India", 12.990005493164062,
                    80.16929626464844);
            a9 = new AirportPlaceholder("Cochin International Airport", "Cochin", "India", 10.152, 76.401901);
            a10 = new AirportPlaceholder("Denver International Airport", "Denver", "USA", 39.861698150635, -104.672996521);

            airports.add(a1);
            airports.add(a2);
            airports.add(a3);
            airports.add(a4);
            airports.add(a5);
            airports.add(a6);
            airports.add(a7);
            airports.add(a8);
            airports.add(a9);
            airports.add(a10);
            // Creating routes
            ArrayList<IAirport> routes = new ArrayList<IAirport>();

            for (IAirport airport : airports) { // Setting routes for the airports. Making a complete graph
                for (IAirport airport1 : airports) {
                    if (!airport1.equals(airport)) {
                        routes.add(airport1);
                    }
                }
                airport.setRoutes(routes);
                routes = new ArrayList<IAirport>();
            }

            flightGraph.temporaryLoadGraph(airports); // Creating the graph
        }

        /**
         * This method tests the ability of AirportGraph to create a graph based on a
         * list of Airports. Specifically, this method checks whether the vertices and
         * edges are correct.
         */
        @Test
        public void testLoadGraph() {

            System.out.println(flightGraph.vertices.keySet().toString());
            // Verifying vertices
            assertEquals(flightGraph.getVertexCount(), 10);

            // Vertifying edges
            for (IAirport source : airports) { // Setting routes for the airports. Making a complete graph
                for (IAirport destination : airports) {
                    if (!source.equals(destination)) {
                        assertTrue(flightGraph.containsEdge(source, destination));
                    }
                }
            }

            assertTrue(a1.isInternational());

        }

        /**
         * This method tests whether dikstra's algorithm works with the planes. Edges
         * will be removed so that the possiblity of a direct flight is removed. This
         * will force dijktras to find a non-stop path to the destination.
         */
        @Test
        public void testShortestPathLosAngelestoChennaiAndSydney() {
            flightGraph.removeEdge(a4, a8);

            assertEquals(flightGraph.shortestPath(a4, a8).toString(),
                    "[Los Angeles International Airport (Los Angeles, USA), Cochin International Airport (Cochin, India), Chennai International Airport (Chennai, India)]");

            a4.getRoutes().add(a8);
            flightGraph.temporaryLoadGraph(airports);

            assertEquals(flightGraph.shortestPath(a4, a5).toString(),
                    "[Los Angeles International Airport (Los Angeles, USA), Sydney Kingsford Smith International Airport (Sydney, Australia)]");

            flightGraph.removeEdge(a1, a6);
            flightGraph.removeEdge(a2, a6);
            flightGraph.removeEdge(a3, a6);

            assertEquals(flightGraph.shortestPath(a2, a6).toString(),
                    "[Chicago O'Hare International Airport (Chicago, USA), Denver International Airport (Denver, USA), Charles de Gaulle International Airport (Paris, France)]");

            a1.getRoutes().add(a6);
            a2.getRoutes().add(a6);
            a3.getRoutes().add(a6);
            flightGraph.temporaryLoadGraph(airports);
        }

        /**
         * This method tests the ablity to calculate the distance between two airports
         * on the eart given thier longitude and latitude. The test gives the method a
         * marigin of error of 0.5% due to irregularities in the Earth's surface.
         */
        @Test
        public void testDistanceBetweenChicagoAndPittsburgh() {
            assertTrue(flightGraph.calculateDistanceBetweenAirports(a1, a2) <= (664 * (664 * 1.005))
                    || flightGraph.calculateDistanceBetweenAirports(a1, a2) >= (664 * (664 * .995)));
        }

        /**
         * Checks the capability to calculate flight time based on a flight path and a
         * estimation on the flight speed.
         */
        @Test
        public void testCalculateFlightTime() {
            flightGraph.removeEdge(a3, a5);
            double averagePlaneSpeed = 880;
            List<IAirport> inOrderPath = flightGraph.shortestPath(a3, a5);

            assertTrue(flightGraph.calculateflightTime(inOrderPath, averagePlaneSpeed)
                    .contains("Total Time: 18 hours and 9 minutes"));
            a3.getRoutes().add(a5);
            flightGraph.temporaryLoadGraph(airports);
        }

        /**
         * Checks whether the shortest path costs are correct.
         */
        @Test
        public void testShortestPathCost() {
            flightGraph.removeEdge(a4, a8);

            assertEquals(flightGraph.getPathCost(a4, a8), 15353);

            a4.getRoutes().add(a8);
            flightGraph.temporaryLoadGraph(airports);

            assertEquals(flightGraph.getPathCost(a4, a5), 12035);

            flightGraph.removeEdge(a1, a6);
            flightGraph.removeEdge(a2, a6);
            flightGraph.removeEdge(a3, a6);

            assertEquals(flightGraph.getPathCost(a2, a6), 9239);

            a1.getRoutes().add(a6);
            a2.getRoutes().add(a6);
            a3.getRoutes().add(a6);
            flightGraph.temporaryLoadGraph(airports);
        }

        // Testing Data Wrangler Code

        /**
         * Checks whether AirportLoader properly returns a list of Airplane objects
         */
        @Test
        public void testAirplaneLoaderPt1() {
            AirportLoader loader = new AirportLoader();
            ArrayList<IAirport> allAirports = new ArrayList<>();
            try {
                allAirports = loader.loadFlights("./src/main/java/com/example/app/flightData.json");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            assertEquals(allAirports.size(), 7698);
        }

        /**
         * Checks whether AirportLoader properly sets routes given a source and a
         * destination
         */
        @Test
        public void testAirplaneLoaderPt2() {
            HashMap<String, IAirport> nameToAirport = new HashMap<>();

            AirportLoader loader = new AirportLoader();
            ArrayList<IAirport> allAirports = new ArrayList<>();
            try {
                allAirports = loader.loadFlights("./src/main/java/com/example/app/flightData.json");
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            for (IAirport a : allAirports) {
                nameToAirport.put(a.toString(), a);
            }

            loader.loadRoutes(nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Whiteman Airport (Los Angeles, United States)"));

            assertEquals(nameToAirport.get("Los Angeles International Airport (Los Angeles, United States)").getRoutes()
                    .toString().trim(),
                    "[Whiteman Airport (Los Angeles, United States)]");
        }

        // Integration Tests
        /**
         * Checks integration between the DataWrangler and Backend code. Test should
         * succesfully create an airport loader class, load data from the JSON file, and
         * create a accurate graph. This test will also ensure the correctness of
         * djikstras once again.
         */
        @Test
        public void JSONToBackendTest() {
            AirportLoader loader = new AirportLoader(); // Creating an AirportLoader class to load the data
            ArrayList<IAirport> allAirports = new ArrayList<>(); // ArrayList to hold all the airports after the data is
                                                                 // loaded
            HashMap<String, IAirport> nameToAirport = new HashMap<>();

            try {
                allAirports = loader.loadFlights("./src/main/java/com/example/app/flightData.json"); // Loading
                                                                                                                         // data
                                                                                                                         // from
                                                                                                                         // flightData.json
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            for (IAirport a : allAirports) {
                nameToAirport.put(a.toString(), a);
            }

            flightGraph = new AirportGraph(); // Graph which is going to hold all airports and their respective routes
            flightGraph.loadGraph(nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Los Angeles International Airport (Los Angeles, United States)"), allAirports,
                    loader); // Loading the specific source and destination into the graph

            assertEquals(flightGraph.getVertexCount(), 7698);

            // assertEquals(flightGraph.getEdgeCount(), 1300962);
            assertEquals(flightGraph.shortestPath(
                    nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Los Angeles International Airport (Los Angeles, United States)")).toString().trim(),
                    "[Pittsburgh International Airport (Pittsburgh, United States), Los Angeles International Airport (Los Angeles, United States)]");

            flightGraph.loadGraph(nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Coventry Airport (Coventry, United Kingdom)"), allAirports,
                    loader); // Loading the specific source and destination into the graph
            assertEquals(flightGraph.shortestPath(
                    nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Coventry Airport (Coventry, United Kingdom)")).toString().trim(),
                    "[Pittsburgh International Airport (Pittsburgh, United States), Birmingham International Airport (Birmingham, United Kingdom), Coventry Airport (Coventry, United Kingdom)]");

            flightGraph.loadGraph(nameToAirport.get("Dane County Regional Truax Field (Madison, United States)"),
                    nameToAirport.get("Coventry Airport (Coventry, United Kingdom)"), allAirports,
                    loader); // Loading the specific source and destination into the graph
            assertEquals(flightGraph.shortestPath(
                    nameToAirport.get("Dane County Regional Truax Field (Madison, United States)"),
                    nameToAirport.get("Coventry Airport (Coventry, United Kingdom)")).toString().trim(),
                    "[Dane County Regional Truax Field (Madison, United States), Chippewa County International Airport (Sault Ste Marie, United States), Birmingham International Airport (Birmingham, United Kingdom), Coventry Airport (Coventry, United Kingdom)]");
        }

        /**
         * Checks integration between the Tries, DataWrangler and the Backend
         */
        @Test
        public void JSONtoTriestoBackendTest() {
            AirportLoader loader = new AirportLoader(); // Creating an AirportLoader class to load the data
            ArrayList<IAirport> allAirports = new ArrayList<>(); // ArrayList to hold all the airports after the data is
                                                                 // loaded

            HashMap<String, IAirport> nameToAirport = new HashMap<>();
            try {
                allAirports = loader.loadFlights("./src/main/java/com/example/app/flightData.json"); // Loading
                                                                                                                         // data
                                                                                                                         // from
                                                                                                                         // flightData.json
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            for (IAirport a : allAirports) {
                nameToAirport.put(a.toString(), a);
            }
            // flightGraph = new AirportGraph(); // Graph which is going to hold all
            // airports and their respective routes
            flightGraph.loadGraph(nameToAirport.get("Pittsburgh International Airport (Pittsburgh, United States)"),
                    nameToAirport.get("Whiteman Airport (Los Angeles, United States)"), allAirports, loader); // Loading all
                                                                                                              // airports
                                                                                                              // into the
                                                                                                              // graph

            assertEquals(flightGraph.getAirportsFromTrie("Pittsburgh").toString().trim(),
                    "[Pittsburgh, United States (Allegheny County Airport), Pittsburgh, United States (Pittsburgh International Airport)]");
        }
    }
}
