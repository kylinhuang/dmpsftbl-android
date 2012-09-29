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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.PostAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.Post;

public class ThreadActivity extends ListActivity {

	/*
	 * retrieves the thread
	 */
	private class GetThread extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			showPosts();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_thread_bar1);
			pb.setVisibility(View.VISIBLE);
		}

	}

	private String url;
	private ArrayList<Post> posts = new ArrayList<Post>();
	private PostAdapter postadapter;
	private TextView titleview;
	private String title;
	private boolean chron;

	private String id;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * initialize screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_thread_title)).setTypeface(font);

		final Bundle extra = getIntent().getExtras();

		id = extra.getString("ID");
		title = extra.getString("title");
		url = de.damps.fantasy.CommonUtilities.URL + "/thread/" + id;
		chron = de.damps.fantasy.CommonUtilities.preferences.getBoolean(
				"chron", false);

		titleview = (TextView) findViewById(R.id.tv_thread_subject);
		titleview.setText(title);

		setLongClickListener();
		new GetThread().execute();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread);

		inititalizeScreen();
	}

	/*
	 * retrieves the data
	 */
	private void parse() {
		DataGet data = new DataGet(url);
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

	/*
	 * refresh thread
	 */
	public void refresh(View view) {
		postadapter.clear();
		new GetThread().execute();
	}

	/*
	 * new post
	 */
	public void reply(View view) {
		if (de.damps.fantasy.CommonUtilities.preferences.contains("token")) {

			Intent intent = new Intent(getApplicationContext(),
					NewPostActivity.class);
			intent.putExtra("ID", id);
			intent.putExtra("title", title);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte einloggen.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	/*
	 * quote post
	 */
	private void setLongClickListener() {
		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
				String quote = "[quote]" + posts.get(pos).message
						+ "[/quote]\n\n";

				Intent intent = new Intent(getApplicationContext(),
						NewPostActivity.class);
				intent.putExtra("quote", quote);
				startActivity(intent);

				return true;
			}
		});

	}

	/*
	 * fill list
	 */
	private void showPosts() {
		postadapter = new PostAdapter(this, R.layout.threaditem, posts);
		setListAdapter(postadapter);
		if (chron) {
			getListView().setSelection(posts.size() - 1);
		}

	}
}
