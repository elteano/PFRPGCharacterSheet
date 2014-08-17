package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.view.activity.ACEditActivity;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class ACButtonListener implements OnClickListener {

	private Fragment mBaseFragment;

	public ACButtonListener(Fragment base) {
		mBaseFragment = base;
	}

	public void onClick(View v) {
		Intent intent = new Intent(mBaseFragment.getActivity(),
				ACEditActivity.class);
		intent.putExtra("input",
				(Parcelable) ((CharacterSheetActivity) mBaseFragment
						.getActivity()).getCharacter().getAC());
		intent.putExtra(ACEditActivity.INPUT_ABILITIES,
				(Parcelable) ((CharacterSheetActivity) mBaseFragment
						.getActivity()).getCharacter().getAbilities());
		intent.putExtra(ACEditActivity.INPUT_SIZE,
				((CharacterSheetActivity) mBaseFragment.getActivity())
						.getCharacter().getSize());
		intent.putExtra(ACEditActivity.INPUT_BAB,
				((CharacterSheetActivity) mBaseFragment.getActivity())
						.getCharacter().getBAB());
		mBaseFragment.startActivityForResult(intent, (short) v.getId());
	}

}
