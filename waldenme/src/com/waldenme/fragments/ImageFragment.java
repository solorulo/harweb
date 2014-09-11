package com.waldenme.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.waldenme.MainActivity;
import com.waldenme.R;
import com.waldenme.utilities.Comunicator;
import com.waldenme.utilities.Comunicator.ResponseListener;

public class ImageFragment extends Fragment {

	static String ip = "http://192.168.20.60:8080/";

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
			mDummyResId = getArguments().getInt(ARG_DUMMY_RSC);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_start, container,
				false);

		rootView = inflater.inflate(R.layout.fragment_image, container, false);
		mImageView = (ImageView) rootView.findViewById(R.id.imageView1);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getQrImage();
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
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		// try {
		// mListener = (OnFragmentInteractionListener) activity;
		// } catch (ClassCastException e) {
		// throw new ClassCastException(activity.toString()
		// + " must implement OnFragmentInteractionListener");
		// }
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

	private void getQrImage() {
		String path = Environment.getExternalStorageDirectory().toString();
		File file = new File(path, "PruebaQR_waldenme.jpg");
		if (file.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(file
					.getAbsolutePath());
			mImageView.setImageBitmap(myBitmap);

		} else {
			new Comunicator().get(ip + "gqr:mr_marshal_@_1234", null,
					new ResponseListener() {

						@Override
						public void onResponseSuccess(String valueMessage) {
							urlImg = valueMessage.trim();// getArguments().getString(ARG_IMG_URL);
							new DownloadImageTask(mImageView).execute(urlImg);
							Log.i("Respuesta: ", valueMessage);
						}

						@Override
						public void onResponseError(String errorMessage) {
							Log.e("Error", "HTTP");
						}

						@Override
						public void onResponseEnd() {
							Log.i("Fin", "Peticion");
						}
					});
		}
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
			try {
				saveImage(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void saveImage(Bitmap img) throws IOException {
		String path = Environment.getExternalStorageDirectory().toString();
		OutputStream fOut = null;
		File file = new File(path, "PruebaQR_waldenme.jpg");
		fOut = new FileOutputStream(file);

		img.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		fOut.flush();
		fOut.close();

		MediaStore.Images.Media.insertImage(getActivity()
				.getApplicationContext().getContentResolver(), file
				.getAbsolutePath(), file.getName(), file.getName());
	}

}
