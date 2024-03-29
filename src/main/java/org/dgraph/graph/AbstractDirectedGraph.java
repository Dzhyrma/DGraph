package org.dgraph.graph;

import org.dgraph.graph.edge.Edge;

import java.io.Serializable;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Main abstract implementation for all directed graphs.
 *
 * @param <V> type for vertices
 * @param <E> type for edges. Should extend
 *            {@link org.dgraph.graph.edge.AbstractEdge
 *            AbstractEdge&lt;V&gt;} abstract class
 * @author Andrii Dzhyrma
 * @since 0.1
 */
public abstract class AbstractDirectedGraph<V, E extends Edge<V>>
        implements Graph<V, E>, Serializable {

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
            final V source = Objects.requireNonNull(e.getSource());
            final V target = Objects.requireNonNull(e.getTarget());
            return (!withLoops || !source.equals(target)) && super.add(e);
        }

        @Override
        public boolean addToOthers(E e) {
            final V source = e.getSource();
            final V target = e.getTarget();
            if (vertices.superAdd(source)) {
                vertices.addToOthers(source);
            }
            if (vertices.superAdd(target)) {
                vertices.addToOthers(target);
            }
            final EdgeMap map = graph.get(source);
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
            final E e = (E) o;
            final V target = e.getTarget();
            final EdgeMap map = graph.get(e.getSource());
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
            final V source = Objects.requireNonNull(e.getSource());
            return Objects.requireNonNull(e.getTarget()).equals(target)
                    && (!withLoops || !source.equals(target)) && super.add(e);
        }

        @Override
        public boolean addToOthers(E e) {
            final V source = e.getSource();
            if (vertices.superAdd(source)) {
                vertices.addToOthers(source);
            }
            if (vertices.superAdd(target)) {
                vertices.addToOthers(target);
            }
            final EdgeMap map = graph.get(source);
            SetExtension<E> set = map.get(target);
            if (set == null) {
                set = new EdgeSetSpecific(source, target);
                map.put(target, set);
            }
            return set.superAdd(e) && map.edges.superAdd(e) && edges.superAdd(e);
        }

        @Override
        public boolean removeFromOthers(Object o) {
            final E e = (E) o;
            final EdgeMap map = graph.get(e.getSource());
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
            final V target = Objects.requireNonNull(e.getTarget());
            return Objects.requireNonNull(e.getSource()).equals(source)
                    && (!withLoops || !source.equals(target)) && super.add(e);
        }

        @Override
        public boolean addToOthers(E e) {
            final V target = e.getTarget();
            if (vertices.superAdd(source)) {
                vertices.addToOthers(source);
            }
            if (vertices.superAdd(target)) {
                vertices.addToOthers(target);
            }
            final EdgeMap map = graph.get(source);
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
            final E e = (E) o;
            final V target = e.getTarget();
            final EdgeMap map = graph.get(source);
            final SetExtension<E> set = map.get(target);
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
            return Objects.requireNonNull(e.getSource()).equals(source)
                    && Objects.requireNonNull(e.getTarget()).equals(target)
                    && (!withLoops || !source.equals(target)) && super.add(e);
        }

        @Override
        public boolean addToOthers(E e) {
            if (vertices.superAdd(source)) {
                vertices.addToOthers(source);
            }
            if (vertices.superAdd(target)) {
                vertices.addToOthers(target);
            }
            final EdgeMap map = graph.get(source);
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
            final EdgeMap map = graph.get(source);
            return edges.superRemove(o) && map.edges.superRemove(o)
                    && incomingEdges.get(target).superRemove(o);
        }

        @Override
        void detach() {
            if (graph.get(source).remove(target) == null) {
                throw new ConcurrentModificationException();
            }
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
            if (!superAdd(t)) {
                return false;
            }
            final int mc = ++modCount;
            if (!addToOthers(t) || modCount != mc) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override
        public void clear() {
            final int mc = ++modCount;
            for (T t : this) {
                removeFromOthers(t);
            }
            super.clear();
            if (mc != modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public Iterator<T> iterator() {
            return new SetIterator(super.iterator());
        }

        @Override
        public boolean remove(Object o) {
            if (!superRemove(o)) {
                return false;
            }
            final int mc = ++modCount;
            if (!removeFromOthers(o) || modCount != mc) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            Objects.requireNonNull(c);
            boolean modified = false;
            for (final Iterator<?> i = c.iterator(); i.hasNext(); ) {
                modified |= remove(i.next());
            }
            return modified;
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            Objects.requireNonNull(filter);
            boolean removed = false;
            final Iterator<T> each = iterator();
            while (each.hasNext()) {
                final T next = each.next();
                if (filter.test(next)) {
                    removeFromOthers(next);
                    removed = true;
                }
            }
            if (removed ^ super.removeIf(filter)) {
                throw new ConcurrentModificationException();
            }
            return removed;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            Objects.requireNonNull(c);
            final HashSet<?> set = new HashSet<>(c);
            final LinkedList<T> toRemove = new LinkedList<>();
            boolean modified = false;
            for (T t : this) {
                if (!set.contains(t)) {
                    toRemove.add(t);
                    modified |= removeFromOthers(t);
                }
            }
            for (T t : toRemove) {
                modified |= superRemove(t);
            }
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
            final EdgeMap map = graph.get(o);
            if (map == null) {
                return false;
            }
            map.source = null;
            map.edges.detach();
            for (V v : map.keySet()) {
                final SetExtension<E> set = map.get(v);
                set.detach();
                final SetExtension<E> incomingSet = incomingEdges.get(v);
                for (E e : set) {
                    result &= edges.superRemove(e);
                    result &= map.edges.superRemove(e);
                    result &= incomingSet.superRemove(e);
                }
                set.superClear();
            }
            graph.remove(o);
            final SetExtension<E> incomingSet = incomingEdges.remove(o);
            incomingSet.detach();
            for (E e : incomingSet) {
                result &= edges.superRemove(e);
                final EdgeMap tempMap = graph.get(e.getSource());
                result &= tempMap.edges.superRemove(e);
                final SetExtension<E> set = tempMap.get(e.getTarget());
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

    private final SetExtension<V> vertices = new VertexSet();

    private final boolean withLoops;

    private transient int modCount = 0;

    protected final BiFunction<V, V, E> edgeFactory;

    public AbstractDirectedGraph(BiFunction<V, V, E> edgeFactory, boolean withLoops) {
        this.edgeFactory = edgeFactory;
        this.withLoops = withLoops;
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        int size = in.readInt();
        for (; size > 0; size--) {
            edges.add((E) in.readObject());
        }
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException {
        out.writeInt(edges.size());
        for (final Iterator<?> i = edges.iterator(); i.hasNext(); ) {
            out.writeObject(i.next());
        }
    }

    @Override
    public boolean addEdge(E e) {
        return edges.add(e);
    }

    @Override
    public boolean addEdge(V v1, V v2) {
        final E e = edgeFactory.apply(v1, v2);
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
        if (e == null) {
            return false;
        }
        return edges.contains(e);
    }

    @Override
    public boolean containsEdge(V v1, V v2) {
        final EdgeMap map = graph.get(v1);
        return map != null && map.containsKey(v2);
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
        final EdgeMap map = graph.get(v1);
        return map == null ? null : map.get(v2);
    }

    @Override
    public Set<E> getEdgesFromSource(V v) {
        final EdgeMap map = graph.get(v);
        return map == null ? null : map.edges;
    }

    @Override
    public Set<E> getEdgesToTarget(V v) {
        return incomingEdges.get(v);
    }

    @Override
    public int getInDegree(V v) {
        final SetExtension<E> set = incomingEdges.get(v);
        return set == null ? -1 : set.size();
    }

    @Override
    public int getOutDegree(V v) {
        final EdgeMap map = graph.get(v);
        return map == null ? -1 : map.edges.size();
    }

    @Override
    public boolean removeAllEdges(Collection<E> e) {
        Objects.requireNonNull(e);
        boolean modified = false;
        for (final Iterator<?> i = e.iterator(); i.hasNext(); ) {
            modified |= edges.remove(i.next());
        }
        return modified;
    }

    @Override
    public boolean removeAllVertices(Collection<V> v) {
        Objects.requireNonNull(v);
        boolean modified = false;
        for (final Iterator<?> i = v.iterator(); i.hasNext(); ) {
            modified |= vertices.remove(i.next());
        }
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
        return "Graph:" + edges;
    }

}
