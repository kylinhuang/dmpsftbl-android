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
import de.damps.fantasy.adapter.MessageAdapter;
import de.damps.fantasy.data.Message;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;

public class InboxActivity extends ListActivity {

	private MessageAdapter messageadapter;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private String url;
	private Message message;
	private String token;
	private String hash;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		url = de.damps.fantasy.activities.HomeActivity.URL + "/readmessage";
		messages = getIntent().getExtras().getParcelableArrayList("messages");
		SharedPreferences pref = de.damps.fantasy.activities.HomeActivity.preferences;
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");
		showThreads();
	}


	/*
	 * fill list
	 */
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);
		setListAdapter(messageadapter);
	}

	/*
	 * back from reading
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			int p = data.getExtras().getInt("pos");
			messages.get(p).read[0] = true;

			messageadapter.notifyDataSetChanged();
		}
	}

	/*
	 * open message
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		new ReadMessage().execute();
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		message = messages.get(position);
		intent.putExtra("message", message);
		intent.putExtra("pos", position);
		startActivityForResult(intent, 1);
	}

	/*
	 * mark message as read
	 */
	private class ReadMessage extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		};

		@Override
		protected Void doInBackground(Void... voids) {
			post();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
		}

	}

	/*
	 * send marker
	 */
	protected void post() {

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("id", ((Integer) message.id)
				.toString()));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));

			try {
				HttpResponse response = client.execute(httppost);
				String s = EntityUtils.toString(response.getEntity());
				String t =s;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
