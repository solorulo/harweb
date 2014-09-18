package com.waldenme.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.waldenme.MainActivity;
import com.waldenme.R;
import com.waldenme.utilities.Preferences;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link ProfileFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class ProfileFragment extends Fragment {
	
	ImageView mImageView;
	String urlImg;
	
	/**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ProfielFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ProfileFragment newInstance(int sectionNumber) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

	public ProfileFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		((TextView) rootView.findViewById(R.id.profile_activity_txt_nombre)).setText(Preferences.get("name")+" "+Preferences.get("lastNP")+" "+Preferences.get("lastNM"));
		((TextView) rootView.findViewById(R.id.profile_activity_txt_fechaAlta)).setText("Fecha de alta: "+Preferences.get("date_added").substring(0, 10));
		((TextView) rootView.findViewById(R.id.profile_activity_txt_credito)).setText("$ "+Preferences.get("credit")+" M.N.");
		mImageView = (ImageView) rootView.findViewById(R.id.profile_fragment_img_profilePicture);
		getProfileImage();
		return rootView;
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
	
	private void getProfileImage() {
		String path = Environment.getExternalStorageDirectory().toString();
		File file = new File(path, "WaldenMe/"+Preferences.get("user")+"/Face.jpg");
		if (file.exists()) {

			Bitmap myBitmap = BitmapFactory.decodeFile(file
					.getAbsolutePath());
			mImageView.setImageBitmap(myBitmap);

		} else {
//			urlImg ="http://www.laredso.com/wp-content/uploads/Brad-Pitt-1.jpg";
			urlImg= Preferences.get("photo").trim().replace(' ', '_');// getArguments().getString(ARG_IMG_URL);
			new DownloadImageTask(mImageView).execute(urlImg);
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
		File file = new File(path, "WaldenMe/"+Preferences.get("user")+"/Face.jpg");
		fOut = new FileOutputStream(file);

		img.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
		fOut.flush();
		fOut.close();

		MediaStore.Images.Media.insertImage(getActivity()
				.getApplicationContext().getContentResolver(), file
				.getAbsolutePath(), file.getName(), file.getName());
	}

}
