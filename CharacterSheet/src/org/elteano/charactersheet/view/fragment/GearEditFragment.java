package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.WeapShield;
import org.elteano.charactersheet.view.support.IntTextWatcher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class GearEditFragment extends Fragment {

	private IntTextWatcher mACBonusWatcher;
	private Spinner.OnItemSelectedListener mAttackAbilityWatcher;
	private IntTextWatcher mAttackBonusWatcher;
	private Spinner.OnItemSelectedListener mDamageAbilityWatcher;
	private IntTextWatcher mDamageBonusWatcher;
	private TextWatcher mDamageDieWatcher;
	private WeapShield mEditing;
	private TextWatcher mNameWatcher;
	private Spinner.OnItemSelectedListener mWeaponTypeWatcher;

	public GearEditFragment(WeapShield editing) {
		mEditing = editing;
	}

	private void fillFields() {
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_attack_ability_spinner))
				.setSelection(mEditing.getAttackAbility());
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_damage_ability_spinner))
				.setSelection(mEditing.getDamageAbility());
		((Spinner) getView().findViewById(R.id.fragment_gear_edit_type_spinner))
				.setSelection(mEditing.getWeaponType());
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_attack_bonus_field))
				.setText(getStringFromValue(mEditing.getAttackBonus()));
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_bonus_field))
				.setText(getStringFromValue(mEditing.getDamageBonus()));
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_die_field)).setText(mEditing
				.getDamageDie());
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_shield_bonus_field))
				.setText(getStringFromValue(mEditing.getACBonus()));
		((EditText) getView().findViewById(R.id.fragment_gear_edit_name_field))
				.setText(mEditing.getName());
	}

	private String getStringFromValue(int value) {
		if (value == 0)
			return "";
		else
			return "" + value;
	}

	public void hookupListeners() {
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_attack_ability_spinner))
				.setOnItemSelectedListener(mAttackAbilityWatcher);
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_damage_ability_spinner))
				.setOnItemSelectedListener(mDamageAbilityWatcher);
		((Spinner) getView().findViewById(R.id.fragment_gear_edit_type_spinner))
				.setOnItemSelectedListener(mWeaponTypeWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_attack_bonus_field))
				.addTextChangedListener(mAttackBonusWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_bonus_field))
				.addTextChangedListener(mDamageBonusWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_die_field))
				.addTextChangedListener(mDamageDieWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_shield_bonus_field))
				.addTextChangedListener(mACBonusWatcher);
		((EditText) getView().findViewById(R.id.fragment_gear_edit_name_field))
				.addTextChangedListener(mNameWatcher);
	}

	private void initializeListeners() {
		mACBonusWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mEditing.setACBonus(newNumber);
			}
		};
		mAttackBonusWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mEditing.setAttackBonus(newNumber);
			}
		};
		mAttackAbilityWatcher = new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View source,
					int index, long id) {
				mEditing.setAttackAbility(index);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		mDamageAbilityWatcher = new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View source,
					int index, long id) {
				mEditing.setDamageAbility(index);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		};
		mDamageBonusWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				mEditing.setDamageBonus(newNumber);
			}
		};
		mDamageDieWatcher = new TextWatcher() {

			public void afterTextChanged(Editable arg0) {
			}

			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				mEditing.setDamageDie(arg0.toString());
			}
		};
		mNameWatcher = new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				mEditing.setName(s.toString());
			}
		};
		mWeaponTypeWatcher = new Spinner.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View source,
					int index, long id) {
				mEditing.setWeaponType(index);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_gear_edit, container,
				false);
		setupAdapters(ret);
		initializeListeners();
		return ret;
	}

	@Override
	public void onPause() {
		unhookListeners();
		super.onPause();
	}

	@Override
	public void onResume() {
		fillFields();
		hookupListeners();
		super.onResume();
	}

	private void setupAdapters(View root) {
		ArrayAdapter<String> abilities = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				getActivity().getResources().getStringArray(
						R.array.ability_array));
		ArrayAdapter<String> abilities_with_none = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				getActivity().getResources().getStringArray(
						R.array.ability_array_with_none));
		ArrayAdapter<String> types = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, getActivity()
						.getResources().getStringArray(
								R.array.weapon_types_array));
		((Spinner) root
				.findViewById(R.id.fragment_gear_edit_attack_ability_spinner))
				.setAdapter(abilities);
		((Spinner) root
				.findViewById(R.id.fragment_gear_edit_damage_ability_spinner))
				.setAdapter(abilities_with_none);
		((Spinner) root.findViewById(R.id.fragment_gear_edit_type_spinner))
				.setAdapter(types);
	}

	public void unhookListeners() {
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_attack_ability_spinner))
				.setOnItemSelectedListener(null);
		((Spinner) getView().findViewById(
				R.id.fragment_gear_edit_damage_ability_spinner))
				.setOnItemSelectedListener(null);
		((Spinner) getView().findViewById(R.id.fragment_gear_edit_type_spinner))
				.setOnItemSelectedListener(null);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_attack_bonus_field))
				.removeTextChangedListener(mAttackBonusWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_bonus_field))
				.removeTextChangedListener(mDamageBonusWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_damage_die_field))
				.removeTextChangedListener(mDamageDieWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_gear_edit_shield_bonus_field))
				.removeTextChangedListener(mACBonusWatcher);
		((EditText) getView().findViewById(R.id.fragment_gear_edit_name_field))
				.removeTextChangedListener(mNameWatcher);
	}
}
