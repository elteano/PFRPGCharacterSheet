package org.elteano.charactersheet.view.fragment;

import java.util.ArrayList;
import java.util.List;

import org.elteano.charactersheet.model.WeapShield;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class OffHandSpinnerAdapter extends ArrayAdapter<WeapShield> {

	private List<WeapShield> mBaseList;
	private Spinner mMainSpinner;
	private int mResource;

	public OffHandSpinnerAdapter(Context context, int resource,
			List<WeapShield> objects, Spinner mainSpinner) {
		super(context, resource);
		mResource = resource;
		mMainSpinner = mainSpinner;
		mBaseList = objects;
		super.setNotifyOnChange(false);
		super.addAll(getCulledList());
	}

	/**
	 * Creates a placeholder non-weapon.
	 *
	 * @return
	 */
	private WeapShield createPlaceholderWeapon() {
		WeapShield ret = new WeapShield("NONE");
		ret.setWeaponType(WeapShield.TYPE_NONE);
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCount() {
		if (isMainTwoHanded()) {
			return 1;
		}
		return super.getCount() + 1;
	}

	private ArrayList<WeapShield> getCulledList() {
		ArrayList<WeapShield> ret = new ArrayList<WeapShield>();
		for (WeapShield ws : mBaseList) {
			if (ws.getWeaponType() != WeapShield.TYPE_TWO_HANDED)
				ret.add(ws);
		}
		return ret;
	}

	@Override
	public WeapShield getItem(int position) {
		if (isMainTwoHanded()) {
			return createPlaceholderWeapon();
		}
		if (position >= super.getCount()) {
			return createPlaceholderWeapon();
		}
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		if (isMainTwoHanded() || position == super.getCount()) {
			return 0;
		}
		return super.getItemId(position);
	}

	private WeapShield getMainSelected() {
		return (WeapShield) mMainSpinner.getSelectedItem();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isMainTwoHanded() || position == super.getCount()) {
			View view;
			if (convertView == null) {
				view = ((LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE)).inflate(mResource,
						parent, false);
			} else {
				view = convertView;
			}
			((TextView) view).setText("NONE");
			return view;
		}
		return super.getView(position, convertView, parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		// We're never empty, because we always have NONE
		return false;
	}

	private boolean isMainTwoHanded() {
		return getMainSelected() != null
				&& getMainSelected().getWeaponType() == WeapShield.TYPE_TWO_HANDED;
	}

	@Override
	public void notifyDataSetChanged() {
		super.setNotifyOnChange(false);
		super.clear();
		super.addAll(getCulledList());
		super.notifyDataSetChanged();
	}
}
