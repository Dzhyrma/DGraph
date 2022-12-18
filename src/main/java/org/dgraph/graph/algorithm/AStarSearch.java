package org.dgraph.graph.algorithm;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.SimpleWeightedPath;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.FibonacciHeap;
import org.dgraph.util.FibonacciHeap.Node;
import org.dgraph.util.Tuple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiFunction;

/**
 * @author Andrii Dzhyrma
 * @since 1.0
 */
public class AStarSearch {

    public static <V, E extends WeightedEdge<V, W>, W> WeightedPath<V, E> findShortestPath(
            Graph<V, E> graph, V source, V target,
            BiFunction<V, V, Double> heuristic
    ) {

        /* Keep track on previous vertices, to reconstruct the shortest path. */
        final HashMap<V, E> previous = new HashMap<>(graph.sizeOfVertices());

        /* Keep track on vertices with distance passed inserted to Fibonacci heap. */
        final HashMap<V, Node<Tuple<V, Double>>> heapNodes = new HashMap<>(graph.sizeOfVertices());
        final FibonacciHeap<Tuple<V, Double>> heap = new FibonacciHeap<>();

        /* Insert the source vertex to the heap. */
        heapNodes.put(source, heap.enqueue(new Tuple<>(source, 0d), 0d));
        while (!heap.isEmpty()) {
            final Node<Tuple<V, Double>> cur = heap.dequeueMin();

            /* Target vertex is reached. */
            if (cur.equals(target)) {
                break;
            }

            /* Go through each edge from the currently picked vertex. */
            for (E e : graph.getEdgesFromSource(cur.getValue().getItem1())) {

                /* A* search algorithm works only with non-negative weights. */
                if (e.getWeight() < 0) {
                    throw new IllegalArgumentException("A* search algorithm can be applied only for graphs with non negative weights.");
                }

                final V adj = e.getTarget();

                /* Calculate the distance using a sum of the passed distance and weight of the edge. */
                final double newDistance = cur.getValue().getItem2().doubleValue() + e.getWeight();

                final Node<Tuple<V, Double>> next = heapNodes.get(adj);

                /* If this vertex has never been added to the heap or its previous distance was larger the the new one. */
                if (next == null || next.getValue().getItem2().doubleValue() > newDistance) {
                    /* Check, whether the vertex is in the heap. */
                    if (next == null || next.isDequeued()) {
                        heapNodes.put(adj, heap.enqueue(new Tuple<>(adj, newDistance), newDistance + heuristic.apply(adj, target)));
                    }
                        /* Otherwise decrease the key of the node in the heap. */
                    else {
                        heap.decreaseKey(next, newDistance + heuristic.apply(adj, target));
                    }

                    /* Change a preceding vertex. */
                    previous.put(adj, e);
                }
            }
        }

        /* Check, whether target node has been reached. */
        if (!heapNodes.containsKey(target)) {
            return null;
        }

        /* Path reconstruction. */
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
