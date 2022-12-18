package org.dgraph.graph.edge;

/**
 * Implementation of a weighted simple edge.
 *
 * @param <V> type for vertices
 * @author Andrii Dzhyrma
 * @since 0.1
 */
public class WeightedSimpleEdge<V> extends AbstractWeightedSimpleEdge<V, Double> {

    private static final double DEFAULT_WEIGHT = Double.valueOf(0d);

    /**
     * Constructor for the weighted simple edge. Default weight of the edge is set
     * to 0.
     *
     * @param source source of this edge
     * @param target target of this edge
     */
    public WeightedSimpleEdge(V source, V target) {
        super(source, target, DEFAULT_WEIGHT);
    }

    /**
     * Constructor for the weighted simple edge.
     *
     * @param source source of this edge
     * @param target target of this edge
     * @param weight weight of this edge
     */
    public WeightedSimpleEdge(V source, V target, double weight) {
        super(source, target, Double.valueOf(weight));
    }

    @Override
    public double getWeight() {
        return weight.doubleValue();
    }

}
