package de.damps.fantasy.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.DataGet;

public class NewsActivity extends Activity {

	/*
	 * get news
	 */
	private class GetNews extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			showNews();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_news_bar1);
			pb.setVisibility(View.VISIBLE);
		}

	}

	private String url;
	private String news;
	private String title;
	private TextView titleView;
	private TextView newsView;
	private int category;
	private String url_news = "";

	public void back(View view) {
		finish();
	}

	private void inititalizeTVScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_news_title1)).setTypeface(font);

		final Bundle extra = getIntent().getExtras();
		String news_id = extra.getString("ID");

		url = de.damps.fantasy.CommonUtilities.URL + "/story/" + news_id;

		titleView = (TextView) findViewById(R.id.tv_news_title);
		newsView = (TextView) findViewById(R.id.tv_news_news);

		new GetNews().execute(url);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		inititalizeTVScreen();
	}

	private void parse() {
		DataGet data = new DataGet(url);
		JSONObject jo = data.data;
		try {
			jo = jo.getJSONObject("News");
			title = jo.getString("title");
			category = jo.getInt("category");
			if(category == 1){
				news = jo.getString("story");
			}else{
				news = jo.getString("synopsis");
				url_news = jo.getString("url");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * fill textviews
	 */
	private void showNews() {
		titleView.setText(Html.fromHtml(this.title));
		news = news + "<br><br>" + url_news;
		newsView.setText(Html.fromHtml(this.news));
		newsView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
