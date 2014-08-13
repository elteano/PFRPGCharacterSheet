package org.elteano.charactersheet.bg.wifid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.elteano.charactersheet.model.PlayerCharacter;

import android.os.AsyncTask;
import android.util.Log;

public class CharacterReceiveTask extends
		AsyncTask<String, Integer, PlayerCharacter> {

	public static final int SOCKET = 10490;

	@Override
	/**
	 * @param arg0 ignored
	 */
	protected PlayerCharacter doInBackground(String... arg0) {
		ServerSocket servSock = null;
		Socket socket = null;
		try {
			servSock = new ServerSocket(SOCKET);
			Log.i("CharacterSheet", "Server socket created: "
					+ servSock.getInetAddress().getHostAddress() + ":"
					+ servSock.getLocalPort());
			socket = servSock.accept();
			Log.i("CharacterSheet", "Connection accepted.");
			ObjectInputStream instream = new ObjectInputStream(
					socket.getInputStream());
			PlayerCharacter ret = (PlayerCharacter) instream.readObject();
			instream.close();
			socket.close();
			servSock.close();
			return ret;
		} catch (IOException ex) {
			Log.e("CharacterSheet", ex.getMessage());
		} catch (ClassNotFoundException ex) {
			Log.e("CharacterSheet", ex.getMessage());
		} finally {
			if (servSock != null) {
				if (servSock.isBound()) {
					try {
						servSock.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				}
			}
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
		return null;
	}
}
