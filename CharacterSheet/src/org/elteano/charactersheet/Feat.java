package org.elteano.charactersheet;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Feat implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	private static final String BUNDLE_NAME = "org.elteano.charactersheet.Feat.name";
	private static final String BUNDLE_DESC = "org.elteano.charactersheet.Feat.desc";

	public static transient final Parcelable.Creator<Feat> CREATOR = new Parcelable.Creator<Feat>() {

		public Feat createFromParcel(Parcel source) {
			return new Feat(source.readString(), source.readString());
		}

		public Feat[] newArray(int size) {
			return new Feat[size];
		}
	};

	private String name;
	private String desc;

	public Feat() {
		this("", "");
	}

	public Feat(String name, String desc) {
		this.name = name.trim();
		this.desc = desc.trim();
	}

	public Feat(Bundle bundle) {
		this(bundle.getString(BUNDLE_NAME), bundle.getString(BUNDLE_DESC));
	}

	public void addSelfToBundle(Bundle out) {
		out.putString(name, desc);
	}

	public static Feat fromSaveString(String saveString) {
		String[] cont = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		if (cont.length != 2)
			return null;
		return new Feat(cont[0], cont[1]);
	}

	public String getDescription() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public Bundle saveToBundle() {
		Bundle ret = new Bundle();
		ret.putString(BUNDLE_NAME, name);
		ret.putString(BUNDLE_DESC, desc);
		return ret;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toSaveString() {
		if (desc.isEmpty())
			desc = " ";
		if (name.isEmpty())
			name = " ";
		return name + PlayerCharacter.SPLITTER_SMALL + desc;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(desc);
	}

	public JSONObject writeToJSON() {
		try {
			JSONObject ret = new JSONObject();
			ret.put("desc", desc);
			ret.put("name", name);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON for Feat");
			return null;
		}
	}

	public static Feat createFromJSON(JSONObject input) {
		try {
			return new Feat(input.getString("name"), input.getString("desc"));
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating Feat from JSON");
			return null;
		}
	}
}
