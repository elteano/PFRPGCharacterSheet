package org.elteano.charactersheet.model;

public class Modifier {

	private int modifier;
	private String source;
	private String desc;

	public Modifier(int modifier, String source, String desc) {
		this.modifier = modifier;
		this.source = source;
		this.desc = desc;
	}

	public int getModifier() {
		return modifier;
	}

	public String getSource() {
		return source;
	}

	public String getDesc() {
		return desc;
	}
}
