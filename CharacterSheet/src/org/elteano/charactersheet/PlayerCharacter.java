package org.elteano.charactersheet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;

public class PlayerCharacter {

	public static final int ABILITY_CHA = 0;
	public static final int ABILITY_CON = 1;
	public static final int ABILITY_DEX = 2;
	public static final int ABILITY_INT = 3;
	public static final int ABILITY_STR = 4;
	public static final int ABILITY_WIS = 5;
	public static final int SIZE_FINE = 0;
	public static final int SIZE_DIMINUTIVE = 1;
	public static final int SIZE_TINY = 2;
	public static final int SIZE_SMALL = 3;
	public static final int SIZE_MEDIUM = 4;
	public static final int SIZE_LARGE = 5;
	public static final int SIZE_HUGE = 6;
	public static final int SIZE_GARGANTUAN = 7;
	public static final int SIZE_COLOSSAL = 8;
	public static final String SPLITTER_LARGE = "#7SPACE7#";
	public static final String SPLITTER_SMALL = ":1space4:";
	// Must remain public for NameFragment
	public static final String SAVESTATE_NAME = "org.elteano.charactersheet.Character.name";
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
	private static final String SAVESTATE_FEATS = PlayerCharacter.class
			.getCanonicalName() + ".feats";
	private static final String SAVESTATE_FORT_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".fortFlags";
	private static final String SAVESTATE_FORT_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".fortCMod";
	private static final String SAVESTATE_FORT_MOD = PlayerCharacter.class
			.getCanonicalName() + ".fortMod";
	private static final String SAVESTATE_GOLD = PlayerCharacter.class
			.getCanonicalName() + ".gold";
	private static final String SAVESTATE_HP_CURRENT = PlayerCharacter.class
			.getCanonicalName() + ".hpC";
	private static final String SAVESTATE_HP_ROLLED = PlayerCharacter.class
			.getCanonicalName() + ".hpR";
	private static final String SAVESTATE_INIT_BONUS = PlayerCharacter.class
			.getCanonicalName() + ".initBonus";
	private static final String SAVESTATE_ITEMS = PlayerCharacter.class
			.getCanonicalName() + ".items";
	private static final String SAVESTATE_LANGUAGES = PlayerCharacter.class
			.getCanonicalName() + ".languages";
	private static final String SAVESTATE_LEVEL = PlayerCharacter.class
			.getCanonicalName() + ".levelString";
	private static final String SAVESTATE_PREP_SPELLS = PlayerCharacter.class
			.getCanonicalName() + ".prepSpells";
	private static final String SAVESTATE_REF_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".reflexFlags";
	private static final String SAVESTATE_REF_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".reflexCMod";
	private static final String SAVESTATE_REF_MOD = PlayerCharacter.class
			.getCanonicalName() + ".reflexMod";
	private static final String SAVESTATE_SIZE = PlayerCharacter.class
			.getCanonicalName() + ".size";
	private static final String SAVESTATE_SKILLS = PlayerCharacter.class
			.getCanonicalName() + ".skills";
	private static final String SAVESTATE_SPELLS = PlayerCharacter.class
			.getCanonicalName() + ".spells";
	private static final String SAVESTATE_WILL_FLAGS = PlayerCharacter.class
			.getCanonicalName() + ".willFlags";
	private static final String SAVESTATE_WILL_CMOD = PlayerCharacter.class
			.getCanonicalName() + ".willCMod";
	private static final String SAVESTATE_WILL_MOD = PlayerCharacter.class
			.getCanonicalName() + ".willMod";
	private static final String SAVESTATE_XP = PlayerCharacter.class
			.getCanonicalName() + ".xp";
	private AbilityScore[] abilities;
	private ArmorClass ac;
	private ArrayList<Attack> attacks;
	private ArrayList<Feat> cfeats;
	private ArrayList<Feat> feats;
	private ArrayList<Item> items;
	private ArrayList<Spell> prepSpells;
	private ArrayList<Skill> skills;
	private ArrayList<Spell> spells;
	private float characterGold;
	private int baseAttackBonus;
	private int characterAge;
	private int characterHealth;
	private int characterSize;
	private int characterXP;
	private int flagsFort;
	private int flagsRef;
	private int flagsWill;
	private int hpCurrent;
	private int hpRolled;
	private int miscInitBonus;
	private int cModFort;
	private int mModFort;
	private int cModRef;
	private int mModRef;
	private int cModWill;
	private int mModWill;
	private String characterBio;
	private String languages;
	private String levelString;
	private String name;

	public PlayerCharacter() {
		abilities = new AbilityScore[6];
		ac = new ArmorClass(0, 0, 0, 0, true);
		name = "New Character";
		characterAge = 0;
		characterSize = SIZE_MEDIUM;
		characterBio = "";
		levelString = "";
		flagsFort = Save.FLAG_CON;
		flagsRef = Save.FLAG_DEX;
		flagsWill = Save.FLAG_WIS;
		attacks = new ArrayList<Attack>();
		feats = new ArrayList<Feat>();
		cfeats = new ArrayList<Feat>();
		items = new ArrayList<Item>();
		skills = new ArrayList<Skill>();
		spells = new ArrayList<Spell>();
		prepSpells = new ArrayList<Spell>();
		for (int i = 0; i < 6; i++) {
			abilities[i] = new AbilityScore(10);
		}
	}

	public void addAttack(Attack attack) {
		attacks.add(attack);
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

	public AbilityScore getAbility(int ability) {
		try {
			return abilities[ability];
		} catch (IndexOutOfBoundsException ex) {
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

	public int getCMB() {
		return baseAttackBonus + getAbility(ABILITY_STR).getTempModifier()
				- ArmorClass.getSizeModifier();
	}

	public Feat getFeat(String featName) {
		for (Feat f : feats) {
			if (f.getName().equals(featName))
				return f;
		}
		return null;
	}
	
	public Feat getCFeat(String featName) {
		for (Feat f : cfeats) {
			if (f.getName().equals(featName))
				return f;
		}
		return null;
	}

	public ArrayList<Feat> getFeatList() {
		return feats;
	}
	
	public ArrayList<Feat> getCFeatList() {
		return cfeats;
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

	public int getHPCurrent() {
		return hpCurrent;
	}

	public int getHPMax() {
		return hpRolled + getTotalLevel()
				* getAbility(ABILITY_CON).getTempModifier();
	}

	public int getHPRolled() {
		return hpRolled;
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

	public String getLevelString() {
		return levelString;
	}

	public int getMiscInitBonus() {
		return miscInitBonus;
	}

	public String getName() {
		return name;
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
		for (String s : levelString.split("[\\s/]")) {
			try {
				ret += Integer.parseInt(s);
			} catch (NumberFormatException ex) {
			}
		}
		return ret;
	}

	public int getXP() {
		return characterXP;
	}

	public Save getWill() {
		return new Save(flagsWill, cModWill, mModWill);
	}

	public int getWillMod() {
		return mModWill;
	}

	public void removeAttack(Attack attack) {
		attacks.remove(attack);
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

	public void removeItem(Item item) {
		items.remove(item);
	}

	public void removeSkill(Skill skill) {
		skills.remove(skill);
	}

	public static PlayerCharacter restoreFromSharedPreferences(
			SharedPreferences state) {
		PlayerCharacter ret = new PlayerCharacter();
		ret.ac = ArmorClass.fromSaveString(state.getString(SAVESTATE_AC, ""));
		ret.hpCurrent = state.getInt(SAVESTATE_HP_CURRENT, 0);
		ret.hpRolled = state.getInt(SAVESTATE_HP_ROLLED, 0);
		ret.languages = state.getString(SAVESTATE_LANGUAGES, "");
		ret.miscInitBonus = state.getInt(SAVESTATE_INIT_BONUS, 0);
		ret.setAge(state.getInt(SAVESTATE_AGE, 0));
		ret.setBAB(state.getInt(SAVESTATE_BAB, 0));
		ret.setBio(state.getString(SAVESTATE_BIO, "No bio found."));
		ret.setGold(state.getFloat(SAVESTATE_GOLD, 0));
		ret.setLevelString(state.getString(SAVESTATE_LEVEL, "error - 0"));
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
		String itemString = state.getString(SAVESTATE_ITEMS, "");
		if (!itemString.isEmpty())
			for (String item : itemString.split(SPLITTER_LARGE))
				ret.items.add(Item.fromSaveString(item));
		String s = state.getString(SAVESTATE_SPELLS, "");
		if (!s.isEmpty())
			for (String spellString : s.split(SPLITTER_LARGE)) {
				ret.spells.add(Spell.fromSaveString(spellString));
			}
		s = state.getString(SAVESTATE_PREP_SPELLS, "");
		if (!s.isEmpty())
			for (String spellString : s.split(SPLITTER_LARGE)) {
				ret.prepSpells.add(Spell.fromSaveString(spellString));
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

	public void saveToSharedPreferences(SharedPreferences state) {
		SharedPreferences.Editor editor = state.edit();
		editor.clear();
		editor.putString(SAVESTATE_AC, ac.toSaveString());
		editor.putInt(SAVESTATE_AGE, characterAge);
		editor.putInt(SAVESTATE_BAB, baseAttackBonus);
		editor.putString(SAVESTATE_BIO, characterBio);
		editor.putFloat(SAVESTATE_GOLD, characterGold);
		editor.putInt(SAVESTATE_HP_CURRENT, hpCurrent);
		editor.putInt(SAVESTATE_HP_ROLLED, hpRolled);
		editor.putString(SAVESTATE_LEVEL, levelString);
		editor.putString(SAVESTATE_NAME, name);
		editor.putInt(SAVESTATE_SIZE, characterSize);
		editor.putInt(SAVESTATE_XP, characterXP);
		editor.putInt(SAVESTATE_FORT_FLAGS, flagsFort);
		editor.putInt(SAVESTATE_FORT_CMOD, cModFort);
		editor.putInt(SAVESTATE_INIT_BONUS, miscInitBonus);
		editor.putInt(SAVESTATE_FORT_MOD, mModFort);
		editor.putInt(SAVESTATE_REF_FLAGS, flagsRef);
		editor.putString(SAVESTATE_LANGUAGES, languages);
		editor.putInt(SAVESTATE_REF_CMOD, cModRef);
		editor.putInt(SAVESTATE_REF_MOD, mModRef);
		editor.putInt(SAVESTATE_WILL_FLAGS, flagsWill);
		editor.putInt(SAVESTATE_WILL_CMOD, cModWill);
		editor.putInt(SAVESTATE_WILL_MOD, mModWill);
		for (int i = 0; i < 6; i++) {
			abilities[i].appendToSharedPreferencesEditor(editor, i);
		}
		String featString = "";
		for (Feat feat : feats) {
			featString += feat.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_FEATS, featString);
		String cFeatString = "";
		for (Feat feat : cfeats) {
			cFeatString += feat.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_CFEATS, cFeatString);
		String itemString = "";
		for (Item item : items) {
			itemString += item.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_ITEMS, itemString);
		String skillString = "";
		for (Skill s : skills) {
			skillString += s.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_SKILLS, skillString);
		String attackString = "";
		for (Attack attack : attacks) {
			attackString += attack.toStorageString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_ATTACKS, attackString);
		String spellString = "";
		for (Spell s : spells) {
			spellString += s.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_SPELLS, spellString);
		spellString = "";
		for (Spell s : prepSpells) {
			spellString += s.toSaveString() + SPLITTER_LARGE;
		}
		editor.putString(SAVESTATE_PREP_SPELLS, spellString);
		editor.commit();
	}

	public void saveSelfByPlayerList(Activity activity) {
		SharedPreferences playerList = activity.getSharedPreferences(
				CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
				Activity.MODE_PRIVATE);
		for (String key : playerList.getAll().keySet()) {
			if (playerList.getString(key, "basaoiunfalksjdbfioaweubf").equals(
					getName())) {
				saveToSharedPreferences(activity.getSharedPreferences(key,
						Activity.MODE_PRIVATE));
				break;
			}
		}
	}

	public void setAbility(int ability, int points) {
		abilities[ability].setBaseValue(points);
	}

	public void setAbility(int ability, AbilityScore score) {
		abilities[ability] = score;
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

	public void setHPCurrent(int hp) {
		hpCurrent = hp;
	}

	public void setHPRolled(int hp) {
		hpRolled = hp;
	}

	public void setLanguages(String langs) {
		languages = langs;
	}

	public void setLevelString(String levelString) {
		this.levelString = levelString;
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

	public void setInitBonus(int newNumber) {
		miscInitBonus = newNumber;
	}
}
