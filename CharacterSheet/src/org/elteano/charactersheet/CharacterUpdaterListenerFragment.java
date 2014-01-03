package org.elteano.charactersheet;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
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
			// if (!frag.equals(source))
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
}
