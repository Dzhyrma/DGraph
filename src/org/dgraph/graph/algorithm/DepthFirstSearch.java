package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dgraph.graph.Graph;

public class DepthFirstSearch {

	public static <V, E extends Graph.Edge<V>> List<V> findPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, V> previous = new HashMap<>();
		LinkedList<V> stack = new LinkedList<>();
		previous.put(source, null);
		stack.push(source);
		while (!stack.isEmpty()) {
			V cur = stack.pop();
			if (cur.equals(target))
				break;
			for (E e : graph.getEdgesFromSource(cur)) {
				V adj = e.getTarget();
				if (!previous.containsKey(adj)) {
					previous.put(adj, cur);
					stack.push(adj);
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
