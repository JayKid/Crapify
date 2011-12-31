package org.jaykid.crapify;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CrapifyActivity extends Activity {
	
	static final String UNDEFINED_STRING = "-1";
	static final int UNDEFINED_INT = -1;
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeConfig();
    }
    
    private void initializeConfig() {
    	TextView moneySavedDisplayer = (TextView)findViewById(R.id.amountOfMoneyHome);
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	initializeMoneyIncrement();
    	setAmountOfMoneySavedOnMainScreen(moneySavedDisplayer);
    }

    private void initializeMoneyIncrement() {
    	String moneyIncrement = prefs.getString("moneyIncrement", UNDEFINED_STRING);
    	if (moneyIncrement == UNDEFINED_STRING) {
    		Editor preferencesEditor = prefs.edit();
    		preferencesEditor.putString("moneyIncrement", "1");
    		preferencesEditor.apply();
    	}
    }
    
	private void setAmountOfMoneySavedOnMainScreen(TextView moneySavedDisplayer) {
		String textSumOfMoney;
		int totalSum = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
    	if (totalSum == UNDEFINED_INT) textSumOfMoney = "You have not started insulting yet!";
    	else textSumOfMoney = Integer.toString(totalSum)+" €";
    	moneySavedDisplayer.setText(textSumOfMoney);
	}

    
    public void onICrappedClick(View button) {
    	Editor preferencesEditor = prefs.edit();
    	TextView moneySavedDisplayer = (TextView)findViewById(R.id.amountOfMoneyHome);
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int totalSum = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
    	String moneyIncrement = prefs.getString("moneyIncrement", UNDEFINED_STRING);
		preferencesEditor.putInt("savedMoneyAmount", totalSum+integerize(moneyIncrement));
		preferencesEditor.apply();
		refreshMoneySavedDisplayer(moneySavedDisplayer);
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
    	preferencesEditor.putInt("savedMoneyAmount", 0);
    	preferencesEditor.apply();
    }
    
	private void refreshMoneySavedDisplayer(TextView moneySavedDisplayer) {
    	prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	int moneySaved = prefs.getInt("savedMoneyAmount", UNDEFINED_INT);
		moneySavedDisplayer.setText(Integer.toString(moneySaved)+" €");
	}
}