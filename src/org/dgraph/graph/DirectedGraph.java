package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.Edge;

/** Implementation of a directed graph.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *          {@link org.dgraph.graph.edge.SimpleEdge SimpleEdge&lt;V&gt;}
 *          implementation
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class DirectedGraph<V, E extends Edge<V>> extends AbstractDirectedGraph<V, E> {

	private static final long serialVersionUID = 396772382551003763L;

	/** Creates an instance of a new directed graph without loops. */
	public DirectedGraph() {
		super(null, false);
	}

	/** Creates an instance of a new directed graph.
	 * 
	 * @param withLoops specifies whether the graph can contain loops or not */
	public DirectedGraph(boolean withLoops) {
		super(null, withLoops);
	}

	/** Creates an instance of a new directed graph without loops and with edge
	 * factory. The edge factory will be used in the
	 * {@link #addEdge(Object, Object) addEdge(V v1, V v2)} method. Otherwise, if
	 * no edge factory is given, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public DirectedGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory, false);
	}

	/** Creates an instance of a new directed graph with edge factory. The edge
	 * factory will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V
	 * v2)} method. Otherwise, if no edge factory is given, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices
	 * @param withLoops specifies whether the graph can contain loops or not */
	public DirectedGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}
}
