package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class FeatEditActivity extends Activity implements OnClickListener {

	public static int REQUEST_NEW_FEAT = 0;
	public static int REQUEST_EDIT_FEAT = 1;
	public static int RESULT_DELETE = 2;
	public static String RESULT_KEY_DESC = "org.elteano.charactersheet.feat.desc";
	public static String RESULT_KEY_NAME = "org.elteano.charactersheet.feat.name";
	public static String INPUT_KEY_DESC = RESULT_KEY_DESC;
	public static String INPUT_KEY_NAME = RESULT_KEY_NAME;

	private EditText featName;
	private EditText featDesc;

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
		setContentView(R.layout.activity_feat_edit);
		featName = (EditText) findViewById(R.id.field_feat_name);
		featDesc = (EditText) findViewById(R.id.field_feat_desc);
		findViewById(R.id.feat_button_done).setOnClickListener(this);
		if (getIntent().getExtras() != null) {
			featName.setText(getIntent().getExtras().getString(INPUT_KEY_NAME,
					""));
			featDesc.setText(getIntent().getExtras().getString(INPUT_KEY_DESC,
					""));
		}
		if (featName.getText().toString().isEmpty())
			featName.requestFocus();
		setResult(RESULT_CANCELED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pfor_redirect_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_info:
			try {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
						Uri.parse("pfsrd://PFSRD/Feats/" + featName.getText()));
				intent.setComponent(new ComponentName(
						"org.evilsoft.pathfinder.reference",
						"org.evilsoft.pathfinder.reference.DetailsActivity"));
				intent.putExtra(SearchManager.QUERY, featName.getText()
						.toString());
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

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.feat_button_done:
			wrapup();
			break;
		}
	}

	public void onClickDeleteButton(View source) {
		setResult(RESULT_DELETE);
		finish();
	}

	private void wrapup() {
		String name = featName.getText().toString();
		if (name.trim().isEmpty()) {
			featName.requestFocus();
			return;
		}
		String desc = featDesc.getText().toString();
		if (desc.trim().isEmpty()) {
			desc = " ";
		}
		Intent intent = new Intent();
		intent.putExtra(RESULT_KEY_NAME, featName.getText().toString());
		intent.putExtra(RESULT_KEY_DESC, featDesc.getText().toString());
		setResult(RESULT_OK, intent);
		finish();
	}
}
