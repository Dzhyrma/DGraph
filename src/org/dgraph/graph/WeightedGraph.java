package org.dgraph.graph;

import org.dgraph.graph.edge.WeightedEdge;

public interface WeightedGraph<V, E extends WeightedEdge<V, W>, W> extends
		Graph<V, E> {
	
	public boolean addEdge(V v1, V v2, W w);

	public boolean addEdge(E e, W w);

}
