package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.AbilityScore;
import org.elteano.charactersheet.view.support.IntTextWatcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ModValueEditActivity extends Activity implements OnClickListener {

	public static final int REQUEST_CHA = 0;
	public static final int REQUEST_CON = 1;
	public static final int REQUEST_DEX = 2;
	public static final int REQUEST_INT = 3;
	public static final int REQUEST_STR = 4;
	public static final int REQUEST_WIS = 5;
	private AbilityScore mAbilityScore;
	private EditText baseEdit;
	private Button baseMod;
	private EditText tempEdit;
	private Button tempMod;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		setContentView(R.layout.mod_value_edit_activity);
		int ability = getIntent().getExtras().getInt("Ability");
		switch (ability) {
		case REQUEST_CHA:
			getActionBar().setTitle("Charisma");
			break;
		case REQUEST_CON:
			getActionBar().setTitle("Constitution");
			break;
		case REQUEST_DEX:
			getActionBar().setTitle("Dexterity");
			break;
		case REQUEST_INT:
			getActionBar().setTitle("Intelligence");
			break;
		case REQUEST_STR:
			getActionBar().setTitle("Strength");
			break;
		case REQUEST_WIS:
			getActionBar().setTitle("Wisdom");
			break;
		}
		findViewById(R.id.mod_value_done_button).setOnClickListener(this);
		findViewById(R.id.base_score_row).setOnClickListener(this);
		findViewById(R.id.temp_score_row).setOnClickListener(this);
		mAbilityScore = new AbilityScore(getIntent().getExtras());
		baseEdit = (EditText) findViewById(R.id.base_score_field);
		baseEdit.setText((mAbilityScore.getBaseValue() != 0) ? ""
				+ mAbilityScore.getBaseValue() : "");
		baseEdit.addTextChangedListener(new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mAbilityScore.setBaseValue(newNumber);
				updateButtons();
			}
		});
		baseMod = (Button) findViewById(R.id.base_score_modifier);
		tempEdit = (EditText) findViewById(R.id.temp_adjust_field);
		tempEdit.setText((mAbilityScore.getTempAdjustment() != 0) ? ""
				+ mAbilityScore.getTempAdjustment() : "");
		tempEdit.addTextChangedListener(new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mAbilityScore.setTempAdjustment(newNumber);
				updateButtons();
			}
		});
		tempMod = (Button) findViewById(R.id.temp_score_modifier);
		updateButtons();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.base_score_row:
			baseEdit.requestFocus();
			break;
		case R.id.temp_score_row:
			tempEdit.requestFocus();
			break;
		case R.id.mod_value_done_button:
			wrapup();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		prepResult();
		super.onDestroy();
	}

	private void prepResult() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		mAbilityScore.addToBundle(bundle);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
	}

	private void updateButtons() {
		baseMod.setText("" + mAbilityScore.getBaseModifier());
		tempMod.setText("" + mAbilityScore.getTempModifier());
	}

	private void wrapup() {
		if (baseEdit.getText().toString().length() == 0)
			mAbilityScore.setBaseValue(0);
		if (tempEdit.getText().toString().length() == 0
				|| tempEdit.getText().toString().equals("-"))
			mAbilityScore.setTempAdjustment(0);
		prepResult();
		finish();
	}

}
