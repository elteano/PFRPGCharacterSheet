package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.fragment.AttackFragment;
import org.elteano.charactersheet.view.fragment.CFeatFragment;
import org.elteano.charactersheet.view.fragment.CounterFragment;
import org.elteano.charactersheet.view.fragment.DefenseFragment;
import org.elteano.charactersheet.view.fragment.FeatFragment;
import org.elteano.charactersheet.view.fragment.ItemFragment;
import org.elteano.charactersheet.view.fragment.NameFragment;
import org.elteano.charactersheet.view.fragment.SkillFragment;
import org.elteano.charactersheet.view.fragment.SpellFragment;
import org.elteano.charactersheet.view.fragment.StatsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public final class NavDrawerListListener implements
		ListView.OnItemClickListener {

	/**
	 * List of Fragment classes to appear in the list.
	 *
	 * This is used to instantiate Fragments as they are used, and so
	 * theoretically speaking, it can be used in order to manage memory usage.
	 */
	// No applicable generic type, and so the warning cannot be fixed.
	@SuppressWarnings("rawtypes")
	private static final Class[] list = { NameFragment.class,
			ItemFragment.class, CounterFragment.class, StatsFragment.class,
			AttackFragment.class, DefenseFragment.class, FeatFragment.class,
			CFeatFragment.class, SkillFragment.class, SpellFragment.class };

	/**
	 * Activity to modify when items are selected.
	 */
	private CharacterSheetActivity mContext;
	/**
	 * Layout containing the drawer panel, required in order to control the
	 * drawer state.
	 */
	private DrawerLayout mDrawerLayout;
	/**
	 * Array containing cached fragments.
	 */
	private Fragment[] frags;

	public NavDrawerListListener(CharacterSheetActivity context,
			DrawerLayout layout) {
		mContext = context;
		mDrawerLayout = layout;
		frags = new Fragment[list.length];
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setToPosition(position);
		mDrawerLayout.closeDrawer((View) view.getParent());
	}

	public void setToBeginning() {
		setToPosition(0);
	}

	private void setToPosition(int position) {
		if (frags[position] == null) {
			frags[position] = Fragment.instantiate(mContext,
					list[position].getName());
		}
		mContext.setToFragment(frags[position]);
	}
}