package org.dgraph.graph.algorithm;

import java.util.ArrayList;
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

public class FloydWarshall {
	public static <V, E extends WeightedEdge<V, ?>> Map<V, Map<V, WeightedPath<V, E>>> findAllShortestPaths(Graph<V, E> graph) {
		int N = graph.sizeOfVertices();
		ArrayList<V> vertices = new ArrayList<>(N);
		double[][] distances = new double[N][N];
		Integer[][] previousVertex = new Integer[N][N];
		@SuppressWarnings("unchecked")
		E[][] previousEdge = (E[][]) new WeightedEdge[N][N];
		for (V v : graph.getAllVertices()) {
			vertices.add(v);
		}
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (i == j)
					distances[i][j] = 0;
				else
					distances[i][j] = Double.POSITIVE_INFINITY;
				Set<E> edges = graph.getEdges(vertices.get(i), vertices.get(j));
				if (edges != null)
					for (E e : edges) {
						if (e.getWeight() < distances[i][j]) {
							distances[i][j] = e.getWeight();
							previousVertex[i][j] = i;
							previousEdge[i][j] = e;
						}
					}
			}
		}
		for (int k = 0; k < N; k++) {
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (distances[i][j] > distances[i][k] + distances[k][j]) {
						distances[i][j] = distances[i][k] + distances[k][j];
						previousVertex[i][j] = previousVertex[k][j];
						previousEdge[i][j] = previousEdge[k][j];
					}
				}
			}
		}

		Map<V, Map<V, WeightedPath<V, E>>> result = new HashMap<>();

		for (int i = 0; i < N; i++) {
			if (distances[i][i] < 0) {
				throw new IllegalArgumentException("Graph contains a negative-weight cycle");
			}
			HashMap<V, WeightedPath<V, E>> map = new HashMap<>();
			V source = vertices.get(i);
			map.put(source, new SimpleWeightedPath<>(source, source, Collections.<E> emptyList()));
			result.put(source, map);
		}

		for (int i = 0; i < N; i++) {
			V source = vertices.get(i);
			Map<V, WeightedPath<V, E>> map = result.get(source);
			for (int j = 0; j < N; j++) {
				V target = vertices.get(j);
				buildPath(previousEdge, previousVertex, map, source, target, i, j);
			}
		}
		return result;
	}

	private static <V, E extends WeightedEdge<V, ?>> void buildPath(E[][] previousEdge, Integer[][] previousVertex, Map<V, WeightedPath<V, E>> map, V source,
			V target, int sourceIndex, int targetIndex) {
		if (!map.containsKey(target)) {
			E edge = previousEdge[sourceIndex][targetIndex];
			if (edge == null) {
				map.put(target, new SimpleWeightedPath<>(source, target, null));
				return;
			}
			if (!map.containsKey(edge.getSource())) {
				buildPath(previousEdge, previousVertex, map, source, edge.getSource(), sourceIndex, previousVertex[sourceIndex][targetIndex]);
			}
			List<E> temp = new LinkedList<E>(map.get(edge.getSource()).getEdges());
			temp.add(edge);
			map.put(target, new SimpleWeightedPath<>(source, target, temp));
		}
	}
}
