package org.dgraph.graph.edge;

import java.util.Objects;

/** Main abstract implementation for all edges in DGraph library.
 *
 * @param <V> type for vertices
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public abstract class AbstractEdge<V> implements Edge<V> {

	private static final String TO_STRING_FORMAT = "[(%s) -> (%s)]";

	protected V source;
	protected V target;

	/** Default constructor for the edge.
	 * 
	 * @param source source of this edge
	 * @param target target of this edge */
	public AbstractEdge(V source, V target) {
		this.source = Objects.requireNonNull(source);
		this.target = Objects.requireNonNull(target);
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
