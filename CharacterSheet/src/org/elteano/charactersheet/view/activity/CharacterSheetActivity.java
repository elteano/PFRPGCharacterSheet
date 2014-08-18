package org.elteano.charactersheet.view.activity;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.fragment.CharacterSelectFragment;
import org.elteano.charactersheet.view.fragment.FeatSkillFragment;
import org.elteano.charactersheet.view.fragment.SelectInfoItemsFragment;
import org.elteano.charactersheet.view.fragment.SpellFragment;
import org.elteano.charactersheet.view.fragment.StatsAttackDefenseFragment;
import org.elteano.charactersheet.view.support.HandsetPagerAdapter;
import org.elteano.charactersheet.view.support.NavDrawerListListener;
import org.elteano.charactersheet.view.support.NavDrawerToggle;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
	private Fragment mPreviousFragment;
	private NavDrawerToggle mToggle;
	private NavDrawerListListener mDrawerListener;
	private boolean isTablet;

	private void addHandsetTabs() {
		ActionBar actionBar = getActionBar();
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
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_stats)
						.setIcon(R.drawable.ic_stats)
						.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_attacks)
				.setIcon(R.drawable.ic_attack)
				.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_defense)
				.setIcon(R.drawable.ic_defense)
				.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_feat)
				.setIcon(R.drawable.ic_feat).setTabListener(new TabListener()));
		actionBar
				.addTab(actionBar.newTab().setText(R.string.title_cfeat)
						.setIcon(R.drawable.ic_other)
						.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_skill)
				.setIcon(R.drawable.ic_skills)
				.setTabListener(new TabListener()));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_spells)
				.setIcon(R.drawable.ic_mag).setTabListener(new TabListener()));
		mAdapter = new HandsetPagerAdapter(getSupportFragmentManager(), this);
	}

	private void addTabletTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_select_info_items)
				.setIcon(R.drawable.ic_list)
				.setTabListener(
						new TabletTabListener<SelectInfoItemsFragment>(this,
								"select", SelectInfoItemsFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_stats_combat)
				.setIcon(R.drawable.ic_stats)
				.setTabListener(
						new TabletTabListener<StatsAttackDefenseFragment>(this,
								"stats", StatsAttackDefenseFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_feat_skill)
				.setIcon(R.drawable.ic_feat)
				.setTabListener(
						new TabletTabListener<FeatSkillFragment>(this,
								"feats/skills", FeatSkillFragment.class)));
		actionBar.addTab(actionBar
				.newTab()
				.setText(R.string.title_spells)
				.setIcon(R.drawable.ic_mag)
				.setTabListener(
						new TabletTabListener<SpellFragment>(this, "spells",
								SpellFragment.class)));
	}

	public void clearBackStack() {
		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void clearPreviousFragment() {
		mPreviousFragment = null;
	}

	public synchronized PlayerCharacter getCharacter() {
		return character;
	}

	public boolean hasCharacter() {
		return character != null;
	}

	public void setToFragment(Fragment f) {
		setToFragment(f, false);
	}

	public void setToFragment(Fragment f, boolean addToBackStack) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.activity_charactersheet_filler, f);
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commit();
	}

	private void initialize() {
		Log.i("CharacterSheet", "Entering initialize()");
		int screenSizeFlag = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;
		if (screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_SMALL
				|| screenSizeFlag == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			isTablet = false;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			setContentView(R.layout.activity_charactersheet_drawer);
			ListView drawers = (ListView) findViewById(R.id.activity_charactersheet_nav_drawer);
			drawers.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getResources()
							.getStringArray(R.array.nav_dests)));
			mDrawerListener = new NavDrawerListListener(
					this,
					(DrawerLayout) findViewById(R.id.activity_charactersheet_drawer_layout));
			mDrawerListener.setToBeginning();
			drawers.setOnItemClickListener(mDrawerListener);
			mToggle = new NavDrawerToggle(
					this,
					(DrawerLayout) findViewById(R.id.activity_charactersheet_drawer_layout));
			mToggle.setDrawerIndicatorEnabled(true);
			((DrawerLayout) findViewById(R.id.activity_charactersheet_drawer_layout))
					.setDrawerListener(mToggle);
			((DrawerLayout) findViewById(R.id.activity_charactersheet_drawer_layout))
					.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		} else {
			isTablet = true;
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
			try {
				JSONObject cjson = new JSONObject(getSharedPreferences(
						CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
						MODE_PRIVATE).getString(
						getIntent().getStringExtra("result"), "ERROR"));
				setCharacter(PlayerCharacter.createFromJSON(cjson));
			} catch (JSONException ex) {
				Log.e("CharacterSheet",
						"Error obtaining JSON to inflate character.");
				ex.printStackTrace();
				Toast.makeText(this, "Error recreating character.",
						Toast.LENGTH_SHORT).show();
				navigateUp();
			}
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Let the drawer toggle handle it, but only for handsets
		if (mToggle != null && mToggle.isDrawerIndicatorEnabled()
				&& mToggle.onOptionsItemSelected(item))
			return true;
		if (isTablet) {
			switch (item.getItemId()) {
			case android.R.id.home:
				// This is called when the Home (Up) button is pressed
				// in the Action Bar.
				navigateUp();
				return true;
			}
		} else {
			switch (item.getItemId()) {
			case android.R.id.home:
				revertToPreviousFragment();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Sets up UI based on whether the user is viewing a top-level fragment.
	 *
	 * @param inTopLevel
	 */
	public void setInTopLevel(boolean inTopLevel) {
		if (mToggle != null)
			mToggle.setDrawerIndicatorEnabled(inTopLevel);
	}

	private void navigateUp() {
		Intent parentActivityIntent = new Intent(this,
				CharacterSelectActivity.class);
		parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(parentActivityIntent);
		finish();
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

	@Override
	protected void onResume() {
		super.onResume();
		if (hasCharacter())
			getActionBar().setTitle(getCharacter().getName());
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Guard statement so that tablets don't crash
		if (mToggle != null)
			mToggle.syncState();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
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
	}

	@Override
	protected void onStop() {
		if (hasCharacter()) {
			getCharacter().saveSelfByPlayerList(this);
		}
		super.onStop();
	}

	private void revertToPreviousFragment() {
		if (mPreviousFragment != null) {
			setToFragment(mPreviousFragment);
		} else {
			mDrawerListener.setToLastSelected();
		}
	}

	public void setCharacter(PlayerCharacter character) {
		this.character = character;
	}

	public void setPreviousFragment(Fragment fragment) {
		mPreviousFragment = fragment;
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