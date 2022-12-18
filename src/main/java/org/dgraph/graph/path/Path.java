package org.dgraph.graph.path;

import org.dgraph.graph.edge.Edge;

import java.util.List;

public interface Path<V, E extends Edge<V>> {

    V getSource();

    V getTarget();

    List<V> getVertices();

    List<E> getEdges();

}
