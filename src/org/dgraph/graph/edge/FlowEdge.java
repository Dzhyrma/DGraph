package org.dgraph.graph.edge;

public interface FlowEdge<V> extends Edge<V>{

	public double getFlow();
	
	public void setFlow(double flow);
	
	public double getCapacity();
	
}
