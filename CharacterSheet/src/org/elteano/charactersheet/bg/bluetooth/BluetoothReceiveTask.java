package org.elteano.charactersheet.bg.bluetooth;

import java.io.IOException;
import java.util.UUID;

import org.elteano.charactersheet.bg.wifid.PlayerCharacterReceiveCallback;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.json.JSONException;
import org.json.JSONObject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for receiving a character via Bluetooth.
 *
 * Requires a BluetoothAdapter as the only argument.
 */
public class BluetoothReceiveTask extends
		AsyncTask<BluetoothAdapter, Integer, PlayerCharacter> {

	/**
	 * Callback to notify upon receipt of the character.
	 */
	private PlayerCharacterReceiveCallback mCallback;

	/**
	 * Create new receive task, hooked up to the given callback receiver.
	 *
	 * @param callback
	 *            Object to notify upon receipt of character.
	 */
	public BluetoothReceiveTask(PlayerCharacterReceiveCallback callback) {
		mCallback = callback;
	}

	@Override
	protected PlayerCharacter doInBackground(BluetoothAdapter... params) {
		// Socket which will receive the connection
		BluetoothServerSocket serv = null;
		// Socket which will actually be used
		BluetoothSocket sock = null;
		try {
			// Get a listening socket
			Log.i("BluetoothReceive", "Opening sockets.");
			serv = params[0].listenUsingRfcommWithServiceRecord(
					"CharacterSheet", new UUID(420, 1739));
			// Get the receiving socket; time out after ten seconds
			Log.i("BluetoothReceive", "Listening.");
			try {
				sock = serv.accept(10000);
			} catch (IOException e) {
				Log.d("BluetoothReceive", "No connection established.");
				return null;
			}
			Log.i("BluetoothReceive", "Connection established.");
			if (sock != null) {
				/*
				 * Begin reading. There's probably a better way to do this, but
				 * wrapping it in a Reader caused problems, as the stream never
				 * ends.
				 */
				String app = "";
				try {
					int val = 0;
					while ((val = sock.getInputStream().read()) != -1) {
						if (val == 4)
							break;
						app += (char) val;
					}
				} catch (IOException ex) {
					// This may not be fatal.
					Log.i("BluetoothReceive", "Reading threw IOException.");
				}

				// Notify the sender that we've finished reading.
				sock.getOutputStream().write(1);
				PlayerCharacter ret = PlayerCharacter
						.createFromJSON(new JSONObject(app));
				Log.i("BluetoothReceive", "Received " + ret.getName());
				sock.close();
				serv.close();
				// Return the character
				return ret;
			}
			if (sock == null) {
				Log.e("BluetoothReceive", "Null socket :(");
			}
			// Socket is null, report error below
		} catch (IOException e) {
			Log.i("BluetoothReceive", "IOException: " + e.getMessage());
			e.printStackTrace();
			try {
				if (serv != null)
					serv.close();
			} catch (IOException two) {
				// Subtle irony here...
			}
		} catch (JSONException ex) {
			Log.e("BluetoothReceive",
					"Error creating JSON from Bluetooth input");
		}
		return null;
	}

	@Override
	protected void onPostExecute(PlayerCharacter result) {
		super.onPostExecute(result);
		// Forward the result to something that can deal with it properly
		mCallback.onCharacterReceive(result);
	}
}
