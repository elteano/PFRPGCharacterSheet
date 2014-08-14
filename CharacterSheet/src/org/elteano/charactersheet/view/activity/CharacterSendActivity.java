package org.elteano.charactersheet.view.activity;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.bg.wifid.CharacterReceiveTask;
import org.elteano.charactersheet.bg.wifid.CharacterTransferAsyncTask;
import org.elteano.charactersheet.bg.wifid.CharacterTransferHandler;
import org.elteano.charactersheet.bg.wifid.MiscellaneousWifiStateListener;
import org.elteano.charactersheet.model.PlayerCharacter;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CharacterSendActivity extends Activity implements OnClickListener,
		PeerListListener, MiscellaneousWifiStateListener,
		ConnectionInfoListener, WifiP2pManager.ActionListener {

	public static final String INPUT = "input";
	public static final String REQUEST = "request";
	public static final int MODE_RECEIVE = 2;
	public static final int MODE_SEND = 1;
	private TextView lastView;
	Channel mChannel;
	private ArrayList<WifiP2pDevice> displayed;
	private ArrayList<WifiP2pDevice> buffer;
	private boolean waitingDevs;
	private int mode;
	// Storage for info on current connection
	private WifiP2pInfo info;

	IntentFilter mIntentFilter;
	// WiFi stuff
	WifiP2pManager mManager;
	CharacterTransferHandler mReceiver;

	private PlayerCharacter sendingChar;

	/**
	 * Method to add a character to the listing. Currently misnamed due to the
	 * fact that it was copied from the SpellFragment class.
	 */
	public void createBuddyListing(WifiP2pDevice dev) {
		TextView destView = new TextView(this);
		destView.setText(dev.deviceName);
		destView.setClickable(true);
		destView.setTextSize(24);
		destView.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.activity_character_send_dest_holder))
				.addView(destView);
	}

	/*
	 * Initialize - but do not start - objects related to WiFi transactions.
	 */
	private void initializeWifi() {
		// Set up WiFi stuff
		mManager = (WifiP2pManager) this
				.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		mReceiver = new CharacterTransferHandler(mManager, mChannel, this,
				this, this);

		// Capture intents
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	public void onClick(View source) {
		if (lastView != null) {
			if (source instanceof TextView) {
				lastView.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
				lastView = (TextView) source;
				lastView.setBackgroundColor(getResources().getColor(
						android.R.color.holo_blue_light));
				startConnect(lastView.getText().toString());
			} else {
				switch (source.getId()) {
				case R.id.activity_character_send_cancel_button:
					setResult(RESULT_CANCELED);
					finish();
					break;
				}
			}
		} else if (source instanceof TextView) {
			lastView = (TextView) source;
			lastView.setBackgroundColor(getResources().getColor(
					android.R.color.holo_blue_light));
			startConnect(lastView.getText().toString());
		}
	}

	private void setOrientation() {
		int screenSizeFlag = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_NORMAL
				|| screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("CharacterSheet", "Entering CSA.onCreate()");
		if ((mode = getIntent().getIntExtra(REQUEST, MODE_RECEIVE)) == MODE_SEND) {
			sendingChar = (PlayerCharacter) getIntent().getExtras()
					.getParcelable(INPUT);
		}
		displayed = new ArrayList<WifiP2pDevice>();
		buffer = new ArrayList<WifiP2pDevice>();
		initializeWifi();
		setContentView(R.layout.activity_character_send);
		setOrientation();
		updateDisplayedList(null);
		Log.i("CharacterSheet", "Exiting CSA.onCreate()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_character_send, menu);
		return true;
	}

	@Override
	protected void onPause() {
		stopWifi();
		super.onPause();
	}

	public void onPeersAvailable(WifiP2pDeviceList list) {
		buffer.clear();
		// Add necessary items
		for (WifiP2pDevice dev : list.getDeviceList()) {
			buffer.add(dev);
			// If we aren't displaying this, then we need to notify
			if (!displayed.contains(dev)) {
				waitingDevs = true;
			}
		}
		if (waitingDevs) {
			((ImageButton) findViewById(R.id.activity_character_send_update_button))
					.setClickable(true);
			Toast.makeText(this, getResources().getString(R.string.new_peers),
					Toast.LENGTH_SHORT).show();
		}
		if (list.getDeviceList().isEmpty()) {
			((ImageButton) findViewById(R.id.activity_character_send_update_button))
					.setClickable(true);
			Toast.makeText(
					this,
					"You no longer have connection to any devices. Please refresh the list.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void updateDisplayedList(View src) {
		LinearLayout destHolder = (LinearLayout) findViewById(R.id.activity_character_send_dest_holder);
		// Tell the world we've updated the display
		waitingDevs = false;
		// Make the update button unpressable
		((ImageButton) findViewById(R.id.activity_character_send_update_button))
				.setClickable(false);
		// Clear the list of displayed items
		displayed.clear();
		// Clear the actual display
		destHolder.removeAllViews();
		// Populate the list of displayed items with the buffer list
		displayed.addAll(buffer);
		// Display all of the items
		for (WifiP2pDevice dev : displayed) {
			createBuddyListing(dev);
		}
	}

	@Override
	protected void onResume() {
		startWifi();
		super.onResume();
	}

	private void startConnect(String dest) {
		WifiP2pDevice device = null;
		final WifiP2pConfig config = new WifiP2pConfig();
		for (WifiP2pDevice dev : displayed) {
			if (dev.deviceName.equals(dest)) {
				device = dev;
				break;
			}
		}
		config.deviceAddress = device.deviceAddress;
		mManager.connect(mChannel, config, null);
	}

	private void startReceive() {
		Log.i("CharacterSheet", "Entering CSA.startReceive()");
		CharacterReceiveTask crt = new CharacterReceiveTask();
		crt.execute();
		Log.i("CharacterSheet", "CRT created");
		try {
			Log.i("CharacterSheet", "Trying to receive character.");
			crt.get().getName();
		} catch (InterruptedException e) {
			Log.e("CharacterSheet", "Character recieve interrupted.");
		} catch (ExecutionException e) {
			Log.e("CharacterSheet",
					"Character recieve has execution exception.");
		}
		Log.i("CharacterSheet", "Exiting CSA.startReceive()");
	}

	private void startTransfer(String dest) {
		Log.i("CharacterSheet", "Entering CSA.startTransfer()");
		int tries = 0;
		int res = 5;
		while (res != 0 && tries++ < 5) {
			Log.i("CharacterSheet", "Try number " + tries);
			CharacterTransferAsyncTask cst = new CharacterTransferAsyncTask();
			cst.execute(sendingChar, dest);
			try {
				res = cst.get();
				if (res == 0) {
					Toast.makeText(
							this,
							getResources()
									.getString(R.string.transfer_complete),
							Toast.LENGTH_SHORT).show();
					break;
				} else {
					Toast.makeText(this, "Character transfer failed.",
							Toast.LENGTH_SHORT).show();

				}
			} catch (InterruptedException e) {
				Log.e("CharacterSheet", "Character transfer interrupted.");
			} catch (ExecutionException e) {
				Log.e("CharacterSheet",
						"Character transfer execution exception.");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		Log.i("CharacterSheet", "Exiting CSA.startTransfer()");
	}

	/**
	 * Start WiFi stuff. Assumes <code>initializeWifi()</code> has been called.
	 */
	private void startWifi() {
		Log.i("CharacterSheet", "Entering CSA.startWifi()");
		registerReceiver(mReceiver, mIntentFilter);
		mManager.cancelConnect(mChannel, null);
		mManager.removeGroup(mChannel, null);
		mReceiver.ping();
		Log.i("CharacterSheet", "Exiting CSA.startWifi()");
	}

	private void stopWifi() {
		Log.i("CharacterSheet", "Entering CSA.stopWifi()");
		mManager.cancelConnect(mChannel, null);
		unregisterReceiver(mReceiver);
		Log.i("CharacterSheet", "Exiting CSA.stopWifi()");
	}

	public void tellWifiDirectState(boolean hasWifiDirect) {
		Log.i("CharacterSheet", "Entering CSA.tellWifiDirectState()");
		if (hasWifiDirect) {
			Toast.makeText(this, "You have Wi-Fi direct!", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(
					this,
					"You do not have Wi-Fi direct enabled! Please enable it to use this feature.",
					Toast.LENGTH_SHORT).show();
			buffer.clear();
			updateDisplayedList(null);
		}
		mReceiver.ping();
		Log.i("CharacterSheet", "Exiting CSA.tellWifiDirectState()");
	}

	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		Log.i("CharacterSheet", "Entering CSA.onConnectionInfoAvailable()");
		this.info = info;
		if (info.groupFormed) {
			Log.i("CharacterSheet",
					"Entering CSA.onConnectionInfoAvailable().info.groupFormed");
			switch (mode) {
			case MODE_SEND:
				Log.i("CharacterSheet",
						"Entering CSA.onConnectionInfoAvailable().MODE_SEND");
				Log.i("CharacterSheet", "Connecting to "
						+ info.groupOwnerAddress.getHostAddress());
				startTransfer(info.groupOwnerAddress.getHostAddress()
						.replaceAll("/", ""));
				break;
			case MODE_RECEIVE:
				Log.i("CharacterSheet",
						"Entering CSA.onConnectionInfoAvailable().MODE_RECEIVE");
				try {
					for (Enumeration<NetworkInterface> en = NetworkInterface
							.getNetworkInterfaces(); en.hasMoreElements();) {
						NetworkInterface netint = en.nextElement();
						for (Enumeration<InetAddress> en2 = netint
								.getInetAddresses(); en2.hasMoreElements();) {
							InetAddress ia = en2.nextElement();
							Log.i("CharacterSheet",
									"Address at " + ia.getHostAddress());
						}
					}
				} catch (SocketException ex) {
					Log.e("CharacterSheet", "Like what the hell");
				}
				Log.i("CharacterSheet", "Listening as "
						+ info.groupOwnerAddress.getHostAddress());
				startReceive();
				break;
			default:
				Log.i("CharacterSheet",
						"CSA.onConnectionInfoAvailable().default - shouldn't be here!");
				break;
			}
		}
		Log.i("CharacterSheet", "Exiting CSA.onConnectionInfoAvailable()");
	}

	public void onFailure(int reason) {
		Toast.makeText(this,
				"Not sure why this happened, but something failed.",
				Toast.LENGTH_SHORT);
	}

	public void onSuccess() {
	}
}
