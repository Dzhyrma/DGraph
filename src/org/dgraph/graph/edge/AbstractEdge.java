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
		return source.hashCode() + 37 * target.hashCode();
	}

	@Override
	public String toString() {
		return String.format(TO_STRING_FORMAT, source, target);
	}

}
