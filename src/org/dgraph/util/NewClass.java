/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgraph.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dgraph.graph.DirectedGraph;
import org.dgraph.graph.WeightedDirectedGraph;
import org.dgraph.graph.algorithm.BellmanFord;
import org.dgraph.graph.algorithm.BreadthFirstSearch;
import org.dgraph.graph.algorithm.DepthFirstSearch;
import org.dgraph.graph.algorithm.Dijkstra;
import org.dgraph.graph.algorithm.FloydWarshall;
import org.dgraph.graph.algorithm.Johnson;
import org.dgraph.graph.edge.FlowEdge;
import org.dgraph.graph.edge.FlowMultiEdge;
import org.dgraph.graph.edge.SimpleEdge;
import org.dgraph.graph.edge.WeightedSimpleEdge;
import org.dgraph.graph.path.Path;
import org.dgraph.graph.path.WeightedPath;

/** @author user */
public class NewClass {



//	public static void main(String[] args) {
//		int M = 100;
//		FibonacciHeap[] heap = new FibonacciHeap[M];
//		Random r = new Random();
//		int N = 10000;
//		double[][] array = new double[M][N];
//		for (int j = 0; j < M; j++) {
//			heap[j] = new FibonacciHeap<Object>();
//			for (int i = 0; i < N; i++) {
//				double d = r.nextDouble();
//				array[j][i] = d;
//				//System.out.print(d + ", ");
//				heap[j].enqueue(null, d);
//			}
//		}
//		System.out.println();
//		long nano = System.nanoTime();
//		for (int j = 0; j < M; j++) {
//			Arrays.sort(array[j]);
//		}
//		System.out.println(System.nanoTime() - nano);
//		nano = System.nanoTime();
//		for (int j = 0; j < M; j++) {
//			while (!heap[j].isEmpty())
//				heap[j].dequeueMin();
//		}
//		System.out.println(System.nanoTime() - nano);
//		//System.out.println(heap.size());
//	}
	
	public static void main(String[] args) {
		DirectedGraph<Integer, SimpleEdge<Integer>> graph =
			new DirectedGraph<>(SimpleEdge<Integer>::new);
		graph.addEdge(0, 1);
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(0, 3);
		graph.addEdge(3, 1);
		graph.addEdge(3, 2);
		graph.addEdge(2, 4);
		System.out.println(graph);
		graph.getEdgesFromSource(0).clear();
		System.out.println(graph);
		graph.removeVertex(1);
		System.out.println(graph);
		graph.getAllVertices().clear();
		System.out.println(graph);
		WeightedDirectedGraph<Integer, WeightedSimpleEdge<Integer>, Double> weightedGraph =
			new WeightedDirectedGraph<Integer, WeightedSimpleEdge<Integer>, Double>(
					WeightedSimpleEdge<Integer>::new);
		//weightedGraph.addEdge(0, 0, -1d);
		weightedGraph.addEdge(0, 1, 2d);
		weightedGraph.addEdge(0, 1, 3d);
		weightedGraph.addEdge(1, 2, 4d);
		weightedGraph.addEdge(0, 3, 0d);
		weightedGraph.addEdge(3, 1, 5d);
		weightedGraph.addEdge(3, 2, 2d);
		weightedGraph.addEdge(2, 4, 2d);
		Path<Integer, WeightedSimpleEdge<Integer>> bfs = BreadthFirstSearch.findPath(weightedGraph, 0, 4);
		System.out.println(bfs);
		Path<Integer, WeightedSimpleEdge<Integer>> dfs = DepthFirstSearch.findPath(weightedGraph, 0, 4);
		System.out.println(dfs);
		WeightedPath<Integer, WeightedSimpleEdge<Integer>> dijkstra =
			Dijkstra.findShortestPath(weightedGraph, 0, 4);
		System.out.println(dijkstra);
		Map<Integer, WeightedPath<Integer, WeightedSimpleEdge<Integer>>> bellmanFord =
			BellmanFord.findAllShortestPaths(weightedGraph, 0, true);
		System.out.println(bellmanFord);
		Map<Integer, Map<Integer, WeightedPath<Integer, WeightedSimpleEdge<Integer>>>> floydWarshall =
			FloydWarshall.findAllShortestPaths(weightedGraph);
		System.out.println(floydWarshall);
		Map<Integer, Map<Integer, WeightedPath<Integer, WeightedSimpleEdge<Integer>>>> johnson =
			Johnson.findAllShortestPaths(weightedGraph);
		System.out.println(johnson);
		System.out.println(weightedGraph);
		weightedGraph.getEdgesFromSource(0).clear();
		System.out.println(weightedGraph);
		weightedGraph.removeVertex(1);
		System.out.println(weightedGraph);
		weightedGraph.getAllVertices().clear();
		System.out.println(weightedGraph);
	}
}
