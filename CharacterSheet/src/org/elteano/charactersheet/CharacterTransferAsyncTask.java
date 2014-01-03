package org.elteano.charactersheet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class CharacterTransferAsyncTask extends
		AsyncTask<Object, Integer, Integer> {

	/**
	 * @param params
	 *            First argument is PlayerCharacter to transfer, second arg is
	 *            String denoting host address
	 */
	@Override
	protected Integer doInBackground(Object... params) {
		PlayerCharacter trans = (PlayerCharacter) params[0];
		String host = (String) params[1];
		Socket socket = new Socket();
		int port = CharacterReceiveTask.SOCKET;
		try {
			socket.bind(null);
			socket.connect(new InetSocketAddress(host, port), 5000);
			ObjectOutputStream outstream = new ObjectOutputStream(
					socket.getOutputStream());
			outstream.writeObject(trans);
			outstream.close();
			socket.close();
			return 0;
		} catch (IOException ex) {
			Log.e("CharacterSheet", ex.getMessage());
		} finally {
			if (socket != null) {
				if (socket.isConnected()) {
					try {
						socket.close();
					} catch (IOException e) {
						// catch logic
					}
				}
			}
		}
		return 1;
	}
}
