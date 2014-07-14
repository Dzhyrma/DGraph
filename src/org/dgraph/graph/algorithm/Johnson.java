package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.Set;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.FibonacciHeap.Node;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

public class Johnson {

	public static <V, E extends WeightedEdge<V, W>, W> HashMap<V, HashMap<V, Tuple<V, Double>>> findAllShortestPaths(Graph<V, E> graph) {
		HashMap<V, Double> h = new HashMap<>();
		Set<E> edges = graph.getAllEdges();
		Set<V> vertices = graph.getAllVertices();
		for (V v : vertices)
			h.put(v, Double.valueOf(0d));
		for (int i = 1; i < graph.sizeOfVertices() - 1; i++)
			for (E e : edges) {
				V src = e.getSource();
				V trg = e.getTarget();
				double distance = h.get(src).doubleValue() + e.getWeight();
				if (h.get(trg).doubleValue() > distance)
					h.put(trg, Double.valueOf(distance));
			}
		for (E e : edges) {
			V src = e.getSource();
			V trg = e.getTarget();
			double distance = h.get(src).doubleValue() + e.getWeight();
			if (h.get(trg).doubleValue() > distance)
				throw new IllegalArgumentException("Graph contains a negative-weight cycle");
		}
		HashMap<V, HashMap<V, Tuple<V, Double>>> result = new HashMap<>();
		for (V source : vertices) {
			HashMap<V, Node<V>> heapNodes = new HashMap<>();
			HashMap<V, V> previous = new HashMap<>();
			FibonacciHeap<V> heap = new FibonacciHeap<>();
			heapNodes.put(source, heap.enqueue(source, 0d));
			while (!heap.isEmpty()) {
				Node<V> cur = heap.dequeueMin();
				for (E e : graph.getEdgesFromSource(cur.getValue())) {
					V adj = e.getTarget();
					double newDistance = cur.getPriority() + e.getWeight() + h.get(cur.getValue()).doubleValue() - h.get(adj).doubleValue();
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
			HashMap<V, Tuple<V, Double>> info = new HashMap<V, Tuple<V, Double>>();
			for (V v : vertices)
				if (previous.containsKey(v)) {
					V prev = previous.get(v);
					info.put(v, new Tuple<>(prev, Double.valueOf(heapNodes.get(v).getPriority() - h.get(source).doubleValue() + h.get(v).doubleValue())));
				} else if (v == source)
					info.put(v, new Tuple<V, Double>(null, Double.valueOf(0d)));
				else
					info.put(v, new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
			result.put(source, info);
		}
		return result;
	}
}
