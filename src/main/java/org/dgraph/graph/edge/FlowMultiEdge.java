package org.dgraph.graph.edge;

/**
 * Implementation of a flow multi-edge.
 *
 * @param <V> type for vertices
 * @author Andrii Dzhyrma
 * @since 0.1
 */
public class FlowMultiEdge<V> extends MultiEdge<V> implements FlowEdge<V> {

    private final double capacity;
    private double flow;

    /**
     * Constructor for the flow multi-edge.
     *
     * @param source   source of this edge
     * @param target   target of this edge
     * @param capacity capacity of this edge
     */
    public FlowMultiEdge(V source, V target, double capacity) {
        super(source, target);
        this.capacity = capacity;
    }

    /**
     * Constructor for the flow multi-edge.
     *
     * @param source   source of this edge
     * @param target   target of this edge
     * @param capacity capacity of this edge
     * @param flow     flow of this edge
     */
    public FlowMultiEdge(V source, V target, double capacity, double flow) {
        this(source, target, capacity);
        this.flow = flow;
    }

    @Override
    public double getFlow() {
        return flow;
    }

    @Override
    public void setFlow(double flow) {
        this.flow = flow;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

}
