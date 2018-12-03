package org.dgraph.graph;

import graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

/** Main interface for all weighted graphs in DGraph library.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should implement
 *          {@link org.dgraph.graph.edge.WeightedEdge WeightedEdge&lt;V, W&gt;}
 *          interface
 * @param <W> type for weights
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public interface WeightedGraph<V, E extends WeightedEdge<V, W>, W> extends Graph<V, E> {

	/** Adds a new weighted edge to the graph.
	 *
	 * <p>
	 * If current graph doesn't contain any of the vertices from the edge, they
	 * will be added to the graph automatically.
	 * </p>
	 *
	 * @param e edge to be added to the graph
	 * @param w weight of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 *         edge
	 *
	 * @throws NullPointerException if the edge or any vertices it connects are
	 *           <tt>null</tt> */
	public boolean addEdge(E e, W w);

	/** Adds a new weighted edge to the graph. In order to use this method, graph should
	 * have an edge factory specified.
	 *
	 * <p>
	 * If current graph doesn't contain any of the vertices from the edge, they
	 * will be added to the graph automatically.
	 * </p>
	 *
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * @param w weight of the edge
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 *         edge
	 *
	 * @throws NullPointerException if the edge or any vertices it connects are
	 *           <tt>null</tt>, or edge factory was not specified. */
	public boolean addEdge(V v1, V v2, W w);

}
