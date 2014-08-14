package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.Feat;
import org.elteano.charactersheet.view.activity.CFeatEditActivity;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CFeatFragment extends CharacterUpdaterFragment implements
		OnClickListener {
	private TextView lastSelectedView;

	private void clearListings() {
		((LinearLayout) getView().findViewById(R.id.fragment_cfeat_layout))
				.removeAllViewsInLayout();
	}

	private void createFeatListing(String featName) {
		TextView newFeatView = new TextView(getActivity());
		newFeatView.setText(featName);
		newFeatView.setClickable(true);
		newFeatView.setTextSize(24);
		newFeatView.setOnClickListener(this);
		newFeatView
				.setBackgroundResource(android.R.drawable.list_selector_background);
		((LinearLayout) getView().findViewById(R.id.fragment_cfeat_layout))
				.addView(newFeatView);
	}

	private void fillListings() {
		ArrayList<Feat> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getCFeatList();
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).getName()
						.compareTo(list.get(swapWith).getName()) < 0)
					swapWith = j;
			}
			Feat f = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, f);
		}
		for (Feat f : list) {
			createFeatListing(f.getName());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != CFeatEditActivity.RESULT_CANCELED) {
			if (lastSelectedView != null)
				((CharacterSheetActivity) getActivity()).getCharacter()
						.removeCFeatByName(
								lastSelectedView.getText().toString());
			lastSelectedView = null;
		}
		if (resultCode == Activity.RESULT_OK) {
			String name = data.getExtras().getString(
					CFeatEditActivity.RESULT_KEY_NAME);
			((CharacterSheetActivity) getActivity())
					.getCharacter()
					.getCFeatList()
					.add(new Feat(name, data.getExtras().getString(
							CFeatEditActivity.RESULT_KEY_DESC)));
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
	}

	public void onClick(View view) {
		if (!(view instanceof TextView))
			return;
		lastSelectedView = (TextView) view;
		Intent intent = new Intent(getActivity(), CFeatEditActivity.class);
		intent.putExtra(CFeatEditActivity.INPUT_KEY_NAME, lastSelectedView
				.getText().toString());
		intent.putExtra(CFeatEditActivity.INPUT_KEY_DESC,
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getCFeat(lastSelectedView.getText().toString())
						.getDescription());
		startActivityForResult(intent, CFeatEditActivity.REQUEST_EDIT_FEAT);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_cfeat_menu_large, menu);
		else
			inflater.inflate(R.menu.fragment_cfeat_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_cfeat, container, false);
		setHasOptionsMenu(true);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_cfeat:
			lastSelectedView = null;
			Intent intent = new Intent(getActivity(), CFeatEditActivity.class);
			startActivityForResult(intent, CFeatEditActivity.REQUEST_NEW_FEAT);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		clearListings();
		fillListings();
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