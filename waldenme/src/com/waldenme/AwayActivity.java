package com.waldenme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AwayActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_away);
		ListView lvw = (ListView) findViewById(R.id.lista_s);
		TextView titulo= (TextView) findViewById(R.id.away_activity_txt_Fecha);
		titulo.setText(getDate());
		
		setTitle("Reservar");

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(getDays()));
		setObj adapter = new setObj(this, 0, 0, list);
		lvw.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

	public static String getDate() {
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo",
				"Junio", "Julio", "Agosto", "Septiembre", "Octubre",
				"Noviembre", "Diciembre" };
		return meses[month]+" "+year;
	}
	public static String[] getDays() {
		String[] days = new String[] { "Sábado", "Domingo","Lunes", "Martes",
				"Miércoles", "Jueves", "Viernes"};
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
