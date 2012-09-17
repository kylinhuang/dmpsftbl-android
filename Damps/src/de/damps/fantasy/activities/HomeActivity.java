package de.damps.fantasy.activities;

import static de.damps.fantasy.CommonUtilities.SENDER_ID;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import de.damps.fantasy.R;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.League;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity {

	public static League league;
	public static String URL;
	public static SharedPreferences preferences;
	public static Editor editor;

	private EditText et_username;
	private EditText et_password;
	private CheckBox cb_save;
	private String urlLogin;
	private static ImageView log;
	private static Button tm;
	private static Button msg;

	private String TAG = "** Homeactivity **";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initializePush();
		initializeApp();
		initializeScreen();
	}

	@Override
	public void onResume() {
		super.onResume();
		initializeApp();
	}

	/** Called when the activity is closed. */
	public void onDestroy() {
		if (!preferences.getBoolean("savelogin", false)) {
			editor.clear();
			editor.commit();
		}
		super.onDestroy();
	}

	/*
	 * initializes Pushnotifications
	 */
	private void initializePush() {

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.i(TAG, "registration id =====  " + regId);

		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			Log.i(TAG, "registration id =====  " + regId);

		} else {
			Log.v(TAG, "Already registered");
		}
	}


	/*
	 * initializes App
	 */
	private void initializeApp() {
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		URL = preferences.getString("domain", getString(R.string.domain))
				+ "/json";

		new GetLeague().execute();
	}

	/*
	 * initializes screen
	 */
	private void initializeScreen() {
		log = (ImageView) findViewById(R.id.iv_hom_log);
		tm = (Button) findViewById(R.id.bu_hom_team);
		msg = (Button) findViewById(R.id.bu_hom_nachrichten);

		if (preferences.contains("token")) {
			log.setImageResource(R.drawable.logout);
			tm.setBackgroundResource(R.drawable.button_selector);
			tm.setClickable(true);
			msg.setBackgroundResource(R.drawable.button_selector);
			msg.setClickable(true);
		} else {
			log.setImageResource(R.drawable.login);
			tm.setBackgroundResource(R.drawable.button_inactive);
			tm.setClickable(false);
			msg.setBackgroundResource(R.drawable.button_inactive);
			msg.setClickable(false);
		}

		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");

		((Button) findViewById(R.id.bu_hom_news)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_forum)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_scores)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_standings)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_roster)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_starters)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_team)).setTypeface(font);
		((Button) findViewById(R.id.bu_hom_nachrichten)).setTypeface(font);
		((TextView) findViewById(R.id.tv_hom_title)).setTypeface(font);
	}

	/*
	 * gets FFLTeams
	 */
	private class GetLeague extends AsyncTask<Void, Void, Void> {
		String url = URL + "/userlist";
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_home_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... voids) {
			Json data = new Json(url);
			JSONObject jo = data.data;
			league = new League(jo);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			pb.setVisibility(View.INVISIBLE);
		}

	}

	/*
	 * inflate menu
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.hauptmenue, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * press button on menu
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// exit
		case R.id.bu_menu_exit:
			finish();
			return true;
			// options
		case R.id.bu_menu_options:
			Intent intent = new Intent(getApplicationContext(),
					OptionsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startNews(View view) {
		Intent intent = new Intent(getApplicationContext(),
				HeadlinesActivity.class);
		startActivity(intent);
	}

	public void startScores(View view) {
		Intent intent = new Intent(getApplicationContext(),
				ScoresActivity.class);
		startActivity(intent);
	}

	public void startRoster(View view) {
		Intent intent = new Intent(getApplicationContext(), TeamsActivity.class);
		startActivity(intent);
	}

	public void startForum(View view) {
		Intent intent = new Intent(getApplicationContext(), ForumActivity.class);
		startActivity(intent);
	}

	public void startStandings(View view) {
		Intent intent = new Intent(getApplicationContext(),
				StandingsActivity.class);
		startActivity(intent);
	}

	public void startStarters(View view) {
		Intent intent = new Intent(getApplication(), StartersActivity.class);
		startActivity(intent);
	}

	public void options(View view) {
		Intent intent = new Intent(getApplicationContext(),
				OptionsActivity.class);
		startActivity(intent);
	}

	public void startTeammanagement(View view) {
		Intent intent = new Intent(getApplicationContext(),
				TeamManagementActivity.class);
		startActivity(intent);
	}

	public void startMessages(View view) {
		Intent intent = new Intent(getApplicationContext(),
				MessagesActivity.class);
		startActivity(intent);
	}

	/*
	 * logging in
	 */
	public void log(View view) {
		if (!preferences.contains("token")) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.log);
			dialog.setTitle("Log in");
			dialog.setCancelable(true);

			// set up button
			Button bu_cancel = (Button) dialog.findViewById(R.id.bu_cancel);
			Button bu_ok = (Button) dialog.findViewById(R.id.bu_ok);
			et_username = (EditText) dialog.findViewById(R.id.et_log_text1);
			et_password = (EditText) dialog.findViewById(R.id.et_log_text2);
			cb_save = (CheckBox) dialog.findViewById(R.id.cb_log_Box1);

			bu_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String[] user = { et_username.getText().toString(),
							et_password.getText().toString() };
					try {
						if (new Login().execute(user).get()) {

							log.setImageResource(R.drawable.logout);
							tm.setBackgroundResource(R.drawable.button_selector);
							tm.setClickable(true);
							msg.setBackgroundResource(R.drawable.button_selector);
							msg.setClickable(true);

							if (cb_save.isChecked()) {
								editor.putBoolean("savelogin", true);
							}

							editor.commit();
							dialog.dismiss();
						} else {
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"Falscher Benutzername/Passwort",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			bu_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			logout();
		}

	}

	/*
	 * retrieve headlines
	 */
	private class Login extends AsyncTask<String, Void, Boolean> {

		protected Boolean doInBackground(String... params) {
			return login(params);
		}

		protected void onPostExecute(Boolean result) {
		}

	}

	/*
	 * loging in
	 * 
	 * returns if logged in sets userdata
	 */
	protected boolean login(String[] user) {
		urlLogin = URL + "/login";
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(urlLogin);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("username", user[0]));
		postPara.add(new BasicNameValuePair("password", user[1]));

		String responsebody = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));
			try {
				HttpResponse response = client.execute(httppost);
				responsebody = EntityUtils.toString(response.getEntity());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (responsebody.equals("false")) {
			return false;
		} else {
			try {
				JSONObject combo = new JSONObject(responsebody);
				JSONObject ret = (JSONObject) combo.get("return");
				String token = (String) ret.get("token");
				String hash = (String) ret.get("encrypted");
				String userid = ((Integer) ret.getInt("id")).toString();
				editor.putString("token", token);
				editor.putString("hash", hash);
				editor.putString("id", userid);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}

	}

	/*
	 * Logging out
	 * 
	 * Removes prefs, sets buttons
	 */
	public static void logout() {
		editor.remove("token");
		editor.remove("hash");
		editor.remove("id");
		editor.remove("savelogin");
		editor.commit();
		log.setImageResource(R.drawable.login);
		tm.setBackgroundResource(R.drawable.button_inactive);
		tm.setClickable(false);
		msg.setBackgroundResource(R.drawable.button_inactive);
		msg.setClickable(false);
	}

}