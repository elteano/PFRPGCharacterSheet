package org.elteano.charactersheet;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HandsetPagerAdapter extends FragmentPagerAdapter {

	private static final Class[] list = { CharacterSelectFragment.class,
			NameFragment.class, StatsFragment.class, FeatFragment.class,
			CFeatFragment.class, SkillFragment.class, ItemFragment.class,
			AttackFragment.class, DefenseFragment.class, SpellFragment.class };
	private final Activity mActivity;
	private Fragment[] frags;

	public HandsetPagerAdapter(FragmentManager fm, Activity activity) {
		super(fm);
		mActivity = activity;
		frags = new Fragment[list.length];
	}

	@Override
	public Fragment getItem(int arg0) {
		if (frags[arg0] == null)
			return frags[arg0] = Fragment.instantiate(mActivity,
					list[arg0].getName());
		else
			return frags[arg0];
	}

	@Override
	public int getCount() {
		return list.length;
	}

}
