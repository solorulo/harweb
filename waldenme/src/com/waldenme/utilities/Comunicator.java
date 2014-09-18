package com.waldenme.utilities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.waldenme.BuildConfig;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Comunicator {

	public static interface ResponseListener {
		public void onResponseError(String errorMessage);
		public void onResponseSuccess(String valueMessage);
		public void onResponseEnd();
	}
	private static HttpClient client;

	public static String ip = "http://marshal.domusmx.com/walden/app/android/";
	
	private static final String LOG_TAG = "comunicador";
	/**
	 * Sirve para construir las urls
	 * @param url_base
	 * @param url_params
	 * @return
	 */
	public static String build_url(String url_base, String ...url_params) {
		StringBuilder sb = new StringBuilder(url_base);
		for (String s : url_params) {
			sb.append(s);
			sb.append('/');
		}
		String res =sb.substring(0, sb.length()-1);
		msgDebug("Build_url"+ res);
		return res;
	}
	///////MD5
	/*
	 * Crea una cadena md5 a partir de una cadena original
	 */
	public static String md5String( String s){
		try {
			s = new String(s.getBytes("ascii"),"ascii");
	        // Create MD5 Hash
	        MessageDigest digest = MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return null;
	}
	public Comunicator() {
		if (client == null)  {
			client = new DefaultHttpClient();
		}
//		HttpParams httpparams = client.getParams();
        //httpparams.setParameter(CoreProtocolPNames.USER_AGENT, "android");
	} 
	//////////////////////////////Peticiones
	
	/*
	 * Petición GET con cabeceras y parametros
	 */
	protected final synchronized String executeHttpGet (
			String URL, 
			HashMap<String, String> headers,
			HashMap<String, String> params) 
			throws IOException {
        StringBuffer sbURL = new StringBuffer(URL);
        if(params != null){
        	Set<Entry<String, String>> entrySet = params.entrySet();
            if (entrySet.size() > 0) {
            	sbURL.append('?');
    	        for (Entry<String, String> key : entrySet) {
    	        	sbURL.append(key.getKey());
    	        	sbURL.append('=');
    	        	sbURL.append(URLEncoder.encode(key.getValue(), "UTF-8"));
    	        	sbURL.append('&');
    	        	//URL += key.getKey()+"="+key.getValue()+"&";
    	        }
    	        URL = sbURL.substring(0, sbURL.length()-1);
            }
        }
        
        msgDebug("GET "+URL);
        StringBuffer sb = new StringBuffer();
        HttpGet request = new HttpGet(URL);
        //request.setHeader("Content-Type", "text/plain; charset=utf-8");
        if (headers != null){
		    Set<Entry<String, String>> entrySet = headers.entrySet();
		    for (Entry<String, String> key : entrySet) {
	        	request.setHeader(key.getKey(), key.getValue());
//	        	msgDebug("header: "+key.getKey()+" : "+key.getValue());
	        }
	    }
        HttpResponse response = client.execute(request);
        BufferedReader in = 
        		new BufferedReader(
        				new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append(NL);
        }
        in.close();
        return sb.toString();
	}
	
	private List<NameValuePair> mapToList(HashMap<String, String> params) {
		if (params == null) {
			return null;
		}
		List<NameValuePair> paramsList = 
				new ArrayList<NameValuePair>(params.size());
		for(String key: params.keySet()) {
			paramsList.add(new BasicNameValuePair(key, params.get(key)));
		}
		return paramsList;
	}
	
	protected final synchronized String executeHttpPost(
			String URL, 
			HashMap<String, String> headers,
			HashMap<String, String> params) 
			throws ClientProtocolException, IOException {
		return executeHttpPost(URL, headers, mapToList(params));
	}
	
	/*
	 * Petición POST con cabeceras y parametros
	 */
	protected final synchronized String executeHttpPost(
			String URL, 
			HashMap<String, String> headers,
			List<NameValuePair> params) 
			throws ClientProtocolException, IOException {
	    // Create a new HttpClient and Post Header
	    HttpPost httppost = new HttpPost(URL);
	    if (headers != null) {
		    Set<Entry<String, String>> entrySet = headers.entrySet();
		    for (Entry<String, String> key : entrySet) {
	        	httppost.addHeader(key.getKey(), key.getValue());
	        	msgDebug(key.getKey()+ " : " +key.getValue());
	        }
	    }
	    if (params != null) {
	    	httppost.setEntity(
	    			new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    }
        // Execute HTTP Post Request
        HttpResponse response = client.execute(httppost);
        msgDebug("status code: "+response.getStatusLine().getStatusCode());

        BufferedReader in = 
        		new BufferedReader(
        				new InputStreamReader(response.getEntity().getContent()));
        String line = "";

        String NL = System.getProperty("line.separator");

	    StringBuffer sb = new StringBuffer();
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append(NL);
        }
        in.close();
	    return sb.toString();
	}
	
	public final AsyncTask<Void, Void, String> get(final String URL, 
			final HashMap<String, String> params, 
			final ResponseListener response){
		return get(URL, null, params, response);
	}
	
	public final AsyncTask<Void, Void, String> get(final String URL, 
			final HashMap<String, String> headers,
			final HashMap<String, String> params, 
			final ResponseListener response){
		
		return new AsyncTask<Void, Void, String>() {
			
			Exception error=null;
			
			@Override
			protected String doInBackground(Void... p) {
				String result=null;
				try {
					result = executeHttpGet(URL, headers, params);
				} catch (ClientProtocolException e) {
					error = e;
					e.printStackTrace();
				} catch (IOException e) {
					error = e;
					e.printStackTrace();
				} 
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
//				msgDebug("response: "+result);
				if (response != null && !isCancelled()) {
					if (error == null){
						response.onResponseSuccess(result);
					}
					else {
						response.onResponseError(error.getMessage());
					}
					response.onResponseEnd();
				}
			}
			
		}.execute((Void) null);
	}
	
	public final AsyncTask<Void, Void, String> post(final String URL, 
			final HashMap<String, String> params, 
			final ResponseListener response){
		return post(URL, 
				null,
				params, 
				response);
	}
	
	public final AsyncTask<Void, Void, String> post(final String URL, 
			final HashMap<String, String> headers,
			final HashMap<String, String> params, 
			final ResponseListener response){	
		return new AsyncTask<Void, Void, String>() {
			Exception error = null;
			@Override
			protected String doInBackground(Void... p) {
				String result = null;
				try {
					result = executeHttpPost(URL, headers, params);
				} catch (ClientProtocolException e) {
					error = e;
					e.printStackTrace();
				} catch (IOException e) {
					error = e;
					e.printStackTrace();
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				if (response != null && !isCancelled()) {
					if (error == null){
						response.onResponseSuccess(result);
					}
					else {
						response.onResponseError(error.toString());
					}
					response.onResponseEnd();
				}
//				msgDebug("response: "+result);
			}
			
		}.execute((Void) null);
	}
	
	public static final void showMessage(Context c, int id) throws NotFoundException {
		String er_message;
		er_message = c.getResources().getString(id);
		Toast.makeText(c, er_message, Toast.LENGTH_LONG).show();
	}
	
	public static void msgDebug(String msg) {
		printLog(msg, DEBUG_D);
	}
	
	public final static int DEBUG_D = 0x10;
	public final static int DEBUG_E = 0x11;
	public final static int DEBUG_I = 0x12;
	public final static int DEBUG_V = 0x13;
	public final static int DEBUG_W = 0x14;
	public final static int DEBUG_WTF = 0x15;
	
	public static void printLog(String msg, int tipo) {
		if (BuildConfig.DEBUG) {
			switch (tipo) {
			case DEBUG_D:
				Log.d(LOG_TAG, msg);
				break;
			case DEBUG_E:
				Log.e(LOG_TAG, msg);
				break;
			case DEBUG_I:
				Log.i(LOG_TAG, msg);
				break;
			case DEBUG_V:
				Log.v(LOG_TAG, msg);
				break;
			case DEBUG_W:
				Log.w(LOG_TAG, msg);
				break;
			case DEBUG_WTF:
				Log.wtf(LOG_TAG, msg);
				break;
			default:
				Log.d(LOG_TAG, msg);
				break;
			}
		}
	}	
}
