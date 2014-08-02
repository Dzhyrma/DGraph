package org.dgraph.util;

/** Implementation of a tuple with two elements.
 * 
 * @param <I1> type of the first element
 * @param <I2> type of the second element
 *
 * @author Andrii Dzhyrma
 * @since 1.0 */
public class Tuple<I1, I2> {
	private I1 item1;
	private I2 item2;

	/** Creates an instance of a tuple with nulls. */
	public Tuple() {
	}

	/** Creates an instance of a tuple with two given items.
	 * 
	 * @param item1 first item
	 * @param item2 second item */
	public Tuple(I1 item1, I2 item2) {
		this.item1 = item1;
		this.item2 = item2;
	}

	/** Retrieves the first item
	 * 
	 * @return first item */
	public final I1 getItem1() {
		return this.item1;
	}

	/** Sets a new value for the first item
	 * 
	 * @param item1 new value */
	public final void setItem1(I1 item1) {
		this.item1 = item1;
	}

	/** Retrieves the second item
	 * 
	 * @return second item */
	public final I2 getItem2() {
		return this.item2;
	}

	/** Sets a new value for the second item
	 * 
	 * @param item2 new value */
	public final void setItem2(I2 item2) {
		this.item2 = item2;
	}

	@Override
	public String toString() {
		return String.format("{%s, %s}", item1, item2);
	}

}
