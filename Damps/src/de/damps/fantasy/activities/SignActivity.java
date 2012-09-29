package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.PlayerAdapter;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Player;

public class SignActivity extends ListActivity {

	private class GetPlayers extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(Void... v) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			showPlayers();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_sign_bar1);
			pb.setVisibility(View.VISIBLE);
			player = new ArrayList<Player>();
		}
	}

	private class SignPlayer extends AsyncTask<Integer, Void, String> {
		String[][] data = new String[2][2];

		@Override
		protected String doInBackground(Integer... i) {
			data[0][0] = "id";
			data[0][1] = ((Integer) player.get((i[0])).player_id).toString();
			data[1][0] = "nflteam_id";
			data[1][1] = ((Integer) player.get((i[0])).nfl_id).toString();

			String response = new DataPost(url_sign, data).response;
			return response;
		};

		@Override
		protected void onPreExecute() {
		}
	}

	private class SignPlayerFromRelease extends AsyncTask<Player, Void, String> {
		String[][] data = new String[2][2];

		@Override
		protected String doInBackground(Player... p) {
			data[0][0] = "id";
			data[0][1] = ((Integer) p[0].player_id).toString();
			data[1][0] = "nflteam_id";
			data[1][1] = ((Integer) p[0].nfl_id).toString();

			String response = new DataPost(url_sign, data).response;
			return response;
		};

		@Override
		protected void onPreExecute() {
		}
	}

	private PlayerAdapter playeradapter;
	private ArrayList<Player> player;
	private String url;
	private String url_player;
	private String url_sign;

	private SearchView searchView;

	private SearchManager searchManager;

	private Player todelete;

	private void askForRelease(final Player p) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.freespot);
		dialog.setTitle("Fehler beim signen");
		dialog.setCancelable(true);

		// set up button
		Button bu_cancel = (Button) dialog
				.findViewById(R.id.bu_freespot_cancel);
		Button bu_ok = (Button) dialog.findViewById(R.id.bu_freespot_ok);

		bu_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				todelete = p;
				Intent intent = new Intent(getApplicationContext(),
						ReleaseActivity.class);
				intent.putExtra("player", p);
				startActivityForResult(intent, 1);
				dialog.dismiss();
			}
		});
		bu_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	private void confirmSign(String name) {
		player.remove(todelete);
		showPlayers();

		Toast toast = Toast.makeText(getApplicationContext(), name
				+ " erfolgreich gesigned.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void getDef(View view) {
		url_player = url + "def";
		getNewPos();
	}

	public void getK(View view) {
		url_player = url + "k";
		getNewPos();
	}

	private void getNewPos() {
		playeradapter.clear();
		playeradapter.notifyDataSetChanged();
		new GetPlayers().execute();
	}

	public void getQb(View view) {
		url_player = url + "qb";
		getNewPos();
	}

	public void getRb(View view) {
		url_player = url + "rb";
		getNewPos();
	}

	public void getTe(View view) {
		url_player = url + "te";
		getNewPos();
	}

	public void getWr(View view) {
		url_player = url + "wr";
		getNewPos();
	}

	private void handleIntent(Intent intent) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			setContentView(R.layout.sign_player11);

			searchView = (SearchView) findViewById(R.id.sv_sign_search_name);
			searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
			searchView.setFocusable(false);
			searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
			searchView.setOnQueryTextListener(new OnQueryTextListener() {

				@Override
				public boolean onQueryTextChange(String newText) {
					return false;
				}

				@Override
				public boolean onQueryTextSubmit(String query) {
					Bundle appData = new Bundle();
					appData.putParcelableArrayList("players", player);
					searchManager
							.startSearch(null, false, null, appData, false);
					return false;
				}
			});

			// Get the intent, verify the action and get the query
			if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
				String query = intent.getStringExtra(SearchManager.QUERY);
				player = searchPlayers(query);
				showPlayers();
			} else {
				initializeScreen();
				new GetPlayers().execute();
			}
		} else {
			setContentView(R.layout.sign_player);
			initializeScreen();
			new GetPlayers().execute();
		}
	}

	/*
	 * init screen
	 */
	private void initializeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_sign_title)).setTypeface(font);

		url = de.damps.fantasy.CommonUtilities.URL + "/freeagents/";
		url_player = url + "qb";
		url_sign = de.damps.fantasy.CommonUtilities.URL + "/signfreeagents";

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			Bundle bundle = data.getExtras();
			Player p = bundle.getParcelable("player");
			String response = null;

			try {
				response = new SignPlayerFromRelease().execute(p).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			if (response.equals("false")) {
				askForRelease(p);
			} else {
				confirmSign(p.name);
			}
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handleIntent(getIntent());
	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {
		super.onListItemClick(l, v, position, id);
		String response = null;
		todelete = player.get(position);
		try {
			response = new SignPlayer().execute(position).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (response.equals("false")) {
			askForRelease(player.get(position));
		} else {
			confirmSign(player.get(position).name);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public boolean onSearchRequested() {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			searchView.requestFocus();
		}
		return true;
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

	private ArrayList<Player> searchPlayers(String subName) {
		ArrayList<Player> oldList = player;
		ArrayList<Player> newList = new ArrayList<Player>();
		for (int i = 0; i < oldList.size(); i++) {
			Player p = oldList.get(i);
			if (p.name.toLowerCase().contains(subName.toLowerCase())) {
				newList.add(p);
			}
		}
		return newList;
	}

	private void showPlayers() {
		playeradapter = new PlayerAdapter(this, R.layout.playeritem, player);
		setListAdapter(playeradapter);
		if (((RadioButton) findViewById(R.id.rb_sign_sortname)).isChecked()) {
			sortName(null);
		} else {
			sortTeam(null);
		}
	}

	public void sortName(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.name.compareToIgnoreCase(rhs.name);
			}
		};
		Collections.sort(player, comp);
		playeradapter.notifyDataSetChanged();
	}

	public void sortTeam(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				if (lhs.nfl_abr == null || lhs.nfl_abr.equals("-")) {
					return 1;
				}
				if (rhs.nfl_abr == null || rhs.nfl_abr.equals("-")) {
					return -1;
				}
				return lhs.nfl_abr.compareToIgnoreCase(rhs.nfl_abr);
			}
		};
		Collections.sort(player, comp);
		playeradapter.notifyDataSetChanged();
	}
}
