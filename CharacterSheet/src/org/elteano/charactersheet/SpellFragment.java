package org.elteano.charactersheet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpellFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private static final int REQUEST_NEW = 0;
	private static final int REQUEST_EDIT = 1;
	private Spell lastSpell;
	private TextView lastView;

	public void clearSpells() {
		clearKnownSpells();
		clearPrepSpells();
	}

	public void clearKnownSpells() {
		LinearLayout knownList = ((LinearLayout) getView().findViewById(
				R.id.fragment_spells_full_list_line));
		if (lastView != null && lastView.getParent() != null
				&& lastView.getParent().equals(knownList))
			lastView = null;
		knownList.removeAllViews();
	}

	public void clearPrepSpells() {
		LinearLayout prepList = ((LinearLayout) getView().findViewById(
				R.id.fragment_spells_prep_list_line));
		if (lastView != null && lastView.getParent() != null
				&& lastView.getParent().equals(prepList))
			lastView = null;
		prepList.removeAllViews();
	}

	public void createSpellListing(Spell spell, boolean goToPrepList) {
		TextView newSpellView = new TextView(getActivity());
		newSpellView.setText(spell.toString());
		newSpellView.setClickable(true);
		newSpellView.setTextSize(24);
		newSpellView.setOnClickListener(this);
		if (goToPrepList)
			((LinearLayout) getView().findViewById(
					R.id.fragment_spells_prep_list_line)).addView(newSpellView);
		else
			((LinearLayout) getView().findViewById(
					R.id.fragment_spells_full_list_line)).addView(newSpellView);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		int[] inds = new int[0];
		if (requestCode == REQUEST_EDIT
				&& resultCode != Activity.RESULT_CANCELED) {
			inds = findAllIndices(((CharacterSheetActivity) getActivity())
					.getCharacter().getPrepSpells(), lastSpell);
			((CharacterSheetActivity) getActivity()).getCharacter().getSpells()
					.remove(lastSpell);
			for (int i = inds.length - 1; i >= 0; i--) {
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getPrepSpells().remove(inds[i]);
			}
		}
		switch (resultCode) {
		case Activity.RESULT_CANCELED:
			break;
		case Activity.RESULT_OK:
			((CharacterSheetActivity) getActivity()).getCharacter().getSpells()
					.add((Spell) data.getExtras().getParcelable("result"));
			for (int i = 0; i < inds.length; i++)
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getPrepSpells()
						.add((Spell) data.getExtras().getParcelable("result"));
			break;
		case SpellEditActivity.RESULT_DELETE:
			break;
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View v) {
		if (lastView != null && lastSpell != null)
			switch (v.getId()) {
			case R.id.fragment_spells_add_button:
				if (lastView.getParent().equals(
						(LinearLayout) getView().findViewById(
								R.id.fragment_spells_full_list_line))) {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getPrepSpells().add(lastSpell);
					clearPrepSpells();
					fillPrepSpells();
					((CharacterSheetActivity) getActivity()).getCharacter()
							.saveSelfByPlayerList(getActivity());
				}
				return;
			case R.id.fragment_spells_remove_button:
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getPrepSpells().remove(lastSpell);
				if (lastView.getParent().equals(
						getView().findViewById(
								R.id.fragment_spells_prep_list_line))) {
					lastSpell = null;
					lastView = null;
				}
				clearPrepSpells();
				fillPrepSpells();
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
				return;
			}
		if (v instanceof TextView && !(v instanceof Button)) {
			TextView tv = (TextView) v;
			String name = tv.getText().toString()
					.substring(tv.getText().toString().indexOf('-') + 1).trim();
			for (Spell s : ((CharacterSheetActivity) getActivity())
					.getCharacter().getSpells()) {
				if (s.name.equals(name)) {
					if (v.equals(lastView)) {
						Intent intent = new Intent(getActivity(),
								SpellEditActivity.class);
						intent.putExtra("input", (Parcelable) lastSpell);
						intent.putExtra(SpellEditActivity.SPELL_ABILITY_IN,
								((CharacterSheetActivity) getActivity())
										.getCharacter().getAbilities());
						startActivityForResult(intent, REQUEST_EDIT);
						return;
					}
					if (lastView != null)
						lastView.setBackgroundColor(getActivity()
								.getResources().getColor(
										android.R.color.transparent));
					// lastView.setBackgroundResource(android.R.drawable.list_selector_background);
					lastView = tv;
					tv.setBackgroundColor(getActivity().getResources()
							.getColor(android.R.color.holo_blue_light));
					lastSpell = s;
					return;
				}
			}
			((CharacterSheetActivity) getActivity()).getCharacter()
					.saveSelfByPlayerList(getActivity());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_spells, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_spells, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		((Button) getView().findViewById(R.id.fragment_spells_add_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(R.id.fragment_spells_remove_button))
				.setOnClickListener(this);
		clearSpells();
		fillSpells();
	}

	@Override
	public void onPause() {
		lastView = null;
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_spells_add_spell:
			Intent intent = new Intent(getActivity(), SpellEditActivity.class);
			intent.putExtra(SpellEditActivity.SPELL_ABILITY_IN,
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getAbilities());
			startActivityForResult(intent, REQUEST_NEW);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private static int[] findAllIndices(ArrayList<? extends Object> list,
			Object obj) {
		int[] ret;
		int length = 0;
		for (Object o : list)
			if (o.equals(obj))
				length++;
		ret = new int[length];
		length = 0;
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).equals(obj))
				ret[length++] = i;
		return ret;
	}

	public void fillSpells() {
		fillKnownSpells();
		fillPrepSpells();
	}

	public void fillKnownSpells() {
		ArrayList<Spell> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getSpells();
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).toString()
						.compareTo(list.get(swapWith).toString()) < 0)
					swapWith = j;
			}
			Spell s = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, s);
		}
		for (Spell s : list) {
			createSpellListing(s, false);
		}
	}

	public void fillPrepSpells() {
		ArrayList<Spell> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getPrepSpells();
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).toString()
						.compareTo(list.get(swapWith).toString()) < 0)
					swapWith = j;
			}
			Spell s = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, s);
		}
		for (Spell s : list) {
			createSpellListing(s, true);
		}
	}

	@Override
	public void updateDisplay() {
		clearSpells();
		fillSpells();
	}

	@Override
	public void preUpdate() {
		// Unused
	}
}
