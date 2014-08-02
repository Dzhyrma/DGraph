package org.dgraph.graph.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.SimpleWeightedPath;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.Tuple;

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
	 * @param <V> type for vertices
	 * @param <E> type for edges. Should implement
	 *          {@link org.dgraph.graph.edge.WeightedEdge WeightedEdge&lt;V,
	 *          W&gt;} interface
	 * @param <W> type for weights
	 * @param graph the weighted graph
	 * @param source the source vertex
	 * @param checkNegativeCycles if true, method will throw
	 *          IllegalArgumentException if a negative cycle exists
	 * @return Map with all vertices from a given graph as keys, and paths as
	 *         values. If there was no single path between two vertices, path will
	 *         be returning the distance as Double.POSITIVE_INFINITY */
	public static <V, E extends WeightedEdge<V, ?>> Map<V, WeightedPath<V, E>> findAllShortestPaths(Graph<V, E> graph, V source, boolean checkNegativeCycles) {
		int vSize = graph.sizeOfVertices();
		Map<V, Tuple<E, Double>> info = new HashMap<>(vSize);
		info.put(source, new Tuple<>(null, Double.valueOf(0d)));
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
					Tuple<E, Double> t;
					if (hasKey) {
						t = info.get(trg);
					} else {
						t = new Tuple<E, Double>();
						info.put(trg, t);
					}
					t.setItem1(e);
					t.setItem2(Double.valueOf(distance));
					hasChanges = true;
				}
			}
			if (!hasChanges)
				break;
		}
		Map<V, WeightedPath<V, E>> result = new HashMap<>();
		result.put(source, new SimpleWeightedPath<>(source, source, Collections.<E> emptyList()));
		for (V v : graph.getAllVertices()) {
			if (!info.containsKey(v))
				result.put(v, new SimpleWeightedPath<>(source, v, null));
			else {
				for (E e : graph.getEdgesFromSource(v)) {
					V adj = e.getTarget();
					if (checkNegativeCycles && info.containsKey(adj) && info.get(adj).getItem2().doubleValue() > info.get(v).getItem2().doubleValue() + e.getWeight())
						throw new IllegalArgumentException("Graph contains a negative-weight cycle");
				}
				buildPath(info, result, source, v);
			}
		}
		return result;
	}

	private static <V, E extends WeightedEdge<V, ?>> void buildPath(Map<V, Tuple<E, Double>> info, Map<V, WeightedPath<V, E>> result, V source, V target) {
		if (!result.containsKey(target)) {
			Tuple<E, Double> tuple = info.get(target);
			E edge = tuple.getItem1();
			if (!result.containsKey(edge.getSource())) {
				buildPath(info, result, source, edge.getSource());
			}
			List<E> temp = new LinkedList<E>(result.get(edge.getSource()).getEdges());
			temp.add(edge);
			result.put(target, new SimpleWeightedPath<>(source, target, temp));
		}
	}
}
