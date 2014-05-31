package org.dgraph.graph.edge;

/** Main interface for all weighted edges in DGraph library.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public interface WeightedEdge<V, W> extends Edge<V> {

	/** Retrieves the weight of this edge.
	 * 
	 * @return weight of this edge */
	public double getWeight();

	/** Assigns the weight to this edge.
	 * 
	 * @param weight new weight for this edge */
	public void setWeight(W weight);
}
