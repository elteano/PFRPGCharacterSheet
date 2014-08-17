package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.AbilityScores;
import org.elteano.charactersheet.model.ArmorClass;
import org.elteano.charactersheet.model.Attack;
import org.elteano.charactersheet.model.Save;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.ACButtonListener;
import org.elteano.charactersheet.view.support.SaveButtonListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * A fragment consolidating all combat statistics.
 */
public class AttackPanelFragment extends Fragment {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_attack_panel, container,
				false);
		Spinner mMainSpinner = (Spinner) ret
				.findViewById(R.id.fragment_attack_panel_main_spinner);
		Spinner mOffSpinner = (Spinner) ret
				.findViewById(R.id.fragment_attack_panel_off_spinner);
		ArrayList<String> list = new ArrayList<String>();
		for (Attack a : ((CharacterSheetActivity) getActivity()).getCharacter()
				.getAttackList()) {
			list.add(a.name);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		mMainSpinner.setAdapter(adapter);
		mOffSpinner.setAdapter(adapter);

		return ret;
	}

	@Override
	public void onStart() {
		fillButtonDisplays();
		hookupListeners();
		super.onStart();
	}

	private void fillButtonDisplays() {
		ArmorClass ac = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getAC();
		AbilityScores abilities = ((CharacterSheetActivity) getActivity())
				.getCharacter().getAbilities();
		int size = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getSize();
		int bab = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getBAB();
		((Button) getView().findViewById(R.id.fragment_attack_panel_ac_button))
				.setText("" + ac.getAC(abilities, size, bab));
		((Button) getView().findViewById(R.id.fragment_attack_panel_cmd_button))
				.setText("" + ac.getCMD(abilities, size, bab));
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

	private void hookupListeners() {
		ACButtonListener acButtonListener = new ACButtonListener(this);
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
	}
}
