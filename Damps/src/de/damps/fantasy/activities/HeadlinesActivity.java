package de.damps.fantasy.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.NewsAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.News;

public class HeadlinesActivity extends ListActivity {

	/*
	 * retrieve headlines
	 */
	private class GetHeadlines extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			showHeadlines();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_headlines_bar1);
			pb.setVisibility(View.VISIBLE);
		}

	}

	private ArrayList<News> headlines = new ArrayList<News>();

	private String url;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_headlines_title)).setTypeface(font);

		String number = de.damps.fantasy.CommonUtilities.preferences.getString(
				"news", "25");
		url = de.damps.fantasy.CommonUtilities.URL + "/news/" + number;

		new GetHeadlines().execute(url);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.headlines);

		inititalizeScreen();
	}

	/*
	 * show news
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
		intent.putExtra("ID", headlines.get(position).id);
		startActivity(intent);
	}

	/*
	 * retrieve data
	 */
	private void parse() {
		DataGet data = new DataGet(url);
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

	/*
	 * refresh news
	 */
	public void refresh(View view) {
		new GetHeadlines().execute(url);
	}

	/*
	 * fill list
	 */
	private void showHeadlines() {
		final NewsAdapter titleAdapter = new NewsAdapter(this,
				android.R.layout.simple_list_item_1, headlines);
		setListAdapter(titleAdapter);
	}

}
