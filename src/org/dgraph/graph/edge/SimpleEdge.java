package org.dgraph.graph.edge;

/** Implementation of a simple edge.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class SimpleEdge<V> extends AbstractEdge<V> {

	/** Constructor for the simple edge.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge */
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