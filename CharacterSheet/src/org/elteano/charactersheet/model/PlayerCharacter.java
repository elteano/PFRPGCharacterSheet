package org.elteano.charactersheet.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

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
	public static Parcelable.Creator<PlayerCharacter> CREATOR = new Parcelable.Creator<PlayerCharacter>() {

		public PlayerCharacter createFromParcel(Parcel src) {
			PlayerCharacter ret = new PlayerCharacter();
			Bundle b = src.readBundle();
			b.setClassLoader(AbilityScores.class.getClassLoader());
			ret.abilities = b.getParcelable("abilities");
			b.setClassLoader(Attack.class.getClassLoader());
			ret.attacks = b.getParcelableArrayList("attacks");
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
	private int miscInitBonus;
	private HP mHP;
	private int mModFort;
	private int mModRef;
	private int mModWill;
	private String name;
	private ArrayList<Spell> prepSpells;
	private ArrayList<Skill> skills;
	private ArrayList<Spell> spells;

	public PlayerCharacter() {
		abilities = new AbilityScores();
		ac = new ArmorClass(0, 0, 0, 0, 0, true);
		name = "New Character";
		characterAge = 0;
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

	public int describeContents() {
		return 0;
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
			if (s.skillName.equals(name))
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
		b.putParcelableArrayList("attacks", attacks);
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
		JSONObject ret = new JSONObject();
		try {
			ret.put("abilities", abilities.writeToJSON());
			ret.put("ac", ac.writeToJSON());
			// attacks handled below
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
			ret.put("name", name);
			// prepSpells handled below
			// skills handled below
			// spells handled below

			// attacks
			JSONArray atk = new JSONArray();
			for (Attack a : attacks) {
				atk.put(a.writeToJSON());
			}
			ret.put("attacks", atk);
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
			ret.name = input.getString("name");
			// prepSpells handled below
			// skills handled below
			// spells handled below

			// inflate attacks
			JSONArray attacks = input.getJSONArray("attacks");
			for (int i = 0; i < attacks.length(); ++i) {
				ret.attacks
						.add(Attack.createFromJSON(attacks.getJSONObject(i)));
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
}
