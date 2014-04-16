package org.dgraph.graph;

import org.dgraph.graph.edge.WeightedEdge;

public interface WeightedGraph<V, E extends WeightedEdge<V>> extends Graph<V, E>{

	public void setEdgeWieght(E e, double wieght);
}
