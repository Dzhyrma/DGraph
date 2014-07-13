package org.dgraph.graph.edge;

/** Main interface for all flow edges in DGraph library.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public interface FlowEdge<V> extends Edge<V> {

	/** Retrieves the flow of this edge.
	 * 
	 * @return flow of this edge */
	public double getFlow();

	/** Assigns the flow to this edge.
	 * 
	 * @param flow new flow for this edge */
	public void setFlow(double flow);

	/** Retrieves the capacity of this edge.
	 * 
	 * @return capacity of this edge */
	public double getCapacity();

}
