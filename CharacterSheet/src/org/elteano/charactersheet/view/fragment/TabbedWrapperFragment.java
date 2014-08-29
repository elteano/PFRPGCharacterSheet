package org.elteano.charactersheet.view.fragment;

import org.elteano.charactersheet.R;
import org.elteano.charactersheet.view.google.SlidingTabLayout;
import org.elteano.charactersheet.view.support.TabletPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabbedWrapperFragment extends Fragment {

	private ViewPager mPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_tabbed_wrapper,
				container, false);
		return ret;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mPager = (ViewPager) view.findViewById(R.id.pager);
		mPager.setAdapter(new TabletPagerAdapter(getActivity()
				.getSupportFragmentManager(), getActivity()));

		SlidingTabLayout l = (SlidingTabLayout) view
				.findViewById(R.id.fragment_tabbed_wrapper_slide_tab);
		l.setViewPager(mPager);
		super.onViewCreated(view, savedInstanceState);
	}
}
