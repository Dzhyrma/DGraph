package org.dgraph.graph.edge;

public abstract class UndirectedEdge<V> implements Edge<V> {

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (this.hashCode() != obj.hashCode())
			return false;
		if (obj instanceof UndirectedEdge) {
			UndirectedEdge<?> edge = (UndirectedEdge<?>) obj;
			if ((this.getSource().equals(edge.getSource()) && this.getTarget().equals(edge.getTarget()))
			                || (this.getSource().equals(edge.getTarget()) && this.getTarget().equals(edge.getSource())))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getSource().hashCode() ^ getTarget().hashCode();
	}

}
