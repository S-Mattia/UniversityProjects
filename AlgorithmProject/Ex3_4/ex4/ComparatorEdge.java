package ex4;

import java.util.Comparator;

class ComparatorEdge<V, L extends Number> implements Comparator<AbstractEdge<V, L>> {
    @Override
    public int compare(AbstractEdge<V, L> a, AbstractEdge<V, L> b) {
        if ((a.getLabel()).doubleValue() < b.getLabel().doubleValue()) {
            return -1;
        } else if ((a.getLabel()).doubleValue() > b.getLabel().doubleValue()) {
            return 1;
        } else {
            return 0;
        }
    }
}