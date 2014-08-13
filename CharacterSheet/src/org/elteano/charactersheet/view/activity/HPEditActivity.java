package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.IntTextWatcher;
import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.R.menu;
import org.elteano.charactersheet.R.string;
import org.elteano.charactersheet.model.HP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HPEditActivity extends Activity {

	/** used by StatsFragment to trace requests to this Activity */
	public static final int REQUEST_EDIT = 21;
	public static final String RESULT = "result";
	public static final String INPUT_TOTAL_LEVELS = "levels";
	public static final String INPUT_PER_LEVEL_MOD = "per level mod";
	public static final String INPUT_HP = "in hp";

	private int perLevelMod;
	private HP mHP;
	private int totalLevels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hpedit);
		mHP = getIntent().getParcelableExtra(INPUT_HP);
		perLevelMod = getIntent().getIntExtra(INPUT_PER_LEVEL_MOD, 0);
		totalLevels = getIntent().getIntExtra(INPUT_TOTAL_LEVELS, 0);
		setResult(RESULT_CANCELED);
		Log.i("CharacterSheet", "levelmod: " + perLevelMod + "; total levels: "
				+ totalLevels);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		TableRow tr = new TableRow(this, null);
		TextView tv = new TextView(this, null);
		tv.setText("Rolled HP");
		tr.addView(tv);
		tv = new TextView(this, null);
		tv.setText("Roll Modifier");
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tr.addView(tv);
		tv = new TextView(this, null);
		tv.setText("Result");
		tv.setGravity(Gravity.RIGHT);
		tr.addView(tv);
		((TableLayout) findViewById(R.id.activity_hpedit_container))
				.addView(tr);
		mHP.ensureLevelCount(totalLevels);
		updateTotalHP();
	}

	public void addLevelListing(final int level, int levelHP) {
		TableRow tr = new TableRow(this, null);
		EditText et = new EditText(this, null);
		final TextView conmod = new TextView(this, null);
		final TextView result = new TextView(this, null);
		IntTextWatcher itw = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mHP.setRolledHP(level, newNumber);
				result.setText("+" + Math.abs(newNumber + perLevelMod));
				updateTotalHP();
			}
		};
		et.setHint(R.string.z0);
		if (levelHP != 0)
			et.setText("" + levelHP);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		conmod.setTextSize(24);
		conmod.setText((perLevelMod < 0) ? "" + perLevelMod : "+" + perLevelMod);
		conmod.setGravity(Gravity.CENTER_HORIZONTAL);
		result.setTextSize(24);
		result.setText((levelHP + perLevelMod < 0) ? ""
				+ (levelHP + perLevelMod) : "+" + (levelHP + perLevelMod));
		result.setGravity(Gravity.RIGHT);
		tr.addView(et);
		tr.addView(conmod);
		tr.addView(result);
		et.addTextChangedListener(itw);
		((TableLayout) findViewById(R.id.activity_hpedit_container))
				.addView(tr);
	}

	public void fillValues() {
		for (int i = 1; i <= totalLevels; i++) {
			Log.i("CharacterSheet", "Adding level listing for " + i);
			addLevelListing(i, mHP.getRolledLevelHP(i));
		}
	}

	public void onClickDoneButton(View source) {
		Intent result = new Intent();
		result.putExtra(RESULT, (Parcelable) mHP);
		setResult(RESULT_OK, result);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hpedit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		fillValues();
	}

	private void updateTotalHP() {
		((TextView) findViewById(R.id.activity_hpedit_total_text)).setText(""
				+ mHP.getMaxHP(perLevelMod));
	}
}