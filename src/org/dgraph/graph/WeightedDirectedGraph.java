package org.dgraph.graph;

import java.util.function.BiFunction;

import org.dgraph.graph.edge.WeightedEdge;

/** Implementation of a weighted directed graph.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *          {@link org.dgraph.graph.edge.WeightedEdge WeightedEdge&lt;V, W&gt;}
 *          interface
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class WeightedDirectedGraph<V, E extends WeightedEdge<V, W>, W> extends AbstractWeightedDirectedGraph<V, E, W> {

	private static final long serialVersionUID = 5337806344935553524L;

	/** Creates an instance of a new weighted directed graph without loops. */
	public WeightedDirectedGraph() {
		super(null, false);
	}

	/** Creates an instance of a new weighted directed graph.
	 * 
	 * @param withLoops specifies whether the graph can contain loops or not */
	public WeightedDirectedGraph(boolean withLoops) {
		super(null, withLoops);
	}

	/** Creates an instance of a new weighted directed graph without loops and with
	 * edge factory. The edge factory will be used in the
	 * {@link #addEdge(Object, Object) addEdge(V v1, V v2)} and
	 * {@link #addEdge(Object, Object, Object) addEdge(V v1, V v2, W w)} methods.
	 * Otherwise, if no edge factory is given, these methods will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public WeightedDirectedGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory, false);
	}

	/** Creates an instance of a new weighted directed graph with edge factory. The
	 * edge factory will be used in the {@link #addEdge(Object, Object) addEdge(V
	 * v1, V v2)} and {@link #addEdge(Object, Object, Object) addEdge(V v1, V v2,
	 * W w)} methods. Otherwise, if no edge factory is given, these methods will
	 * throw {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices
	 * @param withLoops specifies whether the graph can contain loops or not */
	public WeightedDirectedGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}
}
