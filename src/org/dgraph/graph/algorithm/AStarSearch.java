package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.WeightedGraph;

public class AStarSearch {

	public static <V, E extends WeightedGraph.WeightedEdge<V, W>, W> List<Tuple<V, Double>> findShortestPath(
			Graph<V, E> graph, V source, V target, BiFunction<V, V, Double> heuristic) {
		HashMap<V, Tuple<V, Double>> info = new HashMap<>();
		FibonacciHeap<V> priorityQueue = new FibonacciHeap<>();
		info.put(source, new Tuple<V, Double>(null, Double.valueOf(0d)));
		priorityQueue.enqueue(source, 0d);
		while (!priorityQueue.isEmpty()) {
			V cur = priorityQueue.dequeueMin().getValue();
			if (cur.equals(target))
				break;
			for (E e : graph.getEdgesFromSource(cur)) {
				if (e.getWeight() < 0)
					throw new IllegalArgumentException(
							"A* search algorithm can be applied only for graphs with non negative weights.");
				V adj = e.getTarget();
				double newDistance =
					info.get(cur).getItem2().doubleValue() + e.getWeight();
				boolean hasKey = info.containsKey(adj);
				if (!hasKey || info.get(adj).getItem2().doubleValue() > newDistance) {
					Tuple<V, Double> t;
					if (hasKey) {
						t = info.get(adj);
					} else {
						t = new Tuple<V, Double>();
						info.put(adj, t);
					}
					t.setItem1(cur);
					t.setItem2(Double.valueOf(newDistance));
					priorityQueue
							.enqueue(adj, newDistance + heuristic.apply(adj, target));
				}
			}
		}
		if (!info.containsKey(target))
			return null;
		LinkedList<Tuple<V, Double>> result = new LinkedList<>();
		V cur = target;
		result.push(info.get(cur));
		while ((cur = info.get(cur).getItem1()) != null)
			result.push(info.get(cur));
		return result;
	}
}
