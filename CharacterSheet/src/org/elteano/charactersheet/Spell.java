package org.elteano.charactersheet;

import android.os.Parcel;
import android.os.Parcelable;

public class Spell implements Parcelable {

	public static final Parcelable.Creator<Spell> CREATOR = new Parcelable.Creator<Spell>() {

		public Spell createFromParcel(Parcel source) {
			return new Spell(source.readInt(), source.readString(),
					source.readString(), source.readInt(), source.readInt());
		}

		public Spell[] newArray(int size) {
			return new Spell[size];
		}
	};

	public int level, saveBonus, saveAbility;
	public String name, desc;

	/**
	 * Create a new spell conforming to the old ways. Retained for backwards
	 * compatibility; should not be used very often.
	 * 
	 * @deprecated
	 */
	public Spell(int level, String name, String desc) {
		this(level, name, desc, 0, 0);
	}

	/**
	 * Create a new spell containing all fields. This is the version that should
	 * now be called in most cases.
	 */
	public Spell(int level, String name, String desc, int saveBonus,
			int saveAbility) {
		this.level = level;
		this.name = name;
		this.desc = desc;
		this.saveBonus = saveBonus;
		this.saveAbility = saveAbility;
	}

	public int describeContents() {
		return 0;
	}

	public int getSaveDC() {
		return 10
				+ level
				+ saveBonus
				+ CharacterSheetActivity.getCharacter().getAbility(saveAbility)
						.getTempModifier();
	}

	public static Spell fromSaveString(String s) {
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
						saveAbility);
			}
			return new Spell(level, cont[1], cont[2]);
		}
		return new Spell(0, "", "", 0, 0);

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
}
