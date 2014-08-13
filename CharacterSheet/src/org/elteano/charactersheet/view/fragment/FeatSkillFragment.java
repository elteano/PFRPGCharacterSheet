package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeatSkillFragment extends CharacterUpdaterListenerFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_feat_skill, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		FragmentTransaction ft = ((FragmentActivity) getActivity())
				.getSupportFragmentManager().beginTransaction();
		if (noChildren()) {
			FeatFragment csf = new FeatFragment();
			ft.replace(R.id.left_fragment_spacec, csf);
			addListeningChild(csf);
			CFeatFragment lorp = new CFeatFragment();
			ft.replace(R.id.center_fragment_spacec, lorp);
			addListeningChild(lorp);
			SkillFragment sf = new SkillFragment();
			ft.replace(R.id.right_fragment_spacec, sf);
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
