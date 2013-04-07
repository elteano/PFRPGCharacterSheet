package org.elteano.charactersheet;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Attack implements Parcelable {

	public int addAttack;
	public int addDamage;
	public int baseAttackAbility;
	public int baseDamageAbility;
	public String damageDie;
	public String description;
	public String name;

	public static final Parcelable.Creator<Attack> CREATOR = new Parcelable.Creator<Attack>() {

		public Attack createFromParcel(Parcel source) {
			return new Attack(source.readString(), source.readInt(),
					source.readInt(), source.readInt(), source.readInt(),
					source.readString(), source.readString());
		}

		public Attack[] newArray(int size) {
			return new Attack[size];
		}
	};

	public Attack(String name, int baseAttackAbility, int baseDamageAbility,
			int additionalAttackMod, int additionalDamageMod, String damageDie,
			String desc) {
		this.name = name;
		this.baseAttackAbility = baseAttackAbility;
		this.baseDamageAbility = baseDamageAbility;
		addAttack = additionalAttackMod;
		addDamage = additionalDamageMod;
		this.damageDie = damageDie;
		description = desc;
	}

	public static Attack constructFromString(String string) {
		String[] split = string.split(PlayerCharacter.SPLITTER_SMALL);
		if (split.length == 7) {
			return new Attack(split[0], Integer.parseInt(split[1]),
					Integer.parseInt(split[2]), Integer.parseInt(split[3]),
					Integer.parseInt(split[4]), split[5], split[6]);
		} else
			Log.d("CharacterSheet", "Wrong split length:  " + string);
		return null;
	}

	public int describeContents() {
		return 0;
	}

	public String toDescriptionString(AbilityScore[] abilities, int bab) {
		int attackTotal = addAttack
				+ abilities[baseAttackAbility].getTempModifier() + bab;
		String attackBonus = (attackTotal <= 0) ? "" : "+";
		attackBonus += attackTotal;
		int damageTotal = getDamageMod(abilities);
		String damageBonus = ((damageTotal > 0) ? "+" : "") + damageTotal;
		return String.format("%s %s (%s %s)", name, attackBonus, damageDie,
				damageBonus);
	}

	public String toStorageString() {
		if (description.isEmpty())
			description = " ";
		return String.format(Locale.getDefault(),
				"%1$s%7$s%2$d%7$s%3$d%7$s%4$d%7$s%5$d%7$s%6$s%7$s%8$s", name,
				baseAttackAbility, baseDamageAbility, addAttack, addDamage,
				damageDie, PlayerCharacter.SPLITTER_SMALL, description);
	}

	public void writeToParcel(Parcel out, int wut) {
		out.writeString(name);
		out.writeInt(baseAttackAbility);
		out.writeInt(baseDamageAbility);
		out.writeInt(addAttack);
		out.writeInt(addDamage);
		out.writeString(damageDie);
		out.writeString(description);
	}

	private int getDamageMod(AbilityScore[] abilities) {
		return addDamage
				+ ((baseDamageAbility != 6) ? abilities[baseDamageAbility]
						.getTempModifier() : 0);
	}
}
