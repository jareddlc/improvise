package com.jareddlc.improvise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;

import com.jareddlc.improvise.Recorder;
import com.jareddlc.improvise.Player;

public class Game extends Activity {
	
	private static final String LOG_D = "Improvise:Game";
	
	public boolean recording = false;
	public boolean playback = false;
	public boolean playing = false;
	public Button button_game_play;
	public Button button_game_record;
    public Button button_game_playback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);
		
		// Initialize recorder
		Recorder.main();
		Player.main();
		
		// Buttons
        button_game_play = (Button)findViewById(R.id.button_game_play);
        button_game_record = (Button)findViewById(R.id.button_game_record);
        button_game_playback = (Button)findViewById(R.id.button_game_playback);
        
        button_game_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "play pressed!");
                play();
            }
        });
        
        button_game_record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "record pressed!");
                record();
            }
        });

        button_game_playback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "playback pressed!");
                playback();
            }
        });
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
        }
    }
	
	public void record() {
        if(recording) {
        	recording = false;
        	Log.d(LOG_D, "stop recording");
        	button_game_record.setText("Record");
        	Recorder.stopRecording();
        }
        else {
        	recording = true;
        	Log.d(LOG_D, "start recording");
        	button_game_record.setText("Stop");
        	Recorder.startRecording();
        }
    }
	
	public void playback() {
        if(playback) {
        	playback = false;
        	Log.d(LOG_D, "stop playback");
        	button_game_playback.setText("Playback");
        	Player.stopPlaying(); 
        }
        else {
        	playback = true;
        	Log.d(LOG_D, "start playback");
        	button_game_playback.setText("Stop");
        	Player.startPlaying();
        }
    }

}
