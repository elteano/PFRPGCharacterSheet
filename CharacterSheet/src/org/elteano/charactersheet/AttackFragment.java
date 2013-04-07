package org.elteano.charactersheet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
		((EditText) getView().findViewById(R.id.fragment_attack_bab_field))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getBAB());
		updateCMB();
	}

	private void fillListings() {
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

	private void hookupListeners() {
		((EditText) getView().findViewById(R.id.fragment_attack_bab_field))
				.addTextChangedListener(babWatcher);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EDIT
				&& resultCode == Activity.RESULT_CANCELED)
			((CharacterSheetActivity) getActivity()).getCharacter().addAttack(
					lastAttack);
		if (resultCode == Activity.RESULT_OK) {
			((CharacterSheetActivity) getActivity()).getCharacter().addAttack(
					(Attack) data.getExtras().getParcelable(
							AttackEditActivity.RESULT_STRING));
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View source) {
		if (source instanceof TextView) {
			TextView tv = (TextView) source;
			String siftingIsFun = tv.getText().toString().split("\\(")[0]
					.trim();
			int i = siftingIsFun.lastIndexOf(" ");
			if (i < 0)
				return;
			String newStuff = siftingIsFun.substring(0,
					siftingIsFun.lastIndexOf(" ")).trim();
			Attack attack = ((CharacterSheetActivity) getActivity())
					.getCharacter().getAttack(newStuff);
			if (attack != null) {
				Intent intent = new Intent(getActivity(),
						AttackEditActivity.class);
				intent.putExtra("input", lastAttack = attack);
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
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

		};
		return inflater.inflate(R.layout.fragment_attack, container, false);
	}

	@Override
	public void onStop() {
		clearListings();
		unhookListeners();
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onStop();
	}

	@Override
	public void onStart() {
		fillListings();
		fillFields();
		hookupListeners();
		updateCMB();
		super.onStart();
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
