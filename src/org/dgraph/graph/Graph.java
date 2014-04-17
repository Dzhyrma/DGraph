package org.dgraph.graph;

import java.util.Collection;
import java.util.Set;

import org.dgraph.graph.edge.Edge;

/** Main interface for all graphs in DGraph library.
 * 
 * @param <V> type for vertices
 * @param <E> type for edges. Should implement
 *            <code>org.dgraph.edge.Edge&lt;V&gt;</code> interface
 * 
 * @author Andrii Dzhyrma
 * @since April 17, 2014 */
public interface Graph<V, E extends Edge<V>> {

	/** Adds a new edge to the graph.
	 * 
	 * <p>
	 * If current graph doesn't contain any of the vertices from the edge, they
	 * will be added to the graph automatically.
	 * </p>
	 * 
	 * @param e edge to be added to the graph.
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 *         edge
	 * 
	 * @throws NullPointerException if the edge or any vertices it connects are
	 *             <tt>null</tt> */
	public boolean addEdge(E e);

	/** Adds a new vertex to the graph.
	 * 
	 * @param v vertex to be added to the graph.
	 * @return <tt>true</tt> if this graph did not already contain the specified
	 *         vertex
	 * 
	 * @throws NullPointerException if the vertex is <tt>null</tt> */
	public boolean addVertex(V v);

	/** Returns <tt>true</tt> if this graph contains the specified vertex.
	 * 
	 * <p>
	 * This graph uses {@code equals()} to distinguish vertices between each
	 * other.
	 * </p>
	 * 
	 * @param v vertex to be checked
	 * 
	 * @return <tt>true</tt> if this graph contains the specified vertex. */
	public boolean containsVertex(V v);

	/** Returns <tt>true</tt> if this graph contains the specified edge.
	 * 
	 * <p>
	 * This graph will check existence of an edge using vertex {@code v1} as a
	 * source and vertex {@code v2} as a target.
	 * </p>
	 * 
	 * @param v1 source vertex of the edge
	 * @param v2 target vertex of the edge
	 * 
	 * @return <tt>true</tt> if this graph contains at least one edge between
	 *         specified vertices. */
	public boolean containsEdge(V v1, V v2);

	/** Returns <tt>true</tt> if this graph contains the specified edge.
	 * 
	 * <p>
	 * This graph uses {@code equals()} to distinguish edges between each other.
	 * </p>
	 * 
	 * @param e edge to be checked
	 * 
	 * @return <tt>true</tt> if this graph contains the specified edge. */
	public boolean containsEdge(E e);

	/** Returns a {@link Set} view of the edges contained in this graph. The set
	 * is backed by the graph, so changes to the graph are reflected in the set,
	 * and vice-versa. The set supports edge removal, which removes the
	 * corresponding edge from the graph, via the <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations. It
	 * also supports the <tt>add</tt> and <tt>addAll</tt> operations. It does
	 * not support <tt>Iterator.remove</tt>.
	 * 
	 * @return a set view of the edges contained in this graph */
	public Set<E> getAllEdges();

	/** Returns a {@link Set} view of the vertices contained in this graph. The
	 * set is backed by the graph, so changes to the graph are reflected in the
	 * set, and vice-versa. The set supports vertex removal, which removes the
	 * corresponding vertex from the graph, via the <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations. It
	 * also supports the <tt>add</tt> and <tt>addAll</tt> operations. It does
	 * not support <tt>Iterator.remove</tt>.
	 * 
	 * @return a set view of the vertices contained in this graph */
	public Set<V> getAllVertices();

	/** Returns a {@link Set} view of the edges between source vertex <tt>v1</tt>
	 * and target vertex <tt>v2</tt> contained in this graph. The set is backed
	 * by the graph, so changes to the graph are reflected in the set, and
	 * vice-versa. The set supports edge removal, which removes the
	 * corresponding edge from the graph, via the <tt>Set.remove</tt>,
	 * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations. It
	 * also supports the <tt>add</tt> and <tt>addAll</tt> operations. Edges with
	 * different vertices from <tt>v1</tt> and <tt>v2</tt> will not be added.
	 * The set does not support <tt>Iterator.remove</tt>.
	 * 
	 * @param v1 source vertex of the edges
	 * @param v2 target vertex of the edges
	 * 
	 * @return a set view of the edges between source vertex <tt>v1</tt> and
	 *         target vertex <tt>v2</tt> contained in this graph */
	public Set<E> getEdges(V v1, V v2);

	/** Returns a {@link Set} view of the edges from source vertex <tt>v</tt>
	 * contained in this graph. The set is backed by the graph, so changes to
	 * the graph are reflected in the set, and vice-versa. The set supports edge
	 * removal, which removes the corresponding edge from the graph, via the
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
	 * <tt>clear</tt> operations. It also supports the <tt>add</tt> and
	 * <tt>addAll</tt> operations. Edges with different from <tt>v</tt> source
	 * vertex will not be added. The set does not support
	 * <tt>Iterator.remove</tt>.
	 * 
	 * @param v source vertex of the edges
	 * 
	 * @return a set view of the edges from source vertex <tt>v</tt> contained
	 *         in this graph */
	public Set<E> getEdgesFromSource(V v);

	/** Returns a {@link Set} view of the edges to target vertex <tt>v</tt>
	 * contained in this graph. The set is backed by the graph, so changes to
	 * the graph are reflected in the set, and vice-versa. The set supports edge
	 * removal, which removes the corresponding edge from the graph, via the
	 * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt>, and
	 * <tt>clear</tt> operations. It also supports the <tt>add</tt> and
	 * <tt>addAll</tt> operations. Edges with different from <tt>v</tt> target
	 * vertex will not be added. The set does not support
	 * <tt>Iterator.remove</tt>.
	 * 
	 * @param v target vertex of the edges
	 * 
	 * @return a set view of the edges to target vertex <tt>v</tt> contained in
	 *         this graph */
	public Set<E> getEdgesToTarget(V v);

	/** Removes an edge from this graph.
	 * 
	 * @param e edge to be removed
	 * 
	 * @return <tt>true</tt> if the edge has been successfully removed */
	public boolean removeEdge(E e);

	/** Removes all edges specified in the given collection from this graph.
	 * 
	 * @param e collection with edges to be removed
	 * 
	 * @return <tt>true</tt> if this graph has been modified */
	public boolean removeAllEdges(Collection<E> e);

	/** Removes a vertex from this graph.
	 * 
	 * @param v vertex to be removed
	 * 
	 * @return <tt>true</tt> if the vertex has been successfully removed */
	public boolean removeVertex(V v);

	/** Removes all vertices specified in the given collection from this graph.
	 * 
	 * @param v collection with vertices to be removed
	 * 
	 * @return <tt>true</tt> if this graph has been modified */
	public boolean removeAllVertices(Collection<V> v);

	/** Removes all of the vertices and edges from this graph. The graph will be
	 * empty after this call returns. */
	public void clear();

	/** Returns the number of edges in this graph.
	 * 
	 * @return the number of edges in this graph */
	public int sizeOfEdges();

	/** Returns the number of vertices in this graph.
	 * 
	 * @return the number of vertices in this graph */
	public int sizeOfVertices();
}
