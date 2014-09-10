package com.waldenme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AwayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_away);
		ListView lvw = (ListView) findViewById(R.id.lista_s);
		String[] datos = new String[] { getString(R.string.dummy_day1),
				getString(R.string.dummy_day2), getString(R.string.dummy_day3),
				getString(R.string.dummy_day4), getString(R.string.dummy_day5),
				getString(R.string.dummy_day6), getString(R.string.dummy_day7), };

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(datos));
		setObj adapter = new setObj(this, 0, 0, list);
		lvw.setAdapter(adapter);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AwayActivity.this, DaysActivity.class);
				startActivity(intent);
			}
		});
	}

	public class setObj extends ArrayAdapter<String> {
		Context ctx;
		List<String> datos;
		TextView txtView, txtView2;
		View rootV;

		public setObj(Context context, int resource, int textViewResourceId,
				List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			datos = objects;
			ctx = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(ctx);
			rootV = inflater.inflate(R.layout.simple_away_activity_list_item,
					null);
			txtView = (TextView) rootV.findViewById(android.R.id.text1);
			txtView2 = (TextView) rootV.findViewById(android.R.id.text2);
			txtView.setText(datos.get(position).split(",")[0]);
			txtView2.setText(datos.get(position).split(",")[1]);
			return rootV;
		}
	}
}
