package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.Set;

import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

public class BellmanFord {

	/** The Bellman–Ford algorithm is an algorithm that computes shortest paths
	 * from a single source vertex to all of the other vertices in a weighted
	 * digraph. It is slower than Dijkstra's algorithm for the same problem, but
	 * more versatile, as it is capable of handling graphs in which some of the
	 * edge weights are negative numbers.
	 * 
	 * Bellman–Ford runs in O(|V|*|E|) time, where |V| and |E| are the number of
	 * vertices and edges respectively.
	 * 
	 * @param graph the weighted graph
	 * @param source the source vertex
	 * @param checkNegativeCycles if true, method will throw IllegalArgumentException if a negative cycle exists
	 * @return Hash map with all vertices from a given graph as keys, and tuples
	 *         with two parameters as values. First parameter in the tuple
	 *         represents predecessor of the vertex in the key (null, when it is
	 *         a source, or there is no path from path to current vertex), and
	 *         second parameter is a distance from the source vertex to the
	 *         current one (Double.POSITIVE_INFINITY if there is no path from
	 *         source) */
	public static <V, E extends WeightedEdge<V, W>, W> HashMap<V, Tuple<V, Double>> findAllShortestPaths(Graph<V, E> graph, V source, boolean checkNegativeCycles) {
		int vSize = graph.sizeOfVertices();
		HashMap<V, Tuple<V, Double>> info = new HashMap<>(vSize);
		info.put(source, new Tuple<V, Double>(null, Double.valueOf(0d)));
		Set<E> edges = graph.getAllEdges();
		for (int i = 2; i < vSize; i++) {// vSize - 1 times
			boolean hasChanges = false;
			for (E e : edges) {
				V src = e.getSource();
				if (!info.containsKey(src))
					continue;
				V trg = e.getTarget();
				double distance = info.get(src).getItem2().doubleValue() + e.getWeight();
				boolean hasKey = info.containsKey(trg);
				if (!hasKey || info.get(trg).getItem2().doubleValue() > distance) {
					Tuple<V, Double> t;
					if (hasKey) {
						t = info.get(trg);
					} else {
						t = new Tuple<V, Double>();
						info.put(trg, t); 
					}
					t.setItem1(src);
					t.setItem2(Double.valueOf(distance));
					hasChanges = true;
				}
			}
			if (!hasChanges)
				break;
		}
		for (V v : graph.getAllVertices()) {
			if (!info.containsKey(v))
				info.put(v, new Tuple<V, Double>(null, Double.POSITIVE_INFINITY));
			else {
				for (E e : graph.getEdgesFromSource(v)) {
					V adj = e.getTarget();
					if (checkNegativeCycles && info.containsKey(adj) && info.get(adj).getItem2().doubleValue() > info.get(v).getItem2().doubleValue() + e.getWeight())
						throw new IllegalArgumentException("Graph contains a negative-weight cycle");
				}
			}
		}
		return info;
	}
}
