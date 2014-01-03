package org.elteano.charactersheet;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SkillEditActivity extends Activity implements
		OnItemSelectedListener {

	public static final int RESULT_DELETE = 2;
	public static final int REQUEST_NEW_SKILL = 0;
	public static final int REQUEST_EDIT_SKILL = 1;
	public static final String INPUT_SKILL = SkillEditActivity.class
			.getCanonicalName() + ".skill";
	public static final String RESULT_SKILL = SkillEditActivity.class
			.getCanonicalName() + ".skill";
	public static final String INPUT_ABILITIES = "abilities";

	private AbilityScore[] abilities;
	private TextWatcher nameWatcher;
	private IntTextWatcher ranksWatcher;
	private IntTextWatcher miscWatcher;
	private Skill mSkill;

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		Parcelable[] a_in = getIntent().getExtras().getParcelableArray(
				INPUT_ABILITIES);
		abilities = new AbilityScore[6];
		for (int i = 0; i < a_in.length; i++) {
			abilities[i] = (AbilityScore) a_in[i];
		}
		setContentView(R.layout.activity_skill_edit);
		if (getIntent().getExtras() != null)
			mSkill = getIntent().getExtras().getParcelable(INPUT_SKILL);
		if (mSkill == null)
			mSkill = new Skill("", 0, 0, 0);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ability_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) findViewById(R.id.activity_skill_edit_base_ability);
		spinner.setAdapter(adapter);
		spinner.setSelection(mSkill.getBaseAbility());
		spinner.setOnItemSelectedListener(this);
		nameWatcher = new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				mSkill.setName(s.toString());
			}
		};
		ranksWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSkill.setSkillRanks(newNumber);
				updateTotalModButton();
			}
		};
		miscWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSkill.miscMod = newNumber;
				updateTotalModButton();
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pfor_redirect_menu, menu);
		return true;
	}

	public void onClickDeleteButton(View source) {
		setResult(RESULT_DELETE);
		finish();
	}

	public void onClickDoneButton(View v) {
		if (((EditText) findViewById(R.id.activity_skill_edit_skill_name))
				.getText().toString().trim().isEmpty()) {
			((EditText) findViewById(R.id.activity_skill_edit_skill_name))
					.requestFocus();
			return;
		}
		if (((EditText) findViewById(R.id.activity_skill_edit_skill_ranks))
				.getText().toString().trim().isEmpty()) {
			((EditText) findViewById(R.id.activity_skill_edit_skill_ranks))
					.requestFocus();
			return;
		}
		Intent result = new Intent();
		result.putExtra(RESULT_SKILL, (Parcelable) mSkill);
		setResult(RESULT_OK, result);
		finish();
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		mSkill.setBaseAbility(pos);
		((Button) findViewById(R.id.activity_skill_edit_ability_mod_button))
				.setText(((abilities[pos].getTempModifier() < 0) ? "" : "+")
						+ abilities[pos].getTempModifier());
		updateTotalModButton();
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_info:
			try {
				String sString = mSkill.getName().replaceAll("\\(.*\\)", "")
						.trim();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
						Uri.parse("pfsrd://PFSRD/Skills/" + sString));
				intent.setComponent(new ComponentName(
						"org.evilsoft.pathfinder.reference",
						"org.evilsoft.pathfinder.reference.DetailsActivity"));
				intent.putExtra(SearchManager.QUERY, sString);
				startActivity(intent);
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.pfor_toast),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		((EditText) findViewById(R.id.activity_skill_edit_skill_name))
				.setText(mSkill.getName());
		((EditText) findViewById(R.id.activity_skill_edit_skill_name))
				.addTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.activity_skill_edit_skill_ranks))
				.setText("" + mSkill.getNumRanks());
		((EditText) findViewById(R.id.activity_skill_edit_skill_ranks))
				.addTextChangedListener(ranksWatcher);
		((EditText) findViewById(R.id.activity_skill_edit_misc_mod_field))
				.setText("" + mSkill.miscMod);
		((EditText) findViewById(R.id.activity_skill_edit_misc_mod_field))
				.addTextChangedListener(miscWatcher);
		super.onStart();
	}

	@Override
	protected void onStop() {
		((EditText) findViewById(R.id.activity_skill_edit_skill_name))
				.removeTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.activity_skill_edit_skill_ranks))
				.removeTextChangedListener(ranksWatcher);
		((EditText) findViewById(R.id.activity_skill_edit_misc_mod_field))
				.removeTextChangedListener(miscWatcher);
		super.onStop();
	}

	public void updateTotalModButton() {
		int totalMod = mSkill.getTotalModifier(abilities);
		((Button) findViewById(R.id.activity_skill_edit_total_mod_button))
				.setText(((totalMod < 0) ? "" : "+") + totalMod);
	}
}
