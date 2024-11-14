package ex4;

/**
 * Edge class.
 * 
 * @param <V> the type of the objects representing the nodes of the edge
 * @param <L> the type of the objects representing the arc labels of the edge
 *
 * @author Sandri Mattia, Sandri Gabriele;
 * @date 18/05/2024;
 * @details Program that implement the  AbstractEdge;
 */

public class Edge<V, L> implements AbstractEdge<V, L> {

    private V start;
    private V end;
    private L label;

    public Edge(V A, V B, L L) {
        this.start = A;
        this.end = B;
        this.label = L;
    }

    public V getStart() {
        return this.start;
    }; // il nodo di partenza dell'arco

    public V getEnd() {
        return this.end;
    }; // il nodo di arrivo dell'arco

    public L getLabel() {
        return this.label;
    }; // l'etichetta dell'arco

}
