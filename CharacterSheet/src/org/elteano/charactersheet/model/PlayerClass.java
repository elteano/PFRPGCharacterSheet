package org.elteano.charactersheet.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Class for storing information about a player's classes.
 */
public class PlayerClass implements Parcelable, Serializable {

	/**
	 * Serializable stuff.
	 */
	public static final long serialVersionUID = 1L;
	/**
	 * Parcelable stuff.
	 */
	public static Parcelable.Creator<PlayerClass> CREATOR = new Parcelable.Creator<PlayerClass>() {

		public PlayerClass createFromParcel(Parcel source) {
			return new PlayerClass(source.readString(), source.readInt());
		}

		public PlayerClass[] newArray(int size) {
			return new PlayerClass[size];
		}
	};

	/**
	 * Turns a list of player classes into a String as might be seen on a
	 * character sheet.
	 *
	 * Example: a barbarian 5 and bard 3 would end up as barbarian 5 / bard 3
	 *
	 * @param classes
	 *            The list of classes to stringify.
	 * @return A presentable string.
	 */
	public static String compoundToString(ArrayList<PlayerClass> classes) {
		String ret = "";
		for (PlayerClass c : classes) {
			ret += c.toString() + " / ";
		}
		if (ret.length() > 3)
			ret = ret.substring(0, ret.length() - 3);
		return ret;
	}

	/**
	 * Interprets a textual list of classes and turns it into a list of objects
	 * for internal use.
	 *
	 * @param listString
	 *            A String list of classes delimited by forward slashes.
	 * @return An object list of classes.
	 */
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

	/**
	 * The number of levels invested in this class.
	 */
	private int levels;

	/**
	 * The name of the class.
	 */
	private String name;

	/**
	 * Creates a class with the given name and level.
	 *
	 * @param name
	 *            The class's name.
	 * @param levels
	 *            The number of character levels invested into the class.
	 */
	public PlayerClass(String name, int levels) {
		this.name = name;
		this.levels = levels;
	}

	/**
	 * Parcelable stuff.
	 */
	public int describeContents() {
		return 0;
	}

	/**
	 * Gets the number of character levels invested into the class.
	 *
	 * @return The number of character levels invested into the class.
	 */
	public int getLevels() {
		return levels;
	}

	/**
	 * Gets the name of the class.
	 *
	 * @return The name of the class.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName() + " " + getLevels();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(levels);
	}

	public JSONObject writeToJSON() {
		try {
			JSONObject ret = new JSONObject();
			ret.put("levels", levels);
			ret.put("name", name);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON for PlayerClass");
			return null;
		}
	}

	public static PlayerClass createFromJSON(JSONObject input) {
		try {
			return new PlayerClass(input.getString("name"),
					input.getInt("levels"));
		} catch (JSONException e) {
			Log.e("CharacterSheet", "Error inflating PlayerClass from JSON");
			return null;
		}
	}
}
