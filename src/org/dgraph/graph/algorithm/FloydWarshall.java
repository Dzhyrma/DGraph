package org.dgraph.graph.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.WeightedEdge;

public class FloydWarshall {
	public static <V, E extends WeightedEdge<V, W>, W> HashMap<V, HashMap<V, Double>> findAllShortestPaths(
			Graph<V, E> graph) {
		int N = graph.sizeOfVertices();
		ArrayList<V> vertices = new ArrayList<>(N);
		double[][] distances = new double[N][N];
		for (V v : graph.getAllVertices()) {
			vertices.add(v);
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == j)
					distances[i][j] = 0;
				distances[i][j] = Double.POSITIVE_INFINITY;
				Set<E> edges = graph.getEdges(vertices.get(i), vertices.get(j));
				if (edges != null)
					for (E e : edges) {
						if (e.getWeight() < distances[i][j])
							distances[i][j] = e.getWeight();
					}
			}
		}
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (distances[i][j] > distances[i][k] + distances[k][j])
						distances[i][j] = distances[i][k] + distances[k][j];
				}
			}
		}
		for (int i = 0; i < N; i++)
			if (distances[i][i] < 0)
				throw new IllegalArgumentException(
						"Graph contains a negative-weight cycle");

		HashMap<V, HashMap<V, Double>> result = new HashMap<>();
		for (int i = 0; i < N; i++) {
			HashMap<V, Double> map = new HashMap<>();
			result.put(vertices.get(i), map);
			for (int j = 0; j < N; j++) {
				map.put(vertices.get(j), Double.valueOf(distances[i][j]));
			}
		}
		return result;
	}
}
