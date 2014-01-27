package com.jareddlc.improvise;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class Player {

	private static final String LOG_D = "Improvise:Player";
	
	private static MediaPlayer player = null;
	private static String fileName = null;
	private static File fileDirectory = null;
	
	public static void main() {
		// Songs
		Log.d(LOG_D, "Initializing Player");
		fileDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Improvise");
		fileDirectory.mkdirs();
		fileName = "/Songs/ladiesNight.mp3";
	}
	
	public static void startPlaying() {
		// Create player
		Log.d(LOG_D, "Creating MediaPlayer");
		player = new MediaPlayer();
        try {
        	Log.d(LOG_D, "Trying to play file: "+fileDirectory.getAbsolutePath()+fileName);
        	player.setDataSource(fileDirectory.getAbsolutePath()+fileName);
        	player.prepare();
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
    }
}
