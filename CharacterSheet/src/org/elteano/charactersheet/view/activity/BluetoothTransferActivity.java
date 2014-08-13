package org.elteano.charactersheet.view.activity;

import java.util.ArrayList;
import java.util.Set;

import org.elteano.charactersheet.BluetoothSendTask;
import org.elteano.charactersheet.PlayerCharacterSendCallback;
import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.R.menu;
import org.elteano.charactersheet.model.PlayerCharacter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class BluetoothTransferActivity extends Activity implements
		OnItemClickListener, PlayerCharacterSendCallback {

	public static final String INPUT = "input";
	public static final int MODE_SEND = 0x50;
	private ArrayList<BluetoothDevice> devices;
	private BluetoothSendTask mTask;
	private PlayerCharacter sendingCharacter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_transfer);
		sendingCharacter = getIntent().getParcelableExtra(INPUT);
		devices = new ArrayList<BluetoothDevice>();
		((ListView) findViewById(R.id.device_lister))
				.setOnItemClickListener(this);
		// Show the Up button in the action bar.
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.bluetooth_transfer, menu);
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
	protected void onResume() {
		super.onResume();
		BluetoothAdapter blueAdp = BluetoothAdapter.getDefaultAdapter();
		if (blueAdp == null) {
			// complain
		}
		if (!blueAdp.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 0xfa10e);
		}
		ArrayAdapter<CharSequence> mArrayAdapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_list_item_1);
		((ListView) findViewById(R.id.device_lister)).setAdapter(mArrayAdapter);
		Set<BluetoothDevice> pairedDevices = blueAdp.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
				devices.add(device);
			}
		}
	}

	public void onItemClick(AdapterView<?> source, View item, int position,
			long whoknows) {
		// TODO Get destination from list element
		devices.get(position);
		// Spawn AsyncTask to handle transfer
		if (mTask == null
				|| mTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			mTask = new BluetoothSendTask(this);
		}
		if (mTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
			Toast.makeText(this,
					"Character transfer already in progress. Please wait.",
					Toast.LENGTH_SHORT).show();
		} else {
			mTask.execute(devices.get(position), sendingCharacter);
			Toast.makeText(this, "Sending " + sendingCharacter.getName(),
					Toast.LENGTH_SHORT).show();
		}
		// In separate method, report on success
	}

	public void onPlayerCharacterSend(Integer result) {
		if (result == 0) {
			Toast.makeText(this, "Character transfer success.",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Character transfer failed.",
					Toast.LENGTH_SHORT).show();
		}
	}
}