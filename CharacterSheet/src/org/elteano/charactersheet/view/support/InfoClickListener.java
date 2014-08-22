package org.elteano.charactersheet.view.support;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class InfoClickListener implements OnClickListener {

	private Context mContext;
	private String mMessage;

	public InfoClickListener(Context context, String message) {
		mContext = context;
		mMessage = message;
	}

	public void setInfoMessage(String message) {
		mMessage = message;
	}

	public void onClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Info");
		builder.setMessage(mMessage);
		builder.show();
	}
}
