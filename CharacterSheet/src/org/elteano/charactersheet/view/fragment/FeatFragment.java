package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.Feat;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.activity.FeatEditActivity;
import org.elteano.charactersheet.view.support.MenuNestingFragment;

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

public class FeatFragment extends CharacterUpdaterFragment implements
		OnClickListener, MenuNestingFragment {
	private TextView lastSelectedView;

	private void clearListings() {
		((LinearLayout) getView().findViewById(R.id.fragment_feat_layout))
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
		((LinearLayout) getView().findViewById(R.id.fragment_feat_layout))
				.addView(newFeatView);
	}

	private void fillListings() {
		ArrayList<Feat> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getFeatList();
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
		if (resultCode != FeatEditActivity.RESULT_CANCELED) {
			if (lastSelectedView != null)
				((CharacterSheetActivity) getActivity())
						.getCharacter()
						.removeFeatByName(lastSelectedView.getText().toString());
			lastSelectedView = null;
		}
		if (resultCode == Activity.RESULT_OK) {
			String name = data.getExtras().getString(
					FeatEditActivity.RESULT_KEY_NAME);
			((CharacterSheetActivity) getActivity())
					.getCharacter()
					.getFeatList()
					.add(new Feat(name, data.getExtras().getString(
							FeatEditActivity.RESULT_KEY_DESC)));
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
	}

	public void onClick(View view) {
		if (!(view instanceof TextView))
			return;
		lastSelectedView = (TextView) view;
		Intent intent = new Intent(getActivity(), FeatEditActivity.class);
		intent.putExtra(FeatEditActivity.INPUT_KEY_NAME, lastSelectedView
				.getText().toString());
		intent.putExtra(FeatEditActivity.INPUT_KEY_DESC,
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getFeat(lastSelectedView.getText().toString())
						.getDescription());
		startActivityForResult(intent, FeatEditActivity.REQUEST_EDIT_FEAT);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_feat_menu_large, menu);
		else
			inflater.inflate(getMenuId(), menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_feat, container, false);
		if (((CharacterSheetActivity) getActivity()).isPortraitLayout())
			setHasOptionsMenu(true);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_feat:
			lastSelectedView = null;
			Intent intent = new Intent(getActivity(), FeatEditActivity.class);
			startActivityForResult(intent, FeatEditActivity.REQUEST_NEW_FEAT);
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

	public boolean hasMenu() {
		return true;
	}

	public int getMenuId() {
		return R.menu.fragment_feat_menu;
	}
}
