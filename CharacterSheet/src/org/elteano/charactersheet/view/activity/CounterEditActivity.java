package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.R.string;
import org.elteano.charactersheet.model.Counter;
import org.elteano.charactersheet.view.support.IntTextWatcher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CounterEditActivity extends Activity {

	public static final int RESULT_DELETE = 2;
	public static final int REQUEST_NEW_COUNTER = 0;
	public static final int REQUEST_EDIT_COUNTER = 1;
	public static final String INPUT_COUNTER = CounterEditActivity.class
			.getCanonicalName() + ".counter";
	public static final String RESULT_COUNTER = CounterEditActivity.class
			.getCanonicalName() + ".counter";

	private TextWatcher nameWatcher;
	private IntTextWatcher minWatcher;
	private IntTextWatcher maxWatcher;
	private IntTextWatcher curWatcher;
	private Counter mCounter;

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
	protected void onStart() {
		((EditText) findViewById(R.id.dialog_counter_edit_name))
				.setText(mCounter.getName());
		((EditText) findViewById(R.id.dialog_counter_edit_name))
				.addTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_min_field))
				.setText((mCounter.getMin() != 0) ? "" + mCounter.getMin() : "");
		((EditText) findViewById(R.id.dialog_counter_edit_min_field))
				.addTextChangedListener(minWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_max_field))
				.setText((mCounter.getMax() != 0) ? "" + mCounter.getMax() : "");
		((EditText) findViewById(R.id.dialog_counter_edit_max_field))
				.addTextChangedListener(maxWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_init_field))
				.setText((mCounter.getCur() != 0) ? "" + mCounter.getCur() : "");
		((EditText) findViewById(R.id.dialog_counter_edit_init_field))
				.addTextChangedListener(curWatcher);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setOrientation();
		setTitle(R.string.edit_counter);
		setContentView(R.layout.dialog_counter_edit);
		if (getIntent().getExtras() != null)
			mCounter = (Counter) getIntent().getExtras().getParcelable(
					INPUT_COUNTER);
		if (mCounter == null) {
			mCounter = new Counter("", 0, 0, 0);
		}

		nameWatcher = new TextWatcher() {

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				mCounter.setName(arg0.toString());
				Log.d("CharacterSheet",
						"mCounter name is " + mCounter.getName());
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void afterTextChanged(Editable arg0) {

			}
		};

		minWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mCounter.setMin(newNumber);
			}
		};
		maxWatcher = new IntTextWatcher() {
			@Override
			public void numberChanged(int newNumber) {
				mCounter.setMax(newNumber);
			}
		};

		curWatcher = new IntTextWatcher() {
			@Override
			public void numberChanged(int newNumber) {
				mCounter.setCur(newNumber);
			}
		};
		super.onCreate(savedInstanceState);
	}

	public void onClickDeleteButton(View source) {
		setResult(RESULT_DELETE);
		finish();
	}

	public void onClickDoneButton(View v) {
		if (((EditText) findViewById(R.id.dialog_counter_edit_name)).getText()
				.toString().trim().isEmpty()) {
			((EditText) findViewById(R.id.dialog_counter_edit_name))
					.requestFocus();
			return;
		}
		Intent result = new Intent();
		result.putExtra(RESULT_COUNTER, (Parcelable) mCounter);
		setResult(RESULT_OK, result);
		finish();
	}

	@Override
	protected void onStop() {
		((EditText) findViewById(R.id.dialog_counter_edit_name))
				.removeTextChangedListener(nameWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_min_field))
				.removeTextChangedListener(minWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_max_field))
				.removeTextChangedListener(maxWatcher);
		((EditText) findViewById(R.id.dialog_counter_edit_init_field))
				.removeTextChangedListener(curWatcher);
		super.onStop();
	}

}
