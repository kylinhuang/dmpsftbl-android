package fantasyApp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.damps.fantasy.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ForumActivity extends ListActivity {

	private String url;
	private ArrayList<MyThread> threads = new ArrayList<MyThread>();
	private ThreadAdapter threadadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forum);
		String number = fantasyApp.HomeActivity.preferences.getString(
				"threads", "25");
		url = fantasyApp.HomeActivity.URL + "/forum/" + number;

		new GetThreads().execute(url);
	}
	
	public void refresh(View view){
		new GetThreads().execute(url);
	}

	private class GetThreads extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_for_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showThreads();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	//holt die Daten und parst sie
	private void parse() {
		Json data = new Json(url);
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

	//
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// creates Intent and fills with local Data
		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID", threads.get(position).id);
		intent.putExtra("title", threads.get(position).title);
		startActivity(intent);
	}

	// fills ListView with Teams
	private void showThreads() {
		threadadapter = new ThreadAdapter(this,
				R.layout.threaditem, threads);

		
		setListAdapter(threadadapter);
	}
	
	//Methode um neuen Thread zu erstellen
	public void newThread(View view){
		if(fantasyApp.HomeActivity.preferences.contains("token")){
			Intent intent = new Intent(getApplicationContext(),
					NewThreadActivity.class);
			startActivity(intent);
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "Bitte einloggen.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

}
