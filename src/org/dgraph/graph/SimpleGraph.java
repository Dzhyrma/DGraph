package org.dgraph.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.dgraph.graph.edge.Edge;

public class SimpleGraph<V, E extends Edge<V>> implements Graph<V, E> {

	static abstract class SetExtension<T> extends HashSet<T> {

		boolean superAdd(T e) {
			return super.add(e);
		}

		boolean superRemove(Object o) {
			return super.remove(o);
		}
	}

	private Map<V, EdgeMap> graph = new HashMap<>();
	private Map<V, SetExtension<E>> incomingEdges = new HashMap<>();
	private SetExtension<E> edges = new EdgeSetAll();
	private SetExtension<V> vertices = new VertexSet();
	
	final class VertexSet extends SetExtension<V> {

		@Override
		public boolean add(V v) {
			Objects.requireNonNull(v);
			if (graph.containsKey(v))
				return true;
			graph.put(v, new EdgeMap(v));
			incomingEdges.put(v, new EdgeSetIncoming(v));
			return true;
		}

		@Override
		public boolean remove(Object o) {
			EdgeMap map = graph.remove(o);
			if (map == null)
				return false;
			return edges.removeAll(map.edges) && edges.removeAll(incomingEdges.remove(o));
		}

	}

	final class EdgeSetAll extends SetExtension<E> {
		EdgeSetAll() {
		}

		@Override
		public boolean add(E e) {
			V source = Objects.requireNonNull(e.getSource());
			V target = Objects.requireNonNull(e.getTarget());
			if (!super.add(e))
				return false;
			addVertex(source);
			addVertex(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && map.edges.superAdd(e) && incomingEdges.get(target).superAdd(e);
		}

		@Override
		public boolean remove(Object o) {
			if (!super.remove(o)) // otherwise we know that o is a valid edge
				return false;
			E e = (E) o;
			V source = e.getSource();
			V target = e.getTarget();
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			return (set.size() == 1 ? map.remove(target) != null : set.superRemove(o)) && map.edges.superRemove(o) && incomingEdges.get(target).superRemove(o);
		}
	}

	final class EdgeSetIncoming extends SetExtension<E> {
		V target;

		EdgeSetIncoming(V target) {
			Objects.requireNonNull(target);
			this.target = target;
		}

		@Override
		public boolean add(E e) {
			V source = Objects.requireNonNull(e.getSource());
			V target = Objects.requireNonNull(e.getTarget());
			if (!target.equals(this.target) || !edges.superAdd(e))
				return false;
			addVertex(source);
			addVertex(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && map.edges.superAdd(e) && super.add(e);
		}

		@Override
		public boolean remove(Object o) {
			if (!super.remove(o)) // otherwise we know that o is a valid edge
				return false;
			E e = (E) o;
			V source = e.getSource();
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			return edges.superRemove(o) && (set.size() == 1 ? map.remove(target) != null : set.superRemove(o)) && map.edges.superRemove(o);
		}
	}

	final class EdgeSetOutgoing extends SetExtension<E> {
		V source;

		EdgeSetOutgoing(V source) {
			Objects.requireNonNull(source);
			this.source = source;
		}

		@Override
		public boolean add(E e) {
			V source = Objects.requireNonNull(e.getSource());
			V target = Objects.requireNonNull(e.getTarget());
			if (!source.equals(this.source) || !edges.superAdd(e))
				return false;
			addVertex(source);
			addVertex(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && super.add(e) && incomingEdges.get(target).superAdd(e);
		}

		@Override
		public boolean remove(Object o) {
			if (!super.remove(o)) // otherwise we know that o is a valid edge
				return false;
			E e = (E) o;
			V target = e.getTarget();
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			return edges.superRemove(o) && (set.size() == 1 ? map.remove(target) != null : set.superRemove(o)) && incomingEdges.get(target).superRemove(o);
		}
	}

	final class EdgeSetSpecific extends SetExtension<E> {
		private V source, target;

		EdgeSetSpecific(V source, V target) {
			Objects.requireNonNull(source);
			Objects.requireNonNull(target);
			this.source = source;
			this.target = target;
		}

		@Override
		public boolean add(E e) {
			V source = Objects.requireNonNull(e.getSource());
			V target = Objects.requireNonNull(e.getTarget());
			if (!source.equals(this.source) || !target.equals(this.target) || !edges.superAdd(e))
				return false;
			addVertex(source);
			addVertex(target);
			EdgeMap map = graph.get(source);
			return super.add(e) && map.edges.superAdd(e) && incomingEdges.get(target).superAdd(e);
		}

		@Override
		public boolean remove(Object o) {
			if (!super.remove(o)) // otherwise we know that o is a valid edge
				return false;
			EdgeMap map = graph.get(source);
			if (size() == 0)
				map.remove(target);
			return edges.superRemove(o) && map.edges.superRemove(o) && incomingEdges.get(target).superRemove(o);
		}
	}

	final class EdgeMap extends HashMap<V, SetExtension<E>> {

		SetExtension<E> edges;

		V source;

		EdgeMap(V source) {
			edges = new EdgeSetOutgoing(source);
			this.source = source;
		}
	}

	@Override
	public boolean addEdge(E e) {
		return edges.add(e);
	}

	@Override
	public boolean addVertex(V v) {
		return vertices.add(v);
	}

	@Override
	public boolean containsVertex(V v) {
		return graph.containsKey(v);
	}

	@Override
	public boolean containsEdge(E e) {
		if (e == null)
			return false;
		return edges.contains(e);
	}

	@Override
	public Set<E> getAllEdges() {
		return edges;
	}

	@Override
	public Set<V> getAllVertices() {
		return vertices;
	}

	@Override
	public Set<E> getEdges(V v1, V v2) {
		EdgeMap map = graph.get(v1);
		return map == null ? null : map.get(v2);
	}

	@Override
	public Set<E> getEdgesFromSource(V v) {
		EdgeMap map = graph.get(v);
		return map == null ? null : map.edges;
	}

	@Override
	public boolean removeEdge(E e) {
		return edges.remove(e);
	}

	@Override
	public boolean removeVertex(V v) {
		return vertices.remove(v);
	}

	@Override
	public int sizeOfEdges() {
		return edges.size();
	}

	@Override
	public int sizeOfVertices() {
		return graph.size();
	}

}
