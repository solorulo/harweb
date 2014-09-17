package com.waldenme;

import java.util.HashMap;

import com.waldenme.utilities.Comunicator;
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
	
	static String ip= Comunicator.ip;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	final Dialog dialog = new Dialog(StartActivity.this);
    	dialog.setContentView(R.layout.cambia_ip_layout);
		dialog.setTitle("IP");
		
		Button btn_ok_cambiar= (Button) dialog.findViewById(R.id.cambia_ip_btn);
		final TextView txt_cambia_ip= (TextView) dialog.findViewById(R.id.cambia_ip_txt);
		
		btn_ok_cambiar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ip=txt_cambia_ip.getText().toString();
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
//        int id = item.getItemId();
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
            View rootView = inflater.inflate(R.layout.fragment_start, container, false);
            splashLayout = rootView.findViewById(R.id.splash_layout);
            loginLayout = rootView.findViewById(R.id.login_layout);
            loginProgressLayout = rootView.findViewById(R.id.login_status);

            ProgressBar progress = (ProgressBar) loginProgressLayout.findViewById(R.id.progress);
            progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
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
        	}
        	else {
        		showLogin.run();
        	}
        	loginLayout.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loginLayout.postDelayed(goToHome, 1000);
					new Thread(){
						public void run(){
							showProgress.run();
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("name", "Marshal");
							params.put("pass", "1234");
							
							EditText etn=(EditText) loginLayout.findViewById(R.id.login_layout_user_txt);
							EditText etp=(EditText) loginLayout.findViewById(R.id.login_layout_pass_txt);
							
							String name= etn.getText().toString();
							String pass= etp.getText().toString();
							
							if ((name.trim().length()<1)||(pass.trim().length()<1)){
								name="mr_marshal";
								pass="1234";
							}
							
//							new Comunicator().get(ip+"gac:"+name+"_@_"+pass, null, new ResponseListener() {
//								
//								@Override
//								public void onResponseSuccess(String valueMessage) {
//									Log.i("Respuesta",valueMessage);
//									if (valueMessage.trim().equals("ok")){
//										loginLayout.postDelayed(goToHome, 1000);
//										Toast.makeText(getActivity().getApplicationContext(), "Bienvenido", Toast.LENGTH_LONG).show();
//									}else{
//										showLogin.run();
//										Toast.makeText(getActivity().getApplicationContext(), "Ha habido un error", Toast.LENGTH_LONG).show();
//									}
//								}
//								
//								@Override
//								public void onResponseError(String errorMessage) {
//									Toast.makeText(getActivity().getApplicationContext(), "Error de Red", Toast.LENGTH_LONG).show();
//									showLogin.run();
//								}
//								
//								@Override
//								public void onResponseEnd() {
//								}
//							});
						}
					}.run();
				}
			});;
        }
        
        @Override
        public void onSaveInstanceState(Bundle outState) {
        	outState.putBoolean("onLogin", true);
        	super.onSaveInstanceState(outState);
        }
    }

}
