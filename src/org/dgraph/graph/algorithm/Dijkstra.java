package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.FibonacciHeap.Node;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

public class Dijkstra {

	public static <V, E extends WeightedEdge<V, W>, W> List<Tuple<V, Double>> findShortestPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, Node<V>> heapNodes = new HashMap<>(graph.sizeOfVertices());
		HashMap<V, V> previous = new HashMap<>(graph.sizeOfVertices());
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
					previous.put(adj, cur.getValue());
				}
			}
		}
		if (!heapNodes.containsKey(target))
			return null;
		LinkedList<Tuple<V, Double>> result = new LinkedList<>();
		V cur = target;
		result.push(new Tuple<V, Double>(cur, Double.valueOf(heapNodes.get(cur).getPriority())));
		while ((cur = previous.get(cur)) != source)
			result.push(new Tuple<V, Double>(cur, Double.valueOf(heapNodes.get(cur).getPriority())));
		result.push(new Tuple<V, Double>(source, Double.valueOf(0d)));
		return result;
	}
}
