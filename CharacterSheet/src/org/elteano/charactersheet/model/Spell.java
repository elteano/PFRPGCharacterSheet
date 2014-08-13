package org.elteano.charactersheet.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Class for storing data and enacting calculations relating to player spells.
 */
public class Spell implements Parcelable, Serializable {

	/**
	 * Requirement for Serializable.
	 */
	public static final long serialVersionUID = 1L;
	/**
	 * Requirement for Parcelable.
	 */
	public static final Parcelable.Creator<Spell> CREATOR = new Parcelable.Creator<Spell>() {

		public Spell createFromParcel(Parcel source) {
			return new Spell(source.readInt(), source.readString(),
					source.readString(), source.readInt(), source.readInt(),
					null);
		}

		public Spell[] newArray(int size) {
			return new Spell[size];
		}
	};

	/**
	 * The spell level.
	 *
	 * Mostly for the player's benefit, although this is also used in
	 * calculating the DC.
	 */
	public int level;
	/**
	 * Any miscellaneous bonus to the save DC.
	 */
	public int saveBonus;
	/**
	 * The ability score used in calculating the save DC.
	 */
	public int saveAbility;
	/**
	 * The spell's name.
	 */
	public String name;
	/**
	 * A description of the spell.
	 */
	public String desc;

	/**
	 * Create a new spell conforming to the old ways. Retained for backwards
	 * compatibility; should not be used very often.
	 *
	 * @deprecated
	 */
	@Deprecated
	public Spell(int level, String name, String desc, AbilityScores abilities) {
		this(level, name, desc, 0, 0, abilities);
	}

	/**
	 * Create a new spell containing all fields. This is the version that should
	 * now be called in most cases.
	 */
	public Spell(int level, String name, String desc, int saveBonus,
			int saveAbility, AbilityScores abilities) {
		this.level = level;
		this.name = name;
		this.desc = desc;
		this.saveBonus = saveBonus;
		this.saveAbility = saveAbility;
	}

	/**
	 * Requirement for Parcelable.
	 */
	public int describeContents() {
		return 0;
	}

	/**
	 * Calculates the save DC for the spell.
	 *
	 * @param abilities
	 *            The player's ability scores.
	 * @return The spell's save DC.
	 */
	public int getSaveDC(AbilityScores abilities) {
		return 10 + level + saveBonus
				+ abilities.getAbility(saveAbility).getTempModifier();
	}

	/**
	 * Constructs a spell from a save string.
	 *
	 * This is primarily used for saving the character to SharedPreferences.
	 *
	 * @param s
	 *            The String from which to construct the spell.
	 * @param abilities
	 *            Unused.
	 * @return A Spell constructed from the save string.
	 */
	public static Spell fromSaveString(String s, AbilityScores abilities) {
		String[] cont = s.split(PlayerCharacter.SPLITTER_SMALL);
		if (cont.length >= 3) {
			int level = 0;
			try {
				level = Integer.parseInt(cont[0]);
			} catch (NumberFormatException ex) {
			}
			if (cont.length == 5) {
				int saveBonus = 0;
				int saveAbility = 0;
				try {
					saveBonus = Integer.parseInt(cont[3]);
					saveAbility = Integer.parseInt(cont[4]);
				} catch (NumberFormatException ex) {
				}
				return new Spell(level, cont[1], cont[2], saveBonus,
						saveAbility, abilities);
			}
			return new Spell(level, cont[1], cont[2], abilities);
		}
		return new Spell(0, "", "", 0, 0, abilities);

	}

	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(level);
		out.writeString(name);
		out.writeString(desc);
		out.writeInt(saveBonus);
		out.writeInt(saveAbility);
	}

	public String toSaveString() {
		if (desc.isEmpty())
			desc = " ";
		name = name.trim();
		return String.format("%1$d%4$s%2$s%4$s%3$s%4$s%5$d%4$s%6$d", level,
				name, desc, PlayerCharacter.SPLITTER_SMALL, saveBonus,
				saveAbility);
	}

	@Override
	public String toString() {
		return String.format("%d - %s", level, name.trim());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Spell))
			return false;
		Spell s = (Spell) o;
		return name.equals(s.name) && level == s.level;
	}

	public JSONObject writeToJSON() {
		try {
			JSONObject ret = new JSONObject();
			ret.put("desc", desc);
			ret.put("level", level);
			ret.put("name", name);
			ret.put("saveAbility", saveAbility);
			ret.put("saveBonus", saveBonus);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON from Spell");
			return null;
		}
	}

	public static Spell createFromJSON(JSONObject input) {
		try {
			return new Spell(input.getInt("level"), input.getString("name"),
					input.getString("desc"), input.getInt("saveBonus"),
					input.getInt("saveAbility"), null);
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating Spell from JSON");
			return null;
		}
	}
}
