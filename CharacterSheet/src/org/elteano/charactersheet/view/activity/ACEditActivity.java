package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.ArmorClass;
import org.elteano.charactersheet.view.support.IntTextWatcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class ACEditActivity extends Activity implements OnCheckedChangeListener {

	public static final String INPUT_ABILITIES = "abilities";
	public static final String INPUT_SIZE = "size";
	public static final String INPUT_BAB = "bab";

	private ArmorClass ac;
	private AbilityScores abilities;
	private int size;
	private int bab;
	private IntTextWatcher aBonus, deBonus, doBonus, sBonus, cBonus;

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

	public void onCheckedChanged(CompoundButton source, boolean newVal) {
		ac.shield = newVal;
		updateButtons();
	}

	public void onClickDoneButton(View source) {
		Intent ret = new Intent();
		ret.putExtra("result", (android.os.Parcelable) ac);
		setResult(RESULT_OK, ret);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		ac = getIntent().getExtras().getParcelable("input");
		size = getIntent().getExtras().getInt(INPUT_SIZE);
		abilities = getIntent().getExtras().getParcelable(INPUT_ABILITIES);
		bab = getIntent().getExtras().getInt(INPUT_BAB);
		setContentView(R.layout.activity_acedit);
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateButtons();
		updateText();
	}

	@Override
	protected void onStop() {
		super.onStop();
		((EditText) findViewById(R.id.activity_acedit_armor_bonus_field))
				.removeTextChangedListener(aBonus);
		((EditText) findViewById(R.id.activity_acedit_defl_bonus_field))
				.removeTextChangedListener(deBonus);
		((EditText) findViewById(R.id.activity_acedit_dodge_bonus_field))
				.removeTextChangedListener(doBonus);
		((EditText) findViewById(R.id.activity_acedit_shield_bonus_field))
				.removeTextChangedListener(sBonus);
		((EditText) findViewById(R.id.activity_acedit_cmd_bonus_field))
				.removeTextChangedListener(cBonus);
	}

	private void updateText() {
		((CheckBox) findViewById(R.id.activity_acedit_shield_checkbox))
				.setChecked(ac.shield);
		((CheckBox) findViewById(R.id.activity_acedit_shield_checkbox))
				.setOnCheckedChangeListener(this);
		EditText tv = (EditText) findViewById(R.id.activity_acedit_armor_bonus_field);
		if (ac.armorBonus != 0)
			tv.setText("" + ac.armorBonus);
		tv.addTextChangedListener(aBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.armorBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_defl_bonus_field);
		if (ac.miscBonus != 0)
			tv.setText("" + ac.miscBonus);
		tv.addTextChangedListener(deBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.miscBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_dodge_bonus_field);
		if (ac.dodgeBonus != 0)
			tv.setText("" + ac.dodgeBonus);
		tv.addTextChangedListener(doBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.dodgeBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_shield_bonus_field);
		if (ac.shieldBonus != 0)
			tv.setText("" + ac.shieldBonus);
		tv.addTextChangedListener(sBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.shieldBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_cmd_bonus_field);
		if (ac.cmdBonus != 0)
			tv.setText("" + ac.cmdBonus);
		tv.addTextChangedListener(cBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.cmdBonus = newNumber;
				updateButtons();
			}
		});
	}

	private void updateButtons() {
		((Button) findViewById(R.id.activity_acedit_ac_button)).setText(""
				+ ac.getAC(abilities, size, bab));
		((Button) findViewById(R.id.activity_acedit_touch_ac_button))
				.setText("" + ac.getTouchAC(abilities, size, bab));
		((Button) findViewById(R.id.activity_acedit_ffac_button)).setText(""
				+ ac.getFlatFootAC(abilities, size, bab));
		((Button) findViewById(R.id.activity_acedit_ff_t_ac_button)).setText(""
				+ ac.getFlatFootTouchAC(abilities, size, bab));
		((Button) findViewById(R.id.activity_acedit_cmd_button)).setText(""
				+ ac.getCMD(abilities, size, bab));
		((Button) findViewById(R.id.activity_acedit_ffcmd_button)).setText(""
				+ ac.getFlatFootCMD(abilities, size, bab));
	}

}
