package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.WeapShield;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WeapShieldListFragment extends Fragment implements
		OnItemClickListener {

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

	@Override
	public void onResume() {
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}

	public void onItemClick(AdapterView<?> parent, View source, int index,
			long id) {
		WeapShield selected = (WeapShield) parent.getAdapter().getItem(index);
		Fragment f = new GearEditFragment(selected);
		((CharacterSheetActivity) getActivity()).setPreviousFragment(this);
		((CharacterSheetActivity) getActivity()).setToFragment(f, true);
	}
}
