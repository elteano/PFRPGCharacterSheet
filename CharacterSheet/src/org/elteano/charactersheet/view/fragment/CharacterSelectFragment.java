package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.activity.BluetoothTransferActivity;
import org.elteano.charactersheet.view.activity.CharacterReceiveActivity;
import org.elteano.charactersheet.view.activity.CharacterSelectActivity;
import org.elteano.charactersheet.view.activity.CharacterSendActivity;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CharacterSelectFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	public static final String CHARACTER_LIST_PREFERENCE = CharacterSheetActivity.PREFERENCES_PREFIX
			+ "CharacterNames";

	private SharedPreferences playerList;
	private boolean deleting = false;
	private boolean selectingSend = false;

	private PlayerCharacter addCharacter() {
		PlayerCharacter ret = new PlayerCharacter();
		int i = getNextAvailableNumber();
		ret.setName("New Character " + i);
		saveNewCharacter(ret, i);
		Intent result = new Intent();
		result.putExtra("result", ret.getName());
		((CharacterSelectActivity) getActivity()).setResult(Activity.RESULT_OK,
				result);
		// Line removed due to potential issues with other fragments
		// updateOthers();
		updateDisplay();
		return ret;
	}

	private int getNextAvailableNumber() {
		int i = 1;
		while (playerList.contains("Character_" + i)) {
			i++;
		}
		return i;
	}

	private void saveNewCharacter(PlayerCharacter c, int i) {
		c.saveToSharedPreferences(playerList);
	}

	private void addCharacterListing(PlayerCharacter character) {
		addCharacterListing(character, getView());
	}

	private void addCharacterListing(PlayerCharacter character, View dest) {
		addCharacterListing(character.getName(), dest);
	}

	private void addCharacterListing(String name, View dest) {
		TextView characterView = new TextView(getActivity());
		characterView.setText(name);
		characterView.setTextSize(24);
		characterView.setClickable(true);
		characterView
				.setBackgroundResource(android.R.drawable.list_selector_background);
		characterView.setOnClickListener(this);
		((LinearLayout) dest
				.findViewById(R.id.fragment_character_select_layout))
				.addView(characterView);
	}

	/**
	 * Cancel a delete, if necessary. If a delete is canceled, then text is
	 * displayed stating that a delete was canceled.
	 *
	 * @return True if a delete was canceled.
	 */
	private boolean cancelDelete() {
		if (deleting) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.prompt_cancel),
					Toast.LENGTH_SHORT).show();
			deleting = false;
			return true;
		}
		return false;
	}

	private void clearList() {
		((LinearLayout) getView().findViewById(
				R.id.fragment_character_select_layout)).removeAllViews();
	}

	/**
	 * Updates all characters to the newest save format.
	 */
	private void updateSaveData() {
		for (String key : playerList.getAll().keySet()) {
			if (key.startsWith("Character_")) {
				PlayerCharacter updateme = PlayerCharacter.restoreByPlayerList(
						getActivity(), playerList.getString(key, "ERROR"));
				updateme.saveToSharedPreferences(playerList);
				SharedPreferences.Editor editor = playerList.edit();
				editor.remove(key);
				editor.commit();
				SharedPreferences removeThis = getActivity()
						.getSharedPreferences(key, Activity.MODE_PRIVATE);
				editor = removeThis.edit();
				editor.clear();
				editor.commit();
			}
		}
	}

	private void fillList(View dest) {
		String[] list = new String[playerList.getAll().size()];
		{
			int i = 0;
			for (String key : playerList.getAll().keySet()) {
				String charName = key;
				if (charName.startsWith("Character_"))
					charName = playerList.getString(key, "ERROR");
				list[i] = charName;
				i++;
			}
		}
		for (int i = 0; i < list.length - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.length; j++) {
				if (list[j].compareTo(list[swapWith]) < 0)
					swapWith = j;
			}
			String s = list[i];
			list[i] = list[swapWith];
			list[swapWith] = s;
		}
		for (String characterName : list) {
			addCharacterListing(characterName, dest);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Assuming it was CharacterReceive
		if (resultCode == Activity.RESULT_OK) {
			PlayerCharacter recChar = data.getParcelableExtra("result");
			saveNewCharacter(recChar, getNextAvailableNumber());
			updateDisplay();
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	public void onClick(View view) {
		if (!(view instanceof TextView))
			return;
		TextView source = (TextView) view;
		if (deleting) {
			promptDeleteCharacter(source.getText().toString());
			deleting = false;
		} else if (selectingSend) {
			PlayerCharacter c = PlayerCharacter.restoreByPlayerList(
					getActivity(), source.getText().toString());
			Intent intent = new Intent(getActivity(),
					BluetoothTransferActivity.class);
			intent.putExtra(BluetoothTransferActivity.INPUT, (Parcelable) c);
			startActivityForResult(intent, BluetoothTransferActivity.MODE_SEND);
			selectingSend = false;
		} else {
			((CharacterSelectActivity) getActivity()).setResultName(source
					.getText().toString());
			((CharacterSelectActivity) getActivity()).setCharacterSelected();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_character_select_menu_large, menu);
		else
			inflater.inflate(R.menu.fragment_character_select_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		playerList = getActivity().getSharedPreferences(
				CHARACTER_LIST_PREFERENCE, Activity.MODE_PRIVATE);
		View ret = inflater.inflate(R.layout.fragment_character_select,
				container, false);
		updateSaveData();
		fillList(ret);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_character_select_menu_add_character:
			preUpdateOthers();
			addCharacter();
			postUpdateOthers();
			return true;
		case R.id.fragment_character_select_menu_remove_character:
			if (!cancelDelete()) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.prompt_delete),
						Toast.LENGTH_SHORT).show();
				deleting = true;
			}
			return true;
		case R.id.fragment_character_select_menu_send_character:
			if (cancelDelete()) {
				// In the event we want a Toast (unlikely as that may be)
			}
			selectingSend = true;
			Toast.makeText(getActivity(), "Select a character to send.",
					Toast.LENGTH_SHORT).show();
			return true;
		case R.id.fragment_character_select_menu_receive_character:
			Intent intent = new Intent(getActivity(),
					CharacterReceiveActivity.class);
			startActivityForResult(intent, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void promptDeleteCharacter(String characterName) {
		final String name = characterName;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete " + name + "?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								preUpdateOthers();
								removeCharacter(name);
								removeCharacterListing(name);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	private boolean removeCharacter(String characterName) {
		if (playerList.contains(characterName)) {
			SharedPreferences.Editor editor = playerList.edit();
			editor.remove(characterName);
			editor.commit();
			return true;
		} else
			return false;
	}

	private boolean removeCharacterListing(String characterName) {
		LinearLayout container = (LinearLayout) getView().findViewById(
				R.id.fragment_character_select_layout);
		for (int i = 0; i < container.getChildCount(); i++) {
			TextView tv = (TextView) container.getChildAt(i);
			if (tv.getText().toString().equals(characterName)) {
				container.removeView(tv);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void setToFirstCharacter() {
		if (playerList.getAll().isEmpty())
			((CharacterSheetActivity) getActivity())
					.setCharacter(addCharacter());
		else
			((CharacterSheetActivity) getActivity())
					.setCharacter(PlayerCharacter
							.restoreFromSharedPreferences(getActivity()
									.getSharedPreferences(
											(String) playerList.getAll()
													.keySet().toArray()[0],
											Activity.MODE_PRIVATE)));
	}

	private void sendCharacter(PlayerCharacter c) {
		Intent intent = new Intent(getActivity(), CharacterSendActivity.class);
		intent.putExtra(CharacterSendActivity.INPUT, (Parcelable) c);
		startActivityForResult(intent, BluetoothTransferActivity.MODE_SEND);
	}

	@Override
	public void preUpdate() {
		// Do nothing
	}

	@Override
	public void updateDisplay() {
		clearList();
		fillList(getView());
	}
}
