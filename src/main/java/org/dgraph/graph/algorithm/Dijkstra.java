package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

import graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.SimpleWeightedPath;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.FibonacciHeap;
import org.dgraph.util.FibonacciHeap.Node;

public class Dijkstra {

	public static <V, E extends WeightedEdge<V, W>, W> WeightedPath<V, E> findShortestPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, Node<V>> heapNodes = new HashMap<>(graph.sizeOfVertices());
		HashMap<V, E> previous = new HashMap<>(graph.sizeOfVertices());
		FibonacciHeap<V> heap = new FibonacciHeap<>();
		heapNodes.put(source, heap.enqueue(source, 0d));
		while (!heap.isEmpty()) {
			Node<V> cur = heap.dequeueMin();
			//V cur = heap.dequeueMin().getValue();
			if (cur.equals(target))
				break;
			for (E e : graph.getEdgesFromSource(cur.getValue())) {
				if (e.getWeight() < 0)
					throw new IllegalArgumentException("Dijkstra's algorithm can be applied only for graphs with non negative weights.");
				V adj = e.getTarget();
				double newDistance = cur.getPriority() + e.getWeight();
				Node<V> next = heapNodes.get(adj);
				if (next == null || next.getPriority() > newDistance) {
					if (next == null || next.isDequeued())
						heapNodes.put(adj, heap.enqueue(adj, newDistance));
					else
						heap.decreaseKey(next, newDistance);
					previous.put(adj, e);
				}
			}
		}
		if (!heapNodes.containsKey(target))
			return null;
		LinkedList<E> edges = new LinkedList<>();
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
