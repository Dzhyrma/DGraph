package org.dzhyrma.dgraph.algo

import org.dzhyrma.dgraph.Graph
import org.dzhyrma.dgraph.edge.WeightedEdge
import org.dzhyrma.dgraph.forEachEdge
import org.dzhyrma.dgraph.path.SimpleWeightedPath
import org.dzhyrma.dgraph.path.WeightedPath
import org.dzhyrma.dgraph.util.FibonacciHeap
import java.util.LinkedList

/**
 * A* is an informed search algorithm, or a best-first search, meaning that it is formulated in terms of weighted
 * graphs: starting from a specific starting node of a graph, it aims to find a path to the given goal node having
 * the smallest cost (least distance travelled, shortest time, etc.). It does this by maintaining a tree of paths
 * originating at the start node and extending those paths one edge at a time until its termination criterion is
 * satisfied.
 *
 * Peter Hart, Nils Nilsson and Bertram Raphael of Stanford Research Institute (now SRI International) first published
 * the algorithm in 1968.[3] It can be seen as an extension of Edsger Dijkstra's 1959 algorithm. A* achieves better
 * performance by using heuristics to guide its search.
 */
object AStarSearch {

    /**
     * Performs an A* search on a weighted graph and finds one of the optimal paths.
     *
     * @param graph weighted graph
     * @param source source vertex, from which the search should start
     * @param target target vertex, where the search should end
     * @param heuristic admissible function that estimates the cost of the cheapest path from a vertex to the target
     * @return a shortest(if [heuristic] is admissible) path if it is found, null otherwise
     */
    fun <V, E : WeightedEdge<V>> perform(
        graph: Graph<V, E>,
        source: V,
        target: V,
        heuristic: (V) -> Double,
    ): WeightedPath<V, E>? {
        val bestPathEdges = HashMap<V, E>(graph.vertices.size)

        val heapNodes = HashMap<V, FibonacciHeap.Node<PathNode<V>>>(graph.vertices.size)
        val heap = FibonacciHeap<PathNode<V>>()

        heapNodes[source] = heap.enqueue(PathNode(source, 0.0), heuristic(source))
        var targetReached = false
        while (!targetReached) {
            val min = heap.dequeueMin() ?: return null
            val (currentVertex, bestDistance) = min.value
            when (currentVertex) {
                target -> targetReached = true
                else -> prioritizeAdjacentVertices(
                    graph = graph,
                    currentVertex = currentVertex,
                    bestDistance = bestDistance,
                    heapNodes = heapNodes,
                    heap = heap,
                    heuristic = heuristic,
                    bestPathEdges = bestPathEdges,
                )
            }
        }

        return reconstructPath(target, source, bestPathEdges)
    }

    private fun <E : WeightedEdge<V>, V> prioritizeAdjacentVertices(
        graph: Graph<V, E>,
        currentVertex: V,
        bestDistance: Double,
        heapNodes: HashMap<V, FibonacciHeap.Node<PathNode<V>>>,
        heap: FibonacciHeap<PathNode<V>>,
        heuristic: (V) -> Double,
        bestPathEdges: HashMap<V, E>,
    ) = graph.forEachEdge(currentVertex) { edge, nextVertex, weight ->
        check(weight >= 0) { "A* search algorithm can be applied only for graphs with non negative weights." }

        val nextHeapNode = heapNodes[nextVertex]
        val distanceToNextVertex = bestDistance + weight
        if (nextHeapNode == null || distanceToNextVertex < nextHeapNode.value.bestDistance) {
            val nextVertexPriority = distanceToNextVertex + heuristic(nextVertex)
            changeNextVertexPriority(
                nextHeapNode = nextHeapNode,
                heapNodes = heapNodes,
                heap = heap,
                nextVertex = nextVertex,
                distanceToNextVertex = distanceToNextVertex,
                nextVertexPriority = nextVertexPriority
            )
            bestPathEdges[nextVertex] = edge
        }
    }

    private fun <V> changeNextVertexPriority(
        nextHeapNode: FibonacciHeap.Node<PathNode<V>>?,
        heapNodes: HashMap<V, FibonacciHeap.Node<PathNode<V>>>,
        heap: FibonacciHeap<PathNode<V>>,
        nextVertex: V,
        distanceToNextVertex: Double,
        nextVertexPriority: Double,
    ) {
        when {
            nextHeapNode == null || nextHeapNode.isDequeued -> {
                heapNodes[nextVertex] = heap.enqueue(
                    value = PathNode(nextVertex, distanceToNextVertex),
                    priority = nextVertexPriority,
                )
            }
            else -> {
                nextHeapNode.value.bestDistance = distanceToNextVertex
                heap.decreaseKey(nextHeapNode, nextVertexPriority)
            }
        }
    }

    private fun <E : WeightedEdge<V>, V> reconstructPath(
        target: V,
        source: V,
        bestPathEdges: HashMap<V, E>,
    ): SimpleWeightedPath<V, E>? {
        val edges = LinkedList<E>()
        var current: V = target
        var curEdge: E
        while (current != source) {
            curEdge = bestPathEdges[current] ?: return null
            edges.push(curEdge)
            current = if (curEdge.source == current) curEdge.target else curEdge.source
        }
        return SimpleWeightedPath(source, target, edges)
    }

    private data class PathNode<V>(
        val vertex: V,
        var bestDistance: Double,
    )
}
