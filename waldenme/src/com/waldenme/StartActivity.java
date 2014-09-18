package com.waldenme;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import com.waldenme.utilities.Comunicator;
import com.waldenme.utilities.Preferences;
import com.waldenme.utilities.Comunicator.ResponseListener;

import android.graphics.PorterDuff;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

	static String ip = Comunicator.ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final Dialog dialog = new Dialog(StartActivity.this);
		dialog.setContentView(R.layout.cambia_ip_layout);
		dialog.setTitle("IP");

		Button btn_ok_cambiar = (Button) dialog
				.findViewById(R.id.cambia_ip_btn);
		final TextView txt_cambia_ip = (TextView) dialog
				.findViewById(R.id.cambia_ip_txt);

		btn_ok_cambiar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ip = txt_cambia_ip.getText().toString();
				dialog.dismiss();
			}
		});

		dialog.show();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		Activity mActivity;
		View loginLayout;
		View loginProgressLayout;
		View splashLayout;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_start,
					container, false);
			splashLayout = rootView.findViewById(R.id.splash_layout);
			loginLayout = rootView.findViewById(R.id.login_layout);
			loginProgressLayout = rootView.findViewById(R.id.login_status);

			ProgressBar progress = (ProgressBar) loginProgressLayout
					.findViewById(R.id.progress);
			progress.getIndeterminateDrawable().setColorFilter(
					getResources().getColor(R.color.blue),
					PorterDuff.Mode.SRC_ATOP);
			return rootView;
		}

		Runnable showLogin = new Runnable() {
			public void run() {
				splashLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
				loginProgressLayout.setVisibility(View.GONE);
			}
		};

		Runnable showProgress = new Runnable() {
			public void run() {
				splashLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.GONE);
				loginProgressLayout.setVisibility(View.VISIBLE);
			}
		};

		Runnable goToHome = new Runnable() {
			public void run() {
				Intent intent = new Intent();
				intent.setClass(mActivity, MainActivity.class);
				startActivity(intent);
			}
		};

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			mActivity = getActivity();

			if (savedInstanceState == null) {
				splashLayout.postDelayed(showLogin, 2500);
			} else {
				showLogin.run();
			}
			loginLayout.findViewById(R.id.button1).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							new Thread() {
								public void run() {
									showProgress.run();
									HashMap<String, String> params = new HashMap<String, String>();

									EditText etn = (EditText) loginLayout
											.findViewById(R.id.login_layout_user_txt);
									EditText etp = (EditText) loginLayout
											.findViewById(R.id.login_layout_pass_txt);

									String name = etn.getText().toString();
									String pass = etp.getText().toString();

									if ((name.trim().length() < 1)
											|| (pass.trim().length() < 1)) {
										name = "landero";
										pass = "landero123";
									}

									params.put("r", "acceder");
									params.put("user", name);
									params.put("pass", pass);

									new Comunicator().post(Comunicator.ip,
											params, new ResponseListener() {

												@Override
												public void onResponseSuccess(
														String valueMessage) {
													Log.i("Respuesta",
															valueMessage);
													final HashMap<String, String> lista = new HashMap<String, String>();
													if (valueMessage.trim()
															.equals("er")) {
														showLogin.run();
														Toast.makeText(
																getActivity()
																		.getApplicationContext(),
																"Error de autentificación.",
																Toast.LENGTH_LONG)
																.show();
													} else if (valueMessage
															.trim()
															.equals("no")) {
														showLogin.run();
														Toast.makeText(
																getActivity()
																		.getApplicationContext(),
																"Datos incorrectos.",
																Toast.LENGTH_LONG)
																.show();
													} else
														try {
															if (new JSONArray(valueMessage).get(0)!=null) {
																	JSONArray jsonA = new JSONArray(
																			valueMessage);
																	Log.i("JSON",
																			"User: "
																					+ jsonA.getJSONObject(
																							0)
																							.getString(
																									"usuario"));
																	new Preferences();
																	lista.put(
																			"user",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"usuario"));
																	lista.put(
																			"pass",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"password"));
																	lista.put(
																			"user_id",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"idUsuario"));
																	lista.put(
																			"credit",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"credito"));
																	lista.put(
																			"name",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"nombre"));
																	lista.put(
																			"qr",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"qr"));
																	lista.put(
																			"mail",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"email"));
																	lista.put(
																			"lastNP",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"apellidoPaterno"));
																	lista.put(
																			"lastNM",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"apellidoMaterno"));
																	lista.put(
																			"photo",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"foto"));
																	lista.put(
																			"user_type",
																			jsonA.getJSONObject(
																					0)
																					.getString(
																							"tipoUsuario_idtipoUsuario"));
																	Preferences
																			.set("user",
																					lista.get("user"));
																	Preferences
																			.set("user_id",
																					lista.get("user_id"));
																	Preferences
																			.set("credit",
																					lista.get("credit"));
																	Preferences
																			.set("name",
																					lista.get("name"));
																	Preferences.set(
																			"qr",
																			lista.get("qr"));
																	Preferences
																			.set("mail",
																					lista.get("mail"));
																	Preferences
																			.set("lastNP",
																					lista.get("lastNP"));
																	Preferences
																			.set("lastNM",
																					lista.get("lastNM"));
																	Preferences
																			.set("photo",
																					lista.get("photo"));
																	Preferences
																			.set("user_type",
																					lista.get("user_type"));

																loginLayout
																		.postDelayed(
																				goToHome,
																				1000);
																Toast.makeText(
																		getActivity()
																				.getApplicationContext(),
																		"Bienvenido "
																				+ Preferences
																						.get("name")
																				+ ".",
																		Toast.LENGTH_LONG)
																		.show();
															}else{
																showLogin.run();
																Toast.makeText(
																		getActivity()
																				.getApplicationContext(),
																		"Ha habido algún error ¡ :o !. Lo sentimos.",
																		Toast.LENGTH_LONG)
																		.show();
															}
														} catch (JSONException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
												}

												@Override
												public void onResponseError(
														String errorMessage) {
													Toast.makeText(
															getActivity()
																	.getApplicationContext(),
															"Error de Red",
															Toast.LENGTH_LONG)
															.show();
													showLogin.run();
												}

												@Override
												public void onResponseEnd() {
												}
											});
								}
							}.run();
						}
					});
			;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			outState.putBoolean("onLogin", true);
			super.onSaveInstanceState(outState);
		}
	}

}
