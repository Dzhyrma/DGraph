package org.dgraph.util;

import java.util.Objects;

/** This class represents a priority queue backed by a Fibonacci heap, as
 * described by Fredman and Tarjan in 1984. It is used because of the
 * find-minimum, insert, decrease key and merge in O(1) amortized time. Only
 * delete and delete minimum work in O(log n) amortized time.
 * 
 * In this particular library, this queue has been use in algorithms like
 * Dijkstra, MinCostMaxFlow, Johnson and A*Search.
 *
 * @param <T> type for values
 *
 * @author Andrii Dzhyrma
 * @since 0.1 */
public class FibonacciHeap<T> {
	public static class Node<T> {

		private FibonacciHeap<T> creator;
		private T value;
		private double priority;
		private Node<T> left, right, child, parent;
		private int rank;
		private boolean isMarked;

		private Node(T value, double priority, FibonacciHeap<T> creator) {
			this.value = value;
			this.priority = priority;
			this.creator = creator;
		}

		/** Returns the value of the node.
		 * 
		 * @return value of the node */
		public T getValue() {
			return value;
		}

		/** Returns the priority of the node
		 * 
		 * @return priority of the node */
		public double getPriority() {
			return priority;
		}

		/** Returns a dequeued state ot he node.
		 * 
		 * @return true, if this node was dequeued or deleted from a heap */
		public boolean isDequeued() {
			return rank < 0;
		}
	}

	private Node<T> min = null;
	private int size;

	private void cut(Node<T> node) {
		if (node.parent.child == node)
			node.parent.child = (node.left == node) ? null : node.right;
		node.parent.rank--;
		node.right.left = node.left;
		node.left.right = node.right;
		node.isMarked = false;
		insert(node);
		if (node.parent.parent != null)
			if (node.parent.isMarked)
				cut(node.parent);
			else
				node.parent.isMarked = true;
		node.parent = null;
	}

	private void decreaseKeyUnchecked(Node<T> node, double newPriority) {
		node.priority = newPriority;
		if (node.parent == null) {
			if (node.priority <= min.priority)
				min = node;
			return;
		}
		if (node.priority > node.parent.priority)
			return;

		cut(node);
	}

	private void insert(Node<T> node) {
		node.left = min;
		node.right = min.right;
		min.right = min.right.left = node;
		if (min.priority >= node.priority)
			min = node;
	}

	private void merge(Node<T> node) {
		node.left.right = min.right;
		node.left = min;
		min.right.left = node.left;
		min.right = node;
		if (min.priority > node.priority)
			min = node;
	}

	/** Enqueues the specified element into the heap with the specified priority.
	 * Its priority must be a valid double, so every value except NaN will be
	 * accepted.
	 * 
	 * @param value value to insert
	 * @param priority priority, which represents order of the given value
	 * @return a node representing that element in the heap */
	public Node<T> enqueue(T value, double priority) {
		if (Double.isNaN(priority))
			throw new IllegalArgumentException("Priority cannot be NaN.");
		size++;
		Node<T> newNode = new Node<>(value, priority, this);
		if (min == null) {
			min = newNode;
			min.left = min.right = min;
			return newNode;
		}
		insert(newNode);
		return newNode;
	}

	/** Merges current heap with the given one. After merge the second heap will be
	 * empty.
	 * 
	 * @param heap heap to merge with */
	public void merge(FibonacciHeap<T> heap) {
		if (heap == null || heap.min == null)
			return;
		if (min != null)
			merge(heap.min);
		size += heap.size;
		heap.min = null;
		heap.size = 0;
	}

	/** Deletes node from the current heap. The node will be counting as dequeued.
	 * 
	 * @param node node to delete */
	public void delete(Node<T> node) {
		Objects.requireNonNull(node, "Cannot delete null from the heap.");
		if (node.creator != this)
			throw new IllegalArgumentException("Given node belogs to another heap.");
		decreaseKeyUnchecked(node, Double.NEGATIVE_INFINITY);
		dequeueMin();
	}

	/** Decrease key of the given key to a new priority. The new priority must be a
	 * valid double and be less than the old priority. In case when it is bigger
	 * than the old one, delete this node first and then add a new node with same
	 * value and new priority.
	 * 
	 * @param node node to decrease
	 * @param priority new priority */
	public void decreaseKey(Node<T> node, double newPriority) {
		Objects.requireNonNull(node, "Node cannot be a null.");
		if (node.creator != this)
			throw new IllegalArgumentException("Given node belogs to another heap.");
		if (Double.isNaN(newPriority))
			throw new IllegalArgumentException("New priority cannot be NaN.");
		if (newPriority > node.priority)
			throw new IllegalArgumentException("New priority cannot exceed old.");
		decreaseKeyUnchecked(node, newPriority);
	}

	/** Dequeues the minimum from the heap.
	 * 
	 * @return a node representing the element with minimal priority in the heap */
	@SuppressWarnings("unchecked")
	public Node<T> dequeueMin() {
		if (min == null)
			return null;

		Node<T> res = min;
		if (min.left == min) {
			min = min.child;
		} else if (min.child == null) {
			min.left.right = min.right;
			min = min.right.left = min.left;
		} else {
			min.left.right = min.child;
			min.right.left = min.child.left;
			min.child.left.right = min.right;
			min = min.child.left = min.left;
		}

		if (min != null) {
			@SuppressWarnings("rawtypes")
			Node[] visited = new Node[32 - Integer.numberOfLeadingZeros(size)];
			Node<T> cur = min.right, temp;
			min.parent = null;
			visited[min.rank] = min;
			while (visited[cur.rank] != cur) {
				cur.parent = null;
				while (visited[cur.rank] != null) {
					temp = visited[cur.rank];
					visited[cur.rank] = null;
					temp.left.right = temp.right;
					temp.right.left = temp.left;
					if (temp.priority < cur.priority) {
						if (cur.left != cur) {
							temp.left = cur.left;
							temp.right = cur.right;
							cur.left.right = cur.right.left = temp;
						} else
							temp.left = temp.right = temp;
						Node<T> t = temp;
						temp = cur;
						cur = t;
					}
					if (cur.rank == 0) {
						cur.child = temp;
						temp.right = temp.left = temp;
					} else {
						temp.right = cur.child.right;
						temp.left = cur.child;
						cur.child.right.left = cur.child.right = temp;
						if (cur.child.priority > cur.child.right.priority)
							cur.child = cur.child.right;
					}
					temp.parent = cur;
					cur.rank++;
				}
				visited[cur.rank] = cur;
				if (cur.priority < min.priority)
					min = cur;
				cur = cur.right;
			}
		}

		size--;
		res.left = res.right = res.child = null;
		res.rank = -1;
		return res;
	}

	/** Retrieves a minimum from the heap.
	 * 
	 * @return a node representing the element with minimal priority in the heap */
	public Node<T> getMin() {
		return min;
	}

	/** Checks, whether current heap is empty.
	 * 
	 * @return true, if heap contains no elements */
	public boolean isEmpty() {
		return min == null;
	}

	/** Retrieves the size of the heap.
	 * 
	 * @return number of nodes in the heap. */
	public int size() {
		return size;
	}
}
