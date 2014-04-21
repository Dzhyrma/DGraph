package org.dgraph.graph;

import java.io.Serializable;
import java.util.*;

import org.dgraph.graph.edge.Edge;

import java.util.function.BiFunction;

/** Main abstract implementation for all graphs.
*
* @param <V> type for vertices
* @param <E> type for edges. Should implement
*            <code>org.dgraph.edge.Edge&lt;V&gt;</code> interface
*
* @author Andrii Dzhyrma
* @since April 21, 2014 */
public abstract class AbstractGraph<V, E extends Edge<V>> implements Graph<V, E>,
		Serializable {

	final class EdgeMap extends HashMap<V, SetExtension<E>> {

		SetExtension<E> edges;

		V source;

		EdgeMap(V source) {
			edges = new EdgeSetOutgoing(source);
			this.source = source;
		}
	}

	final class EdgeSetAll extends SetExtension<E> {

		EdgeSetAll() {
		}

		@Override
		public boolean add(E e) {
			Objects.requireNonNull(e.getSource());
			Objects.requireNonNull(e.getTarget());
			return super.add(e);
		}

		@Override
		public boolean addToOthers(E e) {
			V source = e.getSource();
			V target = e.getTarget();
			if (vertices.superAdd(source))
				vertices.addToOthers(source);
			if (vertices.superAdd(target))
				vertices.addToOthers(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && map.edges.superAdd(e)
					&& incomingEdges.get(target).superAdd(e);
		}

		@Override
		void detach() {
			throw new UnsupportedOperationException();
		}

		@Override
		boolean removeFromOthers(Object o) {
			E e = (E) o;
			V source = e.getSource();
			V target = e.getTarget();
			EdgeMap map = graph.get(source);
			return map.get(target).superRemove(o) && map.edges.superRemove(o)
					&& incomingEdges.get(target).superRemove(o);
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
			Objects.requireNonNull(e.getSource());
			return Objects.requireNonNull(e.getTarget()).equals(this.target)
					&& super.add(e);
		}

		@Override
		public boolean addToOthers(E e) {
			V source = e.getSource();
			V target = e.getTarget();
			if (vertices.superAdd(source))
				vertices.addToOthers(source);
			if (vertices.superAdd(target))
				vertices.addToOthers(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && map.edges.superAdd(e) && edges.superAdd(e);
		}

		@Override
		public boolean removeFromOthers(Object o) {
			E e = (E) o;
			V source = e.getSource();
			EdgeMap map = graph.get(source);
			return edges.superRemove(o) && map.get(target).superRemove(o)
					&& map.edges.superRemove(o);
		}

		@Override
		void detach() {
			target = null;
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
			Objects.requireNonNull(e.getTarget());
			return Objects.requireNonNull(e.getSource()).equals(this.source)
					&& super.add(e);
		}

		@Override
		public boolean addToOthers(E e) {
			V target = e.getTarget();
			if (vertices.superAdd(source))
				vertices.addToOthers(source);
			if (vertices.superAdd(target))
				vertices.addToOthers(target);
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			if (set == null) {
				set = new EdgeSetSpecific(source, target);
				map.put(target, set);
			}
			return set.superAdd(e) && edges.superAdd(e)
					&& incomingEdges.get(target).superAdd(e);
		}

		@Override
		public boolean removeFromOthers(Object o) {
			E e = (E) o;
			V target = e.getTarget();
			EdgeMap map = graph.get(source);
			SetExtension<E> set = map.get(target);
			return edges.superRemove(o) && set.superRemove(o)
					&& incomingEdges.get(target).superRemove(o);
		}

		@Override
		void detach() {
			source = null;
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
			return Objects.requireNonNull(e.getSource()).equals(this.source)
					&& Objects.requireNonNull(e.getTarget()).equals(this.target)
					&& super.add(e);
		}

		@Override
		public boolean addToOthers(E e) {
			if (vertices.superAdd(source))
				vertices.addToOthers(source);
			if (vertices.superAdd(target))
				vertices.addToOthers(target);
			EdgeMap map = graph.get(source);
			return edges.add(e) && map.edges.superAdd(e)
					&& incomingEdges.get(target).superAdd(e);
		}

		@Override
		public void clear() {
			detach();
			super.clear();
		}

		@Override
		public boolean removeFromOthers(Object o) {
			EdgeMap map = graph.get(source);
			return edges.superRemove(o) && map.edges.superRemove(o)
					&& incomingEdges.get(target).superRemove(o);
		}

		@Override
		void detach() {
			if (graph.get(source).remove(target) == null)
				throw new ConcurrentModificationException();
			source = target = null;
		}

		@Override
		boolean superRemove(Object o) {
			if (super.superRemove(o)) {
				if (size() == 0) {
					detach();
				}
				return true;
			}
			return false;
		}
	}

	abstract class SetExtension<T> extends HashSet<T> {

		class SetIterator implements Iterator<T> {

			final Iterator<T> iterator;

			public SetIterator(Iterator<T> iterator) {
				this.iterator = iterator;
			}

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public T next() {
				return iterator.next();
			}
		}

		@Override
		public boolean add(T t) {
			if (!superAdd(t))
				return false;
			int mc = ++modCount;
			if (!addToOthers(t) || modCount != mc)
				throw new ConcurrentModificationException();
			return true;
		}

		@Override
		public void clear() {
			int mc = ++modCount;
			for (T t : this)
				removeFromOthers(t);
			super.clear();
			if (mc != modCount)
				throw new ConcurrentModificationException();
		}

		@Override
		public Iterator<T> iterator() {
			return new SetIterator(super.iterator());
		}

		@Override
		public boolean remove(Object o) {
			if (!superRemove(o))
				return false;
			int mc = ++modCount;
			if (!removeFromOthers(o) || modCount != mc)
				throw new ConcurrentModificationException();
			return true;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			Objects.requireNonNull(c);
			boolean modified = false;
			for (Iterator<?> i = c.iterator(); i.hasNext();)
				modified |= remove(i.next());
			return modified;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			Objects.requireNonNull(c);
			HashSet<?> set = new HashSet<>(c);
			LinkedList<T> toRemove = new LinkedList<>();
			boolean modified = false;
			for (T t : this)
				if (!set.contains(t)) {
					toRemove.add(t);
					modified |= removeFromOthers(t);
				}
			for (T t : toRemove)
				modified |= superRemove(t);
			return modified;
		}

		abstract boolean addToOthers(T t);

		abstract void detach();

		abstract boolean removeFromOthers(Object o);

		boolean superAdd(T t) {
			return super.add(t);
		}

		void superClear() {
			super.clear();
		}

		boolean superRemove(Object o) {
			return super.remove(o);
		}
	}

	final class VertexSet extends SetExtension<V> {

		@Override
		public boolean add(V v) {
			Objects.requireNonNull(v);
			return super.add(v);
		}

		@Override
		public boolean addToOthers(V v) {
			return graph.put(v, new EdgeMap(v)) == null
					&& incomingEdges.put(v, new EdgeSetIncoming(v)) == null;
		}

		@Override
		public boolean removeFromOthers(Object o) {
			boolean result = true;
			EdgeMap map = graph.get(o);
			if (map == null)
				return false;
			map.source = null;
			map.edges.detach();
			for (V v : map.keySet()) {
				SetExtension<E> set = map.get(v);
				set.detach();
				SetExtension<E> incomingSet = incomingEdges.get(v);
				for (E e : set) {
					result &= edges.superRemove(e);
					result &= map.edges.superRemove(e);
					result &= incomingSet.superRemove(e);
				}
				set.superClear();
			}
			graph.remove(o);
			SetExtension<E> incomingSet = incomingEdges.remove(o);
			incomingSet.detach();
			for (E e : incomingSet) {
				result &= edges.superRemove(e);
				EdgeMap tempMap = graph.get(e.getSource());
				result &= tempMap.edges.superRemove(e);
				SetExtension<E> set = tempMap.get(e.getTarget());
				result &= set.superRemove(e);
			}
			incomingSet.superClear();
			return result;
		}

		@Override
		void detach() {
			throw new UnsupportedOperationException();
		}

	}

	private static final long serialVersionUID = -4055201812984599572L;

	private final SetExtension<E> edges = new EdgeSetAll();

	private final Map<V, EdgeMap> graph = new HashMap<>();

	private final Map<V, SetExtension<E>> incomingEdges = new HashMap<>();

	private transient int modCount = 0;

	private final SetExtension<V> vertices = new VertexSet();

	protected final BiFunction<V, V, E> edgeFactory;

	public AbstractGraph(BiFunction<V, V, E> edgeFactory) {
		this.edgeFactory = edgeFactory;
	}

	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		int size = in.readInt();
		for (; size > 0; size--)
			edges.add((E) in.readObject());
	}

	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		out.writeInt(edges.size());
		for (Iterator<?> i = edges.iterator(); i.hasNext();)
			out.writeObject(i.next());
	}

	@Override
	public boolean addEdge(E e) {
		return edges.add(e);
	}

	@Override
	public boolean addEdge(V v1, V v2) {
		E e = edgeFactory.apply(v1, v2);
		return edges.add(e);
	}

	@Override
	public boolean addVertex(V v) {
		return vertices.add(v);
	}

	@Override
	public void clear() {
		vertices.clear();
	}

	@Override
	public boolean containsEdge(E e) {
		if (e == null)
			return false;
		return edges.contains(e);
	}

	@Override
	public boolean containsEdge(V v1, V v2) {
		EdgeMap map = graph.get(v1);
		return map == null ? false : map.containsKey(v2);
	}

	@Override
	public boolean containsVertex(V v) {
		return graph.containsKey(v);
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
	public Set<E> getEdgesToTarget(V v) {
		return incomingEdges.get(v);
	}

	@Override
	public boolean removeAllEdges(Collection<E> e) {
		Objects.requireNonNull(e);
		boolean modified = false;
		for (Iterator<?> i = e.iterator(); i.hasNext();)
			modified |= edges.remove(i.next());
		return modified;
	}

	@Override
	public boolean removeAllVertices(Collection<V> v) {
		Objects.requireNonNull(v);
		boolean modified = false;
		for (Iterator<?> i = v.iterator(); i.hasNext();)
			modified |= vertices.remove(i.next());
		return modified;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Iterator<?> i = edges.iterator(); i.hasNext();)
			sb.append(i.next()).append('\n');
		return sb.toString();
	}
}
