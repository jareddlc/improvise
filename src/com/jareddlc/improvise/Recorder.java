package com.jareddlc.improvise;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

public class Recorder {

	private static final String LOG_D = "Improvise:Recorder";
	
	private static MediaRecorder recorder = null;
	private static String fileName = null;
	private static String dateFormat = "yyyyMMdd_HHmm";
	private static Integer maxAmplitude = null;
	private static Double decibel = 0.0;
	private static boolean recording = false;
	private static Handler handler = null;
	
	public static void startRecording() {
		// set recording name
		SimpleDateFormat dFormat = new SimpleDateFormat(dateFormat, Locale.US);
		fileName = "/"+Select.getSelectedTrack()+"_"+dFormat.format(new Date())+".m4a";
		
		// Create recorder
		Log.d(LOG_D, "Creating MediaRecorder");
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setOutputFile(Select.getRecordingPath().getAbsolutePath()+fileName);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //recorder.setAudioEncodingBitRate(96000);
        //recorder.setAudioSamplingRate(48000);
		
		Log.d(LOG_D, "Saving file to: "+Select.getRecordingPath().getAbsolutePath()+fileName);
        try {
        	Log.d(LOG_D, "Try Prepare");
        	recorder.prepare();
        }
        catch(IOException e) {
            Log.e(LOG_D, "recorder.prepare() failed", e);
        }
        Log.d(LOG_D, "Recorder recording");
        recorder.start();
        recording = true;
        handler = new Handler();
        handler.postDelayed(getAmplitude, 100);
	}
	
	public static void stopRecording() {
		Log.d(LOG_D, "Recording stopping");
		recorder.stop();
		recorder.release();
		recorder = null;
		recording = false;
    }
	
	public static void updateAmplitude() {
		maxAmplitude = recorder.getMaxAmplitude();
		//double db = (20 * Math.log10(amplitude / REFERENCE));
		//REFERENCE=0.1 (I am aware that this should be something like 2*10^(-5) Pascal ((20 uPascal)), but that returns strange values... 0.1 strangely works better.)
		//http://stackoverflow.com/questions/7189275/how-to-calculate-microphone-audio-input-power-in-decibell-unit
		decibel = (20 * Math.log10(maxAmplitude));
		Log.d(LOG_D, "maxAmplitude: "+maxAmplitude);
		Log.d(LOG_D, "decibel: "+decibel);
	}
	
	private static Runnable getAmplitude = new Runnable() {
		@Override
		public void run() {
			if(recording) {
				updateAmplitude();
				handler.postDelayed(this, 100);
			}
		}
	};
	
	public static Double getDecibel() {
		return decibel;
	}
	
}
