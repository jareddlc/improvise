package com.jareddlc.improvise;

import java.io.File;
import java.util.ArrayList;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Select extends Activity {
	
	private static final String LOG_D = "Improvise:Select";
	
	private static File fileDirectory = null;
	private static File[] files = null;
	private static ListView list_select_tracks;
	private static ArrayList<String> tracks;
	private static ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select);
		
		tracks = new ArrayList<String>();
		list_select_tracks = (ListView)findViewById(R.id.list_select_tracks);
		adapter = new ArrayAdapter<String>(this, R.layout.layout_select, R.id.text_select_list, tracks);
		list_select_tracks.setAdapter(adapter);
		main();
	}
	
	public static void main() {		
		// File directory
		Log.d(LOG_D, "Initializing Select");
		fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Improvise/Songs");
		fileDirectory.mkdirs();
		files = fileDirectory.listFiles();
		for(File file : files) {
			Log.d(LOG_D, "File: "+file.getAbsolutePath());
			tracks.add(file.getName());
	    }
	}
	
	public static File[] getFiles() {
		return files;
	}
}
