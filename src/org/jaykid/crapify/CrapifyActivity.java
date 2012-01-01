package org.jaykid.crapify;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CrapifyActivity extends Activity {
	
	static final String UNDEFINED_STRING = "-1";
	static final int UNDEFINED_INT = -1;
	static final int NO_MONEY = 0;
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeConfig();
        
        final ImageButton crapButton = (ImageButton)findViewById(R.id.ICrappedHome);
    	crapButton.setImageResource(R.drawable.button_normal);

    	crapButton.setOnTouchListener(new OnTouchListener() {
    		@Override
    	    public boolean onTouch(View v, MotionEvent event) {
    	        if (event.getAction() == MotionEvent.ACTION_DOWN ) {
    	        	Editor preferencesEditor = prefs.edit();
    	        	TextView moneySavedDisplayer = (TextView)findViewById(R.id.amountOfMoneyHome);
    	        	prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	        	
    	        	int moneySaved = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
    	        	if (moneySaved == UNDEFINED_INT) moneySaved = NO_MONEY;
    	        	String moneyIncrement = prefs.getString("moneyIncrement", UNDEFINED_STRING);
    	    		preferencesEditor.putInt("savedMoneyAmount", moneySaved+integerize(moneyIncrement));
    	    		preferencesEditor.apply();
    	    		refreshMoneySavedDisplayer(moneySavedDisplayer);
    	        	
    	    		crapButton.setImageResource(R.drawable.button_pressed);
    	    		
	            	playRandomSound();
    	        }
    	         else if (event.getAction() == MotionEvent.ACTION_UP ) {
    	        	 crapButton.setImageResource(R.drawable.button_normal);
    	        }
    	        return false;
    	    }
    	});
    }
    
    private void initializeConfig() {
    	TextView moneySavedDisplayer = (TextView)findViewById(R.id.amountOfMoneyHome);
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	initializeMoneyIncrement();
    	initializeAmountOfMoneySavedOnMainScreen(moneySavedDisplayer);
    }

    private void initializeMoneyIncrement() {
    	String moneyIncrement = prefs.getString("moneyIncrement", UNDEFINED_STRING);
    	if (moneyIncrement == UNDEFINED_STRING) {
    		Editor preferencesEditor = prefs.edit();
    		preferencesEditor.putString("moneyIncrement", "1");
    		preferencesEditor.apply();
    	}
    }
    
	private void initializeAmountOfMoneySavedOnMainScreen(TextView moneySavedDisplayer) {
		String textSumOfMoney;
		int moneySaved = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
    	if (moneySaved == UNDEFINED_INT) textSumOfMoney = "You have not started insulting yet! Press the pig to start saving!";
    	else textSumOfMoney = Integer.toString(moneySaved)+" €";
    	moneySavedDisplayer.setText(textSumOfMoney);
	}

	private int integerize(String moneyIncrement) {
		return Integer.parseInt(moneyIncrement);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
	        case R.id.settingsMenu:
				Intent newIntent = new Intent(this, ConfigurationActivity.class);
				startActivity(newIntent);
	        break;
	        case R.id.resetMoneyMenu:
				Editor preferencesEditor = prefs.edit();
				TextView moneySavedDisplayer = (TextView)findViewById(R.id.amountOfMoneyHome);
				prefs = PreferenceManager.getDefaultSharedPreferences(this);
				resetAmountOfMoneySaved(preferencesEditor);
				refreshMoneySavedDisplayer(moneySavedDisplayer);
	        break;
    	}
    	return true;
    }

    private void resetAmountOfMoneySaved(Editor preferencesEditor) {
    	preferencesEditor.putInt("savedMoneyAmount", NO_MONEY);
    	preferencesEditor.apply();
    }
    
	private void refreshMoneySavedDisplayer(TextView moneySavedDisplayer) {
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int moneySaved = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
		moneySavedDisplayer.setText(Integer.toString(moneySaved)+" €");
	}

	private void playRandomSound() {
		new Thread(new Runnable() {
		    public void run() {
		    	int sound = getRandomSound();
				MediaPlayer mp = MediaPlayer.create(getApplicationContext(), sound);   
		        mp.start();
		    }
		  }).start();
	}
	
	private int getRandomSound() {
		int sounds[] = {R.raw.smb3_1_up,R.raw.smb3_fireball,R.raw.smb3_powerup,R.raw.smw_coin,R.raw.smw_mushroom,R.raw.smw_yoshi};
		Random generator = new Random();
		int sound = sounds[generator.nextInt(6)];
		return sound;
	}
}