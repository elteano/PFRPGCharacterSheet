package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.array;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.model.Attack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AttackEditActivity extends Activity implements
		OnItemSelectedListener {

	public static final int RESULT_DELETE = 2;
	public static final String RESULT_STRING = AttackEditActivity.class
			.getCanonicalName() + ".result";

	private Attack mAttack;

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

	public void onClickDeleteButton(View source) {
		setResult(RESULT_DELETE);
		finish();
	}

	public void onClickDoneButton(View source) {
		EditText nameField = (EditText) findViewById(R.id.activity_attack_edit_attack_name);
		if (nameField.getText().toString().trim().isEmpty()) {
			nameField.requestFocus();
			return;
		}
		mAttack.name = nameField.getText().toString().trim();
		String attackBonusString = ((EditText) findViewById(R.id.activity_attack_edit_attack_bonus_field))
				.getText().toString();
		mAttack.addAttack = (attackBonusString.equals("-") || attackBonusString
				.isEmpty()) ? 0 : Integer.parseInt(attackBonusString);
		String damageBonusString = ((EditText) findViewById(R.id.activity_attack_edit_damage_bonus_field))
				.getText().toString();
		mAttack.addDamage = (damageBonusString.isEmpty() || damageBonusString
				.equals("-")) ? 0 : Integer.parseInt(damageBonusString);
		mAttack.description = ((EditText) findViewById(R.id.activity_attack_edit_attack_description_field))
				.getText().toString();
		String damageDieString = ((EditText) findViewById(R.id.activity_attack_edit_damage_die_field))
				.getText().toString();
		if (damageDieString.isEmpty())
			damageDieString = "1";
		mAttack.damageDie = damageDieString;
		Intent intent = new Intent();
		intent.putExtra(RESULT_STRING, (Parcelable) mAttack);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		setResult(RESULT_CANCELED);
		setContentView(R.layout.activity_attack_edit);

		if (getIntent().getExtras() != null)
			mAttack = (Attack) getIntent().getExtras().getParcelable("input");
		else
			mAttack = new Attack("", 4, 4, 0, 0, "", "");

		// Set to input Attack
		((EditText) findViewById(R.id.activity_attack_edit_attack_name))
				.setText(mAttack.name);
		((EditText) findViewById(R.id.activity_attack_edit_attack_bonus_field))
				.setText((mAttack.addAttack != 0) ? "" + mAttack.addAttack : "");
		((EditText) findViewById(R.id.activity_attack_edit_damage_die_field))
				.setText(mAttack.damageDie);
		((EditText) findViewById(R.id.activity_attack_edit_damage_bonus_field))
				.setText((mAttack.addDamage != 0) ? "" + mAttack.addDamage : "");
		((EditText) findViewById(R.id.activity_attack_edit_attack_description_field))
				.setText(mAttack.description.trim());

		// Attack Spinner setup
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ability_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) findViewById(R.id.activity_attack_edit_base_attack_ability_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(mAttack.baseAttackAbility);
		spinner.setOnItemSelectedListener(this);

		// Damage Spinner setup
		adapter = ArrayAdapter.createFromResource(this,
				R.array.ability_array_with_none,
				android.R.layout.simple_spinner_item);
		spinner = (Spinner) findViewById(R.id.activity_attack_edit_base_damage_ability_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(mAttack.baseDamageAbility);
		spinner.setOnItemSelectedListener(this);
	}

	public void onItemSelected(AdapterView<?> parent, View source, int pos,
			long id) {
		switch (parent.getId()) {
		case R.id.activity_attack_edit_base_attack_ability_spinner:
			mAttack.baseAttackAbility = pos;
			break;
		case R.id.activity_attack_edit_base_damage_ability_spinner:
			mAttack.baseDamageAbility = pos;
			break;
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

}
