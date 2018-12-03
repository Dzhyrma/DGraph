package org.dgraph.graph.edge;

/** Implementation of a weighted multi-edge.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class WeightedMultiEdge<V> extends AbstractWeightedMultiEdge<V, Double> {

	private static final double DEFAULT_WEIGHT = Double.valueOf(0d);

	/** Constructor for the weighted multi-edge. Default weight of the edge is set
	 * to 0.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge */
	public WeightedMultiEdge(V source, V target) {
		super(source, target, DEFAULT_WEIGHT);
	}

	/** Constructor for the weighted multi-edge.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge
	 * @param weight weight of this edge */
	public WeightedMultiEdge(V source, V target, double weight) {
		super(source, target, weight);
	}

	@Override
	public double getWeight() {
		return weight.doubleValue();
	}
}