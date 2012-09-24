package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.PlayerAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Player;

public class SignActivity extends ListActivity {

	private PlayerAdapter playeradapter;
	private ArrayList<Player> player;
	private String url;
	private String url_player;
	private String url_sign;

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
		searchView.setFocusable(false);
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Bundle bundle = intent.getBundleExtra(SearchManager.APP_DATA);
			player = bundle.getParcelableArrayList("players");
			player = searchPlayers(query);
			showPlayers();
		} else {
			initializeScreen();
			new GetPlayers().execute();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			Bundle bundle = data.getExtras();
			Player p = bundle.getParcelable("player");
			new SignPlayerFromRelease().execute(p);
			initializeScreen();
		}
	}

	/*
	 * init screen
	 */
	private void initializeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_sign_title)).setTypeface(font);

		url = de.damps.fantasy.CommonUtilities.URL + "/freeagents/";
		url_player = url +"qb";
		url_sign = de.damps.fantasy.CommonUtilities.URL + "/signfreeagents";		
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
	
	private class SignPlayer extends AsyncTask<Integer, Void, String> {
		String[][] data = new String[2][2];
		@Override
		protected void onPreExecute() {
		};

		@Override
		protected String doInBackground(Integer... i) {
			data[0][0] = "id";
			data[0][1] = ((Integer)player.get(((int)i[0])).player_id).toString();
			data[1][0] = "nftlteam_id";
			data[1][1] = ((Integer)player.get(((int)i[0])).nfl_id).toString();
			
			String response = new DataPost(url_sign, data).response;
			return response;
		}
	}
	
	private class SignPlayerFromRelease extends AsyncTask<Player, Void, String> {
		String[][] data = new String[2][2];
		@Override
		protected void onPreExecute() {
		};

		@Override
		protected String doInBackground(Player... p) {
			data[0][0] = "id";
			data[0][1] = ((Integer)p[0].player_id).toString();
			data[1][0] = "nftlteam_id";
			data[1][1] = ((Integer)p[0].nfl_id).toString();
			
			String response = new DataPost(url_sign, data).response;
			return response;
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
	
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		String response = null;
		
		try {
			response = new SignPlayer().execute(position).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if(response.equals("false")){
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.freespot);
			dialog.setTitle("Fehler beim signen");
			dialog.setCancelable(true);

			// set up button
			Button bu_cancel = (Button) dialog.findViewById(R.id.bu_freespot_cancel);
			Button bu_ok = (Button) dialog.findViewById(R.id.bu_freespot_ok);

			bu_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ReleaseActivity.class);
					intent.putExtra("player", player.get(position));
					startActivityForResult(intent, 1);
				}
			});
			bu_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();
		}else{
			
		}
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
