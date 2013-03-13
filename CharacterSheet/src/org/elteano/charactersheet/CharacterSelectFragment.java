package org.elteano.charactersheet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterSelectFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	public static final String CHARACTER_LIST_PREFERENCE = CharacterSheetActivity.PREFERENCES_PREFIX
			+ "CharacterNames";

	private SharedPreferences playerList;

	private PlayerCharacter addCharacter() {
		PlayerCharacter ret = new PlayerCharacter();
		int i = 1;
		while (playerList.contains("Character_" + i)) {
			i++;
		}
		ret.setName("New Character " + i);
		SharedPreferences.Editor editor = playerList.edit();
		editor.putString("Character_" + i, ret.getName());
		editor.commit();
		ret.saveToSharedPreferences(getActivity().getSharedPreferences(
				"Character_" + i, Activity.MODE_PRIVATE));
		CharacterSheetActivity.setCharacter(ret);
		// Line removed due to potential issues with other fragments
		// updateOthers();
		updateDisplay();
		return ret;
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

	private void clearList() {
		((LinearLayout) getView().findViewById(
				R.id.fragment_character_select_layout)).removeAllViews();
	}

	private void fillList(View dest) {
		String[] list = new String[playerList.getAll().size()];
		{
			int i = 0;
			for (String key : playerList.getAll().keySet()) {
				list[i] = playerList.getString(key, "ERROR");
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

	public void onClick(View view) {
		if (!(view instanceof TextView))
			return;
		TextView source = (TextView) view;
		for (String key : playerList.getAll().keySet()) {
			if (playerList.getString(key, "").equals(
					source.getText().toString())) {
				preUpdateOthers();
				CharacterSheetActivity.getCharacter().saveSelfByPlayerList(
						getActivity());
				CharacterSheetActivity.setCharacter(PlayerCharacter
						.restoreFromSharedPreferences(getActivity()
								.getSharedPreferences(key,
										Activity.MODE_PRIVATE)));
				getActivity().getActionBar().setTitle(
						CharacterSheetActivity.getCharacter().getName());
				postUpdateOthers();
				return;
			}
		}
		Log.e("CharacterSheet", "No character found for TextView!");
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
		fillList(ret);
		// if (CharacterSheetActivity.character == null)
		// setToFirstCharacter();
		// if (CharacterSheetActivity.character != null)
		// getActivity().getActionBar().setTitle(
		// CharacterSheetActivity.character.getName());
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
			if (CharacterSheetActivity.getCharacter() == null)
				Log.v("CharacterSheet", "No character selected; nothing done.");
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage(
						"Delete "
								+ CharacterSheetActivity.getCharacter()
										.getName() + "?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										preUpdateOthers();
										removeCharacter(CharacterSheetActivity
												.getCharacter().getName());
										removeCharacterListing(CharacterSheetActivity
												.getCharacter().getName());
										if (playerList.getAll().isEmpty())
											addCharacterListing(addCharacter());
										else {
											CharacterSheetActivity
													.setCharacter(PlayerCharacter
															.restoreFromSharedPreferences(getActivity()
																	.getSharedPreferences(
																			(String) playerList
																					.getAll()
																					.keySet()
																					.toArray()[0],
																			Activity.MODE_PRIVATE)));
											getActivity()
													.getActionBar()
													.setTitle(
															CharacterSheetActivity
																	.getCharacter()
																	.getName());
											postUpdateOthers();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				builder.create().show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean removeCharacter(int ident) {
		if (playerList.contains("Character_" + ident)) {
			SharedPreferences.Editor editor = playerList.edit();
			editor.remove("Character_" + ident);
			editor.commit();
			SharedPreferences removeThis = getActivity().getSharedPreferences(
					"Character_" + ident, Activity.MODE_PRIVATE);
			editor = removeThis.edit();
			editor.clear();
			editor.commit();
			return true;
		}
		return false;
	}

	private boolean removeCharacter(String characterName) {
		for (String key : playerList.getAll().keySet()) {
			if (playerList.getString(key, "thisisNotacharact3r").equals(
					characterName)) {
				return removeCharacter(Integer.parseInt(key.replace(
						"Character_", "")));
			}
		}
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
		if (CharacterSheetActivity.getCharacter() == null)
			setToFirstCharacter();
		getActivity().getActionBar().setTitle(
				CharacterSheetActivity.getCharacter().getName());
	}

	@Override
	public void onStop() {
		if (CharacterSheetActivity.getCharacter() == null)
			setToFirstCharacter();
		super.onStop();
	}

	private void setToFirstCharacter() {
		if (playerList.getAll().isEmpty())
			CharacterSheetActivity.setCharacter(addCharacter());
		else
			CharacterSheetActivity.setCharacter(PlayerCharacter
					.restoreFromSharedPreferences(getActivity()
							.getSharedPreferences(
									(String) playerList.getAll().keySet()
											.toArray()[0],
									Activity.MODE_PRIVATE)));
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
