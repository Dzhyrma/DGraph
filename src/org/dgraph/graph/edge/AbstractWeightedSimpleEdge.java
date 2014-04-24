package org.dgraph.graph.edge;

import java.util.Objects;

public abstract class AbstractWeightedSimpleEdge<V, W> extends SimpleEdge<V> implements WeightedEdge<V, W> {

	private static final String TO_STRING_FORMAT = "[(%s) -{%.2f}-> (%s)]";

	protected W weight;

	public AbstractWeightedSimpleEdge(V source, V target, W weight) {
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