package ex4;

import ex3.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * Classe Prim
 * 
 * @param <V> il tipo degli oggetti che rappresentano i nodi del grafo
 * @param <L> il tipo degli oggetti che rappresentano le etichette degli archi
 *            del grafo
 * 
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 21/05/2024;
 * @details Program that implement the ADT Graph;
 */

public class Prim {
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * DESCRIPTION: Find the minimum spanning forest of a graph, using a
     * prioirityQueue, a graph and also a hashmap to allocate the visited nodes;
     */
    public static <V, L extends Number> Collection<? extends AbstractEdge<V, L>> minimumSpanningForest(
            Graph<V, L> graph) {
        ArrayList<AbstractEdge<V, L>> minFor = new ArrayList<>();
        ComparatorEdge<V, L> Comparator = new ComparatorEdge<>();
        PriorityQueue<AbstractEdge<V, L>> minHeap = new PriorityQueue<>(Comparator);
        HashMap<V, Integer> Visited = new HashMap<>();

        Collection<V> Nodes = graph.getNodes();
        Collection<V> NodeNeighbours;
        int nPlaceVisited = 0;
        for (V element : Nodes) {
            V place1 = element;
            V place2 = null;
            while (!(minHeap.empty()) || !Visited.containsKey(place1)) {
                if (!Visited.containsKey(place1)) {
                    NodeNeighbours = graph.getNeighbours(place1);
                    for (V neighbour : NodeNeighbours) {
                        minHeap.push(new Edge<V, L>(place1, neighbour, graph.getLabel(place1, neighbour)));
                    }
                    Visited.put(place1, nPlaceVisited++);
                }
                if (!minHeap.empty()) {
                    place1 = minHeap.top().getStart();
                    place2 = minHeap.top().getEnd();
                    if (!Visited.containsKey(place2)) {
                        minFor.add(minHeap.top());
                        place1 = place2;
                    }
                    minHeap.pop();
                }
            }

        }
        return minFor;
    }

    public static void main(String[] args) throws IOException {
        Graph<String, Float> grafo = new Graph<>(false, true);

        if (args[0] == null)
            return;
        // leggi i dati CSV del grafo dal percorso in args[1]
        loadGraph(args[0], grafo);

        // calcola la minima foresta ricoprente con minimumSpanningForest
        Collection<? extends AbstractEdge<String, Float>> minthree = minimumSpanningForest(grafo);

        // scrivi su standard output solo la descrizione della foresta calcolata come
        // CSV con formato analogo a quello in input
        printEdges(minthree, grafo);
        // su standard error si possono scrivere ulteriori informazioni, come il numero
        // di nodi e archi nella foresta calcolata,
        // o il peso totale della foresta
    }

    private static <V, L extends Number> void printEdges(Collection<? extends AbstractEdge<V, L>> edges,
            Graph<V, L> grafo) {
        double sum = 0;
        System.out.println("\nPRINT ALL THE EDGES IN THE GRAPH");
        for (AbstractEdge<V, L> element : edges) {
            System.out.println(element.getStart() + "," + element.getEnd() + "," + element.getLabel() + "\n");
            sum += element.getLabel().doubleValue();
        } // for
        System.err.println("------------------------------------------------------------------------------");
        System.err.println("|NUMBER OF NODE IN GRAPH:\t" + grafo.numNodes());
        System.err.println("|NUMBER OF EDGES IN GRAPH:\t" + grafo.numEdges());
        System.err.println("|NUMBER OF EDGES IN MST:\t" + edges.size());
        System.err.printf("|MST WEIGTH:\t%.6f\n", sum / 1000.00);
        System.err.println("------------------------------------------------------------------------------");
    }

    private static void loadGraph(String filepath, Graph<String, Float> grafo) throws IOException {
        System.out.print("LOADING DATA FROM THE FILE INTO THE GRAPH...");
        Path inputFilePath = Paths.get(filepath);
        try (BufferedReader fileInputReader = Files.newBufferedReader(inputFilePath, ENCODING)) {
            String line = null;
            while ((line = fileInputReader.readLine()) != null) {
                String[] lineElements = line.split(",");
                if (!grafo.addEdge(lineElements[0], lineElements[1], Float.parseFloat(lineElements[2]))) {
                    if (grafo.getLabel(lineElements[0], lineElements[1]) > Float.parseFloat(lineElements[2])) {
                        grafo.removeEdge(lineElements[0], lineElements[1]);
                        grafo.addEdge(lineElements[0], lineElements[1], Float.parseFloat(lineElements[2]));
                    }
                }
            }
        }
        System.out.println("DONE!");
    }

}
