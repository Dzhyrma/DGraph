package org.dgraph.graph.edge;

public interface WeightedEdge<V, W> extends Edge<V> {
	
	public double getWeight();
	
	public void setWeight(W weight);
	
}
