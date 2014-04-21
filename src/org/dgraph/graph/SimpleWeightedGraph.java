package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.WeightedEdge;

/** Implementation of a simple weighted directed graph with edges.
*
* @param <V> type for vertices
* @param <E> type for edges. Should implement
*            <code>org.dgraph.edge.Edge&lt;V&gt;</code> interface
*
* @author Andrii Dzhyrma
* @since April 21, 2014 */
public class SimpleWeightedGraph<V, E extends WeightedEdge<V, W>, W> extends
		AbstractWeightedGraph<V, E, W> {

	private static final long serialVersionUID = 5337806344935553524L;

	/** Creates an instance of a new simple weighted graph. */
	public SimpleWeightedGraph() {
		super(null);
	}

	/** Creates an instance of a new simple weighted graph with edge factory. The edge
	 * factory will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V
	 * v2)} and {@link #addEdge(Object, Object, Object) addEdge(V v1, V
	 * v2, W w)}  methods. Using another constructor, these methods will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public SimpleWeightedGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory);
	}

}
