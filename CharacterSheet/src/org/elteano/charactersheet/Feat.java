package org.elteano.charactersheet;

import android.os.Bundle;

public class Feat {

	private static final String BUNDLE_NAME = "org.elteano.charactersheet.Feat.name";
	private static final String BUNDLE_DESC = "org.elteano.charactersheet.Feat.desc";

	private String name;
	private String desc;

	public Feat() {
		this("", "");
	}

	public Feat(String name, String desc) {
		this.name = name.trim();
		this.desc = desc.trim();
	}

	public Feat(Bundle bundle) {
		this(bundle.getString(BUNDLE_NAME), bundle.getString(BUNDLE_DESC));
	}

	public void addSelfToBundle(Bundle out) {
		out.putString(name, desc);
	}

	public static Feat fromSaveString(String saveString) {
		String[] cont = saveString.split(PlayerCharacter.SPLITTER_SMALL);
		if (cont.length != 2)
			return null;
		return new Feat(cont[0], cont[1]);
	}

	public String getDescription() {
		return desc;
	}

	public String getName() {
		return name;
	}

	public Bundle saveToBundle() {
		Bundle ret = new Bundle();
		ret.putString(BUNDLE_NAME, name);
		ret.putString(BUNDLE_DESC, desc);
		return ret;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toSaveString() {
		if (desc.isEmpty())
			desc = " ";
		if (name.isEmpty())
			name = " ";
		return name + PlayerCharacter.SPLITTER_SMALL + desc;
	}
}
