package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.MenuNestingFragment;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class CharacterUpdaterListenerFragment extends Fragment {

	protected ArrayList<CharacterUpdaterFragment> children;

	protected final void addListeningChild(CharacterUpdaterFragment frag) {
		if (children == null)
			children = new ArrayList<CharacterUpdaterFragment>();
		children.add(frag);
		frag.setCharacterUpdaterListener(this);
	}

	@Override
	public void onStart() {
		children = new ArrayList<CharacterUpdaterFragment>();
		super.onStart();
	}

	protected final void clearChildren() {
		children.clear();
	}

	protected final boolean noChildren() {
		return children.isEmpty();
	}

	public void preUpdateOthers(CharacterUpdaterFragment source) {
		for (CharacterUpdaterFragment frag : children) {
			frag.preUpdate();
		}
	}

	public void postUpdateOthers(CharacterUpdaterFragment source) {
		for (CharacterUpdaterFragment frag : children) {
			frag.updateDisplay();
		}
	}

	public void updateOthers(CharacterUpdaterFragment source) {
		for (CharacterUpdaterFragment frag : children) {
			frag.preUpdate();
		}
		try {
			((CharacterSheetActivity) getActivity()).getCharacter()
					.saveSelfByPlayerList(getActivity());
			for (CharacterUpdaterFragment frag : children) {
				if (!frag.equals(source))
					frag.updateDisplay();
			}
		} catch (NullPointerException ex) {
			Toast.makeText(
					getActivity(),
					"Uh oh!  It seems the system has recycled your previous session.  Don't worry, though, your data was saved when you left!",
					Toast.LENGTH_SHORT).show();
			getActivity().finish();
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		for (Fragment f : children) {
			MenuNestingFragment mnf = (MenuNestingFragment) f;
			if (mnf.hasMenu()) {
				inflater.inflate(mnf.getMenuId(), menu);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		for (Fragment f : children) {
			MenuNestingFragment mnf = (MenuNestingFragment) f;
			if (mnf.hasMenu() && f.onOptionsItemSelected(item)) {
				return true;
			}
		}
		return false;
	}
}
