package org.elteano.charactersheet.bg.bluetooth;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.elteano.charactersheet.bg.wifid.PlayerCharacterSendCallback;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.json.JSONException;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

/**
 * AsyncTask for sending character information via Bluetooth.
 *
 * Requires input of BluetoothDevice and PlayerCharacter.
 */
public class BluetoothSendTask extends AsyncTask<Object, Integer, Integer> {

	/**
	 * Interface to notify upon success or failure of the receipt of the
	 * character.
	 */
	private PlayerCharacterSendCallback mCallback;

	public BluetoothSendTask(PlayerCharacterSendCallback callback) {
		mCallback = callback;
	}

	@Override
	protected Integer doInBackground(Object... arg0) {
		BluetoothDevice dest = (BluetoothDevice) arg0[0];
		PlayerCharacter send = (PlayerCharacter) arg0[1];
		Log.i("BluetoothSend", "dest: " + dest.getName());
		Log.i("BluetoothSend", "send: " + send.getName());
		BluetoothSocket sock;
		try {
			sock = dest.createRfcommSocketToServiceRecord(new UUID(420, 1739));
			sock.connect();
			Log.i("BluetoothSend", "Socket connected.");
			OutputStreamWriter out = new OutputStreamWriter(
					sock.getOutputStream());
			Log.i("BluetoothSend", "Stream created.");
			String json = "";
			try {
				json = send.writeToJSON().toString(2);
			} catch (JSONException ex) {
				json = send.writeToJSON().toString();
			}

			/*
			 * Bluetooth sends raw data, and so we need to mark the end of the
			 * stream manually.
			 */
			out.write(json + '\04');

			// If we don't flush, then more likely than not, nothing will be
			// sent.
			out.flush();
			Log.i("BluetoothSend", String.format("Sent %s.", send.getName()));

			/*
			 * Wait for a response from the other device before closing. If we
			 * don't wait before closing, then we will likely cause the other
			 * device to become incapable of reading the entire message.
			 */
			sock.getInputStream().read();
			out.close();
			// Return 0 for success
			return Integer.valueOf(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return 1 for failure
		return Integer.valueOf(1);
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		// Notify our callback that we have a result
		mCallback.onPlayerCharacterSend(result);
	}
}
