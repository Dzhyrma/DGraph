package org.dgraph.graph;

public interface FlowNetwork<V, E extends FlowNetwork.FlowEdge<V>> extends Graph<V, E> {
	
	public interface FlowEdge<V> extends Graph.Edge<V>{

		public double getFlow();
		
		public void setFlow(double flow);
		
		public double getCapacity();
		
	}
	
}
