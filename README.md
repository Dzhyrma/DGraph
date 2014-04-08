DGraph
======

A Graph library with the most important algorithms.

So far implemented:
###### Graphs:
* BFS : O(|E|);
* DFS : O(|E|).
###### Weighted Graphs:
* A* Search : O(|E|) - worst, but depends on heuristic;
* Bellman-Ford : O(|V||E|);
* Dijkstra : O(|E| + |V|log|V|);
* Floyd-Warshall : O(|V|^3);
* Johnson : O(|V|^2 log |V| + |V||E|).
###### Flow Networks:
* MinCostMaxFlow : O(min(|E|^2 |V|log|V|, |E| log|V|*FLOW);
* Push-Relabel : O(|V|^3).
