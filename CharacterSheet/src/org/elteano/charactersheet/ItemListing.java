package org.elteano.charactersheet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemListing extends TableRow {

	private TextView itemNameView;
	private Button quantityButton;

	public ItemListing(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClickable(true);
		setBackgroundResource(android.R.drawable.list_selector_background);
		itemNameView = new TextView(context);
		itemNameView.setTextSize(18);
		addView(itemNameView);
		quantityButton = new Button(context);
		quantityButton.setClickable(false);
		quantityButton.setTextSize(18);
		addView(quantityButton);
		quantityButton.setWidth(1);
		quantityButton.setHeight(1);
	}

	public String getItemName() {
		return itemNameView.getText().toString();
	}

	public void setToItem(Item item) {
		itemNameView.setText(item.getName());
		quantityButton.setText("" + item.getQuantity());
	}
}
