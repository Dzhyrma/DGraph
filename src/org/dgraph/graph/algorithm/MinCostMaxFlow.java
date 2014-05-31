package org.dgraph.graph.algorithm;

import java.util.HashMap;
import java.util.HashSet;

import org.dgraph.collections.FibonacciHeap;
import org.dgraph.collections.FibonacciHeap.Entry;
import org.dgraph.collections.Tuple;
import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.FlowEdge;
import org.dgraph.graph.edge.WeightedEdge;

public class MinCostMaxFlow {

	private static double error = 0.0000000001;

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
		HashMap<V, HashSet<E>> reverseEdges = new HashMap<>(vSize);
		HashMap<V, E> previousEdge = new HashMap<>(vSize);
		HashMap<V, Double> distance = new HashMap<>(vSize);
		HashMap<V, Double> currentFlow = new HashMap<>(vSize);
		HashMap<V, Double> priority = new HashMap<>(vSize);

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
			HashMap<V, Tuple<V, Double>> dist = BellmanFord.findAllShortestPaths(network, source, false);
			for (V v : dist.keySet()) {
				distance.put(v, dist.get(v).getItem2());
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
			FibonacciHeap<V> priorityQueue = new FibonacciHeap<>();
			priorityQueue.enqueue(source, 0d);
			for (V v : network.getAllVertices()) {
				priority.put(v, Double.POSITIVE_INFINITY);
			}
			priority.put(source, 0d);
			while (!priorityQueue.isEmpty()) {
				Entry<V> entry = priorityQueue.dequeueMin();
				V u = entry.getValue();
				Double uPiority = priority.get(u);
				if (uPiority.isInfinite() || Math.abs(entry.getPriority() - uPiority.doubleValue()) > error)
					continue;
				for (E e : network.getEdgesFromSource(u)) {
					V v = e.getTarget();
					double uDist = distance.get(u).doubleValue();
					double vDist = distance.get(v).doubleValue();
					if (Double.isInfinite(uDist) || Double.isInfinite(vDist) || e.getCapacity() < e.getFlow() - error)
						continue;
					double newPriority = entry.getPriority() + e.getWeight() + distance.get(u).doubleValue() - distance.get(v).doubleValue();
					if (priority.get(v).doubleValue() > newPriority + error) {
						priority.put(v, Double.valueOf(newPriority));
						priorityQueue.enqueue(v, newPriority);
						previousEdge.put(v, e);
						double curFlow = currentFlow.get(v).doubleValue();
						currentFlow.put(v, Math.min(curFlow, e.getCapacity() - e.getFlow()));
					}
				}
				for (E e : reverseEdges.get(u)) {
					V v = e.getSource();
					double uDist = distance.get(u).doubleValue();
					double vDist = distance.get(v).doubleValue();
					if (Double.isInfinite(uDist) || Double.isInfinite(vDist) || e.getFlow() < error)
						continue;
					double newPriority = entry.getPriority() + e.getWeight() + distance.get(u).doubleValue() - distance.get(v).doubleValue();
					if (priority.get(v).doubleValue() > newPriority + error) {
						priority.put(v, Double.valueOf(newPriority));
						priorityQueue.enqueue(v, newPriority);
						previousEdge.put(v, e);
						double curFlow = currentFlow.get(v).doubleValue();
						currentFlow.put(v, Math.min(curFlow, e.getFlow()));
					}
				}
			}
			if (priority.get(sink).isInfinite())
				break;
			for (V v : network.getAllVertices()) {
				double vDist = distance.get(v).doubleValue();
				distance.put(v, Double.valueOf(vDist + priority.get(v).doubleValue()));
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
