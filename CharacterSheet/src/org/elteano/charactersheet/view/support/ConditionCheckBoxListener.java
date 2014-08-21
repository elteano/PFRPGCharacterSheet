package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.fragment.AttackPanelFragment;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Observer for check boxes which determine a player character's conditions.
 *
 * This was created for use in the combat panel (AttackPanelFragment).
 */
public class ConditionCheckBoxListener implements OnCheckedChangeListener {

	private int mConditionCode;
	private PlayerCharacter mEditingCharacter;
	private AttackPanelFragment mNotify;

	public ConditionCheckBoxListener(PlayerCharacter editCharacter,
			int conditionCode, AttackPanelFragment notify) {
		mEditingCharacter = editCharacter;
		mConditionCode = conditionCode;
		mNotify = notify;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mEditingCharacter.setConditions(mConditionCode, isChecked);
		if (mNotify != null)
			mNotify.updateOnConditionChange();
	}
}
