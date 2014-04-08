package org.dgraph.graph;

import java.util.Set;

import org.dgraph.graph.edge.Edge;

public interface Graph<V, E extends Edge<V>> {

	public E addEdge(E e);

	public V addVertex(V v);

	public boolean containsVertex(V v);
	
	public boolean containsEdge(E e);

	public Set<E> getAllEdges();

	public Set<V> getAllVertices();

	public Set<E> getEdges(V v1, V v2);

	public Set<E> getEdgesFromSource(V v);

	public E removeEdge(E e);

	public V removeVertex(V v);
	
	public int sizeOfEdges();
	
	public int sizeOfVertices();
}