package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.ArmorClass;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.model.Save;
import org.elteano.charactersheet.model.WeapShield;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.ACButtonListener;
import org.elteano.charactersheet.view.support.ConditionCheckBoxListener;
import org.elteano.charactersheet.view.support.SaveButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A fragment consolidating all combat statistics.
 */
public class AttackPanelFragment extends Fragment implements
		View.OnClickListener {

	private Spinner mMainSpinner;
	private Spinner mOffSpinner;
	private OffHandSpinnerAdapter mOffSpinnerAdapter;

	private void fillACButton() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		ArmorClass ac = c.getAC();
		AbilityScores abilities = c.getAbilities();
		int size = c.getSize();
		int bab = c.getBAB();
		int modifiers = 0;
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_charge)).isChecked()) {
			modifiers -= 2;
		}
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_defensive)).isChecked()) {
			modifiers += 2;
		}
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_expertise)).isChecked()) {
			modifiers += 1 + c.getBAB() / 4;
		}
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_cleave)).isChecked()) {
			modifiers -= 2;
		}
		WeapShield main = (WeapShield) mMainSpinner.getSelectedItem();
		WeapShield off = (WeapShield) mOffSpinner.getSelectedItem();
		int mainBonus = 0;
		int offBonus = 0;
		if (off != null
				&& !(((CheckBox) getView().findViewById(
						R.id.fragment_attack_panel_check_two_handed))
						.isChecked()))
			offBonus = off.getACBonus();
		((Button) getView().findViewById(R.id.fragment_attack_panel_ac_button))
				.setText(""
						+ (ac.getACFromCharacter(c)
								+ Math.max(mainBonus, offBonus) + modifiers));
		((Button) getView().findViewById(R.id.fragment_attack_panel_cmd_button))
				.setText("" + ac.getCMD(abilities, size, bab));
	}

	private void fillButtonDisplays() {
		fillACButton();
		((Button) getView().findViewById(R.id.fragment_attack_panel_cmb_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getCMB());
		((Button) getView().findViewById(
				R.id.fragment_attack_panel_fortitude_button)).setText(""
				+ ((CharacterSheetActivity) getActivity())
						.getCharacter()
						.getFort()
						.getTotal(
								((CharacterSheetActivity) getActivity())
										.getCharacter().getAbilities()));
		((Button) getView().findViewById(
				R.id.fragment_attack_panel_reflex_button)).setText(""
				+ ((CharacterSheetActivity) getActivity())
						.getCharacter()
						.getRef()
						.getTotal(
								((CharacterSheetActivity) getActivity())
										.getCharacter().getAbilities()));
		((Button) getView()
				.findViewById(R.id.fragment_attack_panel_will_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter()
								.getWill()
								.getTotal(
										((CharacterSheetActivity) getActivity())
												.getCharacter().getAbilities()));
	}

	private void fillCheckBoxes() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_blinded)).setChecked(c
				.isBlinded());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_cowering)).setChecked(c
				.isCowering());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_dazzled)).setChecked(c
				.isDazzled());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_entangled)).setChecked(c
				.isEntangled());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_flat_footed)).setChecked(c
				.isFlatFooted());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_frightened)).setChecked(c
				.isFrightened());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_grappling)).setChecked(c
				.isGrappling());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_helpless)).setChecked(c
				.isHelpless());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_invisible)).setChecked(c
				.isInvisible());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_pinned)).setChecked(c
				.isPinned());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_prone)).setChecked(c
				.isProne());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_shaken)).setChecked(c
				.isShaken());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_squeezing)).setChecked(c
				.isSqueezing());
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_stunned)).setChecked(c
				.isStunned());
	}

	private void fillSpinners() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		for (int i = 0; i < c.getWieldableEquipment().size(); ++i) {
			WeapShield ws = c.getWieldableEquipment().get(i);
			if (ws.getName().equals(c.getPreviouslySelectedMainWeapon())) {
				mMainSpinner.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < c.getWieldableEquipment().size(); ++i) {
			WeapShield ws = c.getWieldableEquipment().get(i);
			if (ws.getName().equals(c.getPreviouslySelectedOffWeapon())) {
				mOffSpinner.setSelection(i);
				break;
			}
		}
	}

	private void hookupListeners() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		ACButtonListener acButtonListener = new ACButtonListener(this);
		((Button) getView().findViewById(R.id.fragment_attack_panel_act_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(
				R.id.fragment_attack_panel_reset_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(R.id.fragment_attack_panel_ac_button))
				.setOnClickListener(acButtonListener);
		((Button) getView().findViewById(R.id.fragment_attack_panel_cmd_button))
				.setOnClickListener(acButtonListener);
		((Button) getView().findViewById(
				R.id.fragment_attack_panel_fortitude_button))
				.setOnClickListener(new SaveButtonListener(this,
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getFort(),
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getAbilities()));
		((Button) getView().findViewById(
				R.id.fragment_attack_panel_reflex_button))
				.setOnClickListener(new SaveButtonListener(this,
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getRef(),
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getAbilities()));
		((Button) getView()
				.findViewById(R.id.fragment_attack_panel_will_button))
				.setOnClickListener(new SaveButtonListener(this,
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getWill(),
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getAbilities()));

		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_blinded))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_BLINDED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_cowering))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_COWERING, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_dazzled))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_DAZZLED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_entangled))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_ENTANGLED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_flat_footed))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_FLAT_FOOTED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_frightened))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_FRIGHTENED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_grappling))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_GRAPPLING, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_helpless))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_HELPLESS, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_invisible))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_INVISIBLE, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_pinned))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_PINNED, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_prone))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_PRONE, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_shaken))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_SHAKEN, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_squeezing))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_SQUEEZING, this));
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_condition_stunned))
				.setOnCheckedChangeListener(new ConditionCheckBoxListener(c,
						PlayerCharacter.CONDITION_STUNNED, this));
		Spinner.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				fillACButton();
				PlayerCharacter c = ((CharacterSheetActivity) getActivity())
						.getCharacter();
				WeapShield selected = (WeapShield) arg0.getSelectedItem();
				switch (arg0.getId()) {
				case R.id.fragment_attack_panel_main_spinner:
					c.setPreviouslySelectedMainWeapon(selected.getName());
					mOffSpinnerAdapter.notifyDataSetChanged();
					checkTwoHandedVisiblility();
					break;
				case R.id.fragment_attack_panel_off_spinner:
					c.setPreviouslySelectedOffWeapon(selected.getName());
					checkTwoHandedVisiblility();
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		mMainSpinner.setOnItemSelectedListener(listener);
		mOffSpinner.setOnItemSelectedListener(listener);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case (short) R.id.fragment_attack_panel_ac_button:
			case (short) R.id.fragment_attack_panel_cmd_button:
				((CharacterSheetActivity) getActivity()).getCharacter().setAC(
						(ArmorClass) data.getExtras().getParcelable("result"));
				break;
			case (short) R.id.fragment_attack_panel_fortitude_button:
				((CharacterSheetActivity) getActivity())
						.getCharacter()
						.setFort(
								(Save) data.getExtras().getParcelable("result"));
				break;
			case (short) R.id.fragment_attack_panel_reflex_button:
				((CharacterSheetActivity) getActivity()).getCharacter().setRef(
						(Save) data.getExtras().getParcelable("result"));
				break;
			case (short) R.id.fragment_attack_panel_will_button:
				((CharacterSheetActivity) getActivity())
						.getCharacter()
						.setWill(
								(Save) data.getExtras().getParcelable("result"));
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_attack_panel_act_button:
			onClickActButton();
			break;
		case R.id.fragment_attack_panel_reset_button:
			onClickResetButton();
			break;
		}
	}

	private void onClickActButton() {
		fillACButton();
		String mainHand = "";
		String offHand = "";
		TextView out = ((TextView) getView().findViewById(
				R.id.fragment_attack_panel_output_text));
		WeapShield main = (WeapShield) mMainSpinner.getSelectedItem();
		WeapShield off = (WeapShield) mOffSpinner.getSelectedItem();
		int flags = 0;
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_charge)).isChecked())
			flags |= WeapShield.MODIFIER_CHARGE;
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_defensive)).isChecked())
			flags |= WeapShield.MODIFIER_DEFENSIVE;
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_expertise)).isChecked())
			flags |= WeapShield.MODIFIER_EXPERTISE;
		if (((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_two_handed)).isChecked()) {
			flags |= WeapShield.MODIFIER_TWO_HANDED;
			if (((CharacterSheetActivity) getActivity()).getCharacter()
					.hasFeat("Two-Weapon Fighting"))
				flags |= WeapShield.MODIFIER_TWO_HANDED_HAS_FEAT;
			if (((WeapShield) mOffSpinner.getSelectedItem()).getWeaponType() == WeapShield.TYPE_LIGHT)
				flags |= WeapShield.MODIFIER_TWO_HANDED_OFFHAND_LIGHT;
			offHand = "\nOff Hand: "
					+ off.calculateAttack(
							((CharacterSheetActivity) getActivity())
									.getCharacter(), flags
									| WeapShield.MODIFIER_TWO_HANDED_IS_OFFHAND);
		}
		mainHand = main.calculateAttack(
				((CharacterSheetActivity) getActivity()).getCharacter(), flags);
		if (!offHand.isEmpty())
			mainHand = "Main Hand: " + mainHand;
		out.setText(mainHand + offHand);
	}

	private void onClickResetButton() {
		TextView out = (TextView) getView().findViewById(
				R.id.fragment_attack_panel_output_text);
		out.setText("");
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_charge)).setChecked(false);
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_cleave)).setChecked(false);
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_defensive)).setChecked(false);
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_expertise)).setChecked(false);
		((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_two_handed)).setChecked(false);
		resetConditions();
		fillACButton();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_attack_panel_menu, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View ret = inflater.inflate(R.layout.fragment_attack_panel, container,
				false);
		mMainSpinner = (Spinner) ret
				.findViewById(R.id.fragment_attack_panel_main_spinner);
		mOffSpinner = (Spinner) ret
				.findViewById(R.id.fragment_attack_panel_off_spinner);
		ArrayAdapter<WeapShield> adapter = new ArrayAdapter<WeapShield>(
				getActivity(), android.R.layout.simple_spinner_item,
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getWieldableEquipment());
		mMainSpinner.setAdapter(adapter);
		mOffSpinnerAdapter = new OffHandSpinnerAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				((CharacterSheetActivity) getActivity()).getCharacter()
						.getWieldableEquipment(), mMainSpinner);
		mOffSpinner.setAdapter(mOffSpinnerAdapter);
		setFeatDependentBoxes(ret);
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_attack_panel_menu_edit:
			((CharacterSheetActivity) getActivity()).setInTopLevel(false);
			((CharacterSheetActivity) getActivity()).setToFragment(
					new WeapShieldListFragment(), true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		((ArrayAdapter) mMainSpinner.getAdapter()).notifyDataSetChanged();
		((ArrayAdapter) mOffSpinner.getAdapter()).notifyDataSetChanged();
		fillCheckBoxes();
		fillSpinners();
		super.onResume();
	}

	@Override
	public void onStart() {
		fillButtonDisplays();
		hookupListeners();
		super.onStart();
	}

	private void resetConditions() {
		((CharacterSheetActivity) getActivity()).getCharacter()
				.clearConditions();
		fillCheckBoxes();
	}

	private void setFeatDependentBoxes(View root) {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		if (c.hasFeat("Cleave")) {
			((CheckBox) root
					.findViewById(R.id.fragment_attack_panel_check_cleave))
					.setVisibility(View.VISIBLE);
		}
		if (c.hasFeat("Combat Expertise")) {
			((CheckBox) root
					.findViewById(R.id.fragment_attack_panel_check_expertise))
					.setVisibility(View.VISIBLE);
		}
	}

	public void updateOnConditionChange() {
		fillACButton();
		TextView resetButton = (TextView) getView().findViewById(
				R.id.fragment_attack_panel_reset_button);
		if (((CharacterSheetActivity) getActivity()).getCharacter()
				.hasAnyConditions()) {
			resetButton.setText(R.string.reset_);
		} else {
			resetButton.setText(R.string.reset);
		}
	}

	private void checkTwoHandedVisiblility() {
		if (((WeapShield) mOffSpinner.getSelectedItem()).getWeaponType() == WeapShield.TYPE_NONE) {
			((CheckBox) getView().findViewById(
					R.id.fragment_attack_panel_check_two_handed))
					.setVisibility(View.INVISIBLE);
		} else {
			((CheckBox) getView().findViewById(
					R.id.fragment_attack_panel_check_two_handed))
					.setVisibility(View.VISIBLE);
		}
	}
}
