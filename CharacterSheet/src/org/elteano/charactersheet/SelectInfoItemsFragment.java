package org.elteano.charactersheet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectInfoItemsFragment extends CharacterUpdaterListenerFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_info_stats, container,
				false);
	}

	@Override
	public void onResume() {
		super.onResume();
		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();
		if (noChildren()) {
			//CharacterSelectFragment csf = new CharacterSelectFragment();
			//ft.replace(R.id.left_fragment_space, csf);
			//addListeningChild(csf);
			NameFragment nf = new NameFragment();
			ft.replace(R.id.center_fragment_space, nf);
			addListeningChild(nf);
			ItemFragment sf = new ItemFragment();
			ft.replace(R.id.right_fragment_space, sf);
			addListeningChild(sf);
		} else {
			for (Fragment f : children) {
				ft.attach(f);
			}
		}
		ft.commit();
	}

	@Override
	public void onStop() {
		Log.d("CharacterSheet", "Detaching");
		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();
		for (Fragment f : children) {
			ft.detach(f);
		}
		ft.commitAllowingStateLoss();
		super.onStop();
	}
}
