package org.dgraph.graph;

import static org.junit.Assert.*;

import org.dgraph.graph.edge.SimpleEdge;
import org.junit.Before;
import org.junit.Test;

public class SimpleGraphTest {

	private Graph<Integer, SimpleEdge<Integer>> graph;

	@Before
	public void setUp() throws Exception {
		graph = new SimpleGraph<>(SimpleEdge<Integer>::new);
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(0, 3);
		graph.addEdge(3, 1);
		graph.addEdge(3, 2);
		graph.addEdge(2, 4);
		System.out.println(graph);
	}

	@Test
	public void testAddEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddVertex() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsVertex() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsEdgeE() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllEdges() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllVertices() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEdges() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEdgesFromSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveEdge() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveVertex() {
		fail("Not yet implemented");
	}

	@Test
	public void testSizeOfEdges() {
		fail("Not yet implemented");
	}

	@Test
	public void testSizeOfVertices() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsEdgeVV() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEdgesToTarget() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAllEdges() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAllVertices() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
	}

}
