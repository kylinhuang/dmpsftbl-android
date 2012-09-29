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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.ThreadAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.MyThread;

public class ForumActivity extends ListActivity {

	private class GetThreads extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			showThreads();
			new LoadList().execute();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_forum_bar1);
			pb.setVisibility(View.VISIBLE);
		}
	}
	private class LoadList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			long ftime = System.currentTimeMillis() + 7200000;
			de.damps.fantasy.activities.HomeActivity.editor.putLong("ftime",
					ftime);
			de.damps.fantasy.activities.HomeActivity.editor.commit();
		}
	}
	private String url;

	private ArrayList<MyThread> threads = new ArrayList<MyThread>();

	private ThreadAdapter threadadapter;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * initializes Screen
	 */
	private void inititaliseScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_forum_title)).setTypeface(font);

		String number = de.damps.fantasy.CommonUtilities.preferences.getString(
				"threads", "25");
		url = de.damps.fantasy.CommonUtilities.URL + "/forum/" + number;

		new GetThreads().execute();
	}

	/*
	 * create new thread
	 */
	public void newThread(View view) {
		if (de.damps.fantasy.CommonUtilities.preferences.contains("token")) {

			Intent intent = new Intent(getApplicationContext(),
					NewThreadActivity.class);
			startActivity(intent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte einloggen.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum);

		inititaliseScreen();
	}

	/*
	 * click onlistitem
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID", threads.get(position).id);
		intent.putExtra("title", threads.get(position).title);

		startActivity(intent);
	}

	/*
	 * retrieves data
	 */
	private void parse() {
		DataGet data = new DataGet(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			joa = jo.getJSONArray("Forum");

			for (int i = 0; i < joa.length(); i++) {
				MyThread t = new MyThread(joa.getJSONObject(i));
				threads.add(t);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*
	 * refresh thread
	 */
	public void refresh(View view) {
		new GetThreads().execute();
	}

	/*
	 * fill list
	 */
	private void showThreads() {
		threadadapter = new ThreadAdapter(this, R.layout.threaditem, threads);

		setListAdapter(threadadapter);

	}

}
