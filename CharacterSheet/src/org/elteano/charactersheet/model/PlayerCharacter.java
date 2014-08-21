package org.elteano.charactersheet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.elteano.charactersheet.view.fragment.CharacterSelectFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class PlayerCharacter implements Parcelable, Serializable {

	public static final long serialVersionUID = 0x1L;
	public static final int ABILITY_CHA = 0;
	public static final int ABILITY_CON = 1;
	public static final int ABILITY_DEX = 2;
	public static final int ABILITY_INT = 3;
	public static final int ABILITY_STR = 4;
	public static final int ABILITY_WIS = 5;

	public static final int LOAD_LIGHT = 0;
	public static final int LOAD_MEDIUM = 1;
	public static final int LOAD_HEAVY = 2;
	public static final int LOAD_OVERLIMIT = 3;

	public static final int CONDITION_BLINDED = 1;
	public static final int CONDITION_COWERING = 2;
	public static final int CONDITION_DAZZLED = 4;
	public static final int CONDITION_ENTANGLED = 8;
	public static final int CONDITION_FLAT_FOOTED = 16;
	public static final int CONDITION_FRIGHTENED = 32;
	public static final int CONDITION_GRAPPLING = 64;
	public static final int CONDITION_HELPLESS = 128;
	public static final int CONDITION_INVISIBLE = 256;
	public static final int CONDITION_PINNED = 512;
	public static final int CONDITION_PRONE = 1024;
	public static final int CONDITION_SHAKEN = 2048;
	public static final int CONDITION_SQUEEZING = 4096;
	public static final int CONDITION_STUNNED = 8192;

	public static Parcelable.Creator<PlayerCharacter> CREATOR = new Parcelable.Creator<PlayerCharacter>() {

		public PlayerCharacter createFromParcel(Parcel src) {
			PlayerCharacter ret = new PlayerCharacter();
			Bundle b = src.readBundle();
			b.setClassLoader(AbilityScores.class.getClassLoader());
			ret.abilities = b.getParcelable("abilities");
			b.setClassLoader(Counter.class.getClassLoader());
			ret.counters = b.getParcelableArrayList("counters");
			b.setClassLoader(Feat.class.getClassLoader());
			ret.cfeats = b.getParcelableArrayList("cfeats");
			ret.feats = b.getParcelableArrayList("feats");
			b.setClassLoader(Item.class.getClassLoader());
			ret.items = b.getParcelableArrayList("items");
			b.setClassLoader(PlayerClass.class.getClassLoader());
			ret.classes = b.getParcelableArrayList("classes");
			b.setClassLoader(Skill.class.getClassLoader());
			ret.skills = b.getParcelableArrayList("skills");
			b.setClassLoader(Spell.class.getClassLoader());
			ret.spells = b.getParcelableArrayList("spells");
			ret.prepSpells = b.getParcelableArrayList("prepSpells");
			b.setClassLoader(WeapShield.class.getClassLoader());
			ret.mWieldableEquipment = b
					.getParcelableArrayList("mWieldableEquipment");
			ret.ac = (ArmorClass) src.readParcelable(ArmorClass.class
					.getClassLoader());
			ret.cmb = (CMB) src.readParcelable(CMB.class.getClassLoader());
			ret.characterGold = src.readFloat();
			ret.baseAttackBonus = src.readInt();
			ret.characterAge = src.readInt();
			ret.characterHealth = src.readInt();
			ret.characterSize = src.readInt();
			ret.characterXP = src.readInt();
			ret.flagsFort = src.readInt();
			ret.flagsRef = src.readInt();
			ret.flagsWill = src.readInt();
			ret.hpCurrent = src.readInt();
			ret.mHP = (HP) src.readParcelable(HP.class.getClassLoader());
			ret.miscInitBonus = src.readInt();
			ret.cModFort = src.readInt();
			ret.mModFort = src.readInt();
			ret.cModRef = src.readInt();
			ret.mModRef = src.readInt();
			ret.cModWill = src.readInt();
			ret.mModWill = src.readInt();
			ret.characterBio = src.readString();
			ret.languages = src.readString();
			ret.name = src.readString();
			return ret;
		}

		public PlayerCharacter[] newArray(int size) {
			return new PlayerCharacter[size];
		}
	};
	private static final String SAVESTATE_AC = PlayerCharacter.class
			.getCanonicalName() + ".ac";
	private static final String SAVESTATE_AGE = PlayerCharacter.class
			.getCanonicalName() + ".age";
	private static final String SAVESTATE_ATTACKS = PlayerCharacter.class
			.getCanonicalName() + ".attacks";
	private static final String SAVESTATE_BAB = PlayerCharacter.class
			.getCanonicalName() + ".bab";
	private static final String SAVESTATE_BIO = PlayerCharacter.class
			.getCanonicalName() + ".bio";
	private static final String SAVESTATE_CFEATS = PlayerCharacter.class
			.getCanonicalName() + ".cfeats";
	private static final String SAVESTATE_CMB = PlayerCharacter.class
			.getCanonicalName() + ".cmb";
	private static final String SAVESTATE_COUNTERS = PlayerCharacter.class
			.getCanonicalName() + ".counters";
	private static final String SAVESTATE_FEATS = PlayerCharacter.class
			.getCanonicalName() + ".feats";
	private static final String SAVESTATE_FORT_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".fortCMod";
	private static final String SAVESTATE_FORT_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".fortFlags";
	private static final String SAVESTATE_FORT_MOD = PlayerCharacter.class
			.getCanonicalName() + ".fortMod";
	private static final String SAVESTATE_GOLD = PlayerCharacter.class
			.getCanonicalName() + ".gold";
	private static final String SAVESTATE_HP_CURRENT = PlayerCharacter.class
			.getCanonicalName() + ".hpC";
	private static final String SAVESTATE_HP_ROLLED = PlayerCharacter.class
			.getCanonicalName() + ".hpR";
	private static final String SAVESTATE_HP_CONTAINER = PlayerCharacter.class
			.getCanonicalName() + ".hpContainer";
	private static final String SAVESTATE_INIT_BONUS = PlayerCharacter.class
			.getCanonicalName() + ".initBonus";
	private static final String SAVESTATE_ITEMS = PlayerCharacter.class
			.getCanonicalName() + ".items";
	private static final String SAVESTATE_LANGUAGES = PlayerCharacter.class
			.getCanonicalName() + ".languages";
	private static final String SAVESTATE_LEVEL = PlayerCharacter.class
			.getCanonicalName() + ".levelString";
	public static final String SAVESTATE_NAME = "org.elteano.charactersheet.Character.name";
	private static final String SAVESTATE_PREP_SPELLS = PlayerCharacter.class
			.getCanonicalName() + ".prepSpells";
	private static final String SAVESTATE_REF_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".reflexCMod";
	private static final String SAVESTATE_REF_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".reflexFlags";
	private static final String SAVESTATE_REF_MOD = PlayerCharacter.class
			.getCanonicalName() + ".reflexMod";
	private static final String SAVESTATE_SIZE = PlayerCharacter.class
			.getCanonicalName() + ".size";
	private static final String SAVESTATE_SKILLS = PlayerCharacter.class
			.getCanonicalName() + ".skills";
	private static final String SAVESTATE_SPELLS = PlayerCharacter.class
			.getCanonicalName() + ".spells";
	private static final String SAVESTATE_WILL_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".willCMod";
	private static final String SAVESTATE_WILL_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".willFlags";
	private static final String SAVESTATE_WILL_MOD = PlayerCharacter.class
			.getCanonicalName() + ".willMod";
	private static final String SAVESTATE_XP = PlayerCharacter.class
			.getCanonicalName() + ".xp";
	public static final int SIZE_COLOSSAL = 8;
	public static final int SIZE_DIMINUTIVE = 1;
	public static final int SIZE_FINE = 0;
	public static final int SIZE_GARGANTUAN = 7;
	public static final int SIZE_HUGE = 6;
	public static final int SIZE_LARGE = 5;
	public static final int SIZE_MEDIUM = 4;
	public static final int SIZE_SMALL = 3;
	public static final int SIZE_TINY = 2;
	public static final String SPLITTER_LARGE = "#7SPACE7#";
	public static final String SPLITTER_SMALL = ":1space4:";

	public static PlayerCharacter restoreByPlayerList(Activity activity,
			String name) {
		SharedPreferences playerList = activity.getSharedPreferences(
				CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
				Activity.MODE_PRIVATE);
		for (String key : playerList.getAll().keySet()) {
			if (playerList.getString(key, "basaoiunfalksjdbfioaweubf").equals(
					name)) {
				return restoreFromSharedPreferences(activity
						.getSharedPreferences(key, Activity.MODE_PRIVATE));
			}
		}
		return null;
	}

	public static PlayerCharacter restoreFromSharedPreferences(
			SharedPreferences state) {
		PlayerCharacter ret = new PlayerCharacter();
		ret.ac = ArmorClass.fromSaveString(state.getString(SAVESTATE_AC, ""));
		if (ret.ac.shieldBonus > 0) {
			// Convert shield bonus to wieldable shield
			WeapShield add = new WeapShield("Shield", ABILITY_STR, ABILITY_STR,
					0, "", 0, ret.ac.shieldBonus);
			add.setDescription("Automatically generated equipment.");
			ret.mWieldableEquipment.add(add);
			ret.ac.shieldBonus = 0;
		}
		ret.hpCurrent = state.getInt(SAVESTATE_HP_CURRENT, 0);
		ret.hpRolled = state.getInt(SAVESTATE_HP_ROLLED, 0);
		// Code to convert old HP system to new HP system
		if (ret.hpRolled != 0) {
			ret.mHP = new HP(ret.hpRolled);
		} else {
			ret.mHP = HP.fromSaveString(state.getString(SAVESTATE_HP_CONTAINER,
					""));
		}
		// End conversion code
		ret.languages = state.getString(SAVESTATE_LANGUAGES, "");
		ret.miscInitBonus = state.getInt(SAVESTATE_INIT_BONUS, 0);
		ret.setAge(state.getInt(SAVESTATE_AGE, 0));
		ret.setBAB(state.getInt(SAVESTATE_BAB, 0));
		ret.setBio(state.getString(SAVESTATE_BIO, "No bio found."));
		ret.setCMB(CMB.fromSaveString(state.getString(SAVESTATE_CMB, "")));
		ret.setGold(state.getFloat(SAVESTATE_GOLD, 0));
		ret.classes = PlayerClass.interpretListString(state.getString(
				SAVESTATE_LEVEL, ""));
		ret.setName(state.getString(SAVESTATE_NAME, "Roy"));
		ret.setSize(state.getInt(SAVESTATE_SIZE, SIZE_MEDIUM));
		ret.setXP(state.getInt(SAVESTATE_XP, 0));
		ret.setFort(new Save(state.getInt(SAVESTATE_FORT_FLAGS, Save.FLAG_CON),
				state.getInt(SAVESTATE_FORT_CMOD, 0), state.getInt(
						SAVESTATE_FORT_MOD, 0)));
		ret.setRef(new Save(state.getInt(SAVESTATE_REF_FLAGS, Save.FLAG_DEX),
				state.getInt(SAVESTATE_REF_CMOD, 0), state.getInt(
						SAVESTATE_REF_MOD, 0)));
		ret.setWill(new Save(state.getInt(SAVESTATE_WILL_FLAGS, Save.FLAG_WIS),
				state.getInt(SAVESTATE_WILL_CMOD, 0), state.getInt(
						SAVESTATE_WILL_MOD, 0)));
		for (int i = 0; i < 6; i++) {
			ret.setAbility(i,
					AbilityScore.restoreFromSharedPreferences(state, i));
		}
		String s = state.getString(SAVESTATE_COUNTERS, "");
		if (!s.isEmpty())
			for (String counterString : s.split(SPLITTER_LARGE))
				ret.addCounter(Counter.fromSaveString(counterString));
		Log.d("CharacterSheet", "Restored " + ret.counters.toString());
		String itemString = state.getString(SAVESTATE_ITEMS, "");
		if (!itemString.isEmpty())
			for (String item : itemString.split(SPLITTER_LARGE))
				ret.items.add(Item.fromSaveString(item));
		s = state.getString(SAVESTATE_SPELLS, "");
		if (!s.isEmpty())
			for (String spellString : s.split(SPLITTER_LARGE)) {
				ret.spells.add(Spell.fromSaveString(spellString,
						ret.getAbilities()));
			}
		s = state.getString(SAVESTATE_PREP_SPELLS, "");
		if (!s.isEmpty())
			for (String spellString : s.split(SPLITTER_LARGE)) {
				ret.prepSpells.add(Spell.fromSaveString(spellString,
						ret.getAbilities()));
			}
		s = state.getString(SAVESTATE_SKILLS, "");
		if (!s.isEmpty())
			for (String skillString : s.split(SPLITTER_LARGE))
				ret.skills.add(Skill.fromSaveString(skillString));
		for (String attackString : state.getString(SAVESTATE_ATTACKS, "")
				.split(SPLITTER_LARGE)) {
			Attack attack = Attack.constructFromString(attackString);
			if (attack != null) {
				ret.attacks.add(attack);
				ret.mWieldableEquipment.add(new WeapShield(attack));
			}
		}
		s = state.getString(SAVESTATE_FEATS, "");
		if (!s.isEmpty())
			for (String featString : s.split(SPLITTER_LARGE))
				ret.feats.add(Feat.fromSaveString(featString));
		s = state.getString(SAVESTATE_CFEATS, "");
		if (!s.isEmpty())
			for (String featString : s.split(SPLITTER_LARGE))
				ret.cfeats.add(Feat.fromSaveString(featString));
		return ret;
	}

	private AbilityScores abilities;
	private ArmorClass ac;
	private ArrayList<Attack> attacks;
	private int baseAttackBonus;
	private ArrayList<Feat> cfeats;
	private int characterAge;
	private String characterBio;
	private float characterGold;
	private int characterHealth;
	private int characterSize;
	private int characterXP;
	private ArrayList<PlayerClass> classes;
	private CMB cmb;
	private int cModFort;
	private int cModRef;
	private int cModWill;
	private ArrayList<Counter> counters;
	private ArrayList<Feat> feats;
	private int flagsFort;
	private int flagsRef;
	private int flagsWill;
	private int hpCurrent;
	private int hpRolled;
	private ArrayList<Item> items;
	private String languages;
	private int mConditions;
	private int miscInitBonus;
	private HP mHP;
	private int mModFort;
	private int mModRef;
	private int mModWill;
	private String name;
	private ArrayList<Spell> prepSpells;
	private ArrayList<Skill> skills;
	private ArrayList<Spell> spells;
	private ArrayList<WeapShield> mWieldableEquipment;

	public PlayerCharacter() {
		abilities = new AbilityScores();
		ac = new ArmorClass(0, 0, 0, 0, 0, true);
		name = "New Character";
		characterAge = 0;
		languages = "";
		characterSize = SIZE_MEDIUM;
		characterBio = "";
		cmb = new CMB();
		// levelString = "";
		flagsFort = Save.FLAG_CON;
		flagsRef = Save.FLAG_DEX;
		flagsWill = Save.FLAG_WIS;
		attacks = new ArrayList<Attack>();
		counters = new ArrayList<Counter>();
		classes = new ArrayList<PlayerClass>();
		feats = new ArrayList<Feat>();
		mWieldableEquipment = new ArrayList<WeapShield>();
		cfeats = new ArrayList<Feat>();
		mHP = new HP();
		items = new ArrayList<Item>();
		skills = new ArrayList<Skill>();
		spells = new ArrayList<Spell>();
		prepSpells = new ArrayList<Spell>();
	}

	public void addAttack(Attack attack) {
		attacks.add(attack);
	}

	public void addCounter(Counter counter) {
		counters.add(counter);
		Log.d("CharacterSheet", "Counters now size " + counters.size()
				+ " and are " + Arrays.toString(counters.toArray()));
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void addSkill(Skill skill) {
		skills.add(skill);
	}

	public float calculateTotalCarriedWeight() {
		float ret = 0;
		for (Item item : items) {
			ret += item.getWeight() * item.getQuantity();
		}
		// TODO determine how to factor in moneys
		return ret;
	}

	public void clearConditions() {
		mConditions = 0;
	}

	public int describeContents() {
		return 0;
	}

	public void disableConditions(int conditions) {
		mConditions &= ~conditions;
	}

	public void enableBlinded() {
		enableConditions(CONDITION_BLINDED);
	}

	public void enableConditions(int conditions) {
		mConditions |= conditions;
	}

	public void enableCowering() {
		enableConditions(CONDITION_COWERING);
	}

	public void enableDazzled() {
		enableConditions(CONDITION_DAZZLED);
	}

	public void enableEntangled() {
		enableConditions(CONDITION_ENTANGLED);
	}

	public void enableFlatFooted() {
		enableConditions(CONDITION_FLAT_FOOTED);
	}

	public void enableFrightened() {
		enableConditions(CONDITION_FRIGHTENED);
	}

	public void enableGrappling() {
		enableConditions(CONDITION_GRAPPLING);
	}

	public void enableHelpless() {
		enableConditions(CONDITION_HELPLESS);
	}

	public void enableInvisible() {
		enableConditions(CONDITION_INVISIBLE);
	}

	public void enablePinned() {
		enableConditions(CONDITION_PINNED);
	}

	public void enableProne() {
		enableConditions(CONDITION_PRONE);
	}

	public void enableShaken() {
		enableConditions(CONDITION_SHAKEN);
	}

	public void enableSqueezing() {
		enableConditions(CONDITION_SQUEEZING);
	}

	public void enableStunned() {
		enableConditions(CONDITION_STUNNED);
	}

	public AbilityScores getAbilities() {
		return abilities;
	}

	public AbilityScore getAbility(int ability) {
		switch (ability) {
		case ABILITY_CHA:
			return abilities.getCha();
		case ABILITY_CON:
			return abilities.getCon();
		case ABILITY_DEX:
			return abilities.getDex();
		case ABILITY_INT:
			return abilities.getInt();
		case ABILITY_STR:
			return abilities.getStr();
		case ABILITY_WIS:
			return abilities.getWis();
		default:
			return null;
		}
	}

	public ArmorClass getAC() {
		return ac;
	}

	public int getAge() {
		return characterAge;
	}

	public Attack getAttack(String name) {
		for (Attack a : attacks) {
			if (a.name.equals(name))
				return a;
		}
		return null;
	}

	public ArrayList<Attack> getAttackList() {
		return attacks;
	}

	public int getBAB() {
		return baseAttackBonus;
	}

	public String getBio() {
		return characterBio;
	}

	public int getCarriedLoad() {
		float weight = calculateTotalCarriedWeight();
		if (weight <= getLightCarryingCapacityMax())
			return LOAD_LIGHT;
		else if (weight <= getMediumCarryingCapacityMax())
			return LOAD_MEDIUM;
		else if (weight <= getHeavyCarryingCapacityMax())
			return LOAD_HEAVY;
		else
			return LOAD_OVERLIMIT;
	}

	public Feat getCFeat(String featName) {
		for (Feat f : cfeats) {
			if (f.getName().equals(featName))
				return f;
		}
		return null;
	}

	public ArrayList<Feat> getCFeatList() {
		return cfeats;
	}

	public int getCMB() {
		return cmb.getCMB(getBAB(), getSize(), abilities, classes);
	}

	public CMB getCMBParc() {
		return cmb;
	}

	public Counter getCounter(String counterName) {
		for (Counter c : counters) {
			if (c.getName().equals(counterName))
				return c;
		}
		return null;
	}

	public ArrayList<Counter> getCounterList() {
		return counters;
	}

	public Feat getFeat(String featName) {
		for (Feat f : feats) {
			if (f.getName().equals(featName))
				return f;
		}
		return null;
	}

	public ArrayList<Feat> getFeatList() {
		return feats;
	}

	public Save getFort() {
		return new Save(flagsFort, cModFort, mModFort);
	}

	public int getFortMod() {
		return mModFort;
	}

	public float getGold() {
		return characterGold;
	}

	public int getHealth() {
		return characterHealth;
	}

	public HP getHP() {
		return mHP;
	}

	public int getHPCurrent() {
		return hpCurrent;
	}

	public int getHPMax() {
		return mHP.getMaxHP(abilities.getCon().getTempModifier());
	}

	public int getHPRolled() {
		return mHP.getRolledHP();
	}

	public int getInitiative() {
		return miscInitBonus + getAbility(ABILITY_DEX).getTempModifier();
	}

	public Item getItem(String itemName) {
		for (Item i : items) {
			if (i.getName().equals(itemName))
				return i;
		}
		return null;
	}

	public ArrayList<Item> getItemList() {
		return items;
	}

	public String getLanguages() {
		return languages;
	}

	/**
	 * @deprecated Use <code>getPlayerClassString()</code> instead.
	 * @return
	 */
	@Deprecated
	public String getLevelString() {
		return getPlayerClassString();
		// return levelString;
	}

	public int getLightCarryingCapacityMax() {
		return getLightCarryingCapacityMax(getAbilities().getStr()
				.getBaseValue());
	}

	private static int getLightCarryingCapacityMax(int score) {
		switch (score) {
		case 1:
			return 3;
		case 2:
			return 6;
		case 3:
			return 10;
		case 4:
			return 13;
		case 5:
			return 16;
		case 6:
			return 20;
		case 7:
			return 23;
		case 8:
			return 26;
		case 9:
			return 30;
		case 10:
			return 33;
		case 11:
			return 38;
		case 12:
			return 43;
		case 13:
			return 50;
		case 14:
			return 58;
		case 15:
			return 66;
		case 16:
			return 76;
		case 17:
			return 86;
		case 18:
			return 100;
		case 19:
			return 116;
		case 20:
			return 133;
		case 21:
			return 153;
		case 22:
			return 173;
		case 23:
			return 200;
		case 24:
			return 233;
		case 25:
			return 266;
		case 26:
			return 306;
		case 27:
			return 346;
		case 28:
			return 400;
		case 29:
			return 466;
		default:
			return getLightCarryingCapacityMax(score - 10) * 4;
		}
	}

	public int getMediumCarryingCapacityMax() {
		return getMediumCarryingCapacityMax(getAbilities().getStr()
				.getBaseValue());
	}

	private static int getMediumCarryingCapacityMax(int score) {
		switch (score) {
		case 1:
			return 6;
		case 2:
			return 13;
		case 3:
			return 20;
		case 4:
			return 26;
		case 5:
			return 33;
		case 6:
			return 40;
		case 7:
			return 46;
		case 8:
			return 53;
		case 9:
			return 60;
		case 10:
			return 66;
		case 11:
			return 76;
		case 12:
			return 86;
		case 13:
			return 100;
		case 14:
			return 116;
		case 15:
			return 133;
		case 16:
			return 153;
		case 17:
			return 173;
		case 18:
			return 200;
		case 19:
			return 233;
		case 20:
			return 266;
		case 21:
			return 306;
		case 22:
			return 346;
		case 23:
			return 400;
		case 24:
			return 466;
		case 25:
			return 533;
		case 26:
			return 613;
		case 27:
			return 693;
		case 28:
			return 800;
		case 29:
			return 933;
		default:
			return getMediumCarryingCapacityMax(score - 10) * 4;
		}
	}

	public int getHeavyCarryingCapacityMax() {
		return getHeavyCarryingCapacityMax(getAbilities().getStr()
				.getBaseValue());
	}

	private static int getHeavyCarryingCapacityMax(int score) {
		switch (score) {
		case 1:
			return 10;
		case 2:
			return 20;
		case 3:
			return 30;
		case 4:
			return 40;
		case 5:
			return 50;
		case 6:
			return 60;
		case 7:
			return 70;
		case 8:
			return 80;
		case 9:
			return 90;
		case 10:
			return 100;
		case 11:
			return 115;
		case 12:
			return 130;
		case 13:
			return 150;
		case 14:
			return 175;
		case 15:
			return 200;
		case 16:
			return 230;
		case 17:
			return 260;
		case 18:
			return 300;
		case 19:
			return 350;
		case 20:
			return 400;
		case 21:
			return 460;
		case 22:
			return 520;
		case 23:
			return 600;
		case 24:
			return 700;
		case 25:
			return 800;
		case 26:
			return 920;
		case 27:
			return 1040;
		case 28:
			return 1200;
		case 29:
			return 1400;
		default:
			return getHeavyCarryingCapacityMax(score - 10) * 4;
		}
	}

	public int getMiscInitBonus() {
		return miscInitBonus;
	}

	public String getName() {
		return name;
	}

	public ArrayList<PlayerClass> getPlayerClasses() {
		return classes;
	}

	public String getPlayerClassString() {
		return PlayerClass.compoundToString(classes);
	}

	public ArrayList<Spell> getPrepSpells() {
		return prepSpells;
	}

	public Save getRef() {
		return new Save(flagsRef, cModRef, mModRef);
	}

	public int getRefMod() {
		return mModRef;
	}

	public int getSize() {
		return characterSize;
	}

	public Skill getSkillFromLongName(String name) {
		for (Skill s : skills) {
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}

	public ArrayList<Skill> getSkillList() {
		return skills;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}

	public int getTotalLevel() {
		int ret = 0;
		for (PlayerClass c : classes) {
			ret += c.getLevels();
		}
		/*
		 * for (String s : levelString.split("[\\s/]")) { try { ret +=
		 * Integer.parseInt(s); } catch (NumberFormatException ex) { } }
		 */
		return ret;
	}

	public Save getWill() {
		return new Save(flagsWill, cModWill, mModWill);
	}

	public int getWillMod() {
		return mModWill;
	}

	public int getXP() {
		return characterXP;
	}

	public boolean hasFeat(String featName) {
		for (Feat f : getFeatList()) {
			if (f.getName().equals(featName))
				return true;
		}
		return false;
	}

	public boolean isAffectedByConditions(int conditions) {
		return (mConditions & conditions) == conditions;
	}

	public boolean isBlinded() {
		return isAffectedByConditions(CONDITION_BLINDED);
	}

	public boolean isCowering() {
		return isAffectedByConditions(CONDITION_COWERING);
	}

	public boolean isDazzled() {
		return isAffectedByConditions(CONDITION_DAZZLED);
	}

	public boolean isEntangled() {
		return isAffectedByConditions(CONDITION_ENTANGLED);
	}

	public boolean isFlatFooted() {
		return isAffectedByConditions(CONDITION_FLAT_FOOTED);
	}

	public boolean isFrightened() {
		return isAffectedByConditions(CONDITION_FRIGHTENED);
	}

	public boolean isGrappling() {
		return isAffectedByConditions(CONDITION_GRAPPLING);
	}

	public boolean isHelpless() {
		return isAffectedByConditions(CONDITION_HELPLESS);
	}

	public boolean isInvisible() {
		return isAffectedByConditions(CONDITION_INVISIBLE);
	}

	public boolean isPinned() {
		return isAffectedByConditions(CONDITION_PINNED);
	}

	public boolean isProne() {
		return isAffectedByConditions(CONDITION_PRONE);
	}

	public boolean isShaken() {
		return isAffectedByConditions(CONDITION_SHAKEN);
	}

	public boolean isSqueezing() {
		return isAffectedByConditions(CONDITION_SQUEEZING);
	}

	public boolean isStunned() {
		return isAffectedByConditions(CONDITION_STUNNED);
	}

	public void removeAttack(Attack attack) {
		attacks.remove(attack);
	}

	public void removeCFeatByName(String featName) {
		Feat removeFeat = null;
		for (Feat f : cfeats)
			if (f.getName().equals(featName)) {
				removeFeat = f;
				break;
			}
		if (removeFeat != null)
			cfeats.remove(removeFeat);
	}

	public void removeCounter(Counter counter) {
		counters.remove(counter);
	}

	public void removeFeatByName(String featName) {
		Feat removeFeat = null;
		for (Feat f : feats)
			if (f.getName().equals(featName)) {
				removeFeat = f;
				break;
			}
		if (removeFeat != null)
			feats.remove(removeFeat);
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public void removeSkill(Skill skill) {
		skills.remove(skill);
	}

	public void saveSelfByPlayerList(Activity activity) {
		SharedPreferences playerList = activity.getSharedPreferences(
				CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
				Activity.MODE_PRIVATE);
		saveToSharedPreferences(playerList);
	}

	public void saveToSharedPreferences(SharedPreferences state) {
		SharedPreferences.Editor editor = state.edit();
		editor.putString(getName(), writeToJSON().toString());
		editor.commit();
	}

	public void setAbility(int ability, AbilityScore score) {
		abilities.setAbility(ability, score);
	}

	public void setAbility(int ability, int points) {
		abilities.getAbility(ability).setBaseValue(points);
	}

	public void setAC(ArmorClass ac) {
		this.ac = ac;
	}

	public void setAge(int age) {
		characterAge = age;
	}

	public void setBAB(int bab) {
		baseAttackBonus = bab;
	}

	public void setBio(String bio) {
		characterBio = bio;
	}

	public void setClassesByString(String levelString) {
		classes = PlayerClass.interpretListString(levelString);
	}

	public void setCMB(CMB cmb) {
		this.cmb = cmb;
	}

	public void setConditions(int conditions, boolean status) {
		if (status) {
			enableConditions(conditions);
		} else {
			disableConditions(conditions);
		}
	}

	public void setFort(Save fort) {
		flagsFort = fort.flags;
		cModFort = fort.classModifiers;
		mModFort = fort.miscModifiers;
	}

	public void setGold(float gold) {
		characterGold = gold;
	}

	public void setHealth(int health) {
		characterHealth = health;
	}

	public void setHP(HP hp) {
		mHP = hp;
	}

	public void setHPCurrent(int hp) {
		hpCurrent = hp;
	}

	/**
	 * @deprecated
	 * @param hp
	 */
	@Deprecated
	public void setHPRolled(int hp) {
		mHP.setMiscModifiers(hp);
	}

	public void setInitBonus(int newNumber) {
		miscInitBonus = newNumber;
	}

	public void setLanguages(String langs) {
		languages = langs;
	}

	/**
	 * @deprecated Use <code>setClassesByString()</code>
	 * @param levelString
	 */
	@Deprecated
	public void setLevelString(String levelString) {
		setClassesByString(levelString);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRef(Save ref) {
		flagsRef = ref.flags;
		cModRef = ref.classModifiers;
		mModRef = ref.miscModifiers;
	}

	public void setSize(int size) {
		characterSize = size;
	}

	public void setWill(Save will) {
		flagsWill = will.flags;
		cModWill = will.classModifiers;
		mModWill = will.miscModifiers;
	}

	public void setXP(int xp) {
		characterXP = xp;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Bundle b = new Bundle();
		b.putParcelable("abilities", abilities);
		b.putParcelableArrayList("mWieldableEquipment", mWieldableEquipment);
		b.putParcelableArrayList("counters", counters);
		b.putParcelableArrayList("cfeats", cfeats);
		b.putParcelableArrayList("feats", feats);
		b.putParcelableArrayList("items", items);
		b.putParcelableArrayList("classes", classes);
		b.putParcelableArrayList("skills", skills);
		b.putParcelableArrayList("spells", spells);
		b.putParcelableArrayList("prepSpells", prepSpells);
		dest.writeBundle(b);
		dest.writeParcelable(ac, 0);
		dest.writeParcelable(cmb, 0);
		dest.writeFloat(characterGold);
		dest.writeInt(baseAttackBonus);
		dest.writeInt(characterAge);
		dest.writeInt(characterHealth);
		dest.writeInt(characterSize);
		dest.writeInt(characterXP);
		dest.writeInt(flagsFort);
		dest.writeInt(flagsRef);
		dest.writeInt(flagsWill);
		dest.writeInt(hpCurrent);
		dest.writeParcelable(mHP, 0);
		dest.writeInt(miscInitBonus);
		dest.writeInt(cModFort);
		dest.writeInt(mModFort);
		dest.writeInt(cModRef);
		dest.writeInt(mModRef);
		dest.writeInt(cModWill);
		dest.writeInt(mModWill);
		dest.writeString(characterBio);
		dest.writeString(languages);
		dest.writeString(name);
	}

	public JSONObject writeToJSON() {
		Collections.sort(mWieldableEquipment);
		JSONObject ret = new JSONObject();
		try {
			ret.put("abilities", abilities.writeToJSON());
			ret.put("ac", ac.writeToJSON());
			// attacks deprecated
			ret.put("baseAttackBonus", baseAttackBonus);
			// cfeats handled below
			ret.put("characterAge", characterAge);
			ret.put("characterBio", characterBio);
			ret.put("characterGold", characterGold);
			ret.put("characterHealth", characterHealth);
			ret.put("characterSize", characterSize);
			ret.put("characterXP", characterXP);
			// classes handled below
			ret.put("cmb", cmb.writeToJSON());
			ret.put("cModFort", cModFort);
			ret.put("cModRef", cModRef);
			ret.put("cModWill", cModWill);
			// counters handled below
			// feats handled below
			ret.put("flagsFort", flagsFort);
			ret.put("flagsRef", flagsRef);
			ret.put("flagsWill", flagsWill);
			ret.put("hpCurrent", hpCurrent);
			ret.put("hpRolled", hpRolled);
			// items handled below
			ret.put("languages", languages);
			ret.put("mHP", mHP.writeToJSON());
			ret.put("miscInitBonus", miscInitBonus);
			ret.put("mModFort", mModFort);
			ret.put("mModRef", mModRef);
			ret.put("mModWill", mModWill);
			// mWieldableEquipment handled below
			ret.put("name", name);
			// prepSpells handled below
			// skills handled below
			// spells handled below

			// cfeats
			JSONArray cfts = new JSONArray();
			for (Feat cf : cfeats) {
				cfts.put(cf.writeToJSON());
			}
			ret.put("cfeats", cfts);
			// classes
			JSONArray cls = new JSONArray();
			for (PlayerClass cl : classes) {
				cls.put(cl.writeToJSON());
			}
			ret.put("classes", cls);
			// counters
			JSONArray ctrs = new JSONArray();
			for (Counter c : counters) {
				ctrs.put(c.writeToJSON());
			}
			ret.put("counters", ctrs);
			// feats
			JSONArray fts = new JSONArray();
			for (Feat f : feats) {
				fts.put(f.writeToJSON());
			}
			ret.put("feats", fts);
			// items
			JSONArray itms = new JSONArray();
			for (Item i : items) {
				itms.put(i.writeToJSON());
			}
			ret.put("items", itms);
			// prepSpells
			JSONArray pspls = new JSONArray();
			for (Spell ps : prepSpells) {
				pspls.put(ps.writeToJSON());
			}
			ret.put("prepSpells", pspls);
			// mWieldableEquipment
			JSONArray cgr = new JSONArray();
			for (WeapShield ws : mWieldableEquipment) {
				cgr.put(ws.writeToJSON());
			}
			ret.put("mWieldableEquipment", cgr);
			// skills
			JSONArray skls = new JSONArray();
			for (Skill s : skills) {
				skls.put(s.writeToJSON());
			}
			ret.put("skills", skls);
			// spells
			JSONArray spls = new JSONArray();
			for (Spell s : spells) {
				spls.put(s.writeToJSON());
			}
			ret.put("spells", spls);
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON from PlayerCharacter");
			return null;
		}
		return ret;
	}

	public static PlayerCharacter createFromJSON(JSONObject input) {
		try {
			PlayerCharacter ret = new PlayerCharacter();
			ret.abilities = AbilityScores.createFromJSON(input
					.getJSONObject("abilities"));
			ret.ac = ArmorClass.createFromJSON(input.getJSONObject("ac"));
			// attacks handled below
			ret.baseAttackBonus = input.getInt("baseAttackBonus");
			// cfeats handled below
			ret.characterAge = input.getInt("characterAge");
			ret.characterBio = input.getString("characterBio");
			ret.characterGold = (float) input.getDouble("characterGold");
			ret.characterHealth = input.getInt("characterHealth");
			ret.characterSize = input.getInt("characterSize");
			ret.characterXP = input.getInt("characterXP");
			// classes handled below
			ret.cmb = CMB.createFromJSON(input.getJSONObject("cmb"));
			ret.cModFort = input.getInt("cModFort");
			ret.cModRef = input.getInt("cModRef");
			ret.cModWill = input.getInt("cModWill");
			// counters handled below
			// feats handled below
			ret.flagsFort = input.getInt("flagsFort");
			ret.flagsRef = input.getInt("flagsRef");
			ret.flagsWill = input.getInt("flagsWill");
			ret.hpCurrent = input.getInt("hpCurrent");
			ret.hpRolled = input.getInt("hpRolled");
			// items handled below
			ret.languages = input.getString("languages");
			ret.mHP = HP.createFromJSON(input.getJSONObject("mHP"));
			ret.miscInitBonus = input.getInt("miscInitBonus");
			ret.mModFort = input.getInt("mModFort");
			ret.mModRef = input.getInt("mModRef");
			ret.mModWill = input.getInt("mModWill");
			// mWieldableEquipment handled below
			ret.name = input.getString("name");
			// prepSpells handled below
			// skills handled below
			// spells handled below

			try {
				// inflate mWieldableEquipment
				// before attacks for now to transition previous development
				// saves
				JSONArray gear = input.getJSONArray("mWieldableEquipment");
				for (int i = 0; i < gear.length(); ++i) {
					ret.mWieldableEquipment.add(WeapShield.createFromJSON(gear
							.getJSONObject(i)));
				}
			} catch (JSONException ex) {
				// might not exist
			}
			boolean needsEquip = ret.mWieldableEquipment.isEmpty();

			try {
				// inflate attacks
				JSONArray attacks = input.getJSONArray("attacks");
				for (int i = 0; i < attacks.length(); ++i) {
					Attack a = Attack.createFromJSON(attacks.getJSONObject(i));
					ret.attacks.add(a);
					if (needsEquip)
						ret.mWieldableEquipment.add(new WeapShield(a));
				}
			} catch (JSONException ex) {
				// might not exist
			}

			// convert AC shield bonus to wieldable shield
			if (ret.ac.shieldBonus > 0) {
				WeapShield asdf = new WeapShield("Shield", ABILITY_STR,
						ABILITY_STR, 0, "", 0, ret.ac.shieldBonus);
				asdf.setDescription("Automatically generated.");
				ret.mWieldableEquipment.add(asdf);
				ret.ac.shieldBonus = 0;
			}

			// inflate cfeats
			JSONArray cfeats = input.getJSONArray("cfeats");
			for (int i = 0; i < cfeats.length(); ++i) {
				ret.cfeats.add(Feat.createFromJSON(cfeats.getJSONObject(i)));
			}
			// inflate classes
			JSONArray classes = input.getJSONArray("classes");
			for (int i = 0; i < classes.length(); ++i) {
				ret.classes.add(PlayerClass.createFromJSON(classes
						.getJSONObject(i)));
			}
			// inflate counters
			JSONArray counters = input.getJSONArray("counters");
			for (int i = 0; i < counters.length(); ++i) {
				ret.counters.add(Counter.createFromJSON(counters
						.getJSONObject(i)));
			}
			// inflate feats
			JSONArray feats = input.getJSONArray("feats");
			for (int i = 0; i < feats.length(); ++i) {
				ret.feats.add(Feat.createFromJSON(feats.getJSONObject(i)));
			}
			// inflate items
			JSONArray items = input.getJSONArray("items");
			for (int i = 0; i < items.length(); ++i) {
				ret.items.add(Item.createFromJSON(items.getJSONObject(i)));
			}
			// inflate prepSpells
			JSONArray prepSpells = input.getJSONArray("prepSpells");
			for (int i = 0; i < prepSpells.length(); ++i) {
				ret.prepSpells.add(Spell.createFromJSON(prepSpells
						.getJSONObject(i)));
			}
			// inflate skills
			JSONArray skills = input.getJSONArray("skills");
			for (int i = 0; i < skills.length(); ++i) {
				ret.skills.add(Skill.createFromJSON(skills.getJSONObject(i)));
			}
			// inflate spells
			JSONArray spells = input.getJSONArray("spells");
			for (int i = 0; i < spells.length(); ++i) {
				ret.spells.add(Spell.createFromJSON(spells.getJSONObject(i)));
			}
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating PlayerCharacter from JSON");
			ex.printStackTrace();
			return null;
		}
	}

	public ArrayList<WeapShield> getWieldableEquipment() {
		return mWieldableEquipment;
	}
}
