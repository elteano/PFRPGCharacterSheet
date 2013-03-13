package org.elteano.charactersheet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DefenseFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private static final int REQUEST_AC = 0;
	private static final int REQUEST_CMD = 1;
	private static final int REQUEST_FORT = 2;
	private static final int REQUEST_REF = 3;
	private static final int REQUEST_WILL = 4;

	private void fillFields() {
		((Button) getView().findViewById(R.id.fragment_defense_ac_button))
				.setText("" + CharacterSheetActivity.getCharacter().getAC().getAC());
		((Button) getView().findViewById(R.id.fragment_defense_cmd_button))
				.setText("" + CharacterSheetActivity.getCharacter().getAC().getCMD());
		((Button) getView()
				.findViewById(R.id.fragment_defense_fortitude_button))
				.setText(""
						+ CharacterSheetActivity.getCharacter().getFort().getTotal());
		((Button) getView().findViewById(R.id.fragment_defense_reflex_button))
				.setText(""
						+ CharacterSheetActivity.getCharacter().getRef().getTotal());
		((Button) getView().findViewById(R.id.fragment_defense_will_button))
				.setText(""
						+ CharacterSheetActivity.getCharacter().getWill().getTotal());
	}

	private void hookupListeners() {
		((Button) getView().findViewById(R.id.fragment_defense_ac_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(R.id.fragment_defense_cmd_button))
				.setOnClickListener(this);
		((Button) getView()
				.findViewById(R.id.fragment_defense_fortitude_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(R.id.fragment_defense_reflex_button))
				.setOnClickListener(this);
		((Button) getView().findViewById(R.id.fragment_defense_will_button))
				.setOnClickListener(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_AC:
			case REQUEST_CMD:
				CharacterSheetActivity.getCharacter().setAC((ArmorClass) data
						.getExtras().getParcelable("result"));
				break;
			case REQUEST_FORT:
				CharacterSheetActivity.getCharacter().setFort((Save) data
						.getExtras().getParcelable("result"));
				break;
			case REQUEST_REF:
				CharacterSheetActivity.getCharacter().setRef((Save) data.getExtras()
						.getParcelable("result"));
				break;
			case REQUEST_WILL:
				CharacterSheetActivity.getCharacter().setWill((Save) data
						.getExtras().getParcelable("result"));
				break;
			}
			CharacterSheetActivity.getCharacter()
					.saveSelfByPlayerList(getActivity());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View source) {
		Intent intent = new Intent(getActivity(), SaveEditActivity.class);
		int request = -1;
		switch (source.getId()) {
		case R.id.fragment_defense_ac_button:
			intent = new Intent(getActivity(), ACEditActivity.class);
			intent.putExtra("input", CharacterSheetActivity.getCharacter().getAC());
			request = REQUEST_AC;
			startActivityForResult(intent, request);
			return;
		case R.id.fragment_defense_cmd_button:
			request = REQUEST_CMD;
			return;
		case R.id.fragment_defense_fortitude_button:
			intent.putExtra("input", CharacterSheetActivity.getCharacter().getFort());
			request = REQUEST_FORT;
			break;
		case R.id.fragment_defense_reflex_button:
			intent.putExtra("input", CharacterSheetActivity.getCharacter().getRef());
			request = REQUEST_REF;
			break;
		case R.id.fragment_defense_will_button:
			intent.putExtra("input", CharacterSheetActivity.getCharacter().getWill());
			request = REQUEST_WILL;
			break;
		}
		startActivityForResult(intent, request);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_defense, container, false);
	}

	@Override
	public void onStart() {
		fillFields();
		hookupListeners();
		super.onStart();
	}

	@Override
	public void preUpdate() {
		// Unused
	}

	@Override
	public void updateDisplay() {
		fillFields();
	}
}
