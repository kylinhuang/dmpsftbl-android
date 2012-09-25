package de.damps.fantasy.activities;

import static de.damps.fantasy.CommonUtilities.SENDER_ID;
import static de.damps.fantasy.CommonUtilities.preferences;
import static de.damps.fantasy.CommonUtilities.league;
import static de.damps.fantasy.CommonUtilities.URL;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import de.damps.fantasy.R;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.League;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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

	public static Editor editor;

	private EditText et_username;
	private EditText et_password;
	private CheckBox cb_save;
	private String url_login;
	private ImageView log;
	private Button msg;
	private Button mt;
	private Button ss;
	private Button rp;
	private Button sp;
	private Button opt;
	private Button oft;
	private Button tl;

	private String TAG = "** Homeactivity **";
	private String reg_id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		initializeApp();
		initializePush();
		initializeScreen();
	}

	@Override
	public void onResume() {
		super.onResume();
		initializeApp();
		initializeScreen();
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
		reg_id = regId;

		if (regId.equals("")) {
			GCMRegistrar.register(this, SENDER_ID);
			reg_id = GCMRegistrar.getRegistrationId(this);
		} else {
			Log.v(TAG, "Already registered");
		}
	}

	/*
	 * initializes App
	 */
	private void initializeApp() {
		de.damps.fantasy.CommonUtilities.context = getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		URL = preferences.getString("domain", getString(R.string.domain))
				+ "/json";

		url_login = URL + "/login";

		new GetLeague().execute();
	}

	/*
	 * initializes screen
	 */
	private void initializeScreen() {
		log = (ImageView) findViewById(R.id.iv_home_log);
		mt = (Button) findViewById(R.id.bu_home_myteam);
		ss = (Button) findViewById(R.id.bu_home_starter);
		rp = (Button) findViewById(R.id.bu_home_release);
		sp = (Button) findViewById(R.id.bu_home_sign);
		opt = (Button) findViewById(R.id.bu_home_open_trades);
		oft = (Button) findViewById(R.id.bu_home_offer_trade);
		tl = (Button) findViewById(R.id.bu_home_tradelist);
		msg = (Button) findViewById(R.id.bu_home_nachrichten);

		if (preferences.contains("token")) {
			activateMemberarea(true);
		} else {
			activateMemberarea(false);
		}

		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");

		((TextView) findViewById(R.id.tv_home_title)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_news)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_forum)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_scores)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_standings)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_roster)).setTypeface(font);
		((Button) findViewById(R.id.bu_home_starters)).setTypeface(font);
		mt.setTypeface(font);
		ss.setTypeface(font);
		rp.setTypeface(font);
		sp.setTypeface(font);
		opt.setTypeface(font);
		oft.setTypeface(font);
		tl.setTypeface(font);
		msg.setTypeface(font);

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
			DataGet data = new DataGet(url);
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

	public void startMessages(View view) {
		Intent intent = new Intent(getApplicationContext(),
				MessagesActivity.class);
		startActivity(intent);
	}

	public void myTeam(View view) {
		Intent intent = new Intent(getApplicationContext(),
				RosterActivity.class);
		intent.putExtra("oid", preferences.getString("id", "0"));
		startActivity(intent);
	}

	public void selectStarter(View view) {
		Intent intent = new Intent(getApplicationContext(),
				SetStartersActivity.class);
		startActivity(intent);
	}

	public void releasePlayer(View view) {
		Intent intent = new Intent(getApplicationContext(),
				ReleaseActivity.class);
		startActivity(intent);
	}

	public void signFreeAgent(View view) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			Intent intent = new Intent(getApplicationContext(),
					SignActivity.class);
			startActivity(intent);
		} else {
			// Do something different to support older versions
		}

	}

	public void openTrades(View view) {
		/*
		 * Intent intent = new Intent(getApplicationContext(),
		 * OpenTradesActivity.class); startActivity(intent);
		 */
	}

	public void offerTrade(View view) {
		/*
		 * Intent intent = new Intent(getApplicationContext(),
		 * SignFreeAgent.class); startActivity(intent);
		 */
	}

	public void tradeLog(View view) {
		
		  Intent intent = new Intent(getApplicationContext(),
		  TradelogActivity.class); startActivity(intent);
		 
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
							activateMemberarea(true);

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
						e.printStackTrace();
					} catch (ExecutionException e) {
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
		String[][] data = new String[3][2];

		data[0][0] = "username";
		data[0][1] = user[0];
		data[1][0] = "password";
		data[1][1] = user[1];
		data[2][0] = "reg_id";
		data[2][1] = reg_id;

		String responsebody = new DataPost(url_login, data).response;

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

	public void logout() {
		editor.remove("token");
		editor.remove("hash");
		editor.remove("id");
		editor.remove("savelogin");
		editor.commit();
		initializeScreen();
	}

	private void activateMemberarea(boolean b) {
		if (b == true) {
			log.setImageResource(R.drawable.logout);
			mt.setBackgroundResource(R.drawable.button_selector);
			mt.setClickable(true);
			ss.setBackgroundResource(R.drawable.button_selector);
			ss.setClickable(true);
			rp.setBackgroundResource(R.drawable.button_selector);
			rp.setClickable(true);

			sp.setBackgroundResource(R.drawable.button_selector);
			sp.setClickable(true);
			/*
			 * opt.setBackgroundResource(R.drawable.button_selector);
			 * opt.setClickable(true);
			 * oft.setBackgroundResource(R.drawable.button_selector);
			 * oft.setClickable(true);
			 */
			tl.setBackgroundResource(R.drawable.button_selector);
			tl.setClickable(true);

			msg.setBackgroundResource(R.drawable.button_selector);
			msg.setClickable(true);
		} else {
			log.setImageResource(R.drawable.login);
			mt.setBackgroundResource(R.drawable.button_inactive);
			mt.setClickable(false);
			ss.setBackgroundResource(R.drawable.button_inactive);
			ss.setClickable(false);
			rp.setBackgroundResource(R.drawable.button_inactive);
			rp.setClickable(false);
			sp.setBackgroundResource(R.drawable.button_inactive);
			sp.setClickable(false);
			opt.setBackgroundResource(R.drawable.button_inactive);
			opt.setClickable(false);
			oft.setBackgroundResource(R.drawable.button_inactive);
			oft.setClickable(false);
			tl.setBackgroundResource(R.drawable.button_inactive);
			tl.setClickable(false);
			msg.setBackgroundResource(R.drawable.button_inactive);
			msg.setClickable(false);
		}
	}

}