package org.elteano.charactersheet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class CharacterSheetActivity extends FragmentActivity {

	public static final String PREFERENCES_PREFIX = "org.elteano.charactersheet.prefs.";
	private static final String INSTANCESTATE_CHARNAME = CharacterSheetActivity.class
			.getCanonicalName() + ".charName";
	private static PlayerCharacter character;
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;

	private void addHandsetTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_character_select)
				.setIcon(R.drawable.ic_list).setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<CharacterSelectFragment>(this,
		// "select", CharacterSelectFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_character)
				.setIcon(R.drawable.ic_pencil)
				.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<NameFragment>(this, "name",
		// NameFragment.class)));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_stats)
						.setIcon(R.drawable.ic_stats)
						.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<StatsFragment>(this, "stats",
		// StatsFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_feat)
				.setIcon(R.drawable.ic_feat).setTabListener(new TabListener()));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_cfeat)
						.setIcon(R.drawable.ic_other)
						.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<FeatFragment>(this, "feat",
		// FeatFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_skill)
				.setIcon(R.drawable.ic_skills)
				.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<SkillFragment>(this, "skill",
		// SkillFragment.class)));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_items)
						.setIcon(R.drawable.ic_items)
						.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<ItemFragment>(this, "item",
		// ItemFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_attacks)
				.setIcon(R.drawable.ic_attack)
				.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<AttackFragment>(this, "attack",
		// AttackFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_defense)
				.setIcon(R.drawable.ic_defense)
				.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<DefenseFragment>(this, "defense",
		// DefenseFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_spells)
				.setIcon(R.drawable.ic_mag).setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<SpellFragment>(this, "spells",
		// SpellFragment.class)));
		mAdapter = new HandsetPagerAdapter(getSupportFragmentManager(), this);
	}

	private void addTabletTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_select_info_items)
				.setIcon(R.drawable.ic_list)
				// .setTabListener(new TabListener()));
				.setTabListener(
						new TabletTabListener<SelectInfoItemsFragment>(this,
								"select", SelectInfoItemsFragment.class)));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.title_stats_combat)
				.setIcon(R.drawable.ic_stats)
				// .setTabListener(new TabListener()));
				.setTabListener(
						new TabletTabListener<StatsAttackDefenseFragment>(this,
								"stats", StatsAttackDefenseFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_feat_skill)
				.setIcon(R.drawable.ic_feat)
				// .setTabListener(new TabListener()));
				.setTabListener(
						new TabletTabListener<FeatSkillFragment>(this,
								"feats/skills", FeatSkillFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_spells)
				.setIcon(R.drawable.ic_mag)
				// .setTabListener(new TabListener()));
				.setTabListener(
						new TabletTabListener<SpellFragment>(this, "spells",
								SpellFragment.class)));
		// mAdapter = new TabletPagerAdapter(getSupportFragmentManager(), this);
	}

	public static PlayerCharacter getCharacter() {
//		if (character != null)
			return character;
	}

	public static void setCharacter(PlayerCharacter character) {
		CharacterSheetActivity.character = character;
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// This is not used, as the root display will be the current fragment

		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		// actionBar.setDisplayShowTitleEnabled(false); //Does the same as above

		// Add all the tabbies
		int screenSizeFlag = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_SMALL
				|| screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.activity_charactersheet);
			mViewPager = (ViewPager) findViewById(R.id.pager);
			addHandsetTabs();
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						private int oldpos = 0;

						@Override
						public void onPageSelected(int position) {
							// When swiping between pages, select the
							// corresponding tab.
							((CharacterUpdaterFragment) mAdapter
									.getItem(oldpos)).preUpdate();
							getActionBar().setSelectedNavigationItem(position);
							getActionBar().selectTab(
									getActionBar().getTabAt(position));
							if (position == 1)
								((CharacterUpdaterFragment) mAdapter
										.getItem(position)).updateDisplay();
							oldpos = position;
						}
					});
			mViewPager.setAdapter(mAdapter);
		} else {
			addTabletTabs();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		setCharacter(PlayerCharacter.restoreByPlayerList(this,
				savedInstanceState.getString(INSTANCESTATE_CHARNAME)));
		getActionBar().setSelectedNavigationItem(
				savedInstanceState.getInt("Tab"));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(INSTANCESTATE_CHARNAME, getCharacter().getName());
		outState.putInt("Tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (getCharacter() != null)
			getActionBar().setTitle(getCharacter().getName());
	}

	@Override
	protected void onStop() {
		if (getCharacter() != null) {
			getCharacter().saveSelfByPlayerList(this);
		}
		super.onStop();
	}

	public final class TabListener implements ActionBar.TabListener {

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		}

		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			// When the tab is selected, switch to the
			// corresponding page in the ViewPager.
			mViewPager.setCurrentItem(tab.getPosition());
		}

		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		}

	}

	public static class TabletTabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final FragmentActivity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabletTabListener(FragmentActivity activity, String tag,
				Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;

			mFragment = mActivity.getSupportFragmentManager()
					.findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		/* The following are each of the ActionBar.TabListener callbacks */
		public void onTabSelected(Tab tab, android.app.FragmentTransaction fut) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
			ft.commit();
		}

		public void onTabUnselected(Tab tab, android.app.FragmentTransaction fut) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
			ft.commit();
		}

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}
}