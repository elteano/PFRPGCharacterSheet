package org.elteano.charactersheet;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HandsetPagerAdapter extends FragmentPagerAdapter {

	/**
	 * List of classes to be contained in the pager. Exists mostly for
	 * convenience. Unfortunately, there is no other method of storing this
	 * list.
	 */
	@SuppressWarnings("rawtypes")
	private static final Class[] list = {// CharacterSelectFragment.class,
	NameFragment.class, StatsFragment.class, FeatFragment.class,
			CFeatFragment.class, SkillFragment.class, ItemFragment.class,
			AttackFragment.class, DefenseFragment.class, SpellFragment.class };
	/**
	 * Activity to which the fragments will be attached.
	 */
	private final Activity mActivity;
	/**
	 * Array for storing initialized forms of the fragments.
	 */
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
