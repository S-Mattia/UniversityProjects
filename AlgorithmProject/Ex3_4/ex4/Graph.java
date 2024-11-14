package ex4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph class.
 * 
 * This class implements a generic graph. The V type used for the nodes of the
 * graph must have the equals() and hashCode() methods properly implemented for
 * guarantee the correct functioning of the methods of this class.
 * 
 * @param <V> the type of the objects representing the nodes of the graph
 * @param <L> the type of the objects representing the arc labels of the graph
 *
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 18/05/2024;
 * @details Program that implement the ADT Graph;
 */
public class Graph<V, L> implements AbstractGraph<V, L> {

    private final HashMap<V, Integer> nodes;
    private ArrayList<V> nodesList;
    private ArrayList<LinkedList<Edge<V, L>>> edges;
    private boolean Directed;
    private boolean Labelled;
    private int nItem;
    private int nEdge;

    /**
     * DESCRIPTION: Constructor of the class ;
     */
    public Graph(boolean d, boolean l) {
        this.Directed = d;
        this.Labelled = l;
        nItem = 0;
        nEdge = 0;
        nodes = new HashMap<V, Integer>();
        nodesList = new ArrayList<V>();
        edges = new ArrayList<LinkedList<Edge<V, L>>>();
    }

    public boolean isDirected() {
        return this.Directed;
    };

    public boolean isLabelled() {
        return this.Labelled;
    };

    /**
     * DESCRIPTION: The implementation addNode insert a new Node in the last free
     * position, creating a new
     * linked list in Edges that is going to contain all the edges of the node;
     */
    public boolean addNode(V a) {
        if (this.containsNode(a))
            return false;
        this.nodes.put(a, this.nItem);
        this.nodesList.add(a);
        this.edges.add(new LinkedList<>());
        this.nItem += 1;
        return true;
    };

    /**
     * DESCRIPTION: The implementation of addEdge insert a new edge in the last free
     * element of the linked list
     * in position nodes.get(a) in Edges, that is the index of a in the hashmap.
     */
    public boolean addEdge(V a, V b, L l) {
        if (containsEdge(a, b))
            return false;
        if (!containsNode(a) | !containsNode(b)) {
            addNode(a);
            addNode(b);
        }
        if (!this.isLabelled()) {
            l = null;
        } else {
            if (l == null)
                return false;
        }
        Edge<V, L> Edge = new Edge<V, L>(a, b, l);
        (this.edges.get(this.nodes.get(a))).add(Edge);
        this.nEdge += 1;
        if (!isDirected()) {
            this.addEdge(b, a, l);
        }
        return true;
    };

    public boolean containsNode(V a) {
        return this.nodes.containsKey(a);
    };

    public boolean containsEdge(V a, V b) {
        if (!containsNode(a) | !containsNode(b))
            return false;
        for (Edge<V, L> element : this.edges.get(this.nodes.get(a))) {
            if (element.getStart().equals(a) && element.getEnd().equals(b))
                return true;
        }
        return false;
    }

    /**
     * DESCRIPTION: The implementation of removeNode remove the node in position
     * nodes.get(a)
     * after removing the related edges in Edges;
     * To remove the Node the method swap the corrent Node with the last inserted,
     * and after remove
     * the last.
     */
    public boolean removeNode(V a) {
        if (!containsNode(a))
            return false;

        for (int i = 0; i < nItem; i++) {
            this.removeEdge(this.nodesList.get(i), a);
        }
        for (V element : this.getNeighbours(a)) {
            this.removeEdge(a, element);
        }

        int remIndex = this.nodes.get(a);
        this.edges.set(remIndex, this.edges.get(nItem - 1));
        this.edges.remove(nItem - 1);
        this.nodesList.set(remIndex, this.nodesList.get(nItem - 1));
        this.nodes.put(this.nodesList.get(nItem - 1), remIndex);
        this.nodes.remove(this.nodesList.get(nItem - 1), nItem - 1);
        this.nodesList.remove(this.nItem - 1);
        this.nItem -= 1;
        return true;
    };

    /**
     * DESCRIPTION: The implementation of removeEdge remove the edge form the Edges
     * Linkedlist,
     * that refer to node a.
     */
    public boolean removeEdge(V a, V b) {
        if (!containsEdge(a, b))
            return false;
        Edge<V, L> remVal = null;
        for (Edge<V, L> element : this.edges.get(this.nodes.get(a))) {
            if (element.getStart().equals(a) && element.getEnd().equals(b)) {
                this.edges.get(this.nodes.get(a)).remove(remVal);
                this.nEdge -= 1;
                break;
            }
        }
        if (!this.isDirected()) {
            this.removeEdge(b, a);
        }
        return true;
    }

    public int numNodes() {
        return this.nItem;
    };

    public int numEdges() {
        if (!isDirected()) {
            return this.nEdge / 2;
        } else {
            return this.nEdge;
        }
    };

    public Collection<V> getNodes() {
        return this.nodesList;
    };

    public Collection<? extends AbstractEdge<V, L>> getEdges() {
        if (this.numEdges() == 0) {
            return null;
        }
        ArrayList<Edge<V, L>> allEdges = new ArrayList<>();
        for (LinkedList<Edge<V, L>> elementList : this.edges) {
            for (Edge<V, L> element : elementList) {
                allEdges.add(element);
            }
        }
        return allEdges;
    };

    public Collection<V> getNeighbours(V a) {
        if (!containsNode(a)) {
            return null;
        }
        ArrayList<V> neighbours = new ArrayList<>();
        for (Edge<V, L> element : this.edges.get(this.nodes.get(a))) {
            neighbours.add(element.getEnd());
        }
        return neighbours;
    };

    public L getLabel(V a, V b) {
        if (!isLabelled() | !containsEdge(a, b)) {
            return null;
        }
        for (Edge<V, L> element : this.edges.get(this.nodes.get(a))) {
            if (element.getStart().equals(a) && element.getEnd().equals(b)) {
                return element.getLabel();
            }
        }
        return null;
    }
}
