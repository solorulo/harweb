package com.waldenme;

import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.waldenme.utilities.Comunicator;
import com.waldenme.utilities.Comunicator.ResponseListener;
import com.waldenme.utilities.Preferences;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DaysActivity extends Activity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	String id_space, day, month, year;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_days);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		id_space = getIntent().getStringExtra("id_space");
		day = getIntent().getStringExtra("day");
		month = getIntent().getStringExtra("month");
		year = getIntent().getStringExtra("year");

//		Toast.makeText(
//				getApplicationContext(),
//				"Id: " + id_space + "\nDay: " + day + "\nMonth: " + month
//						+ "\nYear: " + year, Toast.LENGTH_SHORT).show();
		findViewById(R.id.activity_days_btn_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("r", "apartar");
						params.put("user_id",Preferences.get("user_id"));
						params.put("space_id", id_space);
						params.put("hours", "3");
						params.put("date",year+"-"+month+"-"+day);
						
						new Comunicator().post(Comunicator.ip, params, new ResponseListener() {
							
							@Override
							public void onResponseSuccess(String valueMessage) {
								Log.i("Respuesta apartado: ",valueMessage);
								if (valueMessage.trim().equals("Ok")){
									Toast.makeText(getApplicationContext(), "Espacio reservado.", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(DaysActivity.this,
											MainActivity.class);
									startActivity(intent);
								}else
									try{ 
										if (new JSONArray(valueMessage).get(0)!=null) {
											JSONArray jsonA = new JSONArray(
													valueMessage);
											if (jsonA.getJSONObject(0).getString("user_id").trim().equals(Preferences.get("user_id"))){
												Toast.makeText(getApplicationContext(), "Ya has reservado este espacio.", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(getApplicationContext(), "Este espacio ya está reservado.", Toast.LENGTH_SHORT).show();
											}
										}
									}catch (Exception e) {
										// TODO: handle exception
									}
							}
							
							@Override
							public void onResponseError(String errorMessage) {
								
							}
							
							@Override
							public void onResponseEnd() {}
						});
					}
				});
		
		findViewById(R.id.activity_days_btn_cancel).setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (DaysActivity.this, AwayActivity.class);
				intent.putExtra("id_space", id_space);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.days, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		String[] days = new String[] { getString(R.string.dummy_day1),
				getString(R.string.dummy_day2), getString(R.string.dummy_day3),
				getString(R.string.dummy_day4), getString(R.string.dummy_day5),
				getString(R.string.dummy_day6), getString(R.string.dummy_day7), };

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return HoursFragment.newInstance();
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return days.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// Locale l = Locale.getDefault();
			return days[position];
			// switch (position) {
			// case 0:
			// return getString(R.string.title_section1).toUpperCase(l);
			// case 1:
			// return getString(R.string.title_section2).toUpperCase(l);
			// case 2:
			// return getString(R.string.title_section3).toUpperCase(l);
			// }
			// return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_days, container,
					false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
