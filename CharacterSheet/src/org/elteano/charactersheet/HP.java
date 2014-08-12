package org.elteano.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class HP implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	private ArrayList<Integer> mHPs;
	private int miscMods;

	public static final Parcelable.Creator<HP> CREATOR = new Creator<HP>() {

		public HP[] newArray(int size) {
			return new HP[size];
		}

		public HP createFromParcel(Parcel source) {
			int miscMods = source.readInt();
			ArrayList<Integer> templist = new ArrayList<Integer>();
			source.readList(templist, Integer.class.getClassLoader());
			return new HP(miscMods, templist);
		}
	};

	public HP() {
		mHPs = new ArrayList<Integer>();
	}

	public HP(int miscMods) {
		mHPs = new ArrayList<Integer>();
		this.miscMods = miscMods;
	}

	public HP(int miscMods, ArrayList<Integer> hps) {
		this.miscMods = miscMods;
		mHPs = new ArrayList<Integer>();
		mHPs.addAll(hps);
	}

	public void addRoll(int roll) {
		mHPs.add(roll);
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static HP fromSaveString(String saveString) {
		if (saveString.isEmpty())
			return new HP();
		String[] split = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		int misc = 0;
		if (split.length > 0) {
			try {
				misc = Integer.parseInt(split[0]);
			} catch (NumberFormatException ex) {
				// Avoid crashing at all costs!
			}
		}
		ArrayList<Integer> hps = new ArrayList<Integer>();
		for (int i = 1; i < split.length; i++) {
			try {
				hps.add(Integer.parseInt(split[i]));
			} catch (NumberFormatException ex) {
				// Avoid crashing as much as possible!
			}
		}
		return new HP(misc, hps);
	}

	/**
	 * Get maximum possible HP, applying the given per-level modifiers
	 * (typically constitution modifier)
	 *
	 * @param perLevelModifiers
	 *            Modifiers applied to each level roll.
	 * @return The maximum HP this character can possess.
	 */
	public int getMaxHP(int perLevelModifiers) {
		int ret = miscMods;
		for (int roll : mHPs) {
			roll += perLevelModifiers;
			ret += (roll > 0) ? roll : 1;
		}
		return ret;
	}

	public int getRolledHP() {
		int ret = 0;
		for (int i : mHPs)
			ret += i;
		return ret;
	}

	public int getRolledLevelHP(int level) {
		if (mHPs.size() >= level)
			return mHPs.get(level - 1);
		else
			return -1;
	}

	public String toSaveString() {
		String ret = "" + miscMods;
		for (int i : mHPs)
			ret += PlayerCharacter.SPLITTER_SMALL + i;
		return ret;
	}

	public void ensureLevelCount(int level) {
		if (mHPs.size() < level) {
			for (int i = mHPs.size(); i < level; i++)
				mHPs.add(0);
		}
	}

	public void setRolledHP(int level, int hp) {
		ensureLevelCount(level);
		mHPs.set(level - 1, hp);
		Log.i("CharacterSheet",
				"Level " + level + " is now " + mHPs.get(level - 1));
	}

	public void setMiscModifiers(int mods) {
		miscMods = mods;
	}

	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(miscMods);
		arg0.writeList(mHPs);
	}

	public JSONObject writeToJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("miscMods", miscMods);
			JSONArray arr = new JSONArray();
			for (int ind : mHPs) {
				arr.put(ind);
			}
			ret.put("mHPs", arr);
		} catch (JSONException ex) {
		}
		return ret;
	}

	public static HP createFromJSON(JSONObject input) {
		int miscMods = 0;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		try {
			miscMods = input.getInt("miscMods");
			JSONArray jarr = input.getJSONArray("mHPs");
			for (int i = 0; i < jarr.length(); ++i) {
				arr.add(jarr.getInt(i));
			}
			return new HP(miscMods, arr);
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating HP from JSON");
			return null;
		}
	}
}
