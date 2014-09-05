package com.waldenme.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waldenme.MainActivity;
import com.waldenme.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ImageFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class ImageFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_IMG_URL = "param1";
	private static final String ARG_DUMMY_RSC = "param2";
	private static final String ARG_SECTION_NUMBER = "section_number";

	// TODO: Rename and change types of parameters
	private String urlImg;
	private Integer mDummyResId;
	
	ImageView mImageView;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ImageFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ImageFragment newInstance(int sectionNumber, String urImg) {
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_IMG_URL, urImg);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ImageFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ImageFragment newInstance(int sectionNumber, int urImg) {
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_DUMMY_RSC, urImg);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ImageFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			urlImg = getArguments().getString(ARG_IMG_URL);
			mDummyResId = getArguments().getInt(ARG_DUMMY_RSC);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_image, container, false);
		mImageView = (ImageView) rootView.findViewById(R.id.imageView1);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (urlImg != null) {
			// TODO Cargar imagen desde web
		}
		else {
			mImageView.setImageResource(mDummyResId);
		}
		super.onActivityCreated(savedInstanceState);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
//		try {
//			mListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
