package de.damps.fantasy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MessagesActivity extends TabActivity {

	private TabHost tabHost;
	private ArrayList<Message> outbound = new ArrayList<Message>();
	private ArrayList<Message> inbound = new ArrayList<Message>();
	private String urlin;
	private String urlout;
	private String token;
	private String hash;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		urlin = de.damps.fantasy.HomeActivity.URL + "/messages/to";
		urlout = de.damps.fantasy.HomeActivity.URL + "/messages/from";
		SharedPreferences pref = de.damps.fantasy.HomeActivity.preferences;
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");
		tabHost = getTabHost();

		new GetMessages().execute();

	}
	
	public void refresh(View view){
		tabHost.removeAllViews();
		new GetMessages().execute();
	}

	private class GetMessages extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_mes_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... voids) {
			inbound = parse(urlin);
			outbound = parse(urlout);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			pb.setVisibility(View.INVISIBLE);
			createTabs();
		}

	}

	// holt die Daten und parst sie
	private ArrayList<Message> parse(String url) {
		Json data = new Json(url, token, hash);
		JSONObject jo = data.data;
		JSONArray joa = null;
		ArrayList<Message> list = new ArrayList<Message>();
		try {
			joa = jo.getJSONArray("Messages");

			for (int i = 0; i < joa.length(); i++) {
				Message m = new Message(joa.getJSONObject(i));

				list.add(m);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}

	private void createTabs() {
		View view1 = createTabView(tabHost.getContext(), "Inbox");
		TabSpec inspec = tabHost.newTabSpec("Inbox");
		inspec.setIndicator(view1);
		Intent inintent = new Intent(getApplicationContext(),
				InboxActivity.class);
		inintent.putParcelableArrayListExtra("messages", inbound);
		inspec.setContent(inintent);

		View view2 = createTabView(tabHost.getContext(), "Outbox");
		TabSpec outspec = tabHost.newTabSpec("Outbox");
		outspec.setIndicator(view2);
		Intent outintent = new Intent(getApplicationContext(),
				OutboxActivity.class);
		outintent.putParcelableArrayListExtra("messages", outbound);
		outspec.setContent(outintent);

		tabHost.addTab(inspec);
		tabHost.addTab(outspec);
		tabHost.setCurrentTab(0);
	}

	private View createTabView(Context context, String string) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs, null);
		TextView tv = (TextView) view.findViewById(R.id.tv_tab_tab);
		tv.setText(string);
		return view;
	}

	public void sendMessage(View view) {
		Intent intent = new Intent(getApplicationContext(),
				NewMessageActivity.class);
		startActivity(intent);
	}

	public void back(View view) {
		finish();
	}
}
