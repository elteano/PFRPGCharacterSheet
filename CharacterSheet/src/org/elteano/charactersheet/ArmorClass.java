package org.elteano.charactersheet;

import android.os.Parcel;
import android.os.Parcelable;

public class ArmorClass implements Parcelable {

	public boolean shield;
	public int armorBonus;
	public int miscBonus;
	public int dodgeBonus;
	public int shieldBonus;

	public static final Parcelable.Creator<ArmorClass> CREATOR = new Parcelable.Creator<ArmorClass>() {

		public ArmorClass createFromParcel(Parcel source) {
			return new ArmorClass(source.readInt(), source.readInt(),
					source.readInt(), source.readInt(), source.readByte());
		}

		public ArmorClass[] newArray(int size) {
			return new ArmorClass[size];
		}
	};

	public ArmorClass(int armorBonus, int miscBonus, int dodgeBonus,
			int shieldBonus, byte getShieldBonus) {
		this(armorBonus, miscBonus, dodgeBonus, shieldBonus,
				getShieldBonus == (byte) 1);
	}

	public ArmorClass(int armorBonus, int miscBonus, int dodgeBonus,
			int shieldBonus, boolean getShieldBonus) {
		this.armorBonus = armorBonus;
		this.miscBonus = miscBonus;
		this.dodgeBonus = dodgeBonus;
		this.shieldBonus = shieldBonus;
		shield = getShieldBonus;
	}

	public static ArmorClass fromSaveString(String s) {
		if (s.isEmpty())
			return new ArmorClass(0, 0, 0, 0, true);
		String[] container = s.split(PlayerCharacter.SPLITTER_SMALL);
		if (container.length != 5)
			return new ArmorClass(0, 0, 0, 0, true);
		int abonus, dbonus, mbonus, sbonus;
		abonus = dbonus = mbonus = sbonus = 0;
		boolean shield;
		try {
			abonus = Integer.parseInt(container[0]);
			dbonus = Integer.parseInt(container[1]);
			mbonus = Integer.parseInt(container[2]);
			sbonus = Integer.parseInt(container[3]);
		} catch (NumberFormatException ex) {
		}
		shield = container[4].equals("true");
		return new ArmorClass(abonus, dbonus, mbonus, sbonus, shield);
	}

	public int getAC(AbilityScore[] abilities, int size, int bab) {
		return 10 + armorBonus + miscBonus + dodgeBonus
				+ abilities[PlayerCharacter.ABILITY_DEX].getTempModifier()
				+ getSizeModifier(size) + getShieldBonus();
	}

	public int getCMD(AbilityScore[] abilities, int size, int bab) {
		return getTouchAC(abilities, size, bab) - getSizeModifier(size) * 2
				+ bab
				+ abilities[PlayerCharacter.ABILITY_STR].getTempModifier();
	}

	public int getFlatFootAC(AbilityScore[] abilities, int size, int bab) {
		return getFlatFootTouchAC(abilities, size, bab) + armorBonus
				+ getShieldBonus();
	}

	public int getFlatFootCMD(AbilityScore[] abilities, int size, int bab) {
		return getFlatFootTouchAC(abilities, size, bab) - getSizeModifier(size)
				* 2 + bab;
	}

	public int getShieldlessAC(AbilityScore[] abilities, int size, int bab) {
		return 10 + armorBonus + miscBonus + dodgeBonus
				+ abilities[PlayerCharacter.ABILITY_DEX].getTempModifier()
				+ getSizeModifier(size);
	}

	public int getShieldBonus() {
		if (shield)
			return shieldBonus;
		else
			return 0;
	}

	public static int getSizeModifier(int size) {
		switch (size) {
		case PlayerCharacter.SIZE_FINE:
			return 8;
		case PlayerCharacter.SIZE_DIMINUTIVE:
			return 4;
		case PlayerCharacter.SIZE_TINY:
			return 2;
		case PlayerCharacter.SIZE_SMALL:
			return 1;
		case PlayerCharacter.SIZE_LARGE:
			return -1;
		case PlayerCharacter.SIZE_HUGE:
			return -2;
		case PlayerCharacter.SIZE_GARGANTUAN:
			return -4;
		case PlayerCharacter.SIZE_COLOSSAL:
			return -8;
		default:
			return 0;
		}
	}

	public int getTouchAC(AbilityScore[] abilities, int size, int bab) {
		return 10 + dodgeBonus + miscBonus
				+ abilities[PlayerCharacter.ABILITY_DEX].getTempModifier()
				+ getSizeModifier(size);
	}

	public int getFlatFootTouchAC(AbilityScore[] abilities, int size, int bab) {
		return 10
				+ ((abilities[PlayerCharacter.ABILITY_DEX].getTempModifier() < 0) ? abilities[PlayerCharacter.ABILITY_DEX]
						.getTempModifier() : 0) + getSizeModifier(size)
				+ miscBonus;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(armorBonus);
		out.writeInt(miscBonus);
		out.writeInt(dodgeBonus);
		out.writeInt(shieldBonus);
		out.writeByte((byte) ((shield) ? 1 : 0));
	}

	public String toSaveString() {
		return armorBonus + PlayerCharacter.SPLITTER_SMALL + dodgeBonus
				+ PlayerCharacter.SPLITTER_SMALL + miscBonus
				+ PlayerCharacter.SPLITTER_SMALL + shieldBonus
				+ PlayerCharacter.SPLITTER_SMALL + shield;
	}
}
