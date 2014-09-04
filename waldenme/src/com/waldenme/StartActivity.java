package com.waldenme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class StartActivity extends Activity {

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
        
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.start, menu);
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
					showProgress.run();
					loginLayout.postDelayed(goToHome, 2000);
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
