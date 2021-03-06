package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatsAttackDefenseFragment extends
		CharacterUpdaterListenerFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_stats_attack_defense,
				container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		if (noChildren()) {
			StatsFragment csf = new StatsFragment();
			ft.replace(R.id.left_fragment_spaceb, csf);
			addListeningChild(csf);
			AttackPanelFragment sf = new AttackPanelFragment();
			ft.replace(R.id.center_fragment_spaceb, sf);
			addListeningChild(sf);
			ConditionsFragment cf = new ConditionsFragment(sf);
			ft.replace(R.id.right_fragment_spaceb, cf);
			addListeningChild(cf);
		} else {
			for (Fragment f : children) {
				ft.attach(f);
			}
		}
		ft.commit();
	}

	@Override
	public void onStop() {
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		for (Fragment f : children) {
			ft.detach(f);
		}
		ft.commitAllowingStateLoss();
		super.onStop();
	}
}
