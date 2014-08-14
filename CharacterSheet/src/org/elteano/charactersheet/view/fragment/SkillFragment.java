package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.model.Skill;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;
import org.elteano.charactersheet.view.activity.SkillEditActivity;
import org.elteano.charactersheet.view.support.SkillListing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;

public class SkillFragment extends CharacterUpdaterFragment implements
		OnClickListener {

	private Skill lastSkill;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SkillEditActivity.REQUEST_EDIT_SKILL
				&& resultCode != Activity.RESULT_CANCELED)
			((CharacterSheetActivity) getActivity()).getCharacter()
					.removeSkill(lastSkill);
		if (resultCode == Activity.RESULT_OK) {
			lastSkill = null;
			Skill result = data
					.getParcelableExtra(SkillEditActivity.RESULT_SKILL);
			((CharacterSheetActivity) getActivity()).getCharacter().addSkill(
					result);
			Log.d("CharacterSheet", result.toSaveString());
		}
		((CharacterSheetActivity) getActivity()).getCharacter()
				.saveSelfByPlayerList(getActivity());
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void addSkillListing(Skill skill) {
		SkillListing sl = new SkillListing(getActivity(), null);
		sl.setBackgroundResource(android.R.drawable.list_selector_background);
		sl.setClickable(true);
		sl.setToSkill(skill);
		sl.setOnClickListener(this);
		sl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		((TableLayout) getActivity().findViewById(R.id.skill_line)).addView(sl);
	}

	private void clearListings() {
		((TableLayout) getView().findViewById(R.id.skill_line))
				.removeAllViews();
	}

	private void fillListings() {
		ArrayList<Skill> list = ((CharacterSheetActivity) getActivity())
				.getCharacter().getSkillList();
		for (int i = 0; i < list.size() - 1; i++) {
			int swapWith = i;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(j).skillName
						.compareTo(list.get(swapWith).skillName) < 0)
					swapWith = j;
			}
			Skill s = list.get(i);
			list.set(i, list.get(swapWith));
			list.set(swapWith, s);
		}
		for (Skill s : list) {
			addSkillListing(s);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View ret = inflater.inflate(R.layout.fragment_skill, container, false);
		return ret;
	}

	@Override
	public void onResume() {
		clearListings();
		fillListings();
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
			inflater.inflate(R.menu.fragment_skill_menu_large, menu);
		else
			inflater.inflate(R.menu.skill_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_skill:
			Intent intent = new Intent(getActivity(), SkillEditActivity.class);
			intent.putExtra(SkillEditActivity.INPUT_ABILITIES,
					(Parcelable) ((CharacterSheetActivity) getActivity())
							.getCharacter().getAbilities());
			startActivityForResult(intent, SkillEditActivity.REQUEST_NEW_SKILL);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View source) {
		Intent intent = new Intent(getActivity(), SkillEditActivity.class);
		lastSkill = ((SkillListing) source).getSkill();
		intent.putExtra(SkillEditActivity.INPUT_SKILL, (Parcelable) lastSkill);
		intent.putExtra(SkillEditActivity.INPUT_ABILITIES,
				(Parcelable) ((CharacterSheetActivity) getActivity())
						.getCharacter().getAbilities());
		startActivityForResult(intent, SkillEditActivity.REQUEST_EDIT_SKILL);
	}

	@Override
	public void updateDisplay() {
		clearListings();
		fillListings();
	}

	@Override
	public void preUpdate() {
		// Unused
	}
}
