package org.dgraph.graph;

public interface WeightedFlowNetwork<V, E extends WeightedFlowNetwork.WeightedFlowEdge<V, W>, W>
		extends WeightedGraph<V, E, W>, FlowNetwork<V, E> {

	public interface WeightedFlowEdge<V, W> extends
			WeightedGraph.WeightedEdge<V, W>, FlowNetwork.FlowEdge<V> {
	}

}
