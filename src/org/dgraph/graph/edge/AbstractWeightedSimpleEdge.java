package org.dgraph.graph.edge;

import java.util.Objects;

/** Main abstract implementation for all weighted simple edges in DGraph library.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public abstract class AbstractWeightedSimpleEdge<V, W> extends SimpleEdge<V> implements WeightedEdge<V, W> {

	private static final String TO_STRING_FORMAT = "[(%s) -{%.2f}-> (%s)]";

	protected W weight;

	/** Default constructor for the weighted simple edge.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge
	 * @param weight weight of this edge */
	public AbstractWeightedSimpleEdge(V source, V target, W weight) {
		super(source, target);
		this.weight = Objects.requireNonNull(weight);
	}

	@Override
	public void setWeight(W weight) {
		this.weight = Objects.requireNonNull(weight);
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, source, getWeight(), target);
	}

}