package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.FibonacciHeap.Node;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

public class AStarSearch {

	public static <V, E extends WeightedEdge<V, W>, W> List<Tuple<V, Double>> findShortestPath(
			Graph<V, E> graph, V source, V target, BiFunction<V, V, Double> heuristic) {
		HashMap<V, Node<Tuple<V, Double>>> heapNodes = new HashMap<>(graph.sizeOfVertices());
		HashMap<V, V> previous = new HashMap<>(graph.sizeOfVertices());
		FibonacciHeap<Tuple<V, Double>> heap = new FibonacciHeap<>();
		heapNodes.put(source, heap.enqueue(new Tuple<>(source, 0d), 0d));
		while (!heap.isEmpty()) {
			Node<Tuple<V, Double>> cur = heap.dequeueMin();
			if (cur.equals(target))
				break;
			for (E e : graph.getEdgesFromSource(cur.getValue().getItem1())) {
				if (e.getWeight() < 0)
					throw new IllegalArgumentException(
							"A* search algorithm can be applied only for graphs with non negative weights.");
				V adj = e.getTarget();
				double newDistance = cur.getValue().getItem2().doubleValue() + e.getWeight();
				Node<Tuple<V, Double>> next = heapNodes.get(adj);
				if (next == null || next.getValue().getItem2().doubleValue() > newDistance) {
					if (next == null || next.isDequeued())
						heapNodes.put(adj, heap.enqueue(new Tuple<>(adj, newDistance), newDistance + heuristic.apply(adj, target)));
					else
						heap.decreaseKey(next, newDistance + heuristic.apply(adj, target));
					previous.put(adj, cur.getValue().getItem1());
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
