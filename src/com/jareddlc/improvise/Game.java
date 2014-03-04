package com.jareddlc.improvise;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jareddlc.improvise.Recorder;
import com.jareddlc.improvise.Player;
import com.jareddlc.improvise.Server;

public class Game extends Activity {
	
	private static final String LOG_D = "Improvise:Game";

	private static boolean playing = false;
	private Button button_game_play;
    private TextView text_audio_meta;
    private static TextView text_audio_db;
    private static TextView text_audio_seek;
    private static Handler handler = null;
    //private BroadcastReceiver bReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);
		
		// set player context for sending messages
		Player.setContext(this);
		
		// register events
		LocalBroadcastManager.getInstance(this).registerReceiver(bReceiver, new IntentFilter("player-completed"));
		
		// Layout
        button_game_play = (Button)findViewById(R.id.button_game_play);
        text_audio_meta = (TextView)findViewById(R.id.text_audio_meta);
        text_audio_meta.setText(Select.getSelectedTrack());
        text_audio_db = (TextView)findViewById(R.id.text_audio_db);
        text_audio_db.setText("0");
        text_audio_seek = (TextView)findViewById(R.id.text_audio_seek);
        text_audio_seek.setText("0:00");
        
        button_game_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "play pressed!");
                play();
            }
        });
	}
	
	@Override
	public void onBackPressed() {
	    Log.d(LOG_D, "Backed pressed");
	    if(playing) {
	    	play();
	    }
	    super.onBackPressed();
	}
	
	public void play() {
        if(playing) {
        	playing = false;
        	Log.d(LOG_D, "stop playing");
        	button_game_play.setText("Play");
        	Recorder.stopRecording();
        	Player.stopPlaying();
        }
        else {
        	playing = true;
        	Log.d(LOG_D, "start playing");
        	button_game_play.setText("Stop");
        	Recorder.startRecording();
        	Player.startPlaying();
        	
        	handler = new Handler();
            handler.postDelayed(updateDB, 100);
        }
    }
	
	private static Runnable updateDB = new Runnable() {
		@Override
		public void run() {
			if(playing) {
				text_audio_db.setText(Recorder.getDecibel().toString());
				Integer minute = (Player.getCurrentPosition()/1000)/60;
				Integer second = (Player.getCurrentPosition()/1000)%60;
				text_audio_seek.setText(minute.toString()+":"+second.toString());
				handler.postDelayed(this, 100);
			}
		}
	};
	
	private BroadcastReceiver bReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// stop the player
			play();
			String message = intent.getStringExtra("message");
			Log.d(LOG_D, "Recieved message: " + message);
			Server.postURL();
		}
	};
}
