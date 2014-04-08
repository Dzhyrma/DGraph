package org.dgraph.graph;

import org.dgraph.graph.edge.FlowEdge;
import org.dgraph.graph.edge.WeightedEdge;

public interface WeightedFlowNetwork<V, E extends WeightedEdge<V> & FlowEdge<V>> extends WeightedGraph<V, E>, FlowNetwork<V, E> {

}
