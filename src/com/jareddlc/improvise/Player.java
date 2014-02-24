package com.jareddlc.improvise;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer.OnCompletionListener;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jareddlc.improvise.Select;

public class Player {

	private static final String LOG_D = "Improvise:Player";
	
	private static MediaPlayer player = null;
	private static MediaMetadataRetriever trackMeta = null;
	private static String meta = null;
	private static boolean playing = false;
	private static Context context = null;
	
	public static void setContext(Context c) {
		Log.d(LOG_D, "Set Context");
		context = c;
	}
	
	public static void startPlaying() {
		// Create player
		Log.d(LOG_D, "Creating MediaPlayer");
		player = new MediaPlayer();
		trackMeta = new MediaMetadataRetriever();
        try {
        	Log.d(LOG_D, "Trying to play file: "+Select.getSelectedTrack());
        	player.setDataSource(Select.getSelectedTrackFile().getAbsolutePath());
        	player.prepare();
        	trackMeta.setDataSource(Select.getSelectedTrackFile().getAbsolutePath());
        	Log.d(LOG_D, "Duration: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        	Log.d(LOG_D, "Title: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        	Log.d(LOG_D, "Artist: "+trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        	Log.d(LOG_D, "Player playing");
        	player.start();
        	playing = true;
        } 
        catch (IOException e) {
        	Log.e(LOG_D, "player.prepare() failed", e);
        }
        
        player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.d(LOG_D, "Player completed.");
				//stopPlaying();
				sendMessage();
			}
        });
    }
	public static void stopPlaying() {
		Log.d(LOG_D, "Player stopping");
		player.release();
		player = null;
		playing = false;
		trackMeta.release();
		trackMeta = null;
    }
	
	public static String getMeta() {
		String artist = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		String title = trackMeta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		meta = artist+" - "+title;
		return meta;
	}
	
	public static Integer getDuration() {
		if(playing) {
			return player.getDuration();
		}
		else {
			return 0;
		}
	}
	
	public static Integer getCurrentPosition() {
		if(playing) {
			return player.getCurrentPosition();
		}
		else {
			return 0;
		}
	}
	
	private static void sendMessage() {
	  Log.d(LOG_D, "Broadcasting message");
	  Intent intent = new Intent("player-completed");
	  intent.putExtra("message", "finished playing");
	  LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	  //LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
	}
}
