package org.elteano.charactersheet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

public class CharacterSheetActivity extends FragmentActivity {

	public static final String CHARACTER_LIST_PREFERENCE = CharacterSheetActivity.PREFERENCES_PREFIX
			+ "CharacterNames";

	private static final String INSTANCESTATE_CHARNAME = CharacterSheetActivity.class
			.getCanonicalName() + ".charName";

	private static final String INSTANCESTATE_INITIALIZED = "initialized";
	public static final String PREFERENCES_PREFIX = "org.elteano.charactersheet.prefs.";
	private PlayerCharacter character;
	// Really bad coding
	private boolean initialized = false;
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;

	private void addHandsetTabs() {
		ActionBar actionBar = getActionBar();
		// actionBar.addTab(actionBar.newTab().setText(R.string.title_counter)
		// set icon
		// .setTabListener(new TabListener())
		// this will be counter, but moved to a different position
		// );
		// Not doing this one anymore
		// actionBar.addTab(actionBar.newTab()
		// .setText(R.string.title_character_select)
		// .setIcon(R.drawable.ic_list).setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<CharacterSelectFragment>(this,
		// "select", CharacterSelectFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_character)
				.setIcon(R.drawable.ic_pencil)
				.setTabListener(new TabListener()));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_items)
						.setIcon(R.drawable.ic_items)
						.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText("Counter")
				.setIcon(R.drawable.ic_counter)
				.setTabListener(new TabListener()));
		// .setTabListener(
		// new TabListener<StatsFragment>(this, "stats",
		// .setTabListener(
		// .setTabListener(
		// new TabListener<NameFragment>(this, "name",
		// NameFragment.class)));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_stats)
						.setIcon(R.drawable.ic_stats)
						.setTabListener(new TabListener()));
		// StatsFragment.class)));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_attacks)
				.setIcon(R.drawable.ic_attack)
				.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_defense)
				.setIcon(R.drawable.ic_defense)
				.setTabListener(new TabListener()));
		// .setTabListener(
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
		// new TabListener<ItemFragment>(this, "item",
		// ItemFragment.class)));
		// new TabListener<AttackFragment>(this, "attack",
		// AttackFragment.class)));
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

	public synchronized PlayerCharacter getCharacter() {
		// if (character == null) {
		// Intent intent = new Intent(this, CharacterSelectActivity.class);
		// startActivityForResult(intent, 0);
		// }
		return character;
	}

	public boolean hasCharacter() {
		return character != null;
	}

	/*
	 * The following method is a relic from when this class was the root
	 * activity. This code was part of a malfunctioning system. The setup has
	 * since been changed so that CharacterSelectActivity is the root activity.
	 */
	// public void onActivityResult(int requestCode, int resultCode, Intent i) {
	// SharedPreferences playerList = getSharedPreferences(
	// CHARACTER_LIST_PREFERENCE, FragmentActivity.MODE_PRIVATE);
	// String name = i.getStringExtra("result");
	// for (String key : playerList.getAll().keySet()) {
	// if (playerList.getString(key, "").equals(name)) {
	// // if (hasCharacter()) {
	// // getCharacter().saveSelfByPlayerList(this);
	// // }
	// setCharacter(PlayerCharacter
	// .restoreFromSharedPreferences(getSharedPreferences(key,
	// FragmentActivity.MODE_PRIVATE)));
	// getActionBar().setTitle(getCharacter().getName());
	// break;
	// }
	// }
	// Log.i("CharacterSheet",
	// "Character has been set to " + character.getName());
	// }

	private void initialize() {
		Log.i("CharacterSheet", "Entering initialize()");
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
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
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
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		initialized = true;
		Log.i("CharacterSheet", "Exiting initialize()");
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("CharacterSheet", "Entering CharacterSheetActivity.onCreate()");
		// This is not used, as the root display will be the current fragment

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (getIntent().getStringExtra("result") != null) {
			Log.i("CharacterSheet", "Character name found.");
			setCharacter(PlayerCharacter.restoreByPlayerList(this, getIntent()
					.getStringExtra("result")));
			Log.i("CharacterSheet", "hasCharacter(): " + hasCharacter());
		}

		if (savedInstanceState == null) {
		} else {
			// Moved here from onRestoreInstanceState
			if (savedInstanceState.containsKey(INSTANCESTATE_CHARNAME)) {
				setCharacter(PlayerCharacter.restoreByPlayerList(this,
						savedInstanceState.getString(INSTANCESTATE_CHARNAME)));
			}
			initialized = savedInstanceState.getBoolean(
					INSTANCESTATE_INITIALIZED, false);
		}

		initialize();
		Log.i("CharacterSheet", "Exiting CharacterSheetActivity.onCreate()");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			Intent parentActivityIntent = new Intent(this,
					CharacterSelectActivity.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i("CharacterSheet", "Entering onRestoreInstanceState()");
		if (savedInstanceState.containsKey("Tab"))
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt("Tab"));
		super.onRestoreInstanceState(savedInstanceState);
		Log.i("CharacterSheet", "Exiting onRestoreInstanceState");
	}

	protected void onResume() {
		super.onResume();
		if (hasCharacter())
			getActionBar().setTitle(getCharacter().getName());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Came from StackOverflow, not entirely sure if this does anything
		// outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
		// "WORKAROUND_FOR_BUG_19917_VALUE");
		Log.i("CharacterSheet", "Main activity is saving instance state.");
		super.onSaveInstanceState(outState);
		if (hasCharacter()) {
			outState.putString(INSTANCESTATE_CHARNAME, getCharacter().getName());
		}
		if (initialized)
			outState.putInt("Tab", getActionBar().getSelectedNavigationIndex());
		outState.putBoolean(INSTANCESTATE_INITIALIZED, initialized);
		Log.i("CharacterSheet", "Instance state saved.");
	}

	@Override
	protected void onStart() {
		super.onStart();
		// if (!hasCharacter()) {
		// Intent intent = new Intent(this, CharacterSelectActivity.class);
		// intent.putExtra(CharacterSelectActivity.INPUT_CHAR_REQUIRED, true);
		// startActivityForResult(intent, 0);
		// }
		// if (!initialized)
		// initialize();
	}

	@Override
	protected void onStop() {
		if (hasCharacter()) {
			getCharacter().saveSelfByPlayerList(this);
		}
		super.onStop();
	}

	public void setCharacter(PlayerCharacter character) {
		this.character = character;
	}

	public static class TabletTabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private final FragmentActivity mActivity;
		private final Class<T> mClass;
		private Fragment mFragment;
		private final String mTag;

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

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}

		/* The following are each of the ActionBar.TabListener callbacks */
		public void onTabSelected(Tab tab, android.app.FragmentTransaction fut) {
			Log.i("CharacterSheet", "Entering onTabSelected()");
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
			Log.i("CharacterSheet", "mActivity == null: " + (mActivity == null));
			Log.i("CharacterSheet", "mActivity.getClass() returns: "
					+ mActivity.getClass());
			ft.commitAllowingStateLoss();
			Log.i("CharacterSheet", "Exiting onTabSelected()");
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
}