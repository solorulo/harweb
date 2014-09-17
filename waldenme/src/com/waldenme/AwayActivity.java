package com.waldenme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class AwayActivity extends Activity {
	
	RadioButton[] chss = new RadioButton[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_away);
		ListView lvw = (ListView) findViewById(R.id.lista_s);

		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AwayActivity.this, DaysActivity.class);
				startActivity(intent);
			}
		});
		
		TextView titulo= (TextView) findViewById(R.id.away_activity_txt_Fecha);
		titulo.setText(getDate());
		
		setTitle("Reservar");

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getDays()));
		setObj adapter = new setObj(this, 0,0, list);
//		lvw.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvw.setAdapter(adapter);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(ctx);
			rootV = inflater.inflate(R.layout.simple_away_activity_list_item, null);
			txtView = (TextView) rootV.findViewById(android.R.id.text2);
			txtView2 = (TextView) rootV.findViewById(android.R.id.text1);
			
			chss[position] = (RadioButton) rootV.findViewById(R.id.radioButton1_saaw);
			chss[position].setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					for (int x=0;x<10;x++){
						if (chss[x]!=null){
							if (position!=x)
								chss[x].setChecked(false);
						}
					}
				}
			});
			txtView.setText(datos.get(position).split(",")[0]);
			txtView2.setText(datos.get(position).split(",")[1]);
			return rootV;
		}
	}
	
	public String getDate() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String[] meses = { 
				getString(R.string.dummy_month1), getString(R.string.dummy_month2),
				getString(R.string.dummy_month3), getString(R.string.dummy_month4),
				getString(R.string.dummy_month5), getString(R.string.dummy_month6),
				getString(R.string.dummy_month7), getString(R.string.dummy_month8),
				getString(R.string.dummy_month9), getString(R.string.dummy_month10),
				getString(R.string.dummy_month11), getString(R.string.dummy_month12), };
		return meses[month]+" "+year;
	}
	
	public String[] getDays() {
		String[] days = new String[] { getString(R.string.dummy_day1),
				getString(R.string.dummy_day2), getString(R.string.dummy_day3),
				getString(R.string.dummy_day4), getString(R.string.dummy_day5),
				getString(R.string.dummy_day6), getString(R.string.dummy_day7), };
		String[] rDays = new String[8];
		Calendar calendar = Calendar.getInstance();
		int aDayM= calendar.get(Calendar.DAY_OF_MONTH);
		int aDayW;
		for (int x=0;x<8;x++){
			aDayW=calendar.get(Calendar.DAY_OF_WEEK)+x;
			if (aDayW>6)
				aDayW=aDayW-7;
			rDays[x]=days[aDayW]+","+String.valueOf(aDayM+x);
		}
		return rDays;
	}
}
