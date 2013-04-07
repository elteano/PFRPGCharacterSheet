package org.elteano.charactersheet;

import android.os.Parcel;
import android.os.Parcelable;

public class Skill implements Parcelable {

	public static final String SKILL_SUFFIX_CHA = ".cha";
	public static final String SKILL_SUFFIX_CON = ".con";
	public static final String SKILL_SUFFIX_DEX = ".dex";
	public static final String SKILL_SUFFIX_INT = ".int";
	public static final String SKILL_SUFFIX_STR = ".str";
	public static final String SKILL_SUFFIX_WIS = ".wis";

	public String skillName;
	public int baseAbility;
	public int numRanks;
	public int miscMod;

	public Skill(String extendedName, int ranks) {
		skillName = deriveSkillName(extendedName);
		baseAbility = deriveBaseAbility(extendedName);
		numRanks = ranks;
	}

	public Skill(String name, int baseAbility, int ranks, int mMod) {
		skillName = name;
		this.baseAbility = baseAbility;
		numRanks = ranks;
		miscMod = mMod;
	}

	public static int deriveBaseAbility(String extendedName) {
		String importantPart = extendedName
				.substring(extendedName.length() - 4);
		if (importantPart.equals(SKILL_SUFFIX_CHA))
			return PlayerCharacter.ABILITY_CHA;
		else if (importantPart.equals(SKILL_SUFFIX_CON))
			return PlayerCharacter.ABILITY_CON;
		else if (importantPart.equals(SKILL_SUFFIX_DEX))
			return PlayerCharacter.ABILITY_DEX;
		else if (importantPart.equals(SKILL_SUFFIX_INT))
			return PlayerCharacter.ABILITY_INT;
		else if (importantPart.equals(SKILL_SUFFIX_STR))
			return PlayerCharacter.ABILITY_STR;
		else if (importantPart.equals(SKILL_SUFFIX_WIS))
			return PlayerCharacter.ABILITY_WIS;
		return -1;
	}

	public static String deriveSkillSuffix(int abilityValue) {
		switch (abilityValue) {
		case PlayerCharacter.ABILITY_CHA:
			return SKILL_SUFFIX_CHA;
		case PlayerCharacter.ABILITY_CON:
			return SKILL_SUFFIX_CON;
		case PlayerCharacter.ABILITY_DEX:
			return SKILL_SUFFIX_DEX;
		case PlayerCharacter.ABILITY_INT:
			return SKILL_SUFFIX_INT;
		case PlayerCharacter.ABILITY_STR:
			return SKILL_SUFFIX_STR;
		case PlayerCharacter.ABILITY_WIS:
			return SKILL_SUFFIX_WIS;
		}
		return "";
	}

	public static String deriveSkillName(String extendedName) {
		return extendedName.substring(0, extendedName.length() - 4);
	}

	public int describeContents() {
		return 0;
	}

	public static Skill fromSaveString(String saveString) {
		String[] comp = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		if (comp.length != 4)
			return null;
		int baseAbility, numRanks, miscMod;
		baseAbility = numRanks = miscMod = 0;
		try {
			baseAbility = Integer.parseInt(comp[1]);
			numRanks = Integer.parseInt(comp[2]);
			miscMod = Integer.parseInt(comp[3]);
		} catch (NumberFormatException ex) {
		}
		return new Skill(comp[0], baseAbility, numRanks, miscMod);
	}

	public int getBaseAbility() {
		return baseAbility;
	}

	public String getName() {
		return skillName;
	}

	public String getLongName() {
		return skillName + deriveSkillSuffix(baseAbility);
	}

	public int getNumRanks() {
		return numRanks;
	}

	public int getTotalModifier(AbilityScore[] abilities) {
		return abilities[baseAbility].getTempModifier() + numRanks + miscMod;
	}

	public void setBaseAbility(int baseAbility) {
		this.baseAbility = baseAbility;
	}

	public void setName(String name) {
		skillName = name;
	}

	public void setSkillRanks(int ranks) {
		numRanks = ranks;
	}

	public String toSaveString() {
		return skillName + PlayerCharacter.SPLITTER_SMALL + baseAbility
				+ PlayerCharacter.SPLITTER_SMALL + numRanks
				+ PlayerCharacter.SPLITTER_SMALL + miscMod;
	}

	public void writeToParcel(Parcel out, int iHaveNoClueWhatThisDoes) {
		out.writeString(skillName);
		out.writeInt(baseAbility);
		out.writeInt(numRanks);
		out.writeInt(miscMod);
	}

	public static Parcelable.Creator<Skill> CREATOR = new Parcelable.Creator<Skill>() {

		public Skill createFromParcel(Parcel in) {
			return new Skill(in.readString(), in.readInt(), in.readInt(),
					in.readInt());
		}

		public Skill[] newArray(int size) {
			return new Skill[size];
		}
	};
}
