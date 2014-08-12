package org.elteano.charactersheet;

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

public class SaveEditActivity extends Activity implements
		OnCheckedChangeListener {

	public static final String INPUT_ABILITIES = "ablities_in_here_plz";

	private Save mSave;
	private IntTextWatcher classModWatcher;
	private IntTextWatcher miscModWatcher;
	private AbilityScores abilities;

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

	public void onClickDoneButton(View source) {
		updateByBoxes();
		Intent intent = new Intent();
		intent.putExtra("result", mSave);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		setTitle("Edit Save");
		mSave = (Save) getIntent().getExtras().getParcelable("input");
		abilities = getIntent().getExtras().getParcelable(INPUT_ABILITIES);
		setContentView(R.layout.activity_save_edit);
		((CheckBox) findViewById(R.id.activity_save_edit_cha_box))
				.setChecked((mSave.flags & Save.FLAG_CHA) == Save.FLAG_CHA);
		((CheckBox) findViewById(R.id.activity_save_edit_cha_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_save_edit_con_box))
				.setChecked((mSave.flags & Save.FLAG_CON) == Save.FLAG_CON);
		((CheckBox) findViewById(R.id.activity_save_edit_con_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_save_edit_dex_box))
				.setChecked((mSave.flags & Save.FLAG_DEX) == Save.FLAG_DEX);
		((CheckBox) findViewById(R.id.activity_save_edit_dex_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_save_edit_int_box))
				.setChecked((mSave.flags & Save.FLAG_INT) == Save.FLAG_INT);
		((CheckBox) findViewById(R.id.activity_save_edit_int_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_save_edit_str_box))
				.setChecked((mSave.flags & Save.FLAG_STR) == Save.FLAG_STR);
		((CheckBox) findViewById(R.id.activity_save_edit_str_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_save_edit_wis_box))
				.setChecked((mSave.flags & Save.FLAG_WIS) == Save.FLAG_WIS);
		((CheckBox) findViewById(R.id.activity_save_edit_wis_box))
				.setOnCheckedChangeListener(this);
		int temp = mSave.classModifiers;
		((EditText) findViewById(R.id.activity_save_edit_class_modifiers_field))
				.setText((temp != 0) ? "" + temp : "");
		temp = mSave.miscModifiers;
		((EditText) findViewById(R.id.activity_save_edit_misc_modifiers_field))
				.setText((temp != 0) ? "" + temp : "");
		updateByBoxes();
	}

	public void onCheckedChanged(CompoundButton source, boolean newValue) {
		updateByBoxes();
	}

	@Override
	protected void onStart() {
		miscModWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSave.miscModifiers = newNumber;
				updateByBoxes();
			}
		};
		classModWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSave.classModifiers = newNumber;
				updateByBoxes();
			}
		};
		((EditText) findViewById(R.id.activity_save_edit_class_modifiers_field))
				.addTextChangedListener(classModWatcher);
		((EditText) findViewById(R.id.activity_save_edit_misc_modifiers_field))
				.addTextChangedListener(miscModWatcher);
		super.onStart();
	}

	@Override
	protected void onStop() {
		((EditText) findViewById(R.id.activity_save_edit_misc_modifiers_field))
				.removeTextChangedListener(miscModWatcher);
		((EditText) findViewById(R.id.activity_save_edit_class_modifiers_field))
				.removeTextChangedListener(classModWatcher);
		super.onStop();
	}

	private void updateByBoxes() {
		mSave.setCha(((CheckBox) findViewById(R.id.activity_save_edit_cha_box))
				.isChecked());
		mSave.setCon(((CheckBox) findViewById(R.id.activity_save_edit_con_box))
				.isChecked());
		mSave.setDex(((CheckBox) findViewById(R.id.activity_save_edit_dex_box))
				.isChecked());
		mSave.setInt(((CheckBox) findViewById(R.id.activity_save_edit_int_box))
				.isChecked());
		mSave.setStr(((CheckBox) findViewById(R.id.activity_save_edit_str_box))
				.isChecked());
		mSave.setWis(((CheckBox) findViewById(R.id.activity_save_edit_wis_box))
				.isChecked());
		((Button) findViewById(R.id.activity_save_edit_modifier_button))
				.setText("" + mSave.getTotal(abilities));
	}
}