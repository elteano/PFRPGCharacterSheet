package org.elteano.charactersheet.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Item implements Parcelable, Serializable {

	public static final long serialVersionUID = 1L;
	private static String SPLIT_STRING = "#split#field#";

	private float weight;
	private String desc;
	private String name;
	private int quantity;

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {

		public Item createFromParcel(Parcel source) {
			return new Item(source.readString(), source.readFloat(),
					source.readInt(), source.readString());
		}

		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	public Item(String name, float perUnitWeight, int quantity, String desc) {
		this.name = name;
		this.weight = perUnitWeight;
		this.quantity = quantity;
		this.desc = desc;
	}

	public Item(String name, String quantifiers) {
		this.name = name;
		String[] parts = quantifiers.split(SPLIT_STRING);
		weight = Float.parseFloat(parts[0]);
		quantity = Integer.parseInt(parts[1]);
		if (parts.length > 2)
			desc = parts[2];
		else
			desc = "";
	}

	public int describeContents() {
		return 0;
	}

	public static Item fromSaveString(String saveString) {
		String[] cont = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		if (cont.length != 4)
			return null;
		float weight = 0;
		int quantity = 0;
		try {
			quantity = Integer.parseInt(cont[2]);
			weight = Float.parseFloat(cont[3]);
		} catch (NumberFormatException ex) {
		}
		return new Item(cont[0], weight, quantity, cont[1]);
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public String getQualifierString() {
		return weight + SPLIT_STRING + quantity + SPLIT_STRING + desc;
	}

	public int getQuantity() {
		return quantity;
	}

	public float getWeight() {
		return weight;
	}

	public void setDesc(String description) {
		desc = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(int number) {
		quantity = number;
	}

	public void setPerUnitWeight(float weight) {
		this.weight = weight;
	}

	public String toSaveString() {
		return name + PlayerCharacter.SPLITTER_SMALL + desc
				+ PlayerCharacter.SPLITTER_SMALL + quantity
				+ PlayerCharacter.SPLITTER_SMALL + weight;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeFloat(weight);
		dest.writeInt(quantity);
		dest.writeString(desc);
	}

	public JSONObject writeToJSON() {
		try {
			JSONObject ret = new JSONObject();
			ret.put("desc", desc);
			ret.put("name", name);
			ret.put("quantity", quantity);
			ret.put("weight", weight);
			return ret;
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error creating JSON from Item");
			return null;
		}
	}

	public static Item createFromJSON(JSONObject input) {
		try {
			return new Item(input.getString("name"),
					(float) input.getDouble("weight"),
					input.getInt("quantity"), input.getString("desc"));
		} catch (JSONException ex) {
			Log.e("CharacterSheet", "Error inflating Item from JSON");
			return null;
		}
	}
}
