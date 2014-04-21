package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.WeightedEdge;

public abstract class AbstractWeightedGraph<V, E extends WeightedEdge<V, W>, W>
		extends AbstractGraph<V, E> implements WeightedGraph<V, E, W> {

	private static final long serialVersionUID = -3197139092483967228L;

	public AbstractWeightedGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory);
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
