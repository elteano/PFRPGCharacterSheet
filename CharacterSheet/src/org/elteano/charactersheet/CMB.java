package org.elteano.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CMB implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	private static final int FLAG_BAB = 0x2;
	private static final int FLAG_CHA = 0x8;
	private static final int FLAG_CON = 0x10;
	private static final int FLAG_DEX = 0x20;
	private static final int FLAG_STR = 0x40;
	private static final int FLAG_INT = 0x80;
	private static final int FLAG_WIS = 0x100;
	private static final int FLAG_SIZE = 0x200;

	private int flags;
	private int classModifiers;
	private int miscModifiers;
	private String classList;

	public CMB() {
		flags |= FLAG_BAB | FLAG_STR | FLAG_SIZE;
		classList = "";
	}

	public static Parcelable.Creator<CMB> CREATOR = new Parcelable.Creator<CMB>() {
		public CMB[] newArray(int size) {
			return new CMB[size];
		}

		public CMB createFromParcel(Parcel source) {
			CMB ret = new CMB();
			ret.setFlags(source.readInt());
			ret.setClassModifiers(source.readInt());
			ret.setMiscModifiers(source.readInt());
			ret.classList = source.readString();
			return ret;
		};
	};

	public int getCMB(int bab, int size, AbilityScores abilities,
			ArrayList<PlayerClass> classes) {
		int ret = classModifiers + miscModifiers;
		if (getBAB())
			ret += bab;
		if (getCha())
			ret += abilities.getCha().getTempModifier();
		if (getCon())
			ret += abilities.getCon().getTempModifier();
		if (getDex())
			ret += abilities.getDex().getTempModifier();
		if (getInt())
			ret += abilities.getInt().getTempModifier();
		if (getStr())
			ret += abilities.getStr().getTempModifier();
		if (getWis())
			ret += abilities.getWis().getTempModifier();
		if (getSize())
			ret -= ArmorClass.getSizeModifier(size);
		for (PlayerClass c : classes) {
			if (classList.contains(c.getName())) {
				ret += c.getLevels();
			}
		}
		return ret;
	}

	public static CMB fromSaveString(String saveString) {
		CMB ret = new CMB();
		if (!saveString.isEmpty()) {
			String[] cont = saveString.split(PlayerCharacter.SPLITTER_SMALL);
			ret.setFlags(Integer.parseInt(cont[0]));
			ret.setClassModifiers(Integer.parseInt(cont[1]));
			ret.setMiscModifiers(Integer.parseInt(cont[2]));
			if (cont.length > 3)
				ret.setClasses(cont[3].trim());
		}
		return ret;
	}

	/**
	 *
	 * @param c
	 * @return true if the class was added, false if the list already contained
	 *         that class
	 */
	public boolean addClass(PlayerClass c) {
		return addClass(c.getName());
	}

	public void setClasses(String classes) {
		classList = classes;
	}

	public boolean addClass(String name) {
		if (!classList.contains(name)) {
			classList += "|||" + name;
			return true;
		}
		return false;
	}

	public int describeContents() {
		return 0;
	}

	public int getClassModifiers() {
		return classModifiers;
	}

	public String getClassList() {
		return classList;
	}

	public int getMiscModifiers() {
		return miscModifiers;
	}

	public boolean getBAB() {
		return (flags & FLAG_BAB) == FLAG_BAB;
	}

	public boolean getCha() {
		return (flags & FLAG_CHA) == FLAG_CHA;
	}

	public boolean getCon() {
		return (flags & FLAG_CON) == FLAG_CON;
	}

	public boolean getDex() {
		return (flags & FLAG_DEX) == FLAG_DEX;
	}

	public boolean getInt() {
		return (flags & FLAG_INT) == FLAG_INT;
	}

	public boolean getStr() {
		return (flags & FLAG_STR) == FLAG_STR;
	}

	public boolean getWis() {
		return (flags & FLAG_WIS) == FLAG_WIS;
	}

	public boolean getSize() {
		return (flags & FLAG_SIZE) == FLAG_SIZE;
	}

	public void removeAllClasses() {
		classList = "";
	}

	public boolean removeClass(PlayerClass c) {
		return removeClass(c.getName());
	}

	public boolean removeClass(String s) {
		int index = classList.indexOf(s);
		if (index >= 0) {
			classList = classList.replace(s, "").replace("||||||", "|||");
			return true;
		}
		return false;
	}

	public void setClassModifiers(int mods) {
		classModifiers = mods;
	}

	public void setMiscModifiers(int mods) {
		miscModifiers = mods;
	}

	public void setBAB(boolean status) {
		if (status) {
			flags |= FLAG_BAB;
		} else {
			flags &= ~FLAG_BAB;
		}
	}

	public void setCha(boolean status) {
		if (status) {
			flags |= FLAG_CHA;
		} else {
			flags &= ~FLAG_CHA;
		}
	}

	public void setCon(boolean status) {
		if (status) {
			flags |= FLAG_CON;
		} else {
			flags &= ~FLAG_CON;
		}
	}

	public void setDex(boolean status) {
		if (status) {
			flags |= FLAG_DEX;
		} else {
			flags &= ~FLAG_DEX;
		}
	}

	public void setInt(boolean status) {
		if (status) {
			flags |= FLAG_INT;
		} else {
			flags &= ~FLAG_INT;
		}
	}

	public void setStr(boolean status) {
		if (status) {
			flags |= FLAG_STR;
		} else {
			flags &= ~FLAG_STR;
		}
	}

	public void setWis(boolean status) {
		if (status) {
			flags |= FLAG_WIS;
		} else {
			flags &= ~FLAG_WIS;
		}
	}

	public void setSize(boolean status) {
		if (status) {
			flags |= FLAG_SIZE;
		} else {
			flags &= ~FLAG_SIZE;
		}
	}

	private void setFlags(int flags) {
		this.flags = flags;
	}

	private int getFlags() {
		return flags;
	}

	public String toSaveString() {
		if (classList.isEmpty())
			classList = " ";
		return flags + PlayerCharacter.SPLITTER_SMALL + classModifiers
				+ PlayerCharacter.SPLITTER_SMALL + miscModifiers
				+ PlayerCharacter.SPLITTER_SMALL + classList;
	}

	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(getFlags());
		out.writeInt(classModifiers);
		out.writeInt(miscModifiers);
		out.writeString(classList);
	}

	public JSONObject writeToJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("classList", classList);
			ret.put("classModifiers", classModifiers);
			ret.put("flags", flags);
			ret.put("miscModifiers", miscModifiers);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON for CMB");
			return null;
		}
	}

	public static CMB createFromJSON(JSONObject input) {
		try {
			CMB ret = new CMB();
			ret.classList = input.getString("classList");
			ret.classModifiers = input.getInt("classModifiers");
			ret.flags = input.getInt("flags");
			ret.miscModifiers = input.getInt("flags");
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating CMB from JSON");
			return null;
		}
	}
}
