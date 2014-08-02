package org.dgraph.graph.path;

import org.dgraph.graph.edge.WeightedEdge;

public interface WeightedPath<V, E extends WeightedEdge<V, ?>> extends Path<V, E> {

	public double getDistance();
	
}
