package org.dgraph.graph.algorithm;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.Edge;
import org.dgraph.graph.path.Path;
import org.dgraph.graph.path.SimplePath;

import java.util.HashMap;
import java.util.LinkedList;

public class BreadthFirstSearch {

    public static <V, E extends Edge<V>> Path<V, E> findPath(Graph<V, E> graph, V source, V target) {
        final HashMap<V, E> previous = new HashMap<>();
        final LinkedList<V> queue = new LinkedList<>();
        previous.put(source, null);
        queue.offer(source);
        while (!queue.isEmpty()) {
            final V cur = queue.poll();
            if (cur.equals(target)) {
                break;
            }
            for (E e : graph.getEdgesFromSource(cur)) {
                final V adj = e.getTarget();
                if (!previous.containsKey(adj)) {
                    previous.put(adj, e);
                    queue.offer(adj);
                }
            }
        }
        if (!previous.containsKey(target)) {
            return null;
        }
        final LinkedList<E> edges = new LinkedList<>();
        V cur = target;
        E curEdge;
        while (!cur.equals(source)) {
            curEdge = previous.get(cur);
            edges.push(curEdge);
            cur = curEdge.getSource();
        }
        return new SimplePath<>(source, target, edges);
    }

}
