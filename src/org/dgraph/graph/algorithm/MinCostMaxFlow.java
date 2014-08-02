package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.FlowEdge;
import org.dgraph.graph.edge.WeightedEdge;
import org.dgraph.graph.path.WeightedPath;
import org.dgraph.util.FibonacciHeap;
import org.dgraph.util.Tuple;
import org.dgraph.util.FibonacciHeap.Node;

public class MinCostMaxFlow {

	private static double error = 1e-10;

	public static double getDoubleError() {
		return error;
	}

	public static void setDoubleError(double error) {
		if (error > 0)
			MinCostMaxFlow.error = error;
	}

	public static <V, E extends WeightedEdge<V, W> & FlowEdge<V>, W> Tuple<Double, Double> getMaximumFlowWithMinCost(Graph<V, E> network, V source, V sink,
	                boolean resetFlowOnStart) {
		int vSize = network.sizeOfVertices();
		HashMap<V, Node<V>> heapNodes = new HashMap<>(vSize);
		HashMap<V, HashSet<E>> reverseEdges = new HashMap<>(vSize);
		HashMap<V, E> previousEdge = new HashMap<>(vSize);
		HashMap<V, Double> distance = new HashMap<>(vSize);
		HashMap<V, Double> currentFlow = new HashMap<>(vSize);

		boolean negativeCosts = false;
		for (E e : network.getAllEdges()) {
			if (e.getWeight() < 0)
				negativeCosts = true;
			reverseEdges.get(e.getTarget()).add(e);
			if (resetFlowOnStart)
				e.setFlow(Double.valueOf(0d));
		}

		double maxFlow = 0;
		for (E e : network.getEdgesFromSource(source)) {
			if (e.getCapacity() > 0)
				maxFlow += e.getCapacity();
		}

		if (negativeCosts) {
			Map<V, WeightedPath<V, E>> dist = BellmanFord.findAllShortestPaths(network, source, false);
			for (V v : dist.keySet()) {
				distance.put(v, dist.get(v).getDistance());
				currentFlow.put(v, Double.valueOf(0d));
			}
		} else {
			for (V v : network.getAllVertices()) {
				distance.put(v, Double.valueOf(0d));
				currentFlow.put(v, Double.valueOf(0d));
			}
		}

		double flow = 0;
		double flowCost = 0;
		while (flow < maxFlow) {
			FibonacciHeap<V> heap = new FibonacciHeap<>();
			heapNodes.clear();
			heapNodes.put(source,heap.enqueue(source, 0d));
			while (!heap.isEmpty()) {
				Node<V> cur = heap.dequeueMin();
				V u = cur.getValue();
				double uDist = distance.get(u).doubleValue();
				for (E e : network.getEdgesFromSource(u)) {
					V v = e.getTarget();
					double vDist = distance.get(v).doubleValue();
					if (Double.isInfinite(uDist) || Double.isInfinite(vDist) || e.getCapacity() < e.getFlow() - error)
						continue;
					double newPriority = cur.getPriority() + e.getWeight() + distance.get(u).doubleValue() - distance.get(v).doubleValue();
					Node<V> next = heapNodes.get(v);
					if (next == null || next.getPriority() > newPriority + error) {
						if (next == null || next.isDequeued())
							heapNodes.put(v, heap.enqueue(v, newPriority));
						else
							heap.decreaseKey(next, newPriority);
						previousEdge.put(v, e);
						double curFlow = currentFlow.get(v).doubleValue();
						currentFlow.put(v, Math.min(curFlow, e.getCapacity() - e.getFlow()));
					}
				}
				for (E e : reverseEdges.get(u)) {
					V v = e.getSource();
					double vDist = distance.get(v).doubleValue();
					if (Double.isInfinite(uDist) || Double.isInfinite(vDist) || e.getFlow() < error)
						continue;
					double newPriority = cur.getPriority() + e.getWeight() + distance.get(u).doubleValue() - distance.get(v).doubleValue();
					Node<V> next = heapNodes.get(v);
					if (next == null || next.getPriority() > newPriority + error) {
						if (next == null || next.isDequeued())
							heapNodes.put(v, heap.enqueue(v, newPriority));
						else
							heap.decreaseKey(next, newPriority);
						previousEdge.put(v, e);
						double curFlow = currentFlow.get(v).doubleValue();
						currentFlow.put(v, Math.min(curFlow, e.getFlow()));
					}
				}
			}
			if (heapNodes.get(sink) == null)
				break;
			for (V v : network.getAllVertices()) {
				double vDist = distance.get(v).doubleValue();
				distance.put(v, Double.valueOf(vDist + heapNodes.get(v).getPriority()));
			}
			double deltaFlow = Math.min(currentFlow.get(sink).doubleValue(), maxFlow - flow);
			flow += deltaFlow;
			V v = sink;
			for (E e = previousEdge.get(v); !v.equals(source);) {
				if (e.getTarget().equals(v)) {
					e.setFlow(e.getFlow() + deltaFlow);
					flowCost += deltaFlow * e.getWeight();
					v = e.getSource();
				} else {
					e.setFlow(e.getFlow() - deltaFlow);
					flowCost -= deltaFlow * e.getWeight();
					v = e.getTarget();
				}
			}
		}
		return new Tuple<>(flow, flowCost);
	}
}
