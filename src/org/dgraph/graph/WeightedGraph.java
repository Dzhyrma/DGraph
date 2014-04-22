package org.dgraph.graph;

public interface WeightedGraph<V, E extends WeightedGraph.WeightedEdge<V, W>, W> extends
		Graph<V, E> {

	public interface WeightedEdge<V, W> extends Graph.Edge<V> {
		
		public double getWeight();
		
		public void setWeight(W weight);
		
	}
	
	public boolean addEdge(V v1, V v2, W w);

	public boolean addEdge(E e, W w);

}
