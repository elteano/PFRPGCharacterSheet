package org.elteano.charactersheet;

import android.content.SharedPreferences;
import android.os.Bundle;

public class AbilityScore {

	private static final String BASE_VALUE = "org.elteano.charactersheet.AbilityScore.baseValue";
	private static final String TEMP_ADJUSTMENT = "org.elteano.charactersheet.AbilityScore.tempAdjustment";

	private int baseValue;
	private int tempAdjustment;

	public AbilityScore(int baseValue) {
		this(baseValue, 0);
	}

	public AbilityScore(int baseValue, int tempAdjustment) {
		this.baseValue = baseValue;
		this.tempAdjustment = tempAdjustment;
	}

	public AbilityScore(Bundle b) {
		baseValue = b.getInt(BASE_VALUE);
		tempAdjustment = b.getInt(TEMP_ADJUSTMENT);
	}

	public void addToBundle(Bundle b) {
		b.putInt(BASE_VALUE, baseValue);
		b.putInt(TEMP_ADJUSTMENT, tempAdjustment);
	}

	public void appendToSharedPreferencesEditor(
			SharedPreferences.Editor editor, int identifier) {
		editor.putInt(BASE_VALUE + identifier, baseValue);
		editor.putInt(TEMP_ADJUSTMENT + identifier, tempAdjustment);
	}

	public int getBaseModifier() {
		return baseValue / 2 - 5;
	}

	public int getBaseValue() {
		return baseValue;
	}

	public int getTempModifier() {
		return (baseValue + tempAdjustment) / 2 - 5;
	}

	public int getTempAdjustment() {
		return tempAdjustment;
	}

	public static AbilityScore restoreFromSharedPreferences(
			SharedPreferences state, int identifier) {
		return new AbilityScore(state.getInt(BASE_VALUE + identifier, 0),
				state.getInt(TEMP_ADJUSTMENT + identifier, 0));
	}

	public Bundle saveToBundle() {
		Bundle ret = new Bundle();
		addToBundle(ret);
		return ret;
	}

	public void saveToSharedPreferences(SharedPreferences state, int identifier) {
		SharedPreferences.Editor editor = state.edit();
		appendToSharedPreferencesEditor(editor, identifier);
		editor.commit();
	}

	public void setBaseValue(int baseValue) {
		this.baseValue = baseValue;
	}

	public void setTempAdjustment(int tempAdjustment) {
		this.tempAdjustment = tempAdjustment;
	}

	public String toString() {
		if (getTempModifier() > 0)
			return "+" + getTempModifier();
		else
			return "" + getTempModifier();
	}
}
