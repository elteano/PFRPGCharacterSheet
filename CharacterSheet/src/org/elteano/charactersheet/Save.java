package org.elteano.charactersheet;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Save implements Parcelable {

	public static final int FLAG_NONE = 0;
	public static final int FLAG_CHA = 1;
	public static final int FLAG_CON = 2;
	public static final int FLAG_DEX = 4;
	public static final int FLAG_STR = 8;
	public static final int FLAG_INT = 16;
	public static final int FLAG_WIS = 32;
	public static final int FLAG_ALL = 63;
	public int flags;
	public int classModifiers;
	public int miscModifiers;

	public static final Parcelable.Creator<Save> CREATOR = new Parcelable.Creator<Save>() {

		public Save createFromParcel(Parcel source) {
			return new Save(source.readInt(), source.readInt(),
					source.readInt());
		}

		public Save[] newArray(int size) {
			return new Save[size];
		}
	};

	public Save(int flags, int classMods, int miscModifiers) {
		this.flags = flags;
		classModifiers = classMods;
		this.miscModifiers = miscModifiers;
	}

	public int describeContents() {
		return 0;
	}

	public int getTotal(AbilityScores abilities) {
		int ret = miscModifiers + classModifiers;
		if ((flags & FLAG_CHA) == FLAG_CHA)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_CHA)
					.getTempModifier();
		if ((flags & FLAG_CON) == FLAG_CON)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_CON)
					.getTempModifier();
		if ((flags & FLAG_DEX) == FLAG_DEX)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_DEX)
					.getTempModifier();
		if ((flags & FLAG_INT) == FLAG_INT)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_INT)
					.getTempModifier();
		if ((flags & FLAG_STR) == FLAG_STR)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_STR)
					.getTempModifier();
		if ((flags & FLAG_WIS) == FLAG_WIS)
			ret += abilities.getAbility(PlayerCharacter.ABILITY_WIS)
					.getTempModifier();
		return ret;
	}

	public void setCha(boolean choose) {
		if (choose) {
			flags |= FLAG_CHA;
		} else {
			flags &= ~FLAG_CHA;
		}
	}

	public void setCon(boolean choose) {
		if (choose) {
			flags |= FLAG_CON;
		} else {
			flags &= ~FLAG_CON;
		}
	}

	public void setDex(boolean choose) {
		if (choose) {
			flags |= FLAG_DEX;
		} else {
			flags &= ~FLAG_DEX;
		}
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setInt(boolean choose) {
		if (choose) {
			flags |= FLAG_INT;
		} else {
			flags &= ~FLAG_INT;
		}
	}

	public void setClassModifier(int mod) {
		classModifiers = mod;
	}

	public void setMiscModifier(int mod) {
		miscModifiers = mod;
	}

	public void setStr(boolean choose) {
		if (choose) {
			flags |= FLAG_STR;
		} else {
			flags &= ~FLAG_STR;
		}
	}

	public void setWis(boolean choose) {
		if (choose) {
			flags |= FLAG_WIS;
		} else {
			flags &= ~FLAG_WIS;
		}
	}

	public void writeToParcel(Parcel dest, int lolwut) {
		dest.writeInt(flags);
		dest.writeInt(classModifiers);
		dest.writeInt(miscModifiers);
	}

	public String writeToString() {
		return String.format("%d%s%d", flags, PlayerCharacter.SPLITTER_SMALL,
				miscModifiers);
	}

	public JSONObject writeToJSON() {
		try {
			JSONObject ret = new JSONObject();
			ret.put("classModifiers", classModifiers);
			ret.put("flags", flags);
			ret.put("miscModifiers", miscModifiers);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error writing Save to JSON");
			return null;
		}
	}

	public static Save createFromJSON(JSONObject input) {
		try {
			return new Save(input.getInt("flags"),
					input.getInt("classModifiers"),
					input.getInt("miscModifiers"));
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating Save from JSON");
			return null;
		}
	}
}
