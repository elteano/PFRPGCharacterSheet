package org.elteano.charactersheet;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AbilityScores implements Parcelable, Serializable {

	private static final long serialVersionUID = 1L;

	private static final int ABILITY_CHA = 0;
	private static final int ABILITY_CON = 1;
	private static final int ABILITY_DEX = 2;
	private static final int ABILITY_INT = 3;
	private static final int ABILITY_STR = 4;
	private static final int ABILITY_WIS = 5;

	private AbilityScore[] scores;

	public static final Parcelable.Creator<AbilityScores> CREATOR = new Parcelable.Creator<AbilityScores>() {

		public AbilityScores createFromParcel(Parcel source) {
			return new AbilityScores(
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()),
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()),
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()),
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()),
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()),
					(AbilityScore) source.readParcelable(AbilityScore.class
							.getClassLoader()));
		}

		public AbilityScores[] newArray(int size) {
			return new AbilityScores[size];
		}
	};

	public AbilityScores(AbilityScore cha, AbilityScore con, AbilityScore dex,
			AbilityScore in, AbilityScore str, AbilityScore wis) {
		scores = new AbilityScore[6];
		scores[ABILITY_CHA] = cha;
		scores[ABILITY_CON] = con;
		scores[ABILITY_DEX] = dex;
		scores[ABILITY_INT] = in;
		scores[ABILITY_STR] = str;
		scores[ABILITY_WIS] = wis;
	}

	public AbilityScores() {
		this(new AbilityScore(10), new AbilityScore(10), new AbilityScore(10),
				new AbilityScore(10), new AbilityScore(10),
				new AbilityScore(10));
	}

	public AbilityScore getAbility(int ability) {
		return scores[ability];
	}

	public AbilityScore getCha() {
		return scores[ABILITY_CHA];
	}

	public AbilityScore getCon() {
		return scores[ABILITY_CON];
	}

	public AbilityScore getDex() {
		return scores[ABILITY_DEX];
	}

	public AbilityScore getInt() {
		return scores[ABILITY_INT];
	}

	public AbilityScore getStr() {
		return scores[ABILITY_STR];
	}

	public AbilityScore getWis() {
		return scores[ABILITY_WIS];
	}

	public void setAbility(int ability, AbilityScore value) {
		scores[ability] = value;
	}

	public void setCha(AbilityScore cha) {
		scores[ABILITY_CHA] = cha;
	}

	public void setCon(AbilityScore con) {
		scores[ABILITY_CON] = con;
	}

	public void setDex(AbilityScore dex) {
		scores[ABILITY_DEX] = dex;
	}

	public void setInt(AbilityScore in) {
		scores[ABILITY_INT] = in;
	}

	public void setStr(AbilityScore str) {
		scores[ABILITY_STR] = str;
	}

	public void setWis(AbilityScore wis) {
		scores[ABILITY_WIS] = wis;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(getCha(), 0);
		dest.writeParcelable(getCon(), 0);
		dest.writeParcelable(getDex(), 0);
		dest.writeParcelable(getInt(), 0);
		dest.writeParcelable(getStr(), 0);
		dest.writeParcelable(getWis(), 0);
	}

	public JSONObject writeToJSON() {
		JSONObject ret = new JSONObject();
		try {
			ret.put("cha", getCha().writeToJSON());
			ret.put("con", getCon().writeToJSON());
			ret.put("dex", getDex().writeToJSON());
			ret.put("int", getInt().writeToJSON());
			ret.put("str", getStr().writeToJSON());
			ret.put("wis", getWis().writeToJSON());
		} catch (JSONException ex) {
		}
		return ret;
	}

	public static AbilityScores createFromJSON(JSONObject input) {
		try {
			return new AbilityScores(AbilityScore.createFromJSON(input
					.getJSONObject("cha")), AbilityScore.createFromJSON(input
					.getJSONObject("con")), AbilityScore.createFromJSON(input
					.getJSONObject("dex")), AbilityScore.createFromJSON(input
					.getJSONObject("int")), AbilityScore.createFromJSON(input
					.getJSONObject("str")), AbilityScore.createFromJSON(input
					.getJSONObject("wis")));
		} catch (JSONException ex) {
			Log.e("CharacterSheet",
					"JSONException while inflating AbilityScores");
			return null;
		}
	}
}
