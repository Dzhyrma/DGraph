package org.dzhyrma.dgraph.path

import org.dzhyrma.dgraph.edge.WeightedEdge

/**
 * Main interface for weighted path in graph.
 *
 * @param <V> type for vertices
 * @param <E> type for weighted edges
 */
interface WeightedPath<V, E : WeightedEdge<V>> : Path<V, E> {

	/**
	 * Retrieves the distance of the path.
	 */
	val distance: Double
}

class SimpleWeightedPath<V, E : WeightedEdge<V>>(
	override val source: V,
	override val target: V,
	override val edges: List<E> = emptyList()
) : WeightedPath<V, E> {

	override val vertices: List<V>

	override val distance: Double

	init {
		vertices = if (edges.isNotEmpty()) {
			var temp = source
			edges.map { edge ->
				if (edge.source == temp) {
					temp = edge.target
					edge.source
				} else {
					temp = edge.source
					edge.target
				}
			} + target
		} else {
			emptyList()
		}

		distance = if (edges.isNotEmpty()) {
			edges.fold(0.0) { dist, edge -> dist + edge.weight }
		} else {
			Double.POSITIVE_INFINITY
		}
	}
}