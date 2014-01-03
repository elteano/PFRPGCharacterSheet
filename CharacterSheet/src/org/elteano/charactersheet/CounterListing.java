package org.elteano.charactersheet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CounterListing extends RelativeLayout implements OnClickListener {

	private static final int DEC_BUTTON_ID = 25;
	private static final int INC_BUTTON_ID = 77;
	private static final int EDIT_BUTTON_ID = 256;
	private static final int LABEL_ID = 1245192;
	private static final int COUNT_ID = 0xFAF07;
	private static final int TEXT_SIZE = 22;
	private static final int BUTTON_WIDTH_DPS = 55;

	private Button decButton;
	private Button incButton;
	private ImageButton editButton;
	private Counter mCounter;
	private TextView label;
	private TextView count;

	/**
	 * Creates a new CounterListing within the given Context. Does not set the
	 * Counter to be displayed; that must be set after this using
	 * <code>setCounter()</code>.
	 * 
	 * @param context
	 *            The context in which this will reside.
	 * @param attrs
	 *            Don't really know. May be <code>null</code>.
	 */
	public CounterListing(Context context, AttributeSet attrs) {
		super(context, attrs);
		RelativeLayout.LayoutParams decButtonParams, incButtonParams, editButtonParams, labelParams, countParams;
		decButton = new Button(context);
		decButton.setId(DEC_BUTTON_ID);
		decButton.setText("-");
		incButton = new Button(context);
		incButton.setText("+");
		incButton.setId(INC_BUTTON_ID);
		editButton = new ImageButton(context);
		editButton.setId(EDIT_BUTTON_ID);
		label = new TextView(context);
		label.setId(LABEL_ID);
		count = new TextView(context);
		count.setId(COUNT_ID);

		labelParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		labelParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, TRUE);
		// labelParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		labelParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		label.setTextSize(TEXT_SIZE);
		label.setLayoutParams(labelParams);

		countParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// countParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		countParams.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
		countParams.addRule(RelativeLayout.BELOW, label.getId());
		count.setTextSize(TEXT_SIZE);
		count.setLayoutParams(countParams);

		final float scale = getContext().getResources().getDisplayMetrics().density;
		incButtonParams = new RelativeLayout.LayoutParams(
				(int) (BUTTON_WIDTH_DPS * scale + 0.5f),
				LayoutParams.WRAP_CONTENT);
		incButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
		incButtonParams.addRule(RelativeLayout.BELOW, label.getId());
		incButton.setLayoutParams(incButtonParams);
		incButton.setOnClickListener(this);

		decButtonParams = new RelativeLayout.LayoutParams(
				(int) (BUTTON_WIDTH_DPS * scale + 0.5f),
				LayoutParams.WRAP_CONTENT);
		decButtonParams.addRule(RelativeLayout.BELOW, label.getId());
		decButtonParams.addRule(RelativeLayout.LEFT_OF, incButton.getId());
		decButton.setLayoutParams(decButtonParams);
		decButton.setOnClickListener(this);

		editButtonParams = new RelativeLayout.LayoutParams(
				(int) (BUTTON_WIDTH_DPS * scale + 0.5f),
				LayoutParams.WRAP_CONTENT);
		editButtonParams.addRule(RelativeLayout.BELOW, label.getId());
		editButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		editButton.setImageResource(R.drawable.ic_pencil);
		editButton.setLayoutParams(editButtonParams);

		addView(label);
		addView(count);
		addView(editButton);
		addView(decButton);
		addView(incButton);
	}

	/**
	 * Retrieves the Counter associated with this CounterListing.
	 * 
	 * @return this listing's counter.
	 */
	public Counter getCounter() {
		return mCounter;
	}

	public void setEditButtonListener(OnClickListener listener) {
		editButton.setOnClickListener(listener);
	}

	/**
	 * Sets the displayed Counter to the given counter, updating the display
	 * fields. This method must be called in order for the CounterListing to
	 * display any meaningful data.
	 * 
	 * @param c
	 *            the new Counter
	 */
	public void setCounter(Counter c) {
		mCounter = c;
		label.setText(c.getName());
		updateText();
	}

	public void onClick(View source) {
		switch (source.getId()) {
		case DEC_BUTTON_ID:
			mCounter.decrement();
			updateText();
			break;
		case INC_BUTTON_ID:
			mCounter.increment();
			updateText();
			break;
		}
	}

	public void updateText() {
		count.setText(String.format("%d/%d", mCounter.getCur(),
				mCounter.getMax()));
	}
}
