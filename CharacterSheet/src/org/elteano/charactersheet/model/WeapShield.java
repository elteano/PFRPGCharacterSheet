package org.elteano.charactersheet.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Object representing a wieldable item, typically a weapon or shield.
 *
 * This is the replacement for the current Attack system, and will also replace
 * the shield bonus section of AC.
 */
public class WeapShield implements Parcelable, Comparable<WeapShield> {

	public static final int MODIFIER_CHARGE = 1;
	public static final int MODIFIER_DEFENSIVE = 2;
	public static final int MODIFIER_EXPERTISE = 4;
	public static final int MODIFIER_TWO_HANDED = 8;
	public static final int MODIFIER_TWO_HANDED_HAS_FEAT = 16;
	public static final int MODIFIER_TWO_HANDED_IS_OFFHAND = 32;
	public static final int MODIFIER_TWO_HANDED_OFFHAND_LIGHT = 64;

	public static final Parcelable.Creator<WeapShield> CREATOR = new Parcelable.Creator<WeapShield>() {

		public WeapShield createFromParcel(Parcel source) {
			WeapShield ret = new WeapShield("");
			ret.setACBonus(source.readInt());
			ret.setAttackAbility(source.readInt());
			ret.setAttackBonus(source.readInt());
			ret.setDamageAbility(source.readInt());
			ret.setDamageBonus(source.readInt());
			ret.setName(source.readString());
			ret.setWeaponType(source.readInt());
			return ret;
		}

		public WeapShield[] newArray(int size) {
			return new WeapShield[size];
		}
	};

	public static WeapShield createFromJSON(JSONObject source) {
		WeapShield ret = new WeapShield("");
		try {
			ret.setACBonus(source.getInt("mACBonus"));
			ret.setAttackAbility(source.getInt("mAttackAbility"));
			ret.setAttackBonus(source.getInt("mAttackBonus"));
			ret.setDamageAbility(source.getInt("mDamageAbility"));
			ret.setDamageBonus(source.getInt("mDamageBonus"));
			ret.setDamageDie(source.getString("mDamageDie"));
			ret.setDescription(source.getString("mDescription"));
			ret.setName(source.getString("mName"));
			ret.setWeaponType(source.getInt("mType"));
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating WeapShield from JSON.");
			ex.printStackTrace();
		}
		return ret;
	}

	/**
	 * Type value corresponding to light weapons.
	 *
	 * @see getWeaponType()
	 * @see setWeaponType()
	 */
	public static final int TYPE_LIGHT = 0;
	/**
	 * Type value corresponding to one-handed weapons.
	 *
	 * @see getWeaponType()
	 * @see setWeaponType()
	 */
	public static final int TYPE_ONE_HANDED = 1;
	/**
	 * Type value corresponding to ranged weapons.
	 *
	 * @see getWeaponType()
	 * @see setWeaponType()
	 */
	public static final int TYPE_RANGED = 3;
	/**
	 * Type value corresponding to two-handed weapons.
	 *
	 * @see getWeaponType()
	 * @see setWeaponType()
	 */
	public static final int TYPE_TWO_HANDED = 2;

	/**
	 * Armor class bonus granted by this item. This is nullified if using this
	 * item as a weapon.
	 */
	private int mACBonus;
	/**
	 * Ability used to determine attack bonus while using this item.
	 */
	private int mAttackAbility;
	/**
	 * Intrinsic bonus to attack rolls granted by using this weapon.
	 */
	private int mAttackBonus;
	/**
	 * Ability used to determine the damage bonus while using this item.
	 */
	private int mDamageAbility;
	/**
	 * Intrinsic bonus to damage rolls granted by using this weapon.
	 */
	private int mDamageBonus;
	/**
	 * Die to roll when rolling damage with this weapon.
	 */
	private String mDamageDie;
	/**
	 * Description of the miscellaneous errata regarding the gear piece.
	 */
	private String mDescription;
	/**
	 * The item's name.
	 */
	private String mName;
	/**
	 * The wielding type of the weapon.
	 *
	 * Possible values may be TYPE_LIGHT, TYPE_ONE_HANDED, TYPE_TWO_HANDED, and
	 * TYPE_RANGED. Shields are considered light weapons or one-handed weapons,
	 * as per shield descriptions.
	 */
	private int mType;

	/**
	 * Conversion constructor that turns an Attack into its WeapShield
	 * equivalent.
	 */
	public WeapShield(Attack attack) {
		setName(attack.name);
		setAttackAbility(attack.baseAttackAbility);
		setAttackBonus(attack.addAttack);
		setDamageAbility(attack.baseDamageAbility);
		setDamageBonus(attack.addDamage);
		setDamageDie(attack.damageDie);
		setDescription(attack.description);
		setACBonus(0);
		mDescription = "";
	}

	public WeapShield(String name) {
		this(name, PlayerCharacter.ABILITY_STR, PlayerCharacter.ABILITY_STR, 0,
				"", 0, 0);
	}

	public WeapShield(String name, int attackAbility, int damageAbility,
			int attackBonus, String damageDie, int damageBonus, int acBonus) {
		setName(name);
		setAttackAbility(attackAbility);
		setDamageAbility(damageAbility);
		setAttackBonus(attackBonus);
		setDamageBonus(damageBonus);
		setDamageDie(damageDie);
		setACBonus(acBonus);
		mDescription = "";
	}

	public String calculateAttack(PlayerCharacter c, int otherModifiers) {
		int conditionModifiers = calculateModifiersFromCodes(c, otherModifiers);
		int bab = c.getBAB();
		String ret = "";
		if (c.isDazzled())
			conditionModifiers -= 1;
		if (c.isEntangled()) {
			conditionModifiers -= 2;
		}
		if (c.isInvisible()) {
			conditionModifiers += 2;
		}
		if (c.isProne()) {
			conditionModifiers -= 4;
		}
		if (c.isShaken() || c.isFrightened()) {
			conditionModifiers -= 2;
		}
		if (c.isSqueezing()) {
			conditionModifiers -= 4;
		}
		do {
			int curMod = bab + conditionModifiers
					+ c.getAbility(mAttackAbility).getTempModifier();
			ret += ((curMod >= 0) ? "+" : "") + curMod + " / ";
			bab -= 5;
		} while (bab > 0);
		return ret.substring(0, ret.length() - 3);
	}

	private int calculateModifiersFromCodes(PlayerCharacter c, int modifierCodes) {
		int ret = 0;
		if ((modifierCodes & MODIFIER_CHARGE) == MODIFIER_CHARGE) {
			ret += 2;
		}
		if ((modifierCodes & MODIFIER_DEFENSIVE) == MODIFIER_DEFENSIVE) {
			ret -= 4;
		}
		if ((modifierCodes & MODIFIER_EXPERTISE) == MODIFIER_EXPERTISE) {
			ret -= 1 + c.getBAB() / 4;
		}
		if ((modifierCodes & MODIFIER_TWO_HANDED) == MODIFIER_TWO_HANDED) {
			boolean hasFeat = (modifierCodes & MODIFIER_TWO_HANDED_HAS_FEAT) == MODIFIER_TWO_HANDED_HAS_FEAT;
			boolean offLight = (modifierCodes & MODIFIER_TWO_HANDED_OFFHAND_LIGHT) == MODIFIER_TWO_HANDED_OFFHAND_LIGHT;
			if (hasFeat) {
				if (offLight)
					ret -= 2;
				else
					ret -= 4;
			} else {
				boolean isOff = (modifierCodes & MODIFIER_TWO_HANDED_IS_OFFHAND) == MODIFIER_TWO_HANDED_IS_OFFHAND;
				if (offLight) {
					if (isOff)
						ret -= 8;
					else
						ret -= 4;
				} else {
					if (isOff)
						ret -= 10;
					else
						ret -= 6;
				}
			}
		}
		return ret;
	}

	public int describeContents() {
		return 0;
	}

	public int getACBonus() {
		return mACBonus;
	}

	public int getAttackAbility() {
		return mAttackAbility;
	}

	public int getAttackBonus() {
		return mAttackBonus;
	}

	public int getDamageAbility() {
		return mDamageAbility;
	}

	public int getDamageBonus() {
		return mDamageBonus;
	}

	public String getDamageDie() {
		return mDamageDie;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getName() {
		return mName;
	}

	public int getWeaponType() {
		return mType;
	}

	public void setACBonus(int acBonus) {
		mACBonus = acBonus;
	}

	public void setAttackAbility(int attackAbility) {
		mAttackAbility = attackAbility;
	}

	public void setAttackBonus(int attackBonus) {
		this.mAttackBonus = attackBonus;
	}

	public void setDamageAbility(int damageAbility) {
		this.mDamageAbility = damageAbility;
	}

	/**
	 * Sets the intrinsic damage bonus provided by using this weapon.
	 *
	 * @param damageBonus
	 */
	public void setDamageBonus(int damageBonus) {
		this.mDamageBonus = damageBonus;
	}

	public void setDamageDie(String damageDie) {
		mDamageDie = damageDie;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	/**
	 * Sets the item's name.
	 *
	 * @param name
	 *            The item's new name.
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * Sets the wielding type of the weapon.
	 *
	 * Possible values may be TYPE_LIGHT, TYPE_ONE_HANDED, TYPE_TWO_HANDED, and
	 * TYPE_RANGED. Shields are considered light weapons or one-handed weapons,
	 * as per shield descriptions.
	 *
	 * @param type
	 *            The weapon's new wielding type.
	 */
	public void setWeaponType(int type) {
		this.mType = type;
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Parcelable stuff.
	 */
	public void writeToParcel(Parcel dest, int ignore) {
		dest.writeInt(getACBonus());
		dest.writeInt(getAttackAbility());
		dest.writeInt(getAttackBonus());
		dest.writeInt(getDamageAbility());
		dest.writeInt(getDamageBonus());
		dest.writeString(getName());
		dest.writeString(getDamageDie());
		dest.writeInt(getWeaponType());
	}

	public JSONObject writeToJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("mACBonus", getACBonus());
			ret.put("mAttackAbility", getAttackAbility());
			ret.put("mAttackBonus", getAttackBonus());
			ret.put("mDamageAbility", getDamageAbility());
			ret.put("mDamageBonus", getDamageBonus());
			ret.put("mDamageDie", getDamageDie());
			ret.put("mDescription", getDescription());
			ret.put("mName", getName());
			ret.put("mType", getWeaponType());
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON from WeapShield.");
			ex.printStackTrace();
		}
		return ret;
	}

	public int compareTo(WeapShield another) {
		return getName().compareTo(another.getName());
	}
}
