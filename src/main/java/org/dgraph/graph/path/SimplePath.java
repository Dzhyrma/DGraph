package org.dgraph.graph.path;

import org.dgraph.graph.edge.Edge;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimplePath<V, E extends Edge<V>> implements Path<V, E> {

    private final List<E> edges;

    private final List<V> vertices;

    private final V source, target;

    public SimplePath(V source, V target, List<E> edges) {
        this.source = source;
        this.target = target;

        if (edges == null) {
            this.edges = null;
            this.vertices = null;
        } else {
            this.edges = Collections.unmodifiableList(edges);
            final List<V> vertices = edges.stream().map(edge -> edge.getSource()).collect(Collectors.toList());
            vertices.add(target);
            this.vertices = Collections.unmodifiableList(vertices);
        }
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
    public List<V> getVertices() {
        return vertices;
    }

    @Override
    public List<E> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Path:" + edges;
    }

}
