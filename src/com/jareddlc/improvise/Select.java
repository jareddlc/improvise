package com.jareddlc.improvise;

import java.io.File;
import java.util.ArrayList;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Select extends Activity {
	
	private static final String LOG_D = "Improvise:Select";
	
	private static File fileDirectory = null;
	private static File[] files = null;
	private static ListView list_select_tracks;
	private static ArrayList<String> tracks;
	private static ArrayAdapter<String> adapter;
	private static MediaMetadataRetriever meta = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select);
		
		// Layout
		list_select_tracks = (ListView)findViewById(R.id.list_select_tracks);

		// List view
		tracks = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this, R.layout.layout_list, R.id.text_select_list, tracks);
		list_select_tracks.setAdapter(adapter);

		// List view listener
		list_select_tracks.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(LOG_D, "Position: "+Integer.toString(position));
				Log.d(LOG_D, "ID: "+Long.toString(id));
				Log.d(LOG_D, "Track: "+tracks.get(position));
			}
		});
		main();
	}
	
	public static void main() {		
		// File directory
		Log.d(LOG_D, "Initializing Select");
		fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Improvise/Songs");
		fileDirectory.mkdirs();
		files = fileDirectory.listFiles();

		// Iterate over all tracks
		meta = new MediaMetadataRetriever();
		for(File file : files) {
			Log.d(LOG_D, "File: "+file.getAbsolutePath());
			// Grab track data and add it to array of tracks
			meta.setDataSource(file.getAbsolutePath());
			String artist = meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			String title = meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			String songMeta = artist+" - "+title;
			tracks.add(songMeta);
	    }
	}
	
	public static File[] getFiles() {
		return files;
	}
}
