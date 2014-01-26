package com.jareddlc.improvise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.Activity;

import com.jareddlc.improvise.Recorder;

public class Game extends Activity {
	
	private static final String LOG_D = "Improvise:Game";
	
	public boolean recording = false;
	public boolean playback = false;
	public Button button_game_play;
	public Button button_game_record;
    public Button button_game_playback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);
		
		// Initialize recorder
		Recorder.main();
		
		// Buttons
        button_game_play = (Button)findViewById(R.id.button_game_play);
        button_game_record = (Button)findViewById(R.id.button_game_record);
        button_game_playback = (Button)findViewById(R.id.button_game_playback);
        
        button_game_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(LOG_D, "play pressed!");
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
        	//stopPlayback(); 
        }
        else {
        	playback = true;
        	Log.d(LOG_D, "start playback");
        	button_game_playback.setText("Stop");
        	//startPlayback();
        }
    }

}
