package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class CharacterSelectActivity extends FragmentActivity {

	public static final String INPUT_CHAR_REQUIRED = "charrequired";

	private boolean characterSelected, characterSelectRequired;
	private String resName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("CharacterSheet", "CharacterSelectactivity being created.");
		getActionBar().setTitle(R.string.title_activity_character_select);
		super.onCreate(savedInstanceState);
		if (getIntent() == null) {
			Log.e("CharacterSheet", "Intent is null.");
		}
		characterSelectRequired = getIntent().getBooleanExtra(
				INPUT_CHAR_REQUIRED, true);
		setContentView(R.layout.activity_character_select);
		setOrientation();
		Log.i("CharacterSheet", "CharacterSelectActivity creation complete.");
	}

	private void setOrientation() {
		int screenSizeFlag = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_NORMAL
				|| screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
	}

	public void setResultName(String name) {
		Intent result = new Intent();
		result.putExtra("result", name);
		setResult(RESULT_OK, result);
		resName = name;
		// This next block of code results in the activity moving directly from
		// a click to the corresponding sheet.
		Intent intent = new Intent(this, CharacterSheetActivity.class);
		intent.putExtra("result", resName);
		startActivity(intent);
	}

	public void onClickDoneButton(View source) {
		Log.i("CharacterShee", "Entering onClickDoneButton()");
		if (characterSelectRequired && characterSelected) {
			Intent intent = new Intent(this, CharacterSheetActivity.class);
			intent.putExtra("result", resName);
			startActivity(intent);
			Log.i("CharacterSheet", "Intent for CharacterSheetActivity sent.");
			// finish();
		} else if (!characterSelectRequired || characterSelected) {
			finish();
		}
		Log.i("CharacterShee", "Exiting onClickDoneButton()");
	}

	public void setCharacterSelected() {
		characterSelected = true;
	}
}
