/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dgraph.util;

import java.util.HashMap;
import java.util.List;

import org.dgraph.collections.Tuple;
import org.dgraph.graph.SimpleGraph;
import org.dgraph.graph.WeightedSimpleGraph;
import org.dgraph.graph.algorithm.BellmanFord;
import org.dgraph.graph.algorithm.BreadthFirstSearch;
import org.dgraph.graph.algorithm.DepthFirstSearch;
import org.dgraph.graph.algorithm.Dijkstra;
import org.dgraph.graph.algorithm.FloydWarshall;
import org.dgraph.graph.algorithm.Johnson;

/** @author user */
public class NewClass {

	public static void main(String[] args) {
		SimpleGraph<Integer, SimpleGraph.SimpleEdge<Integer>> graph =
			new SimpleGraph<>(SimpleGraph.SimpleEdge<Integer>::new);
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
		WeightedSimpleGraph<Integer, WeightedSimpleGraph.WeightedSimpleEdge<Integer>> weightedGraph =
			new WeightedSimpleGraph<Integer, WeightedSimpleGraph.WeightedSimpleEdge<Integer>>(
					WeightedSimpleGraph.WeightedSimpleEdge<Integer>::new);
		//weightedGraph.addEdge(0, 0, -1d);
		weightedGraph.addEdge(0, 1, 2d);
		weightedGraph.addEdge(0, 1, 3d);
		weightedGraph.addEdge(1, 2, 4d);
		weightedGraph.addEdge(0, 3, 0d);
		weightedGraph.addEdge(3, 1, 5d);
		weightedGraph.addEdge(3, 2, 2d);
		weightedGraph.addEdge(2, 4, 2d);
		List<Integer> bfs = BreadthFirstSearch.findPath(weightedGraph, 0, 4);
		System.out.println(bfs);
		List<Integer> dfs = DepthFirstSearch.findPath(weightedGraph, 0, 4);
		System.out.println(dfs);
		List<Tuple<Integer, Double>> dijkstra =
			Dijkstra.findShortestPath(weightedGraph, 0, 4);
		System.out.println(dijkstra);
		HashMap<Integer, Tuple<Integer, Double>> bellmanFord =
			BellmanFord.findAllShortestPaths(weightedGraph, 0, true);
		System.out.println(bellmanFord);
		HashMap<Integer, HashMap<Integer, Tuple<Integer, Double>>> floydWarshall =
			FloydWarshall.findAllShortestPaths(weightedGraph);
		System.out.println(floydWarshall);
		HashMap<Integer, HashMap<Integer, Tuple<Integer, Double>>> johnson =
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
