package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.ConditionCheckBoxListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class ConditionsFragment extends CharacterUpdaterFragment {

	private AttackPanelFragment mInform;

	public ConditionsFragment(AttackPanelFragment info) {
		mInform = info;
	}

	private void fillCheckBoxes() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_blinded)).setChecked(c
				.isBlinded());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_cowering)).setChecked(c
				.isCowering());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_dazzled)).setChecked(c
				.isDazzled());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_entangled)).setChecked(c
				.isEntangled());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_flat_footed)).setChecked(c
				.isFlatFooted());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_frightened)).setChecked(c
				.isFrightened());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_grappling)).setChecked(c
				.isGrappling());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_helpless)).setChecked(c
				.isHelpless());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_invisible)).setChecked(c
				.isInvisible());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_kneeling)).setChecked(c
				.isKneeling());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_pinned)).setChecked(c
				.isPinned());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_prone)).setChecked(c
				.isProne());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_shaken)).setChecked(c
				.isShaken());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_squeezing)).setChecked(c
				.isSqueezing());
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_stunned)).setChecked(c
				.isStunned());
	}

	private void hookupListeners() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_blinded))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_BLINDED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_cowering))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_COWERING, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_dazzled))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_DAZZLED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_entangled))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_ENTANGLED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_flat_footed))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_FLAT_FOOTED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_frightened))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_FRIGHTENED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_grappling))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_GRAPPLING, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_helpless))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_HELPLESS, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_invisible))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_INVISIBLE, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_pinned))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_PINNED, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_prone))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_PRONE, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_shaken))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_SHAKEN, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_squeezing))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_SQUEEZING, mInform));
		((CheckBox) getView().findViewById(
				R.id.fragment_conditions_condition_stunned))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_STUNNED, mInform));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conditions, container, false);
	}

	@Override
	public void onStart() {
		fillCheckBoxes();
		hookupListeners();
		super.onStart();
	}

	@Override
	public void preUpdate() {
	}

	@Override
	public void updateDisplay() {
		fillCheckBoxes();
	}
}
