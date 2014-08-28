package org.elteano.charactersheet.view.support;

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
	// This warning cannot be resolved.
	@SuppressWarnings("rawtypes")
	private static final Class[] list = { SelectInfoItemsFragment.class,
			StatsAttackDefenseFragment.class, FeatSkillFragment.class,
			SpellFragment.class };
	private static final String[] titles = { "Info/Items", "Stats/Combat",
			"Feats/Skills/Abilities", "Spells" };
	private final Fragment[] frags;

	public TabletPagerAdapter(FragmentManager fm, Activity activity) {
		super(fm);
		mActivity = activity;
		frags = new Fragment[list.length];
	}

	@Override
	public Fragment getItem(int arg0) {
		if (frags[arg0] == null) {
			frags[arg0] = Fragment.instantiate(mActivity, list[arg0].getName());
		}
		return frags[arg0];
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
