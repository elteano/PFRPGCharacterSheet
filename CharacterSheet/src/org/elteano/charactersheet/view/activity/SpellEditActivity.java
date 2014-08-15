package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.Spell;
import org.elteano.charactersheet.view.support.IntTextWatcher;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SpellEditActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {

	public static final String SPELL_ABILITY_IN = "spells";
	public static final int RESULT_DELETE = 2;

	private IntTextWatcher levelWatcher;
	private TextWatcher saveWatcher;
	private Spell mSpell;
	private TextWatcher nameWatcher;
	private TextWatcher descWatcher;
	private AbilityScores mAbilityScores;

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

	public void onClick(View source) {
		switch (source.getId()) {
		case R.id.activity_spell_edit_delete_button:
			setResult(RESULT_DELETE);
			finish();
			break;
		case R.id.activity_spell_edit_done_button:
			if (mSpell.name.trim().isEmpty()) {
				((EditText) findViewById(R.id.activity_spell_edit_name_field))
						.requestFocus();
				return;
			}
			Intent result = new Intent();
			result.putExtra("result", (Parcelable) mSpell);
			setResult(RESULT_OK, result);
			finish();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		setResult(RESULT_CANCELED);
		setContentView(R.layout.activity_spell_edit);
		((Button) findViewById(R.id.activity_spell_edit_delete_button))
				.setOnClickListener(this);
		((Button) findViewById(R.id.activity_spell_edit_done_button))
				.setOnClickListener(this);
		if (getIntent().hasExtra("input"))
			mSpell = (Spell) getIntent().getExtras().getParcelable("input");
		else
			mSpell = new Spell(0, "", "", 0, 0, null);
		mAbilityScores = getIntent().getParcelableExtra(SPELL_ABILITY_IN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pfor_redirect_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_info:
			try {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
						Uri.parse("pfsrd://PFSRD/Spells/" + mSpell.name));
				intent.setComponent(new ComponentName(
						"org.evilsoft.pathfinder.reference",
						"org.evilsoft.pathfinder.reference.DetailsActivity"));
				intent.putExtra(SearchManager.QUERY, mSpell.name);
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
		levelWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSpell.level = newNumber;
				updateSave();
			}
		};
		nameWatcher = new TextWatcher() {

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void afterTextChanged(Editable text) {
				mSpell.name = text.toString();
			}
		};
		descWatcher = new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				mSpell.desc = s.toString();
			}
		};
		saveWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mSpell.saveBonus = newNumber;
				updateSave();
			}
		};

		// Set up spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ability_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) findViewById(R.id.activity_spell_edit_save_ability_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(mSpell.saveAbility);
		spinner.setOnItemSelectedListener(this);

		// Fill in fields
		((EditText) findViewById(R.id.activity_spell_edit_desc_field))
				.setText(mSpell.desc.trim());
		((EditText) findViewById(R.id.activity_spell_edit_desc_field))
				.addTextChangedListener(descWatcher);
		((EditText) findViewById(R.id.activity_spell_edit_name_field))
				.setText(mSpell.name.trim());
		((EditText) findViewById(R.id.activity_spell_edit_name_field))
				.addTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.activity_spell_edit_level_field))
				.setText("" + mSpell.level);
		((EditText) findViewById(R.id.activity_spell_edit_level_field))
				.addTextChangedListener(levelWatcher);
		((EditText) findViewById(R.id.activity_spell_edit_save_bonus_field))
				.setText("" + mSpell.saveBonus);
		((EditText) findViewById(R.id.activity_spell_edit_save_bonus_field))
				.addTextChangedListener(saveWatcher);
		ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_dropdown_item_1line,
				getResources().getStringArray(R.array.spells_array));
		((AutoCompleteTextView) findViewById(R.id.activity_spell_edit_name_field))
				.setAdapter(autocompleteAdapter);
		updateSave();
		super.onStart();
	}

	@Override
	protected void onStop() {
		((EditText) findViewById(R.id.activity_spell_edit_name_field))
				.removeTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.activity_spell_edit_desc_field))
				.removeTextChangedListener(descWatcher);
		((EditText) findViewById(R.id.activity_spell_edit_level_field))
				.removeTextChangedListener(levelWatcher);
		super.onStop();
	}

	public void onItemSelected(AdapterView<?> parent, View source, int pos,
			long id) {
		mSpell.saveAbility = pos;
		updateSave();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}

	public void updateSave() {
		((Button) findViewById(R.id.activity_spell_edit_save_button))
				.setText("" + mSpell.getSaveDC(mAbilityScores));
	}
}
