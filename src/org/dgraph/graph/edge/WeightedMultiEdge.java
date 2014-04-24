package org.dgraph.graph.edge;

public class WeightedMultiEdge<V> extends AbstractWeightedMultiEdge<V, Double> {

	private static final double DEFAULT_WEIGHT = Double.valueOf(0d);

	public WeightedMultiEdge(V source, V target) {
		super(source, target, DEFAULT_WEIGHT);
	}

	public WeightedMultiEdge(V source, V target, double weight) {
		super(source, target, weight);
	}

	@Override
	public double getWeight() {
		return weight.doubleValue();
	}
}