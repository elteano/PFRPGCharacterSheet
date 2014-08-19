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
public class WeapShield implements Parcelable {

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
}
