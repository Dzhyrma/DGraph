package org.dgraph.graph;

import java.util.function.BiFunction;

/** Implementation of a directed multigraph with edges.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
*            {@link org.dgraph.graph.MultiGraph.MultiEdge MultiEdge&lt;V&gt;} implementation
 *
 * @author Andrii Dzhyrma
 * @since April 21, 2014 */
public class MultiGraph<V, E extends MultiGraph.MultiEdge<V>> extends
		AbstractGraph<V, E> {

	public static class MultiEdge<V> extends AbstractGraph.AbstractEdge<V> {

		public MultiEdge(V source, V target) {
			super(source, target);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			return false;
		}
	}

	private static final long serialVersionUID = 396772382551003763L;

	/** Creates an instance of a new multigraph without loops. */
	public MultiGraph() {
		super(null, false);
	}
	
	/** Creates an instance of a new multigraph. */
	public MultiGraph(boolean withLoops) {
		super(null, withLoops);
	}

	/** Creates an instance of a new multigraph without loops and with edge factory. The edge
	 * factory will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V
	 * v2)} method. Using another constructor, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public MultiGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory, false);
	}
	
	/** Creates an instance of a new multigraph with edge factory. The edge
	 * factory will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V
	 * v2)} method. Using another constructor, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public MultiGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}
}
