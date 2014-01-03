package org.elteano.charactersheet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class CharacterTransferHandler extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private PeerListListener mListener;
	private MiscellaneousWifiStateListener mMiscList;
	private ConnectionInfoListener mConInListener;

	public CharacterTransferHandler(WifiP2pManager manager, Channel channel,
			PeerListListener listener, MiscellaneousWifiStateListener miscList,
			ConnectionInfoListener conInListener) {
		super();
		mManager = manager;
		mChannel = channel;
		mListener = listener;
		mMiscList = miscList;
		mConInListener = conInListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Inform the listener that we have Wi-Fi Direct
				mMiscList.tellWifiDirectState(true);
			} else {
				// Inform the listener that we do not have Wi-Fi direct
				mMiscList.tellWifiDirectState(false);
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			mManager.requestPeers(mChannel, mListener);
			// Call WifiP2pManager.requestPeers() to get a list of current peers
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			// Respond to new connection or disconnections
			NetworkInfo netInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
			mManager.requestConnectionInfo(mChannel, mConInListener);
			Log.i("CharacterSheet",
					"CTH.onReceive().WIFI_P2P_CONNECTION_CHANGED_ACTION");
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			// Respond to this device's wifi state changing
		}
	}

	public void ping() {
		mManager.discoverPeers(mChannel, null);
	}
}
