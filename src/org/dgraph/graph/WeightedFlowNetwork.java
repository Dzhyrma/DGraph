package org.dgraph.graph;

import org.dgraph.graph.edge.FlowEdge;
import org.dgraph.graph.edge.WeightedEdge;

public interface WeightedFlowNetwork<V, E extends WeightedEdge<V, W> & FlowEdge<V>, W>
		extends WeightedGraph<V, E, W>, FlowNetwork<V, E> {

}
