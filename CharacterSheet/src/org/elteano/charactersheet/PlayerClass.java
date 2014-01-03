package org.elteano.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class PlayerClass implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	public static Parcelable.Creator<PlayerClass> CREATOR = new Parcelable.Creator<PlayerClass>() {

		public PlayerClass createFromParcel(Parcel source) {
			return new PlayerClass(source.readString(), source.readInt());
		}

		public PlayerClass[] newArray(int size) {
			return new PlayerClass[size];
		}
	};
	public static String compoundToString(ArrayList<PlayerClass> classes) {
		String ret = "";
		for (PlayerClass c : classes) {
			ret += c.toString() + " / ";
		}
		if (ret.length() > 3)
			ret = ret.substring(0, ret.length() - 3);
		return ret;
	}

	public static ArrayList<PlayerClass> interpretListString(String listString) {
		ArrayList<PlayerClass> ret = new ArrayList<PlayerClass>();
		for (String classLevel : listString.split("/")) {
			if (classLevel.isEmpty())
				break;
			String[] breakdown = classLevel.trim().split("\\s");
			int level = 0;
			if (breakdown.length > 1) {
				try {
					level = Integer.parseInt(breakdown[1]);
				} catch (NumberFormatException ex) {
				}
			}
			ret.add(new PlayerClass(breakdown[0], level));
		}
		return ret;
	}

	private int levels;

	private String name;

	public PlayerClass(String name, int levels) {
		this.name = name;
		this.levels = levels;
	}

	public int describeContents() {
		return 0;
	}

	public int getLevels() {
		return levels;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return getName() + " " + getLevels();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(levels);
	}

}
