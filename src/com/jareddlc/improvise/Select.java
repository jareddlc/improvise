package com.jareddlc.improvise;

import java.io.File;
import java.util.ArrayList;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Select extends Activity {
	
	private static final String LOG_D = "Improvise:Select";
	
	private static File basePath = null;
	private static File trackPath = null;
	private static File[] trackFiles = null;
	private static File recordingPath = null;
	private static ListView list_select_tracks;
	private static ArrayList<String> trackList;
	private static ArrayAdapter<String> tracksAdapter;
	private static MediaMetadataRetriever trackMeta = null;
	private static String selectedTrack = null;
	private static File selectedTrackFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_select);
		
		// Layout
		list_select_tracks = (ListView)findViewById(R.id.list_select_tracks);

		// List view
		trackList = new ArrayList<String>();
		tracksAdapter = new ArrayAdapter<String>(this, R.layout.layout_list, R.id.text_select_list, trackList);
		list_select_tracks.setAdapter(tracksAdapter);

		// List view listener
		list_select_tracks.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(LOG_D, "Track: "+trackList.get(position));
				selectedTrack = trackList.get(position);
				selectedTrackFile = trackFiles[position];
				// Launch game
				launch();
			}
		});
		init();
	}
	
	public static void init() {
		// File directory
		Log.d(LOG_D, "Initializing Select");
		basePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Improvise");
		trackPath = new File(basePath.getAbsolutePath()+"/Songs");
		recordingPath = new File(basePath.getAbsolutePath()+"/Recordings");
		basePath.mkdirs();
		trackPath.mkdirs();
		recordingPath.mkdirs();
		trackFiles = trackPath.listFiles();

		// Iterate over all tracks
		trackMeta = new MediaMetadataRetriever();
		for(File file : trackFiles) {
			Log.d(LOG_D, "File: "+file.getAbsolutePath());
			// Grab track data and add it to array of tracks
			trackMeta.setDataSource(file.getAbsolutePath());
			String artist = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			String title = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			String songMeta = artist+" - "+title;
			trackList.add(songMeta);
	    }
	}
	
	// Launch game
	public void launch() {
		Log.d(LOG_D, "launch pressed!");
		Intent intent = new Intent(this, Game.class);
		startActivity(intent);
	}
	
	public static File[] getFiles() {
		return trackFiles;
	}
	
	public static String getSelectedTrack() {
		return selectedTrack;
	}
	
	public static File getSelectedTrackFile() {
		return selectedTrackFile;
	}
	
	public static File getRecordingPath() {
		return recordingPath;
	}
	
	public static MediaMetadataRetriever getTrackMeta() {
		return trackMeta;
	}
}
