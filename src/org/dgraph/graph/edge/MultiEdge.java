package org.dgraph.graph.edge;

public class MultiEdge<V> extends AbstractEdge<V> {

	public MultiEdge(V source, V target) {
		super(source, target);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		return false;
	}
}
