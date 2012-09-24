package de.damps.fantasy.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.damps.fantasy.R;
import de.damps.fantasy.adapter.LogAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.Log;
import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TradelogActivity extends ListActivity {

	private ArrayList<Log> logs = new ArrayList<Log>();
	private String url;
	private LogAdapter logAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tradelog);

		inititalizeScreen();
	}

	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_tradelog_title)).setTypeface(font);

		url = de.damps.fantasy.CommonUtilities.URL + "/tradelog";

		new GetLog().execute();
	}
	
	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * retrieve headlines
	 */
	private class GetLog extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_tradelog_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showLog();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	/*
	 * retrieve data
	 */
	private void parse() {
		DataGet data = new DataGet(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			joa = jo.getJSONArray("Tradelog");
			for (int i = 0; i < joa.length(); i++) {
				logs.add(new Log(joa.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * fill list
	 */
	private void showLog() {
		logAdapter = new LogAdapter(this,
				R.layout.logitem, logs);
		setListAdapter(logAdapter);
	}

}
