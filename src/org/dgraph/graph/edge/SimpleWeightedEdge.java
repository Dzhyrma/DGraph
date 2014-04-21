package org.dgraph.graph.edge;

public class SimpleWeightedEdge<V> extends AbstractWeightedEdge<V, Double> {
	
	private static final double DEFAULT_WEIGHT = Double.valueOf(0d);
	
	public SimpleWeightedEdge(V source, V target) {
		super(source, target, DEFAULT_WEIGHT);
	}
	
	public SimpleWeightedEdge(V source, V target, double weight) {
		super(source, target, Double.valueOf(weight));
	}

	@Override
	public double getWeight() {
		return weight.doubleValue();
	}

}

