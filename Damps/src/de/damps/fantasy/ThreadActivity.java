package de.damps.fantasy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadActivity extends ListActivity {

	private String url;
	private ArrayList<Post> posts = new ArrayList<Post>();
	private PostAdapter postadapter;
	private TextView titleview;
	private String title;
	private boolean chron;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread);
		final Bundle extra = getIntent().getExtras();

		id = extra.getString("ID");
		title = extra.getString("title");
		url = de.damps.fantasy.HomeActivity.URL + "/thread/" + id;
		chron = de.damps.fantasy.HomeActivity.preferences.getBoolean("chron",
				false);

		titleview = (TextView) findViewById(R.id.tv_thr_title);
		titleview.setText(title);

		new GetThread().execute(url);

	}

	public void refresh(View view) {
		postadapter.clear();
		new GetThread().execute(url);
	}

	private class GetThread extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_thr_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showPosts();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			joa = jo.getJSONArray("Thread");

			for (int i = 0; i < joa.length(); i++) {
				Post p = new Post(joa.getJSONObject(i));
				if (chron) {
					posts.add(p);
				} else {
					posts.add(0, p);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// fills ListView with Teams
	private void showPosts() {

		postadapter = new PostAdapter(this, R.layout.threaditem, posts);
		
		setListAdapter(postadapter);
		if (chron) {
			getListView().setSelection(posts.size() - 1);
		}

	}

	public void reply(View view) {
		if (de.damps.fantasy.HomeActivity.preferences.contains("token")) {
			Intent intent = new Intent(getApplicationContext(),
					NewPostActivity.class);
			intent.putExtra("ID",id);
			intent.putExtra("title", title);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte einloggen.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
}
