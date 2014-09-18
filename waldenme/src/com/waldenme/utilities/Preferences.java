package com.waldenme.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

public class Preferences {
	static String config_path = "WaldenMe/config.txt";

	public Preferences() {
		File tarjeta = Environment.getExternalStorageDirectory();
		File file = new File(tarjeta.getAbsolutePath() + "/WaldenMe");
		if (!file.exists()) {
			file.mkdir();
		}
	}

	private static void guardar(String nomarchivo, String texto) {
		try {
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file));
			osw.write(texto);
			osw.flush();
			osw.close();
		} catch (IOException ioe) {
		}
	}

	private static String leer(String nomarchivo) {
		File tarjeta = Environment.getExternalStorageDirectory();
		File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
		String todo = "";
		try {
			FileInputStream fIn = new FileInputStream(file);
			InputStreamReader archivo = new InputStreamReader(fIn);
			BufferedReader br = new BufferedReader(archivo);
			String linea = br.readLine();
			while (linea != null) {
				todo = todo + linea + '\n';
				linea = br.readLine();
			}
			br.close();
			archivo.close();
			return todo;

		} catch (IOException e) {
		}
		return todo;
	}
	
	public static void set(String campo, String dato){
		try {
			String leido= leer(config_path);
			JSONObject json;
			if (leido=="")
				json = new JSONObject();
			else
				json = new JSONObject(leido);
			json.put(campo, dato);
			guardar(config_path, json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String get(String campo) {
		try {
			JSONObject json = new JSONObject(leer(config_path));
			return json.get(campo).toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
