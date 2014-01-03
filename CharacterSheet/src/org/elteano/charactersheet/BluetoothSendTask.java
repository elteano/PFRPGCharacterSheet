package org.elteano.charactersheet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

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
			ObjectOutputStream out = new ObjectOutputStream(
					sock.getOutputStream());
			Log.i("BluetoothSend", "Stream created.");
			out.writeObject(send);
			Log.i("BluetoothSend", String.format("Sent %s.", send.getName()));
			out.flush();
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
