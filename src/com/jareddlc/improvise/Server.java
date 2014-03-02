package com.jareddlc.improvise;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class Server {
	private static final String LOG_D = "Improvise:Server";
	public static String serverURL = "http://192.168.1.130";
	public static String serverPort = "3000";
	public static Integer responseCode = null;
	public static Integer timeoutRead = 10000;
	public static Integer timeoutConx = 15000;
	
	public static void getURL() {
		/*ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conxMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            new ServerGetTask().execute("tempURL");
        }
        if(networkInfo != null && networkInfo.isConnected()) {
            new ServerGetTask().execute("tempURL");
        }
        else {
        	Log.d(LOG_D, "No network connection available");
        }*/
		Log.d(LOG_D, "About to spawn async");
		new ServerGetTask().execute("tempURL");
	}
	
	public static String get(String getURL) throws IOException {
	    InputStream inStream = null;
	    // length of retrieved content
	    int length = 500;
	        
	    try {
	    	// setup connection
	    	getURL = serverURL;
	        URL url = new URL(serverURL+":"+serverPort);
	        Log.d(LOG_D, "Url: "+url.toString());
	        HttpURLConnection conx = (HttpURLConnection) url.openConnection();
	        conx.setReadTimeout(timeoutRead);
	        conx.setConnectTimeout(timeoutConx);
	        conx.setRequestMethod("GET");
	        conx.setDoInput(true);
	        // start connection
	        conx.connect();
	        responseCode = conx.getResponseCode();
	        Log.d(LOG_D, "The response is: "+responseCode);
	        inStream = conx.getInputStream();

	        // Convert the InputStream into a string
	        String response = parseStream(inStream, length);
	        return response;
	    }
	    finally {
	        if (inStream != null) {
	        	inStream.close();
	        } 
	    }
	}
	
	public static String parseStream(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
	    reader = new InputStreamReader(stream, "UTF-8");        
	    char[] buffer = new char[len];
	    reader.read(buffer);
	    return new String(buffer);
	}
	
	public static class ServerGetTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return Server.get(urls[0]);
            } catch (IOException e) {
            	Log.d(LOG_D, "Error: ", e);
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
        	Log.d(LOG_D, "The result is: "+result);
       }
    }

}
