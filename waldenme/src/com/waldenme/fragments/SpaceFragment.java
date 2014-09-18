package com.waldenme.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waldenme.AwayActivity;
import com.waldenme.MainActivity;
import com.waldenme.R;
import com.waldenme.utilities.Comunicator;
import com.waldenme.utilities.Comunicator.ResponseListener;

public class SpaceFragment extends Fragment {
	private static final String ARG_SECTION_NUMBER = "section_number";

	private OnFragmentInteractionListener mListener;

	public static SpaceFragment newInstance(int sectionNumber) {
		SpaceFragment fragment = new SpaceFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SpaceFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(Uri uri);
	}
	
	AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent (getActivity(), AwayActivity.class);
			startActivity(intent);
		}
	}; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_space, container,
				false);
		Log.i("Haciendo","Peticion");
		
		final ArrayList<String> lista = new ArrayList<String>();
		final ListView lv = (ListView) rootView.findViewById(R.id.list);
		new Comunicator().get(Comunicator.ip+"espacios", null, new ResponseListener() {
			
			@Override
			public void onResponseSuccess(String valueMessage) {
				Log.i("JSON",valueMessage);
				try {
					JSONArray jsonA = new JSONArray(valueMessage);
					for (int x=0; x<jsonA.length();x++){
						String cosa= jsonA.getJSONObject(x).getString("nombre");
						lista.add(cosa);
					}
					SpaceAdapter adapter = new SpaceAdapter(getActivity(), 0, 0, lista);
					lv.setAdapter(adapter);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onResponseError(String errorMessage) {
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onResponseEnd() {
				
			}
		});
		
		lv.setOnItemClickListener(listListener);
		return rootView;

	}

	public class SpaceAdapter extends ArrayAdapter<String> {
		Context ctx;	List<String> datos;	TextView txtView;	View rootV;
		public SpaceAdapter(Context context, int resource, int textViewResourceId,
				List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			datos = objects;	ctx = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(ctx);
			rootV = inflater.inflate(R.layout.simple_space_fragment_list_item, null);
			txtView = (TextView) rootV.findViewById(android.R.id.text1);
			txtView.setText(datos.get(position));
			return rootV;
		}
	}

}
