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
 * Requires input of BluetoothDevice and PlayerCharacter.
 *
 * @author emryn
 *
 */
public class BluetoothSendTask extends AsyncTask<Object, Integer, Integer> {

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
			Log.i("BluetoothSend", "Sending JSON: " + json);
			out.write(json + '\04');
			out.flush();
			Log.i("BluetoothSend", String.format("Sent %s.", send.getName()));
			sock.getInputStream().read();
			out.close();
			return Integer.valueOf(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Integer.valueOf(1);
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		mCallback.onPlayerCharacterSend(result);
	}
}
