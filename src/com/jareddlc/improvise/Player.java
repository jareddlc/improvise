package com.jareddlc.improvise;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

public class Player {

	private static final String LOG_D = "Improvise:Player";
	
	private static MediaPlayer player = null;
	private static MediaMetadataRetriever meta = null;
	private static String fileName = null;
	private static File fileDirectory = null;
	private static String songMeta = null;
	
	public static void main() {		
		// Songs
		Log.d(LOG_D, "Initializing Player");
		fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Improvise");
		fileDirectory.mkdirs();
		fileName = "/Songs/ladiesNight.m4a";
	}
	
	public static void startPlaying() {
		// Create player
		Log.d(LOG_D, "Creating MediaPlayer");
		player = new MediaPlayer();
		meta = new MediaMetadataRetriever();
        try {
        	Log.d(LOG_D, "Trying to play file: "+fileDirectory.getAbsolutePath()+fileName);
        	player.setDataSource(fileDirectory.getAbsolutePath()+fileName);
        	player.prepare();
        	meta.setDataSource(fileDirectory.getAbsolutePath()+fileName);
        	Log.d(LOG_D, "Duration: "+meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        	Log.d(LOG_D, "Title: "+meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        	Log.d(LOG_D, "Artist: "+meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        	Log.d(LOG_D, "Player playing");
        	player.start();
        } catch (IOException e) {
        	Log.e(LOG_D, "player.prepare() failed", e);
        }
    }
	public static void stopPlaying() {
		Log.d(LOG_D, "Player stopping");
		player.release();
		player = null;
		meta.release();
		meta = null;
    }
	
	public static String getMeta() {
		String artist = meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		String title = meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		songMeta = artist+" - "+title;
		return songMeta;
	}
}
