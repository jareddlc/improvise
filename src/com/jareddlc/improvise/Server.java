package com.jareddlc.improvise;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
	public static String uploadPath = "/upload";
	public static String hyphens = "--";
	public static String newLine = "\r\n";
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
	
	public static void postURL() {
		Log.d(LOG_D, "About to spawn async");
		new ServerPostTask().execute("tempURL");
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
	
	private static String convertStream(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
        } 
        catch (IOException e) {
        	Log.d(LOG_D, "Error: "+e);
        } 
        finally {
            try {
            	stream.close();
            }
            catch (IOException e) {
            	Log.d(LOG_D, "Error: "+e);
            }
        }
        return sb.toString();
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
	
	public static String post(File postFile) throws IOException {
		InputStream inStream = null;
		DataOutputStream outStream = null;
		String boundary = "==="+System.currentTimeMillis()+"===";
		
	    try {
	    	// setup connection
	        URL url = new URL(serverURL+":"+serverPort+uploadPath);
	        Log.d(LOG_D, "Url: "+url.toString());
	        HttpURLConnection conx = (HttpURLConnection) url.openConnection();
	        conx.setRequestMethod("POST");
	        conx.setDoInput(true);
	        conx.setDoOutput(true);
	        conx.setRequestProperty("Connection", "Keep-Alive");
	        conx.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
	        
	        // setup output stream
	        outStream = new DataOutputStream(conx.getOutputStream());
	        outStream.writeBytes(hyphens+boundary+newLine);
	        outStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload\"; filename=\""+postFile.getName()+"\""+newLine);
	        outStream.writeBytes("Content-Type: audio/mp4"+newLine);
	        outStream.writeBytes("Content-Transfer-Encoding: binary"+newLine);
	        outStream.writeBytes(newLine);
	        
	        // bytes stuff
	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;
	        int maxBufferSize = 1*1024*1024;

	        FileInputStream fileInStream = new FileInputStream(postFile);
	        bytesAvailable = fileInStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            
            bytesRead = fileInStream.read(buffer, 0, bufferSize);
            while(bytesRead > 0) {
            	outStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInStream.read(buffer, 0, bufferSize);
            }
            outStream.writeBytes(newLine);

            // upload data
            outStream.writeBytes(hyphens+boundary+newLine);
            outStream.writeBytes("Content-Disposition: form-data; name=\"fileUpload\""+newLine);
            outStream.writeBytes("Content-Type: text/plain"+newLine);
            outStream.writeBytes(newLine);
            outStream.writeBytes("androidUpload");
            outStream.writeBytes(newLine);
            
            outStream.writeBytes(hyphens+boundary+hyphens+newLine);
            
            inStream = conx.getInputStream();
            String result = convertStream(inStream);
            
            responseCode = conx.getResponseCode();
	        Log.d(LOG_D, "The response is: "+responseCode);
            
            fileInStream.close();
            inStream.close();
            outStream.flush();
            outStream.close();
            
            return result;
	    }
	    finally {
	        Log.d(LOG_D, "finally");
	    }
	}
	
	public static class ServerPostTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return Server.post(Recorder.getRecordedFile());
            } catch (IOException e) {
            	Log.d(LOG_D, "Error: ", e);
                return "Unable to post to web page. URL may be invalid.";
            }
        }
        @Override
        protected void onPostExecute(String result) {
        	Log.d(LOG_D, "The result is: "+result);
       }
    }

}
