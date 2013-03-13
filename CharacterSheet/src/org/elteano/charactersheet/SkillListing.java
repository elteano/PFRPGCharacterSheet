package org.elteano.charactersheet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class SkillListing extends TableRow {

	private Skill skill;
	private TextView nameView;
	private Button pointsView;

	public SkillListing(Context context, AttributeSet attrs) {
		super(context, attrs);
		nameView = new TextView(context);
		nameView.setTextSize(24);
		nameView.setText("Test SkillListing");
		pointsView = new Button(context);
		pointsView.setClickable(false);
		pointsView.setText("+0");
		addView(nameView, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(pointsView, 1, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

	public Skill getSkill() {
		return skill;
	}

	// @Override
	// protected void onRestoreInstanceState(Parcelable state) {
	// if (!(state instanceof SavedState)) {
	// super.onRestoreInstanceState(state);
	// return;
	// }
	// SavedState ss = (SavedState) state;
	// super.onRestoreInstanceState(ss.getSuperState());
	// abilityUsed = ss.ability;
	// skillRanks = ss.skillRanks;
	// skillName = ss.skillName;
	// updateName();
	// }

	// @Override
	// protected Parcelable onSaveInstanceState() {
	// SavedState ret = new SavedState(super.onSaveInstanceState());
	// ret.ability = abilityUsed;
	// ret.skillRanks = skillRanks;
	// ret.skillName = skillName;
	// return ret;
	// }

	public void setAbilityUsed(int used) {
		skill.baseAbility = used;
	}

	public void setName(String name) {
		skill.skillName = name;
	}

	public void setToSkill(Skill skill) {
		this.skill = skill;
		setAbilityUsed(skill.getBaseAbility());
		setName(skill.getName());
		int totalMod = skill.getTotalModifier();
		String prefix = (totalMod < 0) ? "" : "+";
		pointsView.setText(prefix + totalMod);
		updateName();
	}

	public void updateName() {
		nameView.setText(skill.skillName);
	}

	// private static class SavedState extends View.BaseSavedState {
	// public int ability;
	// public int skillRanks;
	// public String skillName;
	//
	// public SavedState(Parcelable superState) {
	// super(superState);
	// }
	//
	// public SavedState(Parcel p) {
	// super(p);
	// ability = p.readInt();
	// skillRanks = p.readInt();
	// skillName = p.readString();
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// super.writeToParcel(dest, flags);
	// dest.writeParcelable(skill, flags);
	// dest.writeInt(ability);
	// dest.writeInt(skillRanks);
	// dest.writeString(skillName);
	// }
	// }
}
