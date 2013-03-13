package org.elteano.charactersheet;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NameFragment extends CharacterUpdaterFragment implements
		OnItemSelectedListener {

	private TextWatcher ageTextWatcher;
	// private TextWatcher bioTextWatcher;
	private TextWatcher classTextWatcher;
	private TextWatcher nameTextWatcher;
	private IntTextWatcher xpTextWatcher;

	private void fillFields() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.size_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) getView().findViewById(
				R.id.fragment_name_character_size_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(CharacterSheetActivity.getCharacter().getSize());
		EditText ageView = (EditText) getView().findViewById(
				R.id.fragment_name_character_age_field);
		ageView.setText("" + CharacterSheetActivity.getCharacter().getAge());
		EditText bioView = (EditText) getView().findViewById(
				R.id.fragment_name_character_bio_field);
		bioView.setText(CharacterSheetActivity.getCharacter().getBio());
		EditText classView = (EditText) getView().findViewById(
				R.id.fragment_name_class_level_field);
		classView.setText(CharacterSheetActivity.getCharacter()
				.getLevelString());
		EditText nameView = (EditText) getView().findViewById(
				R.id.character_name_name_field);
		nameView.setText(CharacterSheetActivity.getCharacter().getName());
		EditText xpView = (EditText) getView().findViewById(
				R.id.fragment_name_xp_field);
		xpView.setText("" + CharacterSheetActivity.getCharacter().getXP());
		((EditText) getView().findViewById(R.id.fragment_name_languages_field))
				.setText(CharacterSheetActivity.getCharacter().getLanguages());
	}

	private void hookupListeners() {
		((EditText) getView().findViewById(
				R.id.fragment_name_character_bio_field))
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							String s = ((EditText) v).getText().toString();
							CharacterSheetActivity.getCharacter().setBio(
									s.toString());
							CharacterSheetActivity.getCharacter()
									.saveSelfByPlayerList(getActivity());
						}
					}
				});
		Spinner spinner = (Spinner) getView().findViewById(
				R.id.fragment_name_character_size_spinner);
		spinner.setOnItemSelectedListener(this);
		EditText ageView = (EditText) getView().findViewById(
				R.id.fragment_name_character_age_field);
		ageView.addTextChangedListener(ageTextWatcher);
		EditText classView = (EditText) getView().findViewById(
				R.id.fragment_name_class_level_field);
		classView.addTextChangedListener(classTextWatcher);
		EditText nameView = (EditText) getView().findViewById(
				R.id.character_name_name_field);
		nameView.addTextChangedListener(nameTextWatcher);
		EditText xpView = (EditText) getView().findViewById(
				R.id.fragment_name_xp_field);
		xpView.addTextChangedListener(xpTextWatcher);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this CharacterUpdaterFragment
		View ret = inflater.inflate(R.layout.fragment_name, container, false);
		final EditText nameView = (EditText) ret
				.findViewById(R.id.character_name_name_field);
		nameView.setText(CharacterSheetActivity.getCharacter().getName());
		ageTextWatcher = new TextWatcher() {

			public void afterTextChanged(Editable s) {
				try {
					CharacterSheetActivity.getCharacter().setAge(
							Integer.parseInt(s.toString()));
				} catch (NumberFormatException e) {
					CharacterSheetActivity.getCharacter().setAge(0);
				}
				CharacterSheetActivity.getCharacter().saveSelfByPlayerList(
						getActivity());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		xpTextWatcher = new IntTextWatcher() {

			@Override
			public void numberChanged(int newNumber) {
				CharacterSheetActivity.getCharacter().setXP(newNumber);
			}
		};
		classTextWatcher = new ClassTextWatcher();
		nameTextWatcher = new NameTextWatcher();
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_name_save:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		unhookListeners();
		pushFieldsToCharacter();
		super.onPause();
	}

	@Override
	public void onResume() {
		fillFields();
		hookupListeners();
		super.onResume();
	}

	public void onStop() {
		pushFieldsToCharacter();
		CharacterSheetActivity.getCharacter().saveSelfByPlayerList(
				getActivity());
		super.onStop();
	};

	// @Override
	// public void onStart() {
	// fillFields();
	// hookupListeners();
	// super.onStart();
	// }

	private void pushFieldsToCharacter() {
		CharacterSheetActivity.getCharacter().setLanguages(
				((EditText) getView().findViewById(
						R.id.fragment_name_languages_field)).getText()
						.toString());
		CharacterSheetActivity.getCharacter().setBio(
				((EditText) getView().findViewById(
						R.id.fragment_name_character_bio_field)).getText()
						.toString());
		Log.d("CharacterSheet", CharacterSheetActivity.getCharacter().getBio());
		Log.d("CharacterSheet", CharacterSheetActivity.getCharacter()
				.getLanguages());
	}

	private void unhookListeners() {
		((EditText) getView().findViewById(
				R.id.fragment_name_character_age_field))
				.removeTextChangedListener(ageTextWatcher);
		((EditText) getView()
				.findViewById(R.id.fragment_name_class_level_field))
				.removeTextChangedListener(classTextWatcher);
		((EditText) getView().findViewById(R.id.character_name_name_field))
				.removeTextChangedListener(nameTextWatcher);
		((EditText) getView().findViewById(R.id.fragment_name_xp_field))
				.removeTextChangedListener(xpTextWatcher);
	}

	private class ClassTextWatcher implements TextWatcher {

		public void afterTextChanged(Editable text) {
			CharacterSheetActivity.getCharacter().setLevelString(
					text.toString());
			CharacterSheetActivity.getCharacter().saveSelfByPlayerList(
					getActivity());
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

	}

	private class NameTextWatcher implements TextWatcher {

		public void afterTextChanged(Editable text) {
			SharedPreferences playerList = getActivity().getSharedPreferences(
					CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
					Activity.MODE_PRIVATE);
			EditText nameView = (EditText) getView().findViewById(
					R.id.character_name_name_field);
			for (String key : playerList.getAll().keySet()) {
				if (CharacterSheetActivity
						.getCharacter()
						.getName()
						.equals(playerList
								.getString(key, "baaaaaaaaaad7778123"))) {
					SharedPreferences.Editor editor = playerList.edit();
					editor.putString(key, nameView.getText().toString());
					editor.commit();
					editor = getActivity().getSharedPreferences(key,
							Activity.MODE_PRIVATE).edit();
					editor.putString(PlayerCharacter.SAVESTATE_NAME, nameView
							.getText().toString());
					editor.commit();
					break;
				}
			}
			CharacterSheetActivity.getCharacter().setName(
					nameView.getText().toString());
			getActivity().getActionBar().setTitle(
					CharacterSheetActivity.getCharacter().getName());
			updateOthers();
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		CharacterSheetActivity.getCharacter().setSize(pos);
		CharacterSheetActivity.getCharacter().saveSelfByPlayerList(
				getActivity());
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void updateDisplay() {
		unhookListeners();
		fillFields();
		hookupListeners();
	}

	@Override
	public void preUpdate() {
		if (CharacterSheetActivity.getCharacter() != null) {
			Log.d("CharacterSheet", "pushing fields");
			pushFieldsToCharacter();
		}
	}
}