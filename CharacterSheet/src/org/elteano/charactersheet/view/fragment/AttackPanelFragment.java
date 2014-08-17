package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.Attack;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A fragment consolidating all combat statistics.
 */
public class AttackPanelFragment extends Fragment {

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
}
