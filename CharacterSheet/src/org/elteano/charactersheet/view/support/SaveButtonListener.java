package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.Save;
import org.elteano.charactersheet.view.activity.SaveEditActivity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class SaveButtonListener implements OnClickListener {

	private Fragment mBaseFragment;
	private Save mSave;
	private AbilityScores mAbilities;

	public SaveButtonListener(Fragment start, Save save, AbilityScores abilities) {
		mBaseFragment = start;
		mSave = save;
		mAbilities = abilities;
	}

	public void onClick(View v) {
		Intent intent = new Intent(mBaseFragment.getActivity(),
				SaveEditActivity.class);
		intent.putExtra("input", mSave);
		intent.putExtra(SaveEditActivity.INPUT_ABILITIES,
				(Parcelable) mAbilities);
		mBaseFragment.startActivityForResult(intent, (short) v.getId());
	}
}
