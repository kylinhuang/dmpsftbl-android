package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.Player;
import de.damps.fantasy.data.Row;

public class ReleaseActivity extends Activity {

	private TableLayout tbl;
	private ArrayList<Player> roster;
	private int anzahl;

	private String url;
	private String url_release;
	private TextView team;
	private String id;
	private SharedPreferences pref;
	private String dteam;
	private String oid;
	private String pid;
	private String rid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.release);

		inititalizeScreen();
	}

	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_release_title)).setTypeface(font);

		pref = de.damps.fantasy.activities.HomeActivity.preferences;
		oid = pref.getString("id", "X");
		id = de.damps.fantasy.activities.HomeActivity.league
				.getTeamidByOwnerid(oid);
		url = de.damps.fantasy.activities.HomeActivity.URL + "/roster/2012/"
				+ id;
		url_release = de.damps.fantasy.activities.HomeActivity.URL + "/release";

		tbl = (TableLayout) findViewById(R.id.tl_release_roster);
		team = (TextView) findViewById(R.id.tv_release_title);
		dteam = de.damps.fantasy.activities.HomeActivity.league
				.getTeamByOwnerid(oid);
		team.setText(dteam);

		new GetRoster().execute();
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * retrieve current roster
	 */
	private class GetRoster extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_release_bar1);
			pb.setVisibility(View.VISIBLE);

		};

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			contructRoster();
			fillarray();
			pb.setVisibility(View.INVISIBLE);

		}
	}

	/*
	 * release player
	 */
	private class ReleasePlayer extends AsyncTask<Void, Void, Void> {
		String[][] data = new String[2][2];

		@Override
		protected void onPreExecute() {
			data[0][0] = "player_id";
			data[0][1] = pid;
			data[1][0] = "roster_id";
			data[1][1] = rid;
		};

		@Override
		protected Void doInBackground(Void... v) {
			new DataPost(url_release,data);
			return null;
		}

		protected void onPostExecute(Void v) {
		}
	}

	/*
	 * retrieve data
	 */
	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;

		try {
			anzahl = jo.getJSONArray("Roster").length();
			getRoster(jo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// converts String to local Data
	private void getRoster(JSONObject data) throws JSONException {
		JSONArray joa = data.getJSONArray("Roster");
		roster = new ArrayList<Player>();

		for (int i = 0; i < anzahl; i++) {
			Player p = new Player(joa.getJSONObject(i));
			if (!p.contract.equals("R")) {
				roster.add(p);
			}
		}
	}

	/*
	 * construct roster
	 */
	private void contructRoster() {
		TableLayout head = (TableLayout) findViewById(R.id.tl_release_table1);
		Context c = getApplicationContext();
		for (int i = 0; i < roster.size(); i++) {

			TableRow newRow = new Row(c, "pos", head).newRow;

			pid = ((Integer) roster.get(i).player_id).toString();
			rid = ((Integer) roster.get(i).roster_id).toString();
			final String name1 = roster.get(i).name;

			newRow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(ReleaseActivity.this);
					dialog.setContentView(R.layout.confirm_release);
					dialog.setTitle("Confirm Release");
					dialog.setCancelable(true);

					String t = "Release " + name1 + "?\n";
					TextView tv = (TextView) dialog
							.findViewById(R.id.tv_confirm_view1);
					tv.setText(t);

					// set up button
					Button bu_cancel = (Button) dialog
							.findViewById(R.id.bu_cancel);
					Button bu_ok = (Button) dialog.findViewById(R.id.bu_ok);

					bu_ok.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new ReleasePlayer().execute();
							tbl.removeView(v);
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
			});
			tbl.addView(newRow, i);
		}

	}

	/*
	 * fill roster
	 */
	private void fillarray() {
		for (int i = 0; i < roster.size(); i++) {
			if (i % 2 == 1) {
				tbl.getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.hellhellgrau));
			}
			int res = getResources().getIdentifier(
					(roster.get(i).nfl_abr).toLowerCase(), "drawable",
					getPackageName());
			ImageView im = (ImageView) ((TableRow) tbl.getChildAt(i))
					.getVirtualChildAt(0);
			im.setImageResource(res);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
					.setText(roster.get(i).pos);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
					.setText(roster.get(i).name);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(3))
					.setText(roster.get(i).summary);
		}
	}

	public void sortTeam(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.nfl_abr.compareToIgnoreCase(rhs.nfl_abr);
			}
		};
		Collections.sort(roster, comp);
		fillarray();
	}

	public void sortPos(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.pos.compareToIgnoreCase(rhs.pos);
			}
		};
		Collections.sort(roster, comp);
		fillarray();
	}

	public void sortName(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.name.compareToIgnoreCase(rhs.name);
			}
		};
		Collections.sort(roster, comp);
		fillarray();
	}

	public void sortScore(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				if (lhs.total < rhs.total) {
					return 1;
				} else if (lhs.total > rhs.total) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		Collections.sort(roster, comp);
		fillarray();
	}

}
