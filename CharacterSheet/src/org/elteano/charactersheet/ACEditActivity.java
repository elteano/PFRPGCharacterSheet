package org.elteano.charactersheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class ACEditActivity extends Activity implements OnCheckedChangeListener {

	private ArmorClass ac;
	private IntTextWatcher aBonus, deBonus, doBonus, sBonus;

	public void onCheckedChanged(CompoundButton source, boolean newVal) {
		ac.shield = newVal;
		updateButtons();
	}

	public void onClickDoneButton(View source) {
		Intent ret = new Intent();
		ret.putExtra("result", ac);
		setResult(RESULT_OK, ret);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ac = getIntent().getExtras().getParcelable("input");
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
	}

	private void updateText() {
		((CheckBox) findViewById(R.id.activity_acedit_shield_checkbox))
				.setChecked(ac.shield);
		((CheckBox) findViewById(R.id.activity_acedit_shield_checkbox))
				.setOnCheckedChangeListener(this);
		EditText tv = (EditText) findViewById(R.id.activity_acedit_armor_bonus_field);
		tv.setText("" + ac.armorBonus);
		tv.addTextChangedListener(aBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.armorBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_defl_bonus_field);
		tv.setText("" + ac.miscBonus);
		tv.addTextChangedListener(deBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.miscBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_dodge_bonus_field);
		tv.setText("" + ac.dodgeBonus);
		tv.addTextChangedListener(doBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.dodgeBonus = newNumber;
				updateButtons();
			}
		});
		tv = (EditText) findViewById(R.id.activity_acedit_shield_bonus_field);
		tv.setText("" + ac.shieldBonus);
		tv.addTextChangedListener(sBonus = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				ac.shieldBonus = newNumber;
				updateButtons();
			}
		});
	}

	private void updateButtons() {
		((Button) findViewById(R.id.activity_acedit_ac_button)).setText(""
				+ ac.getAC());
		((Button) findViewById(R.id.activity_acedit_touch_ac_button))
				.setText("" + ac.getTouchAC());
		((Button) findViewById(R.id.activity_acedit_ffac_button)).setText(""
				+ ac.getFlatFootAC());
		((Button) findViewById(R.id.activity_acedit_ff_t_ac_button)).setText(""
				+ ac.getFlatFootTouchAC());
		((Button) findViewById(R.id.activity_acedit_cmd_button)).setText(""
				+ ac.getCMD());
		((Button) findViewById(R.id.activity_acedit_ffcmd_button)).setText(""
				+ ac.getFlatFootCMD());
	}

}
