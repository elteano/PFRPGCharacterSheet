package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.Item;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.activity.ItemEditActivity;
import org.elteano.charactersheet.view.support.ItemListing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class ItemFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private Item lastItem;
	private TextWatcher goldWatcher;

	private void clearListings() {
		((TableLayout) getView().findViewById(R.id.fragment_item_table))
				.removeAllViews();
	}

	private void fillFields() {
		EditText goldText = (EditText) getView().findViewById(
				R.id.fragment_item_gold_field);
		goldText.setText(((CharacterSheetActivity) getActivity())
				.getCharacter().getGold() + "");
	}

	private void fillListings() {
		TableLayout table = (TableLayout) getView().findViewById(
				R.id.fragment_item_table);
		ArrayList<Item> itemList = ((CharacterSheetActivity) getActivity())
				.getCharacter().getItemList();
		for (int i = 0; i < itemList.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < itemList.size(); j++) {
				if (itemList.get(j).getName()
						.compareTo(itemList.get(swapWith).getName()) < 0)
					swapWith = j;
			}
			Item item = itemList.get(i);
			itemList.set(i, itemList.get(swapWith));
			itemList.set(swapWith, item);
		}
		for (Item item : itemList) {
			ItemListing il = new ItemListing(getActivity(), null);
			il.setToItem(item);
			il.setOnClickListener(this);
			table.addView(il);
		}
	}

	private void hookupListeners() {
		EditText goldText = (EditText) getView().findViewById(
				R.id.fragment_item_gold_field);
		goldText.addTextChangedListener(goldWatcher);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ItemEditActivity.REQUEST_EDIT_ITEM
				&& resultCode == ItemEditActivity.RESULT_CANCELED)
			((CharacterSheetActivity) getActivity()).getCharacter().addItem(
					lastItem);
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case Activity.RESULT_OK:
			Item item = data.getParcelableExtra(ItemEditActivity.EXTRA_ITEM);
			((CharacterSheetActivity) getActivity()).getCharacter().addItem(
					item);
			lastItem = null;
			break;
		}
		if (getActivity() == null)
			Log.d("CharacterSheet", "no activity");
		if (((CharacterSheetActivity) getActivity()).getCharacter() == null)
			Log.d("CharacterSheet", "no character");
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
	}

	public void onClick(View source) {
		if (source instanceof ItemListing) {
			ItemListing il = (ItemListing) source;
			Intent intent = new Intent(getActivity(), ItemEditActivity.class);
			lastItem = ((CharacterSheetActivity) getActivity()).getCharacter()
					.getItem(il.getItemName());
			Log.d("CharacterSheet", "The selected item is " + il.getItemName());
			Log.d("CharacterSheet", "The found item is " + lastItem.getName());
			((CharacterSheetActivity) getActivity()).getCharacter().removeItem(
					lastItem);
			intent.putExtra(ItemEditActivity.EXTRA_ITEM, (Parcelable) lastItem);
			startActivityForResult(intent, ItemEditActivity.REQUEST_EDIT_ITEM);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_item_menu_large, menu);
		else
			inflater.inflate(R.menu.fragment_item_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		goldWatcher = new TextWatcher() {

			public void afterTextChanged(Editable text) {
				try {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setGold(Float.parseFloat(text.toString()));
				} catch (NumberFormatException ex) {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setGold(0);
				}
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

		};
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_item, container, false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_item_menu_add:
			Intent intent = new Intent(getActivity(), ItemEditActivity.class);
			startActivityForResult(intent, ItemEditActivity.REQUEST_NEW_ITEM);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		clearListings();
		fillFields();
		hookupListeners();
		fillListings();
		updateWeight();
		super.onResume();
	}

	@Override
	public void onPause() {
		unhookListeners();
		super.onPause();
	}

	private void unhookListeners() {
		if (goldWatcher != null)
			((EditText) getView().findViewById(R.id.fragment_item_gold_field))
					.removeTextChangedListener(goldWatcher);
	}

	private void updateWeight() {
		TextView weight = (TextView) getView().findViewById(
				R.id.fragment_item_weight_field);
		if (weight != null)
			weight.setText(""
					+ ((CharacterSheetActivity) getActivity()).getCharacter()
							.calculateTotalCarriedWeight() + " lbs");
		TextView label = (TextView) getView().findViewById(
				R.id.fragment_item_weight_text);
		switch (((CharacterSheetActivity) getActivity()).getCharacter()
				.getCarriedLoad()) {
		case PlayerCharacter.LOAD_LIGHT:
			label.setText(R.string.light_load);
			break;
		case PlayerCharacter.LOAD_MEDIUM:
			label.setText(R.string.medium_load);
			break;
		case PlayerCharacter.LOAD_HEAVY:
			label.setText(R.string.heavy_load);
			break;
		case PlayerCharacter.LOAD_OVERLIMIT:
			label.setText(R.string.over_limit);
			break;
		}
	}

	@Override
	public void preUpdate() {
	}

	@Override
	public void updateDisplay() {
		unhookListeners();
		clearListings();
		fillListings();
		fillFields();
		hookupListeners();
		updateWeight();
	}
}
