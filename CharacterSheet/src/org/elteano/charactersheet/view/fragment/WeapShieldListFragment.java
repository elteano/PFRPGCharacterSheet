package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.WeapShield;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WeapShieldListFragment extends Fragment implements
		OnItemClickListener {

	private boolean deleting;
	private ArrayAdapter<WeapShield> mAdapter;
	private ListView mListView;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_combat_gear_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		((CharacterSheetActivity) getActivity()).clearPreviousFragment();
		View ret = inflater.inflate(R.layout.fragment_weap_shield_list,
				container, false);
		mAdapter = new ArrayAdapter<WeapShield>(getActivity(),
				android.R.layout.simple_list_item_1,
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getWieldableEquipment());
		mListView = (ListView) ret
				.findViewById(R.id.fragment_weap_shield_list_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		return ret;
	}

	public void onItemClick(AdapterView<?> parent, View source, int index,
			long id) {
		WeapShield selected = (WeapShield) parent.getAdapter().getItem(index);
		if (deleting) {
			promptDeleteItem(selected);
		} else {
			Fragment f = new GearEditFragment(selected);
			((CharacterSheetActivity) getActivity()).setPreviousFragment(this);
			((CharacterSheetActivity) getActivity()).setToFragment(f, true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_combat_gear_menu_add_item:
			((CharacterSheetActivity) getActivity()).getCharacter()
					.getWieldableEquipment()
					.add(new WeapShield("New Gear Item"));
			mAdapter.notifyDataSetChanged();
			return true;
		case R.id.fragment_combat_gear_menu_remove_item:
			if (deleting) {
				Toast.makeText(getActivity(),
						"You are no longer deleting an item.",
						Toast.LENGTH_SHORT).show();
				deleting = false;
			} else {
				Toast.makeText(
						getActivity(),
						"You will be prompted to delete the next selected item.",
						Toast.LENGTH_SHORT).show();
				deleting = true;
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

	private void promptDeleteItem(final WeapShield item) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Delete " + item.getName() + "?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((CharacterSheetActivity) getActivity())
										.getCharacter().getWieldableEquipment()
										.remove(item);
								deleting = false;
								mAdapter.notifyDataSetChanged();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
}
