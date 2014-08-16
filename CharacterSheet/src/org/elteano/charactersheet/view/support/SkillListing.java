package org.elteano.charactersheet.view.support;

import org.elteano.charactersheet.model.Skill;
import org.elteano.charactersheet.view.activity.CharacterSheetActivity;

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

	public void setAbilityUsed(int used) {
		skill.setBaseAbility(used);
	}

	public void setName(String name) {
		skill.setName(name);
	}

	public void setToSkill(Skill skill) {
		this.skill = skill;
		setAbilityUsed(skill.getBaseAbility());
		setName(skill.getName());
		int totalMod = skill
				.getTotalModifier(((CharacterSheetActivity) getContext())
						.getCharacter().getAbilities());
		String prefix = (totalMod < 0) ? "" : "+";
		pointsView.setText(prefix + totalMod);
		updateName();
	}

	public void updateName() {
		nameView.setText(skill.getName());
	}
}
