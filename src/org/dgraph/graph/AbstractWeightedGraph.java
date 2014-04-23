package org.dgraph.graph;

import java.util.Objects;
import java.util.function.BiFunction;

/** Abstract implementation for all weighted graphs.
*
* @param <V> type for vertices
* @param <E> type for edges. Should extend
*            {@link org.dgraph.graph.AbstractWeightedGraph.AbstractWeightedEdge AbstractEdge&lt;V, W&gt;} abstract class
*
* @author Andrii Dzhyrma
* @since April 21, 2014 */
public abstract class AbstractWeightedGraph<V, E extends AbstractWeightedGraph.AbstractWeightedEdge<V, W>, W>
		extends AbstractGraph<V, E> implements WeightedGraph<V, E, W> {

	public static abstract class AbstractWeightedEdge<V, W> extends
			AbstractGraph.AbstractEdge<V> implements WeightedEdge<V, W> {

		private static final String TO_STRING_FORMAT = "[(%s) -{%.2f}-> (%s)]";

		protected W weight;

		public AbstractWeightedEdge(V source, V target, W weight) {
			super(source, target);
			this.weight = Objects.requireNonNull(weight);
		}

		public void setWeight(W weight) {
			this.weight = Objects.requireNonNull(weight);
		}

		@Override
		public String toString() {
			return String.format(TO_STRING_FORMAT, source, getWeight(), target);
		}

	}

	private static final long serialVersionUID = -3197139092483967228L;

	public AbstractWeightedGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}

	@Override
	public boolean addEdge(E e, W w) {
		e.setWeight(w);
		return addEdge(e);
	}

	@Override
	public boolean addEdge(V v1, V v2, W w) {
		E e = edgeFactory.apply(v1, v2);
		return addEdge(e, w);
	}

}
