package org.dgraph.graph;

import java.util.function.BiFunction;

/** Implementation of a simple weighted directed graph with edges.
*
* @param <V> type for vertices
* @param <E> type for edges. Should extend
*            {@link org.dgraph.graph.WeightedSimpleGraph.WeightedSimpleEdge WeightedSimpleEdge&lt;V&gt;} implementation
*
* @author Andrii Dzhyrma
* @since April 21, 2014 */
public class WeightedSimpleGraph<V, E extends WeightedSimpleGraph.WeightedSimpleEdge<V>> extends
		AbstractWeightedGraph<V, E, Double> {

	public static class WeightedSimpleEdge<V> extends AbstractWeightedEdge<V, Double> {
		
		private static final double DEFAULT_WEIGHT = Double.valueOf(0d);
		
		public WeightedSimpleEdge(V source, V target) {
			super(source, target, DEFAULT_WEIGHT);
		}
		
		public WeightedSimpleEdge(V source, V target, double weight) {
			super(source, target, Double.valueOf(weight));
		}

		@Override
		public double getWeight() {
			return weight.doubleValue();
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
	
	private static final long serialVersionUID = 5337806344935553524L;

	/** Creates an instance of a new simple weighted graph. */
	public WeightedSimpleGraph() {
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
	public WeightedSimpleGraph(BiFunction<V, V, E> edgeFactory) {
		super(edgeFactory);
	}

}
