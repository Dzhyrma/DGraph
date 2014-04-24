package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.WeightedEdge;

/** Abstract implementation for all weighted directed graphs.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *          {@link org.dgraph.graph.edge.WeightedEdge WeightedEdge&lt;V, W&gt;}
 *          interface
 *
 * @author Andrii Dzhyrma
 * @since April 21, 2014 */
public abstract class AbstractWeightedDirectedGraph<V, E extends WeightedEdge<V, W>, W>
		extends DirectedGraph<V, E> implements WeightedGraph<V, E, W> {

	private static final long serialVersionUID = -3197139092483967228L;

	public AbstractWeightedDirectedGraph(BiFunction<V, V, E> edgeFactory,
			boolean withLoops) {
		super(edgeFactory, withLoops);
	}

	@Override
	public boolean addEdge(E e, W w) {
		e.setWeight(w);
		return addEdge(e);
	}

	@Override
	public boolean addEdge(V v1, V v2, W w) {
		E e = edgeFactory.apply(v1, v2);
		return addEdge(e, w);
	}

}
