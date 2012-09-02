package fantasyApp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import de.damps.fantasy.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class HomeActivity extends Activity {

	public static final int[] ID = { 9, 1, 11, 10, 15, 16, 7, 3, 13, 6, 2, 4,
			14, 5 };
	public static int NR = ID.length;
	public static String[] TEAMS;
	public static String URL;
	public static SharedPreferences preferences;
	public static Editor editor;
	private EditText et_username;
	private EditText et_password;
	private CheckBox cb_save;
	private String urlLogin;
	private ImageView log;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		inititaliseApp();
	}

	@Override
	public void onResume() {
		super.onResume();
		URL = preferences.getString("domain", getString(R.string.domain))
				+ "/json";
	}

	private void inititaliseApp() {
		TEAMS = getResources().getStringArray(R.array.FFLTeams);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		URL = preferences.getString("domain", getString(R.string.domain))
				+ "/json";
		log = (ImageView) findViewById(R.id.iv_hom_log);
		if (preferences.contains("token")) {
			log.setImageResource(R.drawable.logout);
		} else {
			log.setImageResource(R.drawable.login);
		}

	}

	/** Called when the activity is closed. */
	public void onDestroy() {
		if (!preferences.getBoolean("savelogin", false)) {
			editor.clear();
			editor.commit();
		}
		super.onDestroy();
	}

	/** optionmenu */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.hauptmenue, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/** select item on optionsmenue */
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

	/** select news */
	public void startNews(View view) {
		Intent intent = new Intent(getApplicationContext(),
				HeadlinesActivity.class);
		startActivity(intent);
	}

	/** select roster */
	public void startRoster(View view) {
		Intent intent = new Intent(getApplicationContext(), TeamsActivity.class);
		startActivity(intent);
	}

	/** select scores */
	public void startScores(View view) {
		Intent intent = new Intent(getApplicationContext(),
				ScoresActivity.class);
		startActivity(intent);
	}

	/** select standings */
	public void startStandings(View view) {
		Intent intent = new Intent(getApplicationContext(),
				StandingsActivity.class);
		startActivity(intent);
	}

	/** select starters */
	public void startStarters(View view) {
		Intent intent = new Intent(getApplication(), StartersActivity.class);
		startActivity(intent);
	}

	/** select injuries */
	public void startInjuries(View view) {
		//Intent intent = new Intent(getApplicationContext(),
			//	InjuriesActivity.class);
		//startActivity(intent);
	}

	/** select Forum */
	public void startForum(View view) {
		Intent intent = new Intent(getApplicationContext(), ForumActivity.class);
		startActivity(intent);
	}

	public void options(View view) {
		Intent intent = new Intent(getApplicationContext(),
				OptionsActivity.class);
		startActivity(intent);
	}

	/** select log */
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
					if (login(et_username.getText().toString(), et_password
							.getText().toString())) {

						log.setImageResource(R.drawable.logout);
						if (cb_save.isChecked()) {
							editor.putBoolean("savelogin", true);
						} else {
							editor.putBoolean("savelogin", false);
						}

						editor.commit();
						dialog.dismiss();
					} else {
						Toast toast = Toast.makeText(getApplicationContext(),
								"Falscher Benutzername/Passwort",
								Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
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

	protected boolean login(String un, String pw) {
		urlLogin = URL + "/login";
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(urlLogin);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();
		JSONObject combo;

		postPara.add(new BasicNameValuePair("username", un));
		postPara.add(new BasicNameValuePair("password", pw));

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
				combo = new JSONObject(responsebody);
				JSONObject ret = (JSONObject) combo.get("return");
				String token = (String) ret.get("token");
				String hash = (String) ret.get("encrypted");
				editor.putString("token", token);
				editor.putString("hash", hash);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}

	}

	/** select logout */
	public void logout() {
		editor.remove("token");
		editor.remove("hash");
		editor.remove("savelogin");
		editor.commit();
		log.setImageResource(R.drawable.login);
	}

}