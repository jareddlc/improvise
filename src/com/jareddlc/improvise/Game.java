package com.jareddlc.improvise;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

import com.jareddlc.improvise.Recorder;
import com.jareddlc.improvise.Player;

public class Game extends Activity {
	
	private static final String LOG_D = "Improvise:Game";
	
	public boolean recording = false;
	public boolean playback = false;
	public static boolean playing = false;
	public Button button_game_play;
    public TextView text_audio_meta;
    public static TextView text_audio_db;
    public static TextView text_audio_seek;
    private static Handler handler = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);
		
		// Layout
        button_game_play = (Button)findViewById(R.id.button_game_play);
        text_audio_meta = (TextView)findViewById(R.id.text_audio_meta);
        text_audio_meta.setText(Select.getSelectedTrack());
        text_audio_db = (TextView)findViewById(R.id.text_audio_db);
        text_audio_db.setText(Recorder.getDecibel().toString());
        text_audio_seek = (TextView)findViewById(R.id.text_audio_seek);
        text_audio_seek.setText(Player.getCurrentPosition().toString()+"/"+Player.getDuration().toString());
        
        button_game_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "play pressed!");
                play();
            }
        });
	}
	
	@Override
	public void onBackPressed() {
	    Log.d(LOG_D, "backed pressed");
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
				text_audio_seek.setText(Player.getCurrentPosition().toString()+"/"+Player.getDuration().toString());
				handler.postDelayed(this, 100);
			}
		}
	};
}
