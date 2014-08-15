package org.elteano.charactersheet.view.activity;

import java.util.ArrayList;

import org.elteano.charactersheet.R;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class BluetoothSendActivity extends Activity {

	private ArrayList<BluetoothDevice> devices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_send);
		// Show the Up button in the action bar.
		setupActionBar();
		devices = new ArrayList<BluetoothDevice>();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth_send, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
