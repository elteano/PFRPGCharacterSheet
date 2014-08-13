package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.R.menu;
import org.elteano.charactersheet.model.Attack;
import org.elteano.charactersheet.model.CMB;
import org.elteano.charactersheet.model.Feat;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.model.PlayerClass;
import org.elteano.charactersheet.view.activity.AttackEditActivity;
import org.elteano.charactersheet.view.activity.CMBEditActivity;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttackFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private static final int REQUEST_NEW = 0;
	private static final int REQUEST_EDIT = 1;
	private static final int REQUEST_CMB = 2;

	private Attack lastAttack;
	private TextWatcher babWatcher;

	public void addAttackListing(Attack attack) {
		TextView tv = new TextView(getActivity());
		tv.setText(attack.toDescriptionString(
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getAbilities(),
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getBAB()));
		tv.setClickable(true);
		tv.setWidth(0);
		tv.setTextSize(24);
		tv.setOnClickListener(this);
		tv.setBackgroundResource(android.R.drawable.list_selector_background);
		((LinearLayout) getView().findViewById(R.id.fragment_attack_list))
				.addView(tv);
	}

	private void clearListings() {
		((LinearLayout) getView().findViewById(R.id.fragment_attack_list))
				.removeAllViews();
	}

	private void fillFields() {
		int bab = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getBAB();
		((EditText) getView().findViewById(R.id.fragment_attack_bab_field))
				.setText((bab != 0) ? "" + bab : "");
		updateLongBab();
		updateCMB();
	}

	private void updateLongBab() {
		int bab = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getBAB();
		String fulltext = "";
		if (bab > 0) {
			fulltext = "+";
			for (; bab > 0; bab -= 5) {
				fulltext += bab + " / +";
			}
			fulltext = (fulltext.length() > 4) ? fulltext.substring(0,
					fulltext.length() - 4) : "";
		} else {
			fulltext = "" + bab;
		}
		((TextView) getView().findViewById(R.id.fragment_attack_bab_full))
				.setText(fulltext);
	}

	private void fillListings() {
		if (!getMonkFlurry().isEmpty()) {
			TextView tv = new TextView(getActivity());
			tv.setWidth(0);
			tv.setTextSize(24);
			tv.setText("Flurry of Blows\n" + getMonkFlurry());
			((LinearLayout) getView().findViewById(R.id.fragment_attack_list))
					.addView(tv);
		}
		ArrayList<Attack> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getAttackList();
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).name.compareTo(list.get(swapWith).name) < 0)
					swapWith = j;
			}
			Attack a = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, a);
		}
		for (Attack a : list) {
			addAttackListing(a);
		}
	}

	/*
	 * Calculates the string to be printed for the Monk's flurry of blows.
	 */
	private String getMonkFlurry() {
		String ret = "";
		for (PlayerClass c : ((CharacterSheetActivity) getActivity())
				.getCharacter().getPlayerClasses()) {
			if (c.getName().equalsIgnoreCase("monk")) {
				int abilityBonus = ((CharacterSheetActivity) getActivity())
						.getCharacter().getAbilities()
						.getAbility(PlayerCharacter.ABILITY_STR)
						.getTempModifier();
				// Determine if they have weapon finesse, which makes these
				// attacks get bonus based on dex instead of str
				for (Feat f : ((CharacterSheetActivity) getActivity())
						.getCharacter().getFeatList()) {
					if (f.getName().equalsIgnoreCase("weapon finesse")) {
						abilityBonus = ((CharacterSheetActivity) getActivity())
								.getCharacter().getAbilities()
								.getAbility(PlayerCharacter.ABILITY_DEX)
								.getTempModifier();
						break;
					}
				}
				int level = c.getLevels();
				int doubledAttacks = 0;
				if (level < 8)
					doubledAttacks = 1; // Double only one attack
				else if (level < 15)
					doubledAttacks = 2; // Double two attacks
				else
					doubledAttacks = 3; // Double three attacks
				String appendString;
				int attack = 0;
				for (; level > 0; level -= 5) {
					attack = level - 2 + abilityBonus;
					appendString = ((attack < 0) ? "" + (attack) : "+"
							+ (attack))
							+ "/";
					ret += appendString;
					if (doubledAttacks-- > 0)
						ret += appendString;
				}
			}
		}
		// Chop off the trailing /
		if (ret.length() > 0)
			ret = ret.substring(0, ret.length() - 1);
		return ret;
	}

	private void hookupListeners() {
		((EditText) getView().findViewById(R.id.fragment_attack_bab_field))
				.addTextChangedListener(babWatcher);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EDIT
				&& resultCode == Activity.RESULT_CANCELED) {
			((CharacterSheetActivity) getActivity()).getCharacter().addAttack(
					lastAttack);
		}
		if (requestCode == REQUEST_EDIT || requestCode == REQUEST_NEW) {
			if (resultCode == Activity.RESULT_OK) {
				((CharacterSheetActivity) getActivity()).getCharacter()
						.addAttack(
								(Attack) data.getExtras().getParcelable(
										AttackEditActivity.RESULT_STRING));
			}
		} else if (requestCode == REQUEST_CMB) {
			if (resultCode == Activity.RESULT_OK)
				((CharacterSheetActivity) getActivity()).getCharacter().setCMB(
						(CMB) data.getExtras().getParcelable(
								CMBEditActivity.OUTPUT));
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View source) {
		if (source.getId() == R.id.fragment_attack_cmb_button) {
			Intent intent = new Intent(getActivity(), CMBEditActivity.class);
			intent.putExtra(CMBEditActivity.INPUT_ABILITIES,
					(Parcelable) ((CharacterSheetActivity) getActivity())
							.getCharacter().getAbilities());
			intent.putExtra(CMBEditActivity.INPUT_BAB,
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getBAB());
			intent.putExtra(CMBEditActivity.INPUT_SIZE,
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getSize());
			intent.putExtra(CMBEditActivity.INPUT_CMB,
					(Parcelable) ((CharacterSheetActivity) getActivity())
							.getCharacter().getCMBParc());
			intent.putExtra(CMBEditActivity.INPUT_CLASSES,
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getPlayerClasses().toArray(new PlayerClass[0]));
			startActivityForResult(intent, REQUEST_CMB);
		} else if (source instanceof TextView) {
			TextView tv = (TextView) source;
			String[] blocks = tv.getText().toString().split("\\(");
			String siftingIsFun = "";
			for (int i = 0; i < blocks.length - 1; i++) {
				siftingIsFun += "(" + blocks[i];
			}
			siftingIsFun = siftingIsFun.substring(1);
			Log.i("CharacterSheet", "AttackFragment sifting: " + siftingIsFun);
			siftingIsFun = siftingIsFun.trim();
			int i = siftingIsFun.lastIndexOf(" ");
			if (i < 0) {
				Log.i("CharacterSheet", "Weird blocker");
				return;
			}
			String newStuff = siftingIsFun.substring(0,
					siftingIsFun.lastIndexOf(" ")).trim();
			Attack attack = ((CharacterSheetActivity) getActivity())
					.getCharacter().getAttack(newStuff);
			if (attack != null) {
				Intent intent = new Intent(getActivity(),
						AttackEditActivity.class);
				intent.putExtra("input", (Parcelable) (lastAttack = attack));
				((CharacterSheetActivity) getActivity()).getCharacter()
						.removeAttack(attack);
				startActivityForResult(intent, REQUEST_EDIT);
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_attack, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		babWatcher = new TextWatcher() {

			public void afterTextChanged(Editable text) {
				try {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setBAB(Integer.parseInt(text.toString()));
				} catch (NumberFormatException ex) {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setBAB(0);
				}
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
				((LinearLayout) getView().findViewById(
						R.id.fragment_attack_list)).removeAllViews();
				fillListings();
				updateCMB();
				updateOthers();
				updateLongBab();
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

		};
		View ret = inflater.inflate(R.layout.fragment_attack, container, false);
		((Button) ret.findViewById(R.id.fragment_attack_cmb_button))
				.setOnClickListener(this);
		return ret;
	}

	@Override
	public void onPause() {
		unhookListeners();
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onPause();
	}

	@Override
	public void onResume() {
		clearListings();
		fillListings();
		fillFields();
		hookupListeners();
		updateCMB();
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_attack_add:
			Intent intent = new Intent(getActivity(), AttackEditActivity.class);
			startActivityForResult(intent, REQUEST_NEW);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void unhookListeners() {
		((EditText) getView().findViewById(R.id.fragment_attack_bab_field))
				.removeTextChangedListener(babWatcher);
	}

	private void updateCMB() {
		int cmb = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getCMB();
		String text = ((cmb > 0) ? "+" : "") + cmb;
		((Button) getView().findViewById(R.id.fragment_attack_cmb_button))
				.setText("" + text);
	}

	@Override
	public void preUpdate() {
		// Unused
	}

	@Override
	public void updateDisplay() {
		unhookListeners();
		clearListings();
		fillListings();
		fillFields();
		hookupListeners();
	}
}
