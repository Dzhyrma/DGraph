package org.dgraph.collections;

public class FibonacciHeap<T> {
	public static class Node<T> {
		private T value;
		private double priority;
		private Node<T> left, right, child, parent;
		private int rank;
		private boolean isMarked;

		private Node(T value, double priority) {
			this.value = value;
			this.priority = priority;
		}

		public T getValue() {
			return value;
		}

		public double getPriority() {
			return priority;
		}
		
		public boolean isDequeued() {
			return rank < 0;
		}
	}

	private Node<T> min = null;
	private int size;

	public Node<T> enqueue(T value, double priority) {
		if (Double.isNaN(priority))
			throw new IllegalArgumentException("Priority cannot be NaN.");
		size++;
		Node<T> newNode = new Node<>(value, priority);
		if (min == null) {
			min = newNode;
			min.left = min.right = min;
			return newNode;
		}
		insert(newNode);
		return newNode;
	}

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
	
	public void decreaseKeyUnchecked(Node<T> node, double newPriority) {
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

	public void merge(FibonacciHeap<T> heap) {
		if (heap == null || heap.min == null)
			return;
		if (min != null)
			merge(heap.min);
		size += heap.size;
		heap.min = null;
		heap.size = 0;
	}
	
	public void delete(Node<T> node) {
		decreaseKeyUnchecked(node, Double.NEGATIVE_INFINITY);
		dequeueMin();
	}

	public void decreaseKey(Node<T> node, double newPriority) {
		if (Double.isNaN(newPriority))
			throw new IllegalArgumentException("New priority cannot be NaN.");
		if (newPriority > node.priority)
			throw new IllegalArgumentException("New priority cannot exceed old.");

		decreaseKeyUnchecked(node, newPriority);
	}

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
			Node[] visited = new Node[32 - Integer.numberOfLeadingZeros(size)]; // 32 - Integer.numberOfLeadingZeros(size) would save more space
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

	public Node<T> getMin() {
		return min;
	}

	public boolean isEmpty() {
		return min == null;
	}

	public int size() {
		return size;
	}
}
