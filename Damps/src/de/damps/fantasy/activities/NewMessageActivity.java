package de.damps.fantasy.activities;

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

import de.damps.fantasy.R;
import de.damps.fantasy.adapter.OwnerAdapter;
import de.damps.fantasy.data.DampsTeam;
import de.damps.fantasy.data.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class NewMessageActivity extends Activity {

	private EditText title;
	private EditText msg;
	private String titletxt;
	private String msgtxt;
	private String url;
	private String token;
	private String hash;
	private Spinner spinner;
	private String receiver;
	private String from;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);
		
		initializeScreen();
	}

	/*
	 * initialize screen
	 */
	private void initializeScreen() {
		url = de.damps.fantasy.activities.HomeActivity.URL + "/sendmessage";
		SharedPreferences pref = de.damps.fantasy.activities.HomeActivity.preferences;
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");
		
		title = (EditText) findViewById(R.id.et_newm_title);
		msg = (EditText) findViewById(R.id.et_newm_msg);
		
		initializeSpinner();
		
		Bundle extra = getIntent().getExtras();
		
		if(extra != null){
			titletxt = extra.getString("title");
			msgtxt = extra.getString("message");
			from = extra.getString("from");
			int i =de.damps.fantasy.activities.HomeActivity.league.getPosition(from);
			spinner.setSelection(i);
			
			title.setText(titletxt);
			msg.setText(msgtxt);
		}
	}

	/*
	 * retrieve title and message
	 */
	public void sendMessage(View view) {
		
		String s = ((DampsTeam)spinner.getSelectedItem()).owner_id;
		receiver = de.damps.fantasy.activities.HomeActivity.league
				.getOwneridByOwner(s);

		titletxt = title.getText().toString();
		msgtxt = msg.getText().toString();

		if (titletxt.equals("") || msgtxt.equals("")) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte Titel und Nachricht eingeben.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			new SendMessage().execute(url);
		}
	}

	/*
	 * init spinner
	 */
	private void initializeSpinner() {
		spinner = (Spinner) findViewById(R.id.spi_mewm_dest);
		OwnerAdapter ownerAdapter = new OwnerAdapter(this,
				android.R.layout.simple_spinner_item,
				de.damps.fantasy.activities.HomeActivity.league.league);
		spinner.setAdapter(ownerAdapter);

	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * send message and return to inbox
	 */
	private class SendMessage extends AsyncTask<String, Void, Void> {
		ProgressBar pb;
		String response = null;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newm_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			response = post();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			back(response);
			pb.setVisibility(View.INVISIBLE);

		}

		private void back(String response2) {
			if (!response.equals("false")) {
				Message m = new Message(titletxt, msgtxt, receiver);
				Intent intent = new Intent();
				intent.putExtra("message", m);
				setResult(2, intent);
				finish();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Fehler beim senden", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}

		}

	}

	/*
	 * send message
	 */
	protected String post() {

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("to", receiver));
		postPara.add(new BasicNameValuePair("subject", titletxt));
		postPara.add(new BasicNameValuePair("content", msgtxt));

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

		return responsebody;
	}

}
