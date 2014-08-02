package org.dgraph.graph.path;

import java.util.List;

import org.dgraph.graph.edge.Edge;

public interface Path<V, E extends Edge<V>> {
	
	public V getSource();
	
	public V getTarget();
	
	public List<V> getVertices();
	
	public List<E> getEdges();
	
}
