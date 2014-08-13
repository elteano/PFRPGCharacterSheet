package org.elteano.charactersheet.view.fragment;

import android.support.v4.app.Fragment;

public abstract class CharacterUpdaterFragment extends Fragment {

	private CharacterUpdaterListenerFragment listener;

	public abstract void preUpdate();

	public abstract void updateDisplay();

	protected final void updateOthers() {
		if (listener != null)
			listener.updateOthers(this);
	}

	protected final void preUpdateOthers() {
		if (listener != null) {
			listener.preUpdateOthers(this);
		}
	}

	protected final void postUpdateOthers() {
		if (listener != null)
			listener.postUpdateOthers(this);
	}

	/**
	 * Do not call this method directly! Instead, use the addListeningChild
	 * method from within CharacterUpdaterListener.
	 * 
	 * @param cul
	 */
	public final void setCharacterUpdaterListener(
			CharacterUpdaterListenerFragment cul) {
		listener = cul;
	}
}
