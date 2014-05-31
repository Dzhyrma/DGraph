package org.dgraph.graph.edge;

/** Main interface for all edges in DGraph library.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public interface Edge<V> {

	/** Retrieves the source of this edge.
	 * 
	 * @return source of this edge */
	public V getSource();

	/** Retrieves the target of this edge.
	 * 
	 * @return target of this edge */
	public V getTarget();
}