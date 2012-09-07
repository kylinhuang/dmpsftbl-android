package de.damps.fantasy.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.damps.fantasy.R;
import de.damps.fantasy.adapter.NewsAdapter;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.News;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HeadlinesActivity extends ListActivity {

	private ArrayList<News> headlines = new ArrayList<News>();
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headlines);
		String number = de.damps.fantasy.activities.HomeActivity.preferences.getString(
				"news", "25");
		url = de.damps.fantasy.activities.HomeActivity.URL + "/news/" + number;

		inititaliseApp();
		new GetHeadlines().execute(url);
	}

	private void inititaliseApp() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_hea_title)).setTypeface(font);

	}

	// News refreshen
	public void refresh(View view) {
		new GetHeadlines().execute(url);
	}

	private class GetHeadlines extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_hea_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showHeadlines();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	public void back(View view) {
		finish();
	}

	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			joa = jo.getJSONArray("News");
			for (int i = 0; i < joa.length(); i++) {
				News t = new News(joa.getJSONObject(i));
				headlines.add(t);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showHeadlines() {
		final NewsAdapter titleAdapter = new NewsAdapter(this,
				android.R.layout.simple_list_item_1, headlines);
		setListAdapter(titleAdapter);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
		intent.putExtra("ID", headlines.get(position).id);
		startActivity(intent);
	}

}
