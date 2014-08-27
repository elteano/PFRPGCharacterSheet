package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.ArmorClass;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.model.Save;
import org.elteano.charactersheet.model.WeapShield;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.ACButtonListener;
import org.elteano.charactersheet.view.support.InfoClickListener;
import org.elteano.charactersheet.view.support.SaveButtonListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A fragment consolidating all combat statistics.
 */
public class AttackPanelFragment extends CharacterUpdaterFragment implements
		View.OnClickListener {

	private InfoClickListener mACInfoListener;
	private Spinner mMainSpinner;
	private Spinner mOffSpinner;
	private OffHandSpinnerAdapter mOffSpinnerAdapter;

	private void fillACButton() {
		fillACButton(false);
	}

	private void fillACButton(boolean useMain) {
		fillACText();
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		ArmorClass ac = c.getAC();
		AbilityScores abilities = c.getAbilities();
		int size = c.getSize();
		int bab = c.getBAB();
		int modifiers = 0;
		if (isCharging()) {
			modifiers -= 2;
		}
		if (isDefensive()) {
			modifiers += 2;
		}
		if (isExpertise()) {
			modifiers += c.getExpertiseBonus();
		}
		if (isCleaving()) {
			modifiers -= 2;
		}
		if (isLunging()) {
			modifiers -= 2;
		}
		WeapShield main = (WeapShield) mMainSpinner.getSelectedItem();
		WeapShield off = (WeapShield) mOffSpinner.getSelectedItem();
		int mainBonus = 0;
		if (main != null && useMain)
			mainBonus = main.getACBonus();
		int offBonus = 0;
		if (off != null && !isTwoHanded()) {
			offBonus = off.getACBonus();
		} else if (isTwoHanded() && c.hasFeat("Two-Weapon Defense")) {
			if (isDefensive()) {
				modifiers += 1;
			} else {
				modifiers += 2;
			}
		}
		((Button) getView().findViewById(R.id.fragment_attack_panel_ac_button))
				.setText(""
						+ (ac.getACFromCharacter(c)
								+ Math.max(mainBonus, offBonus) + modifiers));
		((Button) getView().findViewById(R.id.fragment_attack_panel_cmd_button))
				.setText("" + ac.getCMD(abilities, size, bab));
	}

	private void fillACText() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		TextView acText = ((TextView) getView().findViewById(
				R.id.fragment_attack_panel_ac_text));
		if (c.hasAnyConditions()) {
			acText.setText(R.string.ac_);
		} else {
			acText.setText(R.string.ac);
		}
		mACInfoListener.setInfoMessage(getConditionACInfoString());
	}

	private void fillButtonDisplays() {
		fillACButton(true);
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
		mOffSpinner.setSelection(mOffSpinner.getCount());
		for (int i = 0; i < c.getWieldableEquipment().size(); ++i) {
			WeapShield ws = c.getWieldableEquipment().get(i);
			if (ws.getName().equals(c.getPreviouslySelectedOffWeapon())) {
				mOffSpinner.setSelection(i);
				break;
			}
		}
	}

	private String getConditionACInfoString() {
		StringBuilder ret = new StringBuilder();
		int totalModifiers = 0;
		int rangedDifference = 0;
		boolean rangedDiffers = false;
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		if (c.isBlinded()) {
			ret.append("Blinded: -2\n");
			totalModifiers -= 2;
		}
		if (c.isCowering()) {
			ret.append("Cowering: -2\n");
			totalModifiers -= 2;
		}
		if (ArmorClass.getAbilityToDodge(c)) {
			if (c.hasFeat("Dodge, Mythic") || c.hasFeat("Mythic Dodge")
					|| c.hasFeat("Dodge (Mythic)")) {
				ret.append("Mythic Dodge: +2 to AC\n");
			} else if (c.hasFeat("Dodge")) {
				ret.append("Dodge: +1 to AC\n");
			}
		}
		if (c.isEntangled()) {
			ret.append("Entangled: -4 to DEX\n");
		}
		if (c.isFlatFooted()) {
			ret.append("Flat-Footed: DEX bonus to AC lost\n");
		}
		if (c.isHelpless()) {
			ret.append("Helpless: -4 to melee (ranged unaffected), DEX bonus to AC lost\n");
			totalModifiers -= 4;
			rangedDifference += 4;
		}
		if (c.isKneeling()) {
			ret.append("Kneeling or Sitting: -2 to melee, +2 to ranged\n");
			totalModifiers -= 2;
			rangedDifference += 4;
		}
		if (c.isPinned()) {
			ret.append("Pinned: -4 to melee (ranged unaffected)\n");
			totalModifiers -= 4;
			rangedDifference += 4;
			rangedDiffers = true;
		}
		if (c.isProne()) {
			ret.append("Prone: -4 to melee, +4 to ranged\n");
			totalModifiers -= 4;
			rangedDifference += 8;
			rangedDiffers = true;
		}
		if (c.isSqueezing()) {
			ret.append("Squeezing: -4\n");
			totalModifiers -= 4;
		}
		if (c.isStunned()) {
			ret.append("Stunned: -2, DEX bonus to AC lost\n");
			totalModifiers -= 2;
		}
		ret.append("Total modifier: ");
		ret.append(totalModifiers);
		ret.append("\n");
		ArmorClass ac = c.getAC();
		if (rangedDiffers) {
			ret.append("Melee AC: ");
			ret.append(ac.getACFromCharacter(c));
			ret.append("\n");
			ret.append("Ranged AC: ");
			ret.append(ac.getACFromCharacter(c) + rangedDifference);
		} else {
			ret.append("AC: ");
			ret.append(ac.getACFromCharacter(c));
		}
		ret.append("\n");
		ret.append(getResources().getString(R.string.note_ac_mods));
		return ret.toString();
	}

	private void handleConditionsDrawerCreation(View root) {
		View v = root.findViewById(R.id.fragment_attack_panel_condition_drawer);
		DrawerLayout layout = ((DrawerLayout) root
				.findViewById(R.id.fragment_attack_panel_drawer_layout));
		if (v != null)
			layout.removeView(v);
		if (((CharacterSheetActivity) getActivity()).isPortraitLayout()) {
			Fragment f = new ConditionsFragment(this);
			DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(
					(int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 180, getResources()
									.getDisplayMetrics()),
					DrawerLayout.LayoutParams.MATCH_PARENT, Gravity.END);
			v = new LinearLayout(getActivity(), null);
			v.setLayoutParams(params);
			v.setId(R.id.fragment_attack_panel_condition_drawer);
			layout.addView(v);
			FragmentTransaction t = getActivity().getSupportFragmentManager()
					.beginTransaction();
			t.add(R.id.fragment_attack_panel_condition_drawer, f);
			t.commit();
		}
	}

	private void hookupListeners() {
		ACButtonListener acButtonListener = new ACButtonListener(this);
		((TextView) getView().findViewById(R.id.fragment_attack_panel_ac_text))
				.setOnClickListener(mACInfoListener);
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

	private boolean isCharging() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_charge)).isChecked();
	}

	private boolean isCleaving() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_cleave)).isChecked();
	}

	private boolean isDefensive() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_defensive)).isChecked();
	}

	private boolean isExpertise() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_expertise)).isChecked();
	}

	private boolean isLunging() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_lunge)).isChecked();
	}

	private boolean isTwoHanded() {
		return ((CheckBox) getView().findViewById(
				R.id.fragment_attack_panel_check_two_handed)).isChecked();
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
		if (main != null) {
			mainHand = main.calculateAttack(
					((CharacterSheetActivity) getActivity()).getCharacter(),
					flags);
		} else {
			Toast.makeText(getActivity(), R.string.no_weapon_notice,
					Toast.LENGTH_LONG).show();
			return;
		}
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
		fillACButton(true);
		((CharacterSheetActivity) getActivity()).getCharacter()
				.clearConditions();
		updateOthers();
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
		mACInfoListener = new InfoClickListener(getActivity(), "");
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
		handleConditionsDrawerCreation(ret);
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
		case R.id.fragment_attack_panel_menu_info:
			AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
			build.setTitle(R.string.info);
			if (((CharacterSheetActivity) getActivity()).isPortraitLayout()) {
				build.setMessage(getResources().getString(
						R.string.attack_panel_info_standard)
						+ " "
						+ getResources().getString(
								R.string.attack_panel_info_portrait));
			} else {
				build.setMessage(R.string.attack_panel_info_standard);
			}
			build.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		((ArrayAdapter) mMainSpinner.getAdapter()).notifyDataSetChanged();
		((ArrayAdapter) mOffSpinner.getAdapter()).notifyDataSetChanged();
		fillSpinners();
		super.onResume();
	}

	@Override
	public void onStart() {
		fillButtonDisplays();
		hookupListeners();
		super.onStart();
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
		if (c.hasFeat("Lunge")) {
			((CheckBox) root
					.findViewById(R.id.fragment_attack_panel_check_lunge))
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

	@Override
	public void preUpdate() {
	}

	@Override
	public void updateDisplay() {
		fillButtonDisplays();
	}
}
