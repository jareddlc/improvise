package com.jareddlc.improvise;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.media.MediaRecorder;
import android.util.Log;

public class Recorder {

	private static final String LOG_D = "Improvise:Recorder";
	
	private static MediaRecorder recorder = null;
	private static String fileName = null;
	private static String dateFormat = "yyyyMMdd_HHmm";
	
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
	}
	
	public static void stopRecording() {
		Log.d(LOG_D, "Recording stopping");
		recorder.stop();
		recorder.release();
		recorder = null;
    }
}
