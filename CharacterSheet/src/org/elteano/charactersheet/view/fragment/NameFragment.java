package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.support.IntTextWatcher;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	private EditText nameView;
	private IntTextWatcher xpTextWatcher;
	/**
	 * The old name of the character currently being edited.
	 *
	 * An empty value signifies that the name was not changed.
	 */
	private String mOldName;

	private void fillFields() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.size_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner spinner = (Spinner) getView().findViewById(
				R.id.fragment_name_character_size_spinner);
		spinner.setAdapter(adapter);
		spinner.setSelection(((CharacterSheetActivity) getActivity())
				.getCharacter().getSize());
		EditText ageView = (EditText) getView().findViewById(
				R.id.fragment_name_character_age_field);
		int tempVal = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getAge();
		ageView.setText((tempVal != 0) ? "" + tempVal : "");
		EditText bioView = (EditText) getView().findViewById(
				R.id.fragment_name_character_bio_field);
		bioView.setText(((CharacterSheetActivity) getActivity()).getCharacter()
				.getBio());
		EditText classView = (EditText) getView().findViewById(
				R.id.fragment_name_class_level_field);
		classView.setText(((CharacterSheetActivity) getActivity())
				.getCharacter().getLevelString());
		EditText nameView = (EditText) getView().findViewById(
				R.id.character_name_name_field);
		nameView.setText(((CharacterSheetActivity) getActivity())
				.getCharacter().getName());
		EditText xpView = (EditText) getView().findViewById(
				R.id.fragment_name_xp_field);
		tempVal = ((CharacterSheetActivity) getActivity()).getCharacter()
				.getXP();
		xpView.setText((tempVal != 0) ? "" + tempVal : "");
		((EditText) getView().findViewById(R.id.fragment_name_languages_field))
				.setText(((CharacterSheetActivity) getActivity())
						.getCharacter().getLanguages());
	}

	private void hookupListeners() {
		((EditText) getView().findViewById(
				R.id.fragment_name_character_bio_field))
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							String s = ((EditText) v).getText().toString();
							((CharacterSheetActivity) getActivity())
									.getCharacter().setBio(s.toString());
							((CharacterSheetActivity) getActivity())
									.getCharacter().saveSelfByPlayerList(
											getActivity());
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		nameView.setText(((CharacterSheetActivity) getActivity())
				.getCharacter().getName());
		ageTextWatcher = new TextWatcher() {

			public void afterTextChanged(Editable s) {
				try {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setAge(Integer.parseInt(s.toString()));
				} catch (NumberFormatException e) {
					((CharacterSheetActivity) getActivity()).getCharacter()
							.setAge(0);
				}
				((CharacterSheetActivity) getActivity()).getCharacter()
						.saveSelfByPlayerList(getActivity());
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
				((CharacterSheetActivity) getActivity()).getCharacter().setXP(
						newNumber);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this CharacterUpdaterFragment
		mOldName = "";
		View ret = inflater.inflate(R.layout.fragment_name, container, false);
		nameView = (EditText) ret.findViewById(R.id.character_name_name_field);
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
		if (!mOldName.isEmpty()) {
			SharedPreferences.Editor editor = getActivity()
					.getSharedPreferences(
							CharacterSelectFragment.CHARACTER_LIST_PREFERENCE,
							Activity.MODE_PRIVATE).edit();
			editor.remove(mOldName);
			editor.commit();
		}
		pushFieldsToCharacter();
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onPause();
	}

	@Override
	public void onResume() {
		fillFields();
		hookupListeners();
		super.onResume();
	}

	private void pushFieldsToCharacter() {
		((CharacterSheetActivity) getActivity()).getCharacter().setLanguages(
				((EditText) getView().findViewById(
						R.id.fragment_name_languages_field)).getText()
						.toString());
		((CharacterSheetActivity) getActivity()).getCharacter().setBio(
				((EditText) getView().findViewById(
						R.id.fragment_name_character_bio_field)).getText()
						.toString());
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
			((CharacterSheetActivity) getActivity()).getCharacter()
					.setLevelString(text.toString());
			((CharacterSheetActivity) getActivity()).getCharacter()
					.saveSelfByPlayerList(getActivity());
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
			if (mOldName.isEmpty()) {
				mOldName = ((CharacterSheetActivity) getActivity())
						.getCharacter().getName();
			}
			EditText nameView = (EditText) getView().findViewById(
					R.id.character_name_name_field);
			((CharacterSheetActivity) getActivity()).getCharacter().setName(
					nameView.getText().toString());
			getActivity().getActionBar().setTitle(
					((CharacterSheetActivity) getActivity()).getCharacter()
							.getName());
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
		((CharacterSheetActivity) getActivity()).getCharacter().setSize(pos);
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
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
		if (((CharacterSheetActivity) getActivity()).getCharacter() != null) {
			pushFieldsToCharacter();
		}
	}
}