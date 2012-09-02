package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private String url;
	private String news;
	private String title;
	private TextView titleView;
	private TextView newsView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		final Bundle extra = getIntent().getExtras();
		String news_id = extra.getString("ID");
		url = de.damps.fantasy.HomeActivity.URL + "/story/" + news_id;
		titleView = (TextView) findViewById(R.id.tv_news_title);
		newsView = (TextView) findViewById(R.id.tv_news_news);

		new GetNews().execute(url);
	}
	
	public void back(View view){
		finish();
	}

	private class GetNews extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_news_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			format();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showNews();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;
		try {
			jo = jo.getJSONObject("News");
			title = jo.getString("title");
			news = jo.getString("story");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showNews() {
		titleView.setText(Html.fromHtml(this.title));
		newsView.setText(this.news);
	}

	private void format() {
		news = news.replace("<p>", "");
		news = news.replaceAll("\\s+", " ");
		news = news.replace(" </p> ", "\n\n");
		news.trim();
	}
}
