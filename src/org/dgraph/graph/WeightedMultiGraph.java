package org.dgraph.graph;

import java.util.function.BiFunction;

/** Implementation of a directed weighted multigraph with edges.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *          {@link org.dgraph.graph.WeightedMultiGraph.WeightedMultiEdge
 *          WeightedMultiEdge&lt;V&gt;} implementation
 *
 * @author Andrii Dzhyrma
 * @since April 21, 2014 */
public class WeightedMultiGraph<V, E extends WeightedMultiGraph.WeightedMultiEdge<V>>
		extends AbstractWeightedGraph<V, E, Double> {

	public static class WeightedMultiEdge<V> extends
			AbstractWeightedGraph.AbstractWeightedEdge<V, Double> {

		private static final double DEFAULT_WEIGHT = Double.valueOf(0d);

		public WeightedMultiEdge(V source, V target) {
			super(source, target, DEFAULT_WEIGHT);
		}

		public WeightedMultiEdge(V source, V target, double weight) {
			super(source, target, weight);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (this == obj)
				return true;
			return false;
		}

		@Override
		public double getWeight() {
			return weight.doubleValue();
		}
	}

	private static final long serialVersionUID = 396772382551003763L;

	/** Creates an instance of a new weighted multigraph without loops. */
	public WeightedMultiGraph() {
		super(null, false);
	}

	/** Creates an instance of a new weighted multigraph. */
	public WeightedMultiGraph(boolean withLoops) {
		super(null, withLoops);
	}

	/** Creates an instance of a new weighted multigraph without loops and with
	 * edge factory. The edge factory will be used in the
	 * {@link #addEdge(Object, Object) addEdge(V v1, V v2)} method. Using another
	 * constructor, this method will throw {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public WeightedMultiGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory, false);
	}

	/** Creates an instance of a new weighted multigraph with edge factory. The
	 * edge factory will be used in the {@link #addEdge(Object, Object) addEdge(V
	 * v1, V v2)} method. Using another constructor, this method will throw
	 * {@link UnsupportedOperationException}.
	 * 
	 * @param edgeFactory the edge factory to create edge by given source and
	 *          target vertices */
	public WeightedMultiGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
		super(edgeFactory, withLoops);
	}
}
