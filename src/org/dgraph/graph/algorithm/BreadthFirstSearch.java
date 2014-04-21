package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.Edge;

public class BreadthFirstSearch {

	public static <V, E extends Edge<V>> List<V> findPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, V> previous = new HashMap<>();
		LinkedList<V> queue = new LinkedList<>();
		previous.put(source, null);
		queue.offer(source);
		while (!queue.isEmpty()) {
			V cur = queue.poll();
			if (cur.equals(target))
				break;
			for (E e : graph.getEdgesFromSource(cur)) {
				V adj = e.getTarget();
				if (!previous.containsKey(adj)) {
					previous.put(adj, cur);
					queue.offer(adj);
				}
			}
		}
		if (!previous.containsKey(target))
			return null;
		LinkedList<V> result = new LinkedList<>();
		V cur = target;
		result.push(cur);
		while (previous.get(cur) != null) {
			cur = previous.get(cur);
			result.push(cur);
		}
		return result;
	}
}
