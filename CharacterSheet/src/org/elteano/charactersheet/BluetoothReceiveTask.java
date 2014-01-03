package org.elteano.charactersheet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

public class BluetoothReceiveTask extends
		AsyncTask<BluetoothAdapter, Integer, PlayerCharacter> {

	private PlayerCharacterReceiveCallback mCallback;

	public BluetoothReceiveTask(PlayerCharacterReceiveCallback callback) {
		mCallback = callback;
	}

	@Override
	protected PlayerCharacter doInBackground(BluetoothAdapter... params) {
		// Socket which will receive the connection
		BluetoothServerSocket serv = null;
		// Socket which will actually be used
		BluetoothSocket sock = null;
		// Input stream for reading the character
		ObjectInputStream in = null;
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
				Log.d("BluetoothReceive", "Socket :(");
				e.printStackTrace();
			}
			Log.i("BluetoothReceive", "Done listening.");
			if (sock != null) {
				// Read in a character (which is being sent)
				in = new ObjectInputStream(sock.getInputStream());
				Log.i("BluetoothReceive", "Input stream created.");
				PlayerCharacter ret = (PlayerCharacter) in.readObject();
				Log.i("BluetoothReceive", "Received " + ret.getName());
				in.close();
				sock.close();
				serv.close();
				// Return the character
				return ret;
			}
			// Socket is null, report error below
		} catch (IOException e) {
			Log.i("BluetoothReceive", e.getMessage());
			e.printStackTrace();
			try {
				if (in != null)
					in.close();
				if (in != null)
					sock.close();
				if (serv != null)
					serv.close();
			} catch (IOException two) {
				// Subtle irony here...
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			try {
				in.close();
				sock.close();
				serv.close();
			} catch (IOException two) {
				// Like honestly what the hell
			}
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
