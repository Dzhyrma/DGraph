package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.Edge;
import org.dgraph.graph.path.Path;
import org.dgraph.graph.path.SimplePath;

public class BreadthFirstSearch {

	public static <V, E extends Edge<V>> Path<V, E> findPath(Graph<V, E> graph, V source, V target) {
		HashMap<V, E> previous = new HashMap<>();
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
					previous.put(adj, e);
					queue.offer(adj);
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
