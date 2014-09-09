package com.waldenme.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.waldenme.MainActivity;
import com.waldenme.R;

@SuppressLint("ViewHolder")
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_space, container,
				false);

		String[] datos = new String[] { "Sala de juntas","Área de trabajo",
				"Estudio de diseño","Taller de herramientas",
				"Sala de cómputo","Sala creativa","Sala de juntas","Otra sala de juntas"
		};

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(datos));
		setObj adapter = new setObj(getActivity(), 0, 0, list);
		ListView lv = (ListView) rootView.findViewById(R.id.list);
		lv.setAdapter(adapter);
		return rootView;

	}

	public class setObj extends ArrayAdapter<String> {
		Context ctx;	List<String> datos;	TextView txtView;	View rootV;
		public setObj(Context context, int resource, int textViewResourceId,
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
