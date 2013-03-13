package org.elteano.charactersheet;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ItemEditActivity extends Activity {

	public static final int RESULT_DELETE = 2;
	public static final int REQUEST_NEW_ITEM = 0;
	public static final int REQUEST_EDIT_ITEM = 1;
	public static final String EXTRA_ITEM = ItemEditActivity.class
			.getCanonicalName() + ".itemExtra";

	private Item mItem;

	public void onClickDoneButton(View source) {
		mItem.setName(((EditText) findViewById(R.id.activity_item_edit_item_name))
				.getText().toString().trim());
		if (mItem.getName().isEmpty()) {
			((EditText) findViewById(R.id.activity_item_edit_item_name))
					.requestFocus();
			return;
		}
		EditText itemQuantityField = (EditText) findViewById(R.id.activity_item_edit_quantity_field);
		if (itemQuantityField.getText().toString().isEmpty())
			mItem.setQuantity(0);
		else
			mItem.setQuantity(Integer.parseInt(itemQuantityField.getText()
					.toString()));
		EditText itemWeightField = (EditText) findViewById(R.id.activity_item_edit_weight_field);
		if (itemWeightField.getText().toString().isEmpty())
			mItem.setPerUnitWeight(0);
		else
			mItem.setPerUnitWeight(Float.parseFloat(itemWeightField.getText()
					.toString()));
		mItem.setDesc(((EditText) findViewById(R.id.activity_item_edit_item_desc))
				.getText().toString());
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ITEM, mItem);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void onClickDeleteButton(View source) {
		setResult(RESULT_DELETE);
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_edit);
		if (getIntent().getExtras() != null)
			mItem = getIntent().getParcelableExtra(EXTRA_ITEM);
		else
			mItem = new Item("", 0, 0, "");
		Log.d("CharacterSheet", "The received item is " + mItem.getName());
		((EditText) findViewById(R.id.activity_item_edit_item_name))
				.setText(mItem.getName());
		((EditText) findViewById(R.id.activity_item_edit_quantity_field))
				.setText("" + mItem.getQuantity());
		((EditText) findViewById(R.id.activity_item_edit_weight_field))
				.setText("" + mItem.getWeight());
		((EditText) findViewById(R.id.activity_item_edit_item_desc))
				.setText(mItem.getDesc());
		setResult(RESULT_CANCELED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pfor_redirect_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.more_info:
			try {
				Intent intent = new Intent(Intent.ACTION_SEARCH);
				intent.setComponent(new ComponentName(
						"org.evilsoft.pathfinder.reference",
						"org.evilsoft.pathfinder.reference.DetailsActivity"));
				String sString = mItem.getName()
						.replaceAll("([wW]and|[sS]croll) [oO]f", "").trim();
				intent.putExtra(SearchManager.QUERY, sString);
				startActivity(intent);
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.pfor_toast),
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
