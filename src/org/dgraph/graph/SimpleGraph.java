package org.dgraph.graph;

import java.util.function.BiFunction;

/** Implementation of a simple directed graph with edges.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *          {@link org.dgraph.graph.SimpleGraph.SimpleEdge SimpleEdge&lt;V&gt;}
 *          implementation
 *
 * @author Andrii Dzhyrma
 * @since April 21, 2014 */
public class SimpleGraph<V, E extends SimpleGraph.SimpleEdge<V>> extends
		AbstractGraph<V, E> {

	public static class SimpleEdge<V> extends AbstractGraph.AbstractEdge<V> {

		public SimpleEdge(V source, V target) {
			super(source, target);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			if (this.hashCode() != obj.hashCode())
				return false;
			if (obj instanceof AbstractEdge) {
				AbstractEdge<?> edge = (AbstractEdge<?>) obj;
				if (source.equals(edge.source) && target.equals(edge.target))
					return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return 37 * source.hashCode() + target.hashCode();
		}
	}

	private static final long serialVersionUID = 396772382551003763L;

	/** Creates an instance of a new simple graph without loops. */
	public SimpleGraph() {
		super(null, false);
	}

	/** Creates an instance of a new simple graph. */
	public SimpleGraph(boolean withLoops) {
		super(null, withLoops);
	}

	/** Creates an instance of a new simple graph without loops and with edge
	 * factory. The edge factory will be used in the
	 * {@link #addEdge(Object, Object) addEdge(V v1, V v2)} method. Using another
	 * constructor, this method will throw {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public SimpleGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory, false);
	}

	/** Creates an instance of a new simple with edge factory. The edge factory
	 * will be used in the {@link #addEdge(Object, Object) addEdge(V v1, V v2)}
	 * method. Using another constructor, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public SimpleGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}
}
