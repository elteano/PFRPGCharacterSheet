package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.R;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

public class NavDrawerToggle extends ActionBarDrawerToggle {

	public NavDrawerToggle(Activity activity, DrawerLayout drawerLayout) {
		super(activity, drawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close);
	}
}
