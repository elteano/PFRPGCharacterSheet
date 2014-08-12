package org.elteano.charactersheet;

import java.util.ArrayList;

import android.app.Activity;
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
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
		}
		return super.onOptionsItemSelected(item);
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
