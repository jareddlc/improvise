package com.jareddlc.improvise;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.jareddlc.improvise.Select;

public class Player {

	private static final String LOG_D = "Improvise:Player";
	
	private static MediaPlayer player = null;
	private static MediaMetadataRetriever trackMeta = null;
	private static String meta = null;
	
	public static void startPlaying() {
		// Create player
		Log.d(LOG_D, "Creating MediaPlayer");
		player = new MediaPlayer();
		trackMeta = new MediaMetadataRetriever();
        try {
        	Log.d(LOG_D, "Trying to play file: "+Select.getSelectedTrack());
        	//player.setDataSource(fileDirectory.getAbsolutePath()+fileName);
        	player.setDataSource(Select.getSelectedTrackFile().getAbsolutePath());
        	player.prepare();
        	//meta.setDataSource(fileDirectory.getAbsolutePath()+fileName);
        	trackMeta.setDataSource(Select.getSelectedTrackFile().getAbsolutePath());
        	Log.d(LOG_D, "Duration: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        	Log.d(LOG_D, "Title: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        	Log.d(LOG_D, "Artist: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
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
		trackMeta.release();
		trackMeta = null;
    }
	
	public static String getMeta() {
		String artist = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		String title = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		meta = artist+" - "+title;
		return meta;
	}
}
