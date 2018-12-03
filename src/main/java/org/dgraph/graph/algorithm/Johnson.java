package org.dgraph.graph.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.SimpleWeightedPath;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.FibonacciHeap;
import org.dgraph.util.Tuple;
import org.dgraph.util.FibonacciHeap.Node;

public class Johnson {

	public static <V, E extends WeightedEdge<V, ?>> Map<V, Map<V, WeightedPath<V, E>>> findAllShortestPaths(Graph<V, E> graph) {
		HashMap<V, Double> h = new HashMap<>();
		Set<E> edges = graph.getAllEdges();
		Set<V> vertices = graph.getAllVertices();
		for (V v : vertices)
			h.put(v, Double.valueOf(0d));
		for (int i = 1; i < graph.sizeOfVertices() - 1; i++) {
			for (E e : edges) {
				V src = e.getSource();
				V trg = e.getTarget();
				double distance = h.get(src).doubleValue() + e.getWeight();
				if (h.get(trg).doubleValue() > distance)
					h.put(trg, Double.valueOf(distance));
			}
		}
		for (E e : edges) {
			V src = e.getSource();
			V trg = e.getTarget();
			double distance = h.get(src).doubleValue() + e.getWeight();
			if (h.get(trg).doubleValue() > distance) {
				throw new IllegalArgumentException("Graph contains a negative-weight cycle");
			}
		}
		Map<V, Map<V, WeightedPath<V, E>>> result = new HashMap<>();
		for (V source : vertices) {
			HashMap<V, Node<V>> heapNodes = new HashMap<>();
			HashMap<V, E> previous = new HashMap<>();
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
						previous.put(adj, e);
					}
				}
			}
			Map<V, WeightedPath<V, E>> map = new HashMap<>();
			map.put(source, new SimpleWeightedPath<>(source, source, Collections.<E> emptyList()));
			for (V target : vertices) {
				if (target == source)
					continue;
				if (previous.containsKey(target)) {
					buildPath(previous, map, source, target);
				} else {
					map.put(target, new SimpleWeightedPath<>(source, target, null));
				}
			}
			result.put(source, map);
		}
		return result;
	}
	
	private static <V, E extends WeightedEdge<V, ?>> void buildPath(Map<V, E> previous, Map<V, WeightedPath<V, E>> map, V source, V target) {
		if (!map.containsKey(target)) {
			E edge = previous.get(target);
			if (!map.containsKey(edge.getSource())) {
				buildPath(previous, map, source, edge.getSource());
			}
			List<E> temp = new LinkedList<E>(map.get(edge.getSource()).getEdges());
			temp.add(edge);
			map.put(target, new SimpleWeightedPath<>(source, target, temp));
		}
	}
}
