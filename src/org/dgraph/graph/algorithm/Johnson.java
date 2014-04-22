package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.Set;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.WeightedGraph;

public class Johnson {

	public static <V, E extends WeightedGraph.WeightedEdge<V, W>, W> HashMap<V, HashMap<V, Tuple<V, Double>>> findAllShortestPaths(Graph<V, E> graph) {
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
			HashMap<V, Tuple<V, Double>> info = new HashMap<>();
			FibonacciHeap<V> priorityQueue = new FibonacciHeap<>();
			info.put(source, new Tuple<V, Double>(null, Double.valueOf(0d)));
			priorityQueue.enqueue(source, 0d);
			while (!priorityQueue.isEmpty()) {
				V cur = priorityQueue.dequeueMin().getValue();
				for (E e : graph.getEdgesFromSource(cur)) {
					V adj = e.getTarget();
					double newDistance = info.get(cur).getItem2().doubleValue() + e.getWeight() + h.get(cur).doubleValue() - h.get(adj).doubleValue();
					boolean hasKey = info.containsKey(adj);
					if (!hasKey || info.get(adj).getItem2().doubleValue() > newDistance) {
						Tuple<V, Double> t;
						if (hasKey)
							t = info.get(adj);
						else {
							t = new Tuple<V, Double>();
							info.put(adj, t);
						}
						t.setItem1(cur);
						t.setItem2(Double.valueOf(newDistance));
						priorityQueue.enqueue(adj, newDistance);
					}
				}
			}
			for (V v : vertices)
				if (info.containsKey(v)) {
					Tuple<V, Double> t = info.get(v);
					t.setItem2(Double.valueOf(t.getItem2().doubleValue() - h.get(source).doubleValue() + h.get(v).doubleValue()));
				} else
					info.put(v, new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
			result.put(source, info);
		}
		return result;
	}
}
