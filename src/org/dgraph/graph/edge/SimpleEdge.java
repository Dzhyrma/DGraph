package org.dgraph.graph.edge;

public class SimpleEdge<V> extends AbstractEdge<V> {

	public SimpleEdge(V source, V target) {
		super(source, target);
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