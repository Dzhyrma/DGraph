package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

import graph.Graph;
import org.dgraph.graph.edge.Edge;
import org.dgraph.graph.path.Path;
import org.dgraph.graph.path.SimplePath;

public class DepthFirstSearch {

	public static <V, E extends Edge<V>> Path<V, E> findPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, E> previous = new HashMap<>();
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
					previous.put(adj, e);
					stack.push(adj);
				}
			}
		}
		if (!previous.containsKey(target))
			return null;
		LinkedList<E> edges = new LinkedList<>();
		V cur = target;
		E curEdge;
		while (!cur.equals(source)) {
			curEdge = previous.get(cur);
			edges.push(curEdge);
			cur = curEdge.getSource();
		}
		return new SimplePath<>(source, target, edges);
	}
}
