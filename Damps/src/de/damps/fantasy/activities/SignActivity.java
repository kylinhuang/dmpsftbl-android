package de.damps.fantasy.activities;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.PlayerAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.Player;

public class SignActivity extends ListActivity {

	private PlayerAdapter playeradapter;
	private ArrayList<Player> player;
	private String url;
	private String url_player;
	private ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_player);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) findViewById(R.id.sv_sign_search_name);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			/*String query = intent.getStringExtra(SearchManager.QUERY);
			player = intent.getParcelableArrayListExtra("players");
			player = searchPlayers(query);
			listView = getListView();
			listView.removeAllViews();
			showPlayers();*/
		} else {
			inititalizeScreen();
		}
	}

	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_sign_title)).setTypeface(font);

		url = de.damps.fantasy.CommonUtilities.URL + "/freeagents/";
		url_player = url +"qb";
		
		new GetPlayers().execute();
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	private class GetPlayers extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_sign_bar1);
			pb.setVisibility(View.VISIBLE);
			player = new ArrayList<Player>();
		};

		@Override
		protected Void doInBackground(Void... v) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			showPlayers();
			pb.setVisibility(View.INVISIBLE);
		}
	}

	private void parse() {
		DataGet data = new DataGet(url_player);
		JSONObject jo = data.data;
		JSONArray joa = null;

		try {
			joa = jo.getJSONArray("Freeagents");
			
			for (int i = 0; i < joa.length(); i++) {
				player.add(new Player(joa.getJSONObject(i)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showPlayers() {
		playeradapter = new PlayerAdapter(this, R.layout.playeritem, player);
		setListAdapter(playeradapter);
	}
	
	@Override
	public boolean onSearchRequested() {
	     Bundle appData = new Bundle();
	     appData.putParcelableArrayList("players", player);
	     startSearch(null, false, appData, false);
	     return true;
	 }

	private ArrayList<Player> searchPlayers(String subName) {
		ArrayList<Player> players = player;
		for (int i = 0; i < player.size(); i++) {
			Player p = players.get(i);
			if (!p.name.contains(subName)) {
				players.remove(p);
			}
		}
		return player;
	}

	public void getQb(View view) {
		url_player = url + "qb";
		getNewPos();
	}

	public void getRb(View view) {
		url_player = url + "rb";
		getNewPos();
	}

	public void getWr(View view) {
		url_player = url + "wr";
		getNewPos();
	}

	public void getTe(View view) {
		url_player = url + "te";
		getNewPos();
	}

	public void getK(View view) {
		url_player = url + "k";
		getNewPos();
	}

	public void getDef(View view) {
		url_player = url + "def";
		getNewPos();
	}
	
	private void getNewPos(){
		playeradapter.clear();
		playeradapter.notifyDataSetChanged();
		new GetPlayers().execute();
	}
}
