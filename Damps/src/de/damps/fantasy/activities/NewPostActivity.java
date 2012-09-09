package de.damps.fantasy.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import de.damps.fantasy.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewPostActivity extends Activity {

	private String title;
	private EditText msg;
	private String url;
	private String msgtxt;
	private String token;
	private String hash;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_post);
		
		
		inititalizeScreen();
	} 
	
	/*
	 * initialize screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_newthread_title1)).setTypeface(font);

		final Bundle extra = getIntent().getExtras();

		id = extra.getString("ID");
		title = extra.getString("title");
		
		url = de.damps.fantasy.activities.HomeActivity.URL + "/postforum";
		
		SharedPreferences pref = de.damps.fantasy.activities.HomeActivity.preferences;
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");

		msg = (EditText) findViewById(R.id.et_newpost_msg);
		if (extra.containsKey("quote")) {
			msg.setText(extra.getString("quote"));
		}
	}
	
	/*
	 * back to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * post reply
	 */
	public void post(View view) {
		msgtxt = msg.getText().toString();

		if (msgtxt.equals("")) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte Titel und Nachricht eingeben.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			new Reply().execute();
		}
	}

	
	/*
	 * send reply
	 */
	private class Reply extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newpost_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... params) {
			reply();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			openThread();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	/*
	 * send reply
	 */
	protected void reply() {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("message", msgtxt));
		postPara.add(new BasicNameValuePair("forum_id", id));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));
			try {
				client.execute(httppost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/*
	 * open thread
	 */
	public void openThread() {
		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID", id);
		intent.putExtra("title", title);
		startActivity(intent);
	}
}
