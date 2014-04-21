package org.dgraph.graph.edge;

import java.util.Objects;

public abstract class AbstractWeightedEdge<V, W> extends AbstractEdge<V>
		implements WeightedEdge<V, W> {

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
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.hashCode() != obj.hashCode())
			return false;
		if (obj instanceof AbstractWeightedEdge) {
			AbstractWeightedEdge<?,?> edge = (AbstractWeightedEdge<?,?>) obj;
			if (source.equals(edge.source) && target.equals(edge.target) && weight.equals(edge.weight))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 37 + weight.hashCode();
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, source, getWeight(), target);
	}

}
