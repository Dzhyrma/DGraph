package org.dgraph.graph.path;

import org.dgraph.graph.edge.WeightedEdge;

import java.util.List;

public class SimpleWeightedPath<V, E extends WeightedEdge<V, ?>> extends SimplePath<V, E> implements WeightedPath<V, E> {

    private double distance = 0;

    public SimpleWeightedPath(V source, V target, List<E> edges) {
        super(source, target, edges);

        if (edges != null) {
            edges.forEach(edge -> distance += edge.getWeight());
        } else {
            distance = Double.POSITIVE_INFINITY;
        }
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("Path(%.2f):", distance) + getEdges();
    }

}
