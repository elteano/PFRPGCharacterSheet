package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.model.PlayerCharacter;

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

	public ConditionCheckBoxListener(PlayerCharacter editCharacter,
			int conditionCode) {
		mEditingCharacter = editCharacter;
		mConditionCode = conditionCode;
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mEditingCharacter.setConditions(mConditionCode, isChecked);
	}

}
