package org.elteano.charactersheet.view.support;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class IntTextWatcher implements TextWatcher {

	public final void afterTextChanged(Editable s) {
		int b;
		try {
			b = Integer.parseInt(s.toString());
		} catch (NumberFormatException ex) {
			b = 0;
		}
		numberChanged(b);
	}

	public final void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public final void onTextChanged(CharSequence s, int start, int before,
			int count) {
	}

	public abstract void numberChanged(int newNumber);
}
