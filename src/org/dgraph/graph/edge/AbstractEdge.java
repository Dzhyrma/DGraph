package org.dgraph.graph.edge;

import java.util.Objects;

public abstract class AbstractEdge<V> implements Edge<V> {

	private static final String TO_STRING_FORMAT = "[(%s) -> (%s)]";

	protected V source;
	protected V target;

	public AbstractEdge(V source, V target) {
		Objects.requireNonNull(source);
		Objects.requireNonNull(target);
		this.source = source;
		this.target = target;
	}

	@Override
	public V getSource() {
		return source;
	}

	@Override
	public V getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, source, target);
	}

}
