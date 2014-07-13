package org.dgraph.graph.edge;

/** Implementation of a multi-edge.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class MultiEdge<V> extends AbstractEdge<V> {

	/** Constructor for the multi-edge.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge */
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
