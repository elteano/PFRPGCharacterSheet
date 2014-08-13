package org.elteano.charactersheet;

import java.util.ArrayList;

import org.elteano.charactersheet.view.fragment.FeatSkillFragment;
import org.elteano.charactersheet.view.fragment.SelectInfoItemsFragment;
import org.elteano.charactersheet.view.fragment.SpellFragment;
import org.elteano.charactersheet.view.fragment.StatsAttackDefenseFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabletPagerAdapter extends FragmentPagerAdapter {

	private final Activity mActivity;
	private static final Class[] list = { SelectInfoItemsFragment.class,
			StatsAttackDefenseFragment.class, FeatSkillFragment.class,
			SpellFragment.class };
	private final ArrayList<Fragment> frags;

	public TabletPagerAdapter(FragmentManager fm, Activity activity) {
		super(fm);
		mActivity = activity;
		frags = new ArrayList<Fragment>();
		for (Class c : list) {
			frags.add(Fragment.instantiate(mActivity, c.getName()));
		}
	}

	@Override
	public Fragment getItem(int arg0) {
		return frags.get(arg0);
	}

	@Override
	public int getCount() {
		return list.length;
	}
}
