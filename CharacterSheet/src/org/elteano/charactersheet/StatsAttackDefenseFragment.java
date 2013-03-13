package org.elteano.charactersheet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatsAttackDefenseFragment extends
		CharacterUpdaterListenerFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stats_attack_defense,
				container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();
		if (noChildren()) {
			StatsFragment csf = new StatsFragment();
			ft.replace(R.id.left_fragment_spaceb, csf);
			addListeningChild(csf);
			AttackFragment nf = new AttackFragment();
			ft.replace(R.id.center_fragment_spaceb, nf);
			addListeningChild(nf);
			DefenseFragment sf = new DefenseFragment();
			ft.replace(R.id.right_fragment_spaceb, sf);
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
		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();
		for (Fragment f : children) {
			ft.detach(f);
		}
		ft.commitAllowingStateLoss();
		super.onStop();
	}
}
