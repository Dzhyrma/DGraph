package org.dgraph.graph.algorithm;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.SimpleWeightedPath;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.FibonacciHeap;
import org.dgraph.util.FibonacciHeap.Node;

import java.util.HashMap;
import java.util.LinkedList;

public class Dijkstra {

    public static <V, E extends WeightedEdge<V, W>, W> WeightedPath<V, E> findShortestPath(
            Graph<V, E> graph,
            V source,
            V target
    ) {
        final HashMap<V, Node<V>> heapNodes = new HashMap<>(graph.sizeOfVertices());
        final HashMap<V, E> previous = new HashMap<>(graph.sizeOfVertices());
        final FibonacciHeap<V> heap = new FibonacciHeap<>();
        heapNodes.put(source, heap.enqueue(source, 0d));
        while (!heap.isEmpty()) {
            final Node<V> cur = heap.dequeueMin();
            //V cur = heap.dequeueMin().getValue();
            if (cur.equals(target)) {
                break;
            }
            for (E e : graph.getEdgesFromSource(cur.getValue())) {
                if (e.getWeight() < 0) {
                    throw new IllegalArgumentException("Dijkstra's algorithm can be applied only for graphs with non negative weights.");
                }
                final V adj = e.getTarget();
                final double newDistance = cur.getPriority() + e.getWeight();
                final Node<V> next = heapNodes.get(adj);
                if (next == null || next.getPriority() > newDistance) {
                    if (next == null || next.isDequeued()) {
                        heapNodes.put(adj, heap.enqueue(adj, newDistance));
                    } else {
                        heap.decreaseKey(next, newDistance);
                    }
                    previous.put(adj, e);
                }
            }
        }
        if (!heapNodes.containsKey(target)) {
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
        return new SimpleWeightedPath<>(source, target, edges);
    }

}
