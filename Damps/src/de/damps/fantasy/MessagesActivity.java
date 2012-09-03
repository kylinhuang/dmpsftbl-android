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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MessagesActivity extends TabActivity {

	private TabHost tabHost;
	private String url;
	private ArrayList<Message> outbound = new ArrayList<Message>();
	private ArrayList<Message> inbound = new ArrayList<Message>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		url = de.damps.fantasy.HomeActivity.URL;

		tabHost = getTabHost();

		new GetMessages().execute(url);

	}

	private class GetMessages extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_mes_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			pb.setVisibility(View.INVISIBLE);
			createTabs();
		}

	}

	// holt die Daten und parst sie
	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			joa = jo.getJSONArray("Forum");

			for (int i = 0; i < joa.length(); i++) {
				Message m = new Message(joa.getJSONObject(i));
				if (m.inbound) {
					inbound.add(m);
				} else {
					outbound.add(m);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
