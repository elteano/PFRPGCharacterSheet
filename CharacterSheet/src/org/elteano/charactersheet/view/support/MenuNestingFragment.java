package org.elteano.charactersheet.view.support;

/**
 * An interface defining a method which purely exists in order to acquire the
 * menu resource ID.
 *
 * This is to be used for nested fragments, which should (maybe) allow the
 * containing fragment to inflate its menu resource, rather than inflating it
 * itself and causing general mayhem.
 */
public interface MenuNestingFragment {

	/**
	 * Returns <code>true</code> if this fragment has a menu. Sort of like a
	 * setHasOptionsMenu, except without causing this fragment to create a menu.
	 */
	public boolean hasMenu();

	/**
	 * Gets the resource ID for this fragment's menu.
	 *
	 * If <code>hasMenu()</code> returns <code>false</code>, then this should
	 * return 0.
	 */
	public int getMenuId();
}
