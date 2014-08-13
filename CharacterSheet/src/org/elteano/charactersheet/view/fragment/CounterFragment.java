package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.CounterListing;
import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.R.menu;
import org.elteano.charactersheet.model.Counter;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.activity.CounterEditActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class CounterFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private Counter lastCounter;

	private void addCounterListing(Counter c) {
		CounterListing cl = new CounterListing(getActivity(), null);
		cl.setCounter(c);
		cl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		cl.setEditButtonListener(this);
		((LinearLayout) getActivity().findViewById(R.id.counter_holder))
				.addView(cl);
	}

	private void clearListings() {
		((LinearLayout) getView().findViewById(R.id.counter_holder))
				.removeAllViews();
	}

	private void fillListings() {
		ArrayList<Counter> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getCounterList();
		Log.d("CharacterSheet", "CounterFragment list size: " + list.size());
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).getName()
						.compareTo(list.get(swapWith).getName()) < 0)
					swapWith = j;
			}
			Counter s = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, s);
		}
		for (Counter s : list) {
			Log.d("CharacterSheet", "Adding " + s.getName());
			addCounterListing(s);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("CharacterSheet", "CounterFragment activity result");
		if (requestCode == CounterEditActivity.REQUEST_EDIT_COUNTER
				&& resultCode != Activity.RESULT_CANCELED)
			((CharacterSheetActivity) getActivity()).getCharacter()
					.removeCounter(lastCounter);
		if (resultCode == Activity.RESULT_OK) {
			Log.d("CharacterSheet", "CounterFragment RESULT_OK");
			lastCounter = null;
			Counter result = data
					.getParcelableExtra(CounterEditActivity.RESULT_COUNTER);
			((CharacterSheetActivity) getActivity()).getCharacter().addCounter(
					result);
			Log.d("CharacterSheet", result.toSaveString());
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View source) {
		Intent intent = new Intent(getActivity(), CounterEditActivity.class);
		lastCounter = ((CounterListing) source.getParent()).getCounter();
		intent.putExtra(CounterEditActivity.INPUT_COUNTER,
				(Parcelable) lastCounter);
		startActivityForResult(intent, CounterEditActivity.REQUEST_EDIT_COUNTER);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_counter_menu_large, menu);
		else
			inflater.inflate(R.menu.fragment_counter_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_counter, container, false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_counter:
			Intent intent = new Intent(getActivity(), CounterEditActivity.class);
			startActivityForResult(intent,
					CounterEditActivity.REQUEST_NEW_COUNTER);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		if (getActivity().findViewById(R.id.counter_holder) != null) {
			clearListings();
			fillListings();
		} else {
			Log.i("CharacterSheet",
					"In CounterFragment.onResume(), counter_holder was null! This has been handled.");
		}
		super.onResume();
	}

	@Override
	public void preUpdate() {
		// Unused
	}

	@Override
	public void updateDisplay() {
		clearListings();
		fillListings();
	}
}
