package org.dgraph.graph.edge;

public abstract class DirectedEdge<V> implements Edge<V> {

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.hashCode() != obj.hashCode())
			return false;
		if (obj instanceof DirectedEdge) {
			DirectedEdge<?> edge = (DirectedEdge<?>) obj;
			if (this.getSource().equals(edge.getSource()) && this.getTarget().equals(edge.getTarget()))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getSource().hashCode() + 31 * getTarget().hashCode();
	}

}
