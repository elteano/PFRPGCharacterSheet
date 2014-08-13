package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.IntTextWatcher;
import org.elteano.charactersheet.R;
import org.elteano.charactersheet.R.id;
import org.elteano.charactersheet.R.layout;
import org.elteano.charactersheet.model.AbilityScore;
import org.elteano.charactersheet.model.HP;
import org.elteano.charactersheet.model.PlayerCharacter;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.activity.HPEditActivity;
import org.elteano.charactersheet.view.activity.ModValueEditActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StatsFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private IntTextWatcher currentWatcher;
	private IntTextWatcher initWatcher;

	private void fillFields() {
		EditText et = (EditText) getView().findViewById(
				R.id.fragment_stats_current_hp_field);
		int tempVal = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getHPCurrent();
		et.setText((tempVal != 0) ? "" + tempVal : "");
		et = (EditText) getView().findViewById(
				R.id.fragment_stats_misc_init_bonus_field);
		tempVal = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getMiscInitBonus();
		((CharacterSheetActivity) getActivity())
				.getCharacter()
				.getHP()
				.ensureLevelCount(
						((CharacterSheetActivity) getActivity()).getCharacter()
								.getTotalLevel());
		((Button) getView().findViewById(R.id.fragment_stats_rolled_hp_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getHPRolled());
		et.setText((tempVal != 0) ? "" + tempVal : "");
	}

	private void hookupClickListeners() {
		getView().findViewById(R.id.cha_button).setOnClickListener(this);
		getView().findViewById(R.id.con_button).setOnClickListener(this);
		getView().findViewById(R.id.dex_button).setOnClickListener(this);
		getView().findViewById(R.id.int_button).setOnClickListener(this);
		getView().findViewById(R.id.str_button).setOnClickListener(this);
		getView().findViewById(R.id.wis_button).setOnClickListener(this);
		getView().findViewById(R.id.fragment_stats_rolled_hp_button)
				.setOnClickListener(this);
		getView().findViewById(R.id.fragment_stats_max_hp_button)
				.setOnClickListener(this);
	}

	private void hookupTextListeners() {
		((EditText) getView()
				.findViewById(R.id.fragment_stats_current_hp_field))
				.addTextChangedListener(currentWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_stats_misc_init_bonus_field))
				.addTextChangedListener(initWatcher);
	}

	private int idToRequest(int id) {
		switch (id) {
		case R.id.cha_button:
			return ModValueEditActivity.REQUEST_CHA;
		case R.id.con_button:
			return ModValueEditActivity.REQUEST_CON;
		case R.id.dex_button:
			return ModValueEditActivity.REQUEST_DEX;
		case R.id.int_button:
			return ModValueEditActivity.REQUEST_INT;
		case R.id.str_button:
			return ModValueEditActivity.REQUEST_STR;
		case R.id.wis_button:
			return ModValueEditActivity.REQUEST_WIS;
		}
		return -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				((CharacterSheetActivity) getActivity()).getCharacter()
						.setHPCurrent(newNumber);
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
			}
		};
		initWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				((CharacterSheetActivity) getActivity()).getCharacter()
						.setInitBonus(newNumber);
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
				updateButtons();
			}
		};
		return inflater.inflate(R.layout.fragment_stats, container, false);
	}

	/**
	 * Updates all of the display buttons within the StatsFragment.
	 */
	public void updateButtons() {
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		if (getView() == null)
			Log.d("CharacterSheet", "Error, view null");
		((Button) getView().findViewById(R.id.cha_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_CHA).toString());
		((Button) getView().findViewById(R.id.con_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_CON).toString());
		((Button) getView().findViewById(R.id.dex_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_DEX).toString());
		((Button) getView().findViewById(R.id.int_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_INT).toString());
		((Button) getView().findViewById(R.id.str_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_STR).toString());
		((Button) getView().findViewById(R.id.wis_button)).setText(c
				.getAbility(PlayerCharacter.ABILITY_WIS).toString());
		((Button) getView().findViewById(R.id.fragment_stats_rolled_hp_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getHPRolled());
		((Button) getView().findViewById(R.id.fragment_stats_init_bonus_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getInitiative());
		((Button) getView().findViewById(R.id.fragment_stats_max_hp_button))
				.setText(""
						+ ((CharacterSheetActivity) getActivity())
								.getCharacter().getHPMax());

		// Update temporary bonus labels
		for (int i = 0; i < 6; i++) {
			int id = 0;
			switch (i) {
			case PlayerCharacter.ABILITY_CHA:
				id = R.id.cha_text;
				break;
			case PlayerCharacter.ABILITY_CON:
				id = R.id.con_text;
				break;
			case PlayerCharacter.ABILITY_DEX:
				id = R.id.dex_text;
				break;
			case PlayerCharacter.ABILITY_INT:
				id = R.id.int_text;
				break;
			case PlayerCharacter.ABILITY_STR:
				id = R.id.str_text;
				break;
			case PlayerCharacter.ABILITY_WIS:
				id = R.id.wis_text;
				break;
			}
			if (id != 0) {
				TextView tv = (TextView) getView().findViewById(id);
				if (((CharacterSheetActivity) getActivity()).getCharacter()
						.getAbility(i).getTempAdjustment() != 0) {
					if (!tv.getText().toString().contains("*"))
						tv.setText("*" + tv.getText().toString());
				} else if (tv.getText().toString().startsWith("*"))
					tv.setText(tv.getText().toString().substring(1));
			}
		}
	}

	public void onClick(View view) {
		Intent intent = new Intent(getActivity(), ModValueEditActivity.class);
		intent.putExtra("Ability", view.getId());
		Bundle b = new Bundle();
		PlayerCharacter c = ((CharacterSheetActivity) getActivity())
				.getCharacter();
		switch (view.getId()) {
		case R.id.fragment_stats_rolled_hp_button:
			Log.i("CharacterSheet", "HP button clicked");
			intent = new Intent(getActivity(), HPEditActivity.class);
			intent.putExtra(HPEditActivity.INPUT_HP, (Parcelable) c.getHP());
			intent.putExtra(HPEditActivity.INPUT_PER_LEVEL_MOD,
					c.getAbility(PlayerCharacter.ABILITY_CON).getTempModifier());
			intent.putExtra(HPEditActivity.INPUT_TOTAL_LEVELS,
					c.getTotalLevel());
			startActivityForResult(intent, HPEditActivity.REQUEST_EDIT);
			return;
		case R.id.cha_button:
			c.getAbility(PlayerCharacter.ABILITY_CHA).addToBundle(b);
			break;
		case R.id.con_button:
			c.getAbility(PlayerCharacter.ABILITY_CON).addToBundle(b);
			break;
		case R.id.dex_button:
			c.getAbility(PlayerCharacter.ABILITY_DEX).addToBundle(b);
			break;
		case R.id.int_button:
			c.getAbility(PlayerCharacter.ABILITY_INT).addToBundle(b);
			break;
		case R.id.str_button:
			c.getAbility(PlayerCharacter.ABILITY_STR).addToBundle(b);
			break;
		case R.id.wis_button:
			c.getAbility(PlayerCharacter.ABILITY_WIS).addToBundle(b);
			break;
		case R.id.fragment_stats_max_hp_button:
			((EditText) getView().findViewById(
					R.id.fragment_stats_current_hp_field)).setText(""
					+ ((CharacterSheetActivity) getActivity()).getCharacter()
							.getHPMax());
			return;
		}
		intent.putExtras(b);
		startActivityForResult(intent, idToRequest(view.getId()));
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("CharacterSheet", "StatsFragment.onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			Log.i("CharacterSheet",
					"StatsFragment receiving cancelled request.");
			return;
		}
		int dest = 0;
		Log.i("CharacterSheet", "Request code is " + requestCode);
		switch (requestCode) {
		case ModValueEditActivity.REQUEST_CHA:
			dest = PlayerCharacter.ABILITY_CHA;
			break;
		case ModValueEditActivity.REQUEST_CON:
			dest = PlayerCharacter.ABILITY_CON;
			break;
		case ModValueEditActivity.REQUEST_DEX:
			dest = PlayerCharacter.ABILITY_DEX;
			break;
		case ModValueEditActivity.REQUEST_INT:
			dest = PlayerCharacter.ABILITY_INT;
			break;
		case ModValueEditActivity.REQUEST_STR:
			dest = PlayerCharacter.ABILITY_STR;
			break;
		case ModValueEditActivity.REQUEST_WIS:
			dest = PlayerCharacter.ABILITY_WIS;
			break;
		case HPEditActivity.REQUEST_EDIT:
			((CharacterSheetActivity) getActivity()).getCharacter().setHP(
					(HP) data.getParcelableExtra(HPEditActivity.RESULT));
			Log.i("CharacterSheet", "Max HP: "
					+ ((CharacterSheetActivity) getActivity()).getCharacter()
							.getHPMax());
			((CharacterSheetActivity) getActivity()).getCharacter()
					.saveSelfByPlayerList(getActivity());
			return;
		}
		((CharacterSheetActivity) getActivity()).getCharacter().setAbility(
				dest, new AbilityScore(data.getExtras()));
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
	}

	@Override
	public void onResume() {
		updateButtons();
		fillFields();
		hookupClickListeners();
		hookupTextListeners();
		super.onResume();
	}

	@Override
	public void onPause() {
		unhookTextListeners();
		super.onPause();
	}

	private void unhookTextListeners() {
		((EditText) getView()
				.findViewById(R.id.fragment_stats_current_hp_field))
				.removeTextChangedListener(currentWatcher);
		((EditText) getView().findViewById(
				R.id.fragment_stats_misc_init_bonus_field))
				.removeTextChangedListener(initWatcher);
	}

	@Override
	public void updateDisplay() {
		unhookTextListeners();
		updateButtons();
		fillFields();
		hookupTextListeners();
	}

	@Override
	public void preUpdate() {
		// Unused
	}
}
