package org.dgraph.collections;

public class Tuple<I1, I2> {
	private I1 item1;
	private I2 item2;

	public Tuple() {
	}

	public Tuple(I1 item1, I2 item2) {
		this.item1 = item1;
		this.item2 = item2;
	}

	public final I1 getItem1() {
		return this.item1;
	}

	public final void setItem1(I1 item1) {
		this.item1 = item1;
	}

	public final I2 getItem2() {
		return this.item2;
	}

	public final void setItem2(I2 item2) {
		this.item2 = item2;
	}
}
