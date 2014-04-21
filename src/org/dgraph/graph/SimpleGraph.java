package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.Edge;

/** Implementation of a simple directed graph with edges.
*
* @param <V> type for vertices
* @param <E> type for edges. Should implement
*            <code>org.dgraph.edge.Edge&lt;V&gt;</code> interface
*
* @author Andrii Dzhyrma
* @since April 21, 2014 */
public class SimpleGraph<V, E extends Edge<V>> extends AbstractGraph<V, E> {

	private static final long serialVersionUID = 396772382551003763L;

	/** Creates an instance of a new simple graph. */
	public SimpleGraph() {
		super(null);
	}

	/** Creates an instance of a new simple graph with edge factory. The edge
	 * factory will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V
	 * v2)} method. Using another constructor, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public SimpleGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory);
	}
}
