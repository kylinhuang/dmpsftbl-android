package de.damps.fantasy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ResponseCache;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
		url = de.damps.fantasy.HomeActivity.URL + "/readmessage";
		messages = getIntent().getExtras().getParcelableArrayList("messages");
		SharedPreferences pref = de.damps.fantasy.HomeActivity.preferences;
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");
		showThreads();
	}

	// Anzeigen der Threads
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);

		setListAdapter(messageadapter);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ImageView iv = (ImageView)findViewById(R.id.iv_mes_read);
		iv.setImageResource(R.drawable.content_read);
		new ReadMessage().execute();
		// creates Intent and fills with local Data
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		message = messages.get(position);
		intent.putExtra("message", message);
		startActivity(intent);
	}
	
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

	protected void post() {
		
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("id", ((Integer) message.id).toString()));
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


}
