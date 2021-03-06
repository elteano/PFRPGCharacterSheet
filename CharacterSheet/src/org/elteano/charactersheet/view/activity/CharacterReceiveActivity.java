package org.elteano.charactersheet.view.activity;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.bg.bluetooth.BluetoothReceiveTask;
import org.elteano.charactersheet.bg.wifid.PlayerCharacterReceiveCallback;
import org.elteano.charactersheet.model.PlayerCharacter;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CharacterReceiveActivity extends Activity implements
		PlayerCharacterReceiveCallback, OnItemClickListener {

	BluetoothReceiveTask mReceiveTask;
	ArrayAdapter<CharSequence> mArrayAdapter;
	ArrayList<PlayerCharacter> mReceivedCharacters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_receive);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mReceivedCharacters = new ArrayList<PlayerCharacter>();
		mArrayAdapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_list_item_1);
		((ListView) findViewById(R.id.activity_character_receive_list))
				.setAdapter(mArrayAdapter);
		((ListView) findViewById(R.id.activity_character_receive_list))
				.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_character_receive, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_info:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.info);
			builder.setMessage(R.string.character_receive_info);
			builder.show();
			return true;
		case R.id.menu_receive_character:
			// Begin listening for a character
			if (mReceiveTask == null
					|| mReceiveTask.getStatus().equals(
							AsyncTask.Status.FINISHED)) {
				mReceiveTask = new BluetoothReceiveTask(this);
			}
			if (mReceiveTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
				Toast.makeText(this, R.string.listening_for_chars_already,
						Toast.LENGTH_SHORT).show();
			} else {
				mReceiveTask.execute(BluetoothAdapter.getDefaultAdapter());
				Toast.makeText(this, R.string.listening_for_chars,
						Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		BluetoothAdapter blueAdp = BluetoothAdapter.getDefaultAdapter();
		if (!blueAdp.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, 0xfa10e);
		}
		super.onResume();
	}

	public void onCharacterReceive(PlayerCharacter character) {
		if (character == null) {
			// Notify the user of the failure
			Toast.makeText(this, "Character receipt failed.",
					Toast.LENGTH_SHORT).show();
		} else {
			// Notify the user of the success
			Toast.makeText(this, "Character receipt successful.",
					Toast.LENGTH_SHORT).show();
			mReceivedCharacters.add(character);
			mArrayAdapter.add(character.getName());
		}
	}

	public void onItemClick(AdapterView<?> parentSource, View source, int pos,
			long beh) {
		Toast.makeText(
				this,
				String.format("You have selected %s.",
						mReceivedCharacters.get(pos).getName()),
				Toast.LENGTH_SHORT).show();
		Intent result = new Intent();
		result.putExtra("result", (Parcelable) mReceivedCharacters.get(pos));
		setResult(RESULT_OK, result);
	}
}
