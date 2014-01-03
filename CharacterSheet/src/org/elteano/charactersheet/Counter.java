package org.elteano.charactersheet;

import java.io.Serializable;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Counter class used for keeping track of castings, rages, etc. for the day, or
 * whatever else someone may want.
 * 
 * @author emryn
 * 
 */
public class Counter implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	/** The minimum value of the Counter; access via methods */
	private int min;
	/** The maximum value of the Counter; access via methods */
	private int max;
	/** The current value of the Counter; access via methods */
	private int cur;
	/** The display name of the Counter; access via methods */
	private String counterName;

	/**
	 * Value required for Parcelables.
	 */
	public static Parcelable.Creator<Counter> CREATOR = new Parcelable.Creator<Counter>() {

		public Counter createFromParcel(Parcel source) {
			return new Counter(source.readString(), source.readInt(),
					source.readInt(), source.readInt());
		}

		public Counter[] newArray(int size) {
			return new Counter[size];
		}
	};

	/**
	 * Creates a new Counter with the given parameters. This is the only
	 * constructor, and so there are no default values other than those which
	 * are set by the GUI.
	 * 
	 * @param counterName
	 *            the counterName of the Counter as it will be displayed
	 * @param minValue
	 *            the minimum possible value of the counter, usually zero
	 *            (probably)
	 * @param maxValue
	 *            the maximum possible value of the counter
	 * @param startingValue
	 *            the value at which the counter will have at creation time
	 *            (should default to zero in GUI)
	 */
	public Counter(String counterName, int minValue, int maxValue,
			int startingValue) {
		this.counterName = counterName;
		min = minValue;
		max = maxValue;
		cur = startingValue;
	}

	/**
	 * Decrements the value stored by the Counter, not passing the min value as
	 * returned by <code>getMin()</code>.
	 */
	public void decrement() {
		setCur(getCur() - 1);
		if (getCur() < getMin()) {
			setCur(getMin());
		}
	}

	/**
	 * Only one kind of Counter...
	 */
	public int describeContents() {
		return 0;
	}

	/**
	 * Expands a Counter from the given save string. For use by
	 * PlayerCharacter's <code>restoreFromSharedPreferences()</code> method.
	 * 
	 * @param saveString
	 *            String to expand.
	 * @return a Counter represented by the savestring.
	 */
	public static Counter fromSaveString(String saveString) {
		String[] cont = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		return new Counter(cont[0], Integer.parseInt(cont[1]),
				Integer.parseInt(cont[2]), Integer.parseInt(cont[3]));
	}

	/**
	 * Increments the value stored within this Counter, not passing the max
	 * value as returned by <code>getMax()</code>.
	 */
	public void increment() {
		setCur(getCur() + 1);
		if (getCur() > getMax()) {
			setCur(getMax());
		}
	}

	/**
	 * Retrieves the current value of the counter.
	 * 
	 * @return the current value.
	 */
	public int getCur() {
		return cur;
	}

	/**
	 * Retrieves the name of the counter. This should be used to define the
	 * labels for this Counter in the GUI.
	 * 
	 * @return the name.
	 */
	public String getName() {
		return counterName;
	}

	/**
	 * Retrieves the maximum value permitted for this counter.
	 * 
	 * @return the maximum value.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Retrieves the minimum value permitted for this counter.
	 * 
	 * @return the minimum value.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Changes the current value of this Counter.
	 * 
	 * @param newCur
	 *            the new value.
	 */
	public void setCur(int newCur) {
		this.cur = newCur;
	}

	/**
	 * Changes the name of this Counter. This should reflect itself in a change
	 * of the label in the GUI.
	 * 
	 * @param newName
	 *            the new counterName.
	 */
	public void setName(String newName) {
		this.counterName = newName;
	}

	/**
	 * Changes the maximum value of this Counter.
	 * 
	 * @param newMax
	 *            the new max.
	 */
	public void setMax(int newMax) {
		this.max = newMax;
	}

	/**
	 * Changes the minimum value of this Counter.
	 * 
	 * @param newMin
	 *            the new min.
	 */
	public void setMin(int newMin) {
		this.min = newMin;
	}

	/**
	 * Compiles the Counter into a saveable String for use by PlayerCharacter's
	 * <code>saveToSharedPreference()</code> method.
	 * 
	 * @return a save String.
	 */
	public String toSaveString() {
		return String.format(Locale.US, "%2$s%1$s%3$d%1$s%4$d%1$s%5$d",
				PlayerCharacter.SPLITTER_SMALL, getName(), getMin(), getMax(),
				getCur());
	}

	public void writeToParcel(Parcel out, int iForget) {
		out.writeString(getName());
		out.writeInt(getMin());
		out.writeInt(getMax());
		out.writeInt(getCur());
	}

}
