package com.jareddlc.improvise;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class Main extends Activity {
	
	private static final String LOG_D = "Improvise:Main";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
	}
	
	// Launch
	public void launch(View view) {
		Log.d(LOG_D, "launch pressed!");
		Intent intent = new Intent(this, Game.class);
		startActivity(intent);
	}
	
	public void Select(View view) {
		Log.d(LOG_D, "Select pressed!");
		Intent intent = new Intent(this, Select.class);
		startActivity(intent);
	}
}
