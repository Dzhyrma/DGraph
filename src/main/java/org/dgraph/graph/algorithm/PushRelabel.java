package org.dgraph.graph.algorithm;

import org.dgraph.graph.Graph;
import org.dgraph.graph.edge.FlowEdge;

import java.util.HashMap;
import java.util.HashSet;

public class PushRelabel {

    private static double error = 0.0000000001;

    public static double getDoubleError() {
        return error;
    }

    public static void setDoubleError(double error) {
        if (error > 0) {
            PushRelabel.error = error;
        }
    }

    public static <V, E extends FlowEdge<V>> double getMaximumFlow(
            Graph<V, E> network, V source, V sink, boolean resetFlowOnStart
    ) {
        final int vSize = network.sizeOfVertices();
        final HashMap<V, Integer> label = new HashMap<>(vSize);
        final HashMap<V, Double> excess = new HashMap<>(vSize);
        final HashMap<V, HashSet<E>> reverseEdges = new HashMap<>(vSize);

        for (V v : network.getAllVertices()) {
            excess.put(v, Double.valueOf(0d));
            label.put(v, Integer.valueOf(0));
            reverseEdges.put(v, new HashSet<E>());
        }

        for (E e : network.getAllEdges()) {
            reverseEdges.get(e.getTarget()).add(e);
            if (resetFlowOnStart) {
                e.setFlow(Double.valueOf(0d));
            }
        }

        HashSet<V> activeVertices = new HashSet<>();
        label.put(source, Integer.valueOf(vSize));
        for (E e : network.getEdgesFromSource(source)) {
            if (e.getCapacity() > 0) {
                e.setFlow(e.getCapacity());
                excess.put(e.getTarget(), Double.valueOf(e.getCapacity()));
                if (!e.getTarget().equals(sink)) {
                    activeVertices.add(e.getTarget());
                }
            }
        }

        HashSet<V> newActiveVertices = new HashSet<>();
        while (!activeVertices.isEmpty()) {

            active:
            for (V v : activeVertices) {
                int minLabel = 2 * vSize;
                final int vLabel = label.get(v).intValue();
                final double vExcess = excess.get(v).doubleValue();
                boolean pushed = false;
                for (E e : network.getEdgesFromSource(v)) {
                    final V t = e.getTarget();
                    if (e.getFlow() < e.getCapacity()) {
                        minLabel = Math.min(minLabel, label.get(t).intValue());
                        if (vLabel < label.get(t).intValue()) {
                            pushed = true;
                            newActiveVertices.add(t);
                            final double diff = e.getCapacity() - e.getFlow();
                            if (diff - vExcess > -error) {
                                excess.put(v, Double.valueOf(0d));
                                excess.put(t, excess.get(t).doubleValue() + vExcess);
                                e.setFlow(e.getFlow() + vExcess);
                                continue active;
                            } else {
                                excess.put(t, excess.get(t).doubleValue() + diff);
                                excess.put(v, vExcess - diff);
                                e.setFlow(e.getFlow() + diff);
                            }
                        }
                    }
                }
                for (E e : reverseEdges.get(v)) {
                    final V t = e.getSource();
                    if (e.getFlow() > 0) {
                        minLabel = Math.min(minLabel, label.get(t).intValue());
                        if (vLabel < label.get(t).intValue()) {
                            pushed = true;
                            newActiveVertices.add(t);
                            final double diff = e.getFlow();
                            if (e.getFlow() - vExcess > -error) {
                                excess.put(v, Double.valueOf(0d));
                                excess.put(t, excess.get(t).doubleValue() + vExcess);
                                e.setFlow(Math.abs(e.getFlow() - vExcess) < error ? 0d : e
                                        .getFlow()
                                        - vExcess);
                                continue active;
                            } else {
                                excess.put(t, excess.get(t).doubleValue() + diff);
                                excess.put(v, vExcess - diff);
                                e.setFlow(0d);
                            }
                        }
                    }
                }

                // Relabel
                if (!pushed) {
                    label.put(v, minLabel + 1);
                }
                newActiveVertices.add(v);
            }

            final HashSet<V> temp = activeVertices;
            activeVertices = newActiveVertices;
            newActiveVertices = temp;
            newActiveVertices.clear();
        }

        return excess.get(sink).doubleValue();
    }

}
