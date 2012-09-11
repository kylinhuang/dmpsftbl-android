package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionsActivity extends Activity {

	private EditText domain, news, threads;
	private RadioButton oben;
	private SharedPreferences pref;
	private Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		initialiseScreen();
	}

	/*
	 * init screen
	 */
	private void initialiseScreen() {
		pref = de.damps.fantasy.activities.HomeActivity.preferences;
		editor = de.damps.fantasy.activities.HomeActivity.editor;
		domain = (EditText) findViewById(R.id.et_options_Text1);
		news = (EditText) findViewById(R.id.et_options_Text2);
		threads = (EditText) findViewById(R.id.et_options_Text3);
		oben = (RadioButton) findViewById(R.id.rb_options_oben);

		domain.setText(pref.getString("domain", getString(R.string.domain)));
		news.setText(pref.getString("news", "25"));
		threads.setText(pref.getString("threads", "25"));
		if(pref.getBoolean("chron", false)){
			((RadioGroup) findViewById(R.id.rg_opt_Group1)).check(R.id.rb_options_unten);
		}else{
			((RadioGroup) findViewById(R.id.rg_opt_Group1)).check(R.id.rb_options_oben);
		}
	}
	
	/*
	 * return to last screen
	 */
	public void back(View view){
		finish();
	}

	public void setDomain(View view) {
		String dom = domain.getText().toString();
		editor.putString("domain", domain.getText().toString());
		editor.commit();
		String msg = getString(R.string.domain_set) + " " + dom + " " + getString(R.string.setted);
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		toast.show();
		de.damps.fantasy.activities.HomeActivity.logout();
	}

	public void setNewsNumber(View view) {
		String number = news.getText().toString();
		editor.putString("news", number);
		editor.commit();
		String msg = getString(R.string.news_set) + " " + number + " " + getString(R.string.setted);
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void setThreadsNumber(View view) {
		String number = threads.getText().toString();
		editor.putString("threads", number);
		editor.commit();
		String msg = getString(R.string.threads_set) + " " + number + " " + getString(R.string.setted);
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void setOrientation(View view) {
		if(oben.isChecked()){
			editor.putBoolean("chron", false);
			editor.commit();
			String msg = getString(R.string.posts_set) + " oben " + getString(R.string.show);
			Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
			toast.show();
		}else{
			editor.putBoolean("chron", true);
			editor.commit();
			String msg = getString(R.string.posts_set) + " unten " + getString(R.string.show);
			Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
			toast.show();
		}
	}

	/*
	 * return to last screen
	 */
	public void ok(View view) {
		this.finish();
	}
}
