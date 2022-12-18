package org.dgraph.graph;

import org.dgraph.graph.edge.WeightedEdge;

import java.util.function.BiFunction;

/**
 * Abstract implementation for all weighted directed graphs.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *            {@link org.dgraph.graph.edge.WeightedEdge WeightedEdge&lt;V, W&gt;}
 *            interface
 * @author Andrii Dzhyrma
 * @since 0.1
 */
public abstract class AbstractWeightedDirectedGraph<V, E extends WeightedEdge<V, W>, W> extends DirectedGraph<V, E> implements WeightedGraph<V, E, W> {

    private static final long serialVersionUID = -3197139092483967228L;

    /**
     * This constructor allows a creation of an instances of a directed weighted
     * graph with edge factory. The edge factory will be used in the
     * {@link #addEdge(Object, Object) addEdge(V v1, V v2)} and
     * {@link #addEdge(Object, Object, Object) addEdge(V v1, V v2, W w)} methods.
     * Otherwise, if no edge factory is given, these methods will throw
     * {@link UnsupportedOperationException}.
     *
     * @param edgeFactory the edge factory to create edge by given source and
     *                    target vertices
     * @param withLoops   specifies whether the graph can contain loops or not
     */
    public AbstractWeightedDirectedGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
        super(edgeFactory, withLoops);
    }

    @Override
    public boolean addEdge(E e, W w) {
        e.setWeight(w);
        return addEdge(e);
    }

    @Override
    public boolean addEdge(V v1, V v2, W w) {
        final E e = edgeFactory.apply(v1, v2);
        return addEdge(e, w);
    }

}
