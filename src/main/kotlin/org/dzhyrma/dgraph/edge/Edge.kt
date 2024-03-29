package org.dzhyrma.dgraph.edge

/**
 * Main interface for all edges.
 *
 * @param <V> type for vertices
 */
interface Edge<V> {

    /**
     * Returns the source of this edge.
     */
    val source: V

    /**
     * Returns the target of this edge.
     */
    val target: V
}

data class SimpleEdge<V>(
    override val source: V,
    override val target: V,
) : Edge<V>
