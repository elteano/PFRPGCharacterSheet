package org.elteano.charactersheet;

import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CMBEditActivity extends Activity implements
		OnCheckedChangeListener {

	public static final String INPUT_CMB = "cmb";
	public static final String INPUT_ABILITIES = "abilities";
	public static final String INPUT_CLASSES = "classes";
	public static final String INPUT_SIZE = "size";
	public static final String INPUT_BAB = "bab";

	public static final String OUTPUT = "output";

	private CMB mCMB;
	private IntTextWatcher classModWatcher;
	private IntTextWatcher miscModWatcher;
	private AbilityScore[] abilities;
	private ArrayList<PlayerClass> classes;
	private int size;
	private int bab;

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
		Intent result = new Intent();
		result.putExtra(OUTPUT, (Parcelable) mCMB);
		setResult(RESULT_OK, result);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOrientation();
		Parcelable[] b = getIntent().getExtras().getParcelableArray(
				INPUT_ABILITIES);
		mCMB = getIntent().getExtras().getParcelable(INPUT_CMB);
		abilities = new AbilityScore[b.length];
		for (int i = 0; i < b.length; i++) {
			abilities[i] = (AbilityScore) b[i];
		}
		classes = new ArrayList<PlayerClass>();
		b = getIntent().getExtras().getParcelableArray(INPUT_CLASSES);
		for (Parcelable parc : b) {
			classes.add((PlayerClass) parc);
		}
		size = getIntent().getExtras().getInt(INPUT_SIZE, 0);
		bab = getIntent().getExtras().getInt(INPUT_BAB, 0);
		setContentView(R.layout.activity_cmbedit);
		((CheckBox) findViewById(R.id.activity_cmb_edit_bab_box))
				.setChecked(mCMB.getBAB());
		((CheckBox) findViewById(R.id.activity_cmb_edit_bab_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_size_box))
				.setChecked(mCMB.getSize());
		((CheckBox) findViewById(R.id.activity_cmb_edit_size_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_cha_box))
				.setChecked(mCMB.getCha());
		((CheckBox) findViewById(R.id.activity_cmb_edit_cha_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_con_box))
				.setChecked(mCMB.getCon());
		((CheckBox) findViewById(R.id.activity_cmb_edit_con_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_dex_box))
				.setChecked(mCMB.getDex());
		((CheckBox) findViewById(R.id.activity_cmb_edit_dex_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_int_box))
				.setChecked(mCMB.getInt());
		((CheckBox) findViewById(R.id.activity_cmb_edit_int_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_str_box))
				.setChecked(mCMB.getStr());
		((CheckBox) findViewById(R.id.activity_cmb_edit_str_box))
				.setOnCheckedChangeListener(this);
		((CheckBox) findViewById(R.id.activity_cmb_edit_wis_box))
				.setChecked(mCMB.getWis());
		((CheckBox) findViewById(R.id.activity_cmb_edit_wis_box))
				.setOnCheckedChangeListener(this);
		((EditText) findViewById(R.id.activity_cmb_edit_class_modifiers_field))
				.setText("" + mCMB.getClassModifiers());
		((EditText) findViewById(R.id.activity_cmb_edit_misc_modifiers_field))
				.setText("" + mCMB.getMiscModifiers());
		int temp = mCMB.getClassModifiers();
		((EditText) findViewById(R.id.activity_cmb_edit_class_modifiers_field))
				.setText((temp != 0) ? "" + temp : "");
		temp = mCMB.getMiscModifiers();
		((EditText) findViewById(R.id.activity_cmb_edit_misc_modifiers_field))
				.setText((temp != 0) ? "" + temp : "");
		LinearLayout classHolder = (LinearLayout) findViewById(R.id.activity_cmb_edit_class_holder);
		for (PlayerClass c : classes) {
			CheckBox cb = new CheckBox(this);
			cb.setText(c.getName());
			if (mCMB.getClassList().contains(c.getName())) {
				cb.setChecked(true);
			}
			cb.setOnCheckedChangeListener(this);
			classHolder.addView(cb);
		}
		updateByBoxes();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pfor_redirect_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_info:
			try {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
						Uri.parse("pfsrd://PFSRD/Rules/"
								+ "Combat Maneuver Bonus"));
				intent.setComponent(new ComponentName(
						"org.evilsoft.pathfinder.reference",
						"org.evilsoft.pathfinder.reference.DetailsActivity"));
				intent.putExtra(SearchManager.QUERY, "Combat Maneuver Bonus");
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

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		updateByBoxes();
	}

	@Override
	protected void onStart() {
		miscModWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mCMB.setMiscModifiers(newNumber);
				updateByBoxes();
			}
		};
		classModWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mCMB.setClassModifiers(newNumber);
				updateByBoxes();
			}
		};
		((EditText) findViewById(R.id.activity_cmb_edit_class_modifiers_field))
				.addTextChangedListener(classModWatcher);
		((EditText) findViewById(R.id.activity_cmb_edit_misc_modifiers_field))
				.addTextChangedListener(miscModWatcher);
		super.onStart();
	}

	@Override
	protected void onStop() {
		((EditText) findViewById(R.id.activity_cmb_edit_misc_modifiers_field))
				.removeTextChangedListener(miscModWatcher);
		((EditText) findViewById(R.id.activity_cmb_edit_class_modifiers_field))
				.removeTextChangedListener(classModWatcher);
		super.onStop();
	}

	private void updateByBoxes() {
		mCMB.removeAllClasses();
		LinearLayout classHolder = (LinearLayout) findViewById(R.id.activity_cmb_edit_class_holder);
		for (int i = 0; i < classHolder.getChildCount(); i++) {
			CheckBox cb = (CheckBox) classHolder.getChildAt(i);
			if (cb.isChecked()) {
				mCMB.addClass(cb.getText().toString());
			}
		}
		mCMB.setBAB(((CheckBox) findViewById(R.id.activity_cmb_edit_bab_box))
				.isChecked());
		mCMB.setSize(((CheckBox) findViewById(R.id.activity_cmb_edit_size_box))
				.isChecked());
		mCMB.setCha(((CheckBox) findViewById(R.id.activity_cmb_edit_cha_box))
				.isChecked());
		mCMB.setCon(((CheckBox) findViewById(R.id.activity_cmb_edit_con_box))
				.isChecked());
		mCMB.setDex(((CheckBox) findViewById(R.id.activity_cmb_edit_dex_box))
				.isChecked());
		mCMB.setInt(((CheckBox) findViewById(R.id.activity_cmb_edit_int_box))
				.isChecked());
		mCMB.setStr(((CheckBox) findViewById(R.id.activity_cmb_edit_str_box))
				.isChecked());
		mCMB.setWis(((CheckBox) findViewById(R.id.activity_cmb_edit_wis_box))
				.isChecked());
		((Button) findViewById(R.id.activity_cmb_edit_modifier_button))
				.setText("" + mCMB.getCMB(bab, size, abilities, classes));
	}
}