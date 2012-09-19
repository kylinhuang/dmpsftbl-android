package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.Player;
import de.damps.fantasy.data.Row;

public class RosterActivity extends Activity {

	private TableLayout tbl;
	private ArrayList<Player> roster;
	private int anzahl;

	private String url;
	private TextView team;
	private String dteam;
	private String id;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);

		initializeScreen();
	}

	/*
	 * init screen
	 */
	public void initializeScreen() {
		final Bundle extra = getIntent().getExtras();
		if(extra.containsKey("team")){
			dteam = extra.getString("team");
			id = de.damps.fantasy.activities.HomeActivity.league
					.getTeamidByTeam(dteam);
		}else{
			String oid = extra.getString("oid");
			id = de.damps.fantasy.activities.HomeActivity.league
					.getTeamidByOwnerid(oid);
		}
		
		url = de.damps.fantasy.activities.HomeActivity.URL + "/roster/2012/"
				+ id;
		tbl = (TableLayout) findViewById(R.id.tl_roster_roster);
		team = (TextView) findViewById(R.id.tv_roster_title);
		team.setText(dteam);

		new GetRoster().execute(url);
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * show first half of season
	 */
	public void showHalf1(View view) {
		for (int j = 0; j < 17; j++) {
			if (j < 9) {
				tbl.setColumnCollapsed(3 + j, false);
			} else {
				tbl.setColumnCollapsed(3 + j, true);
			}
		}
	}

	/*
	 * show second half of season
	 */
	public void showHalf2(View view) {
		for (int j = 0; j < 17; j++) {
			if (j < 9) {
				tbl.setColumnCollapsed(3 + j, true);
			} else {
				tbl.setColumnCollapsed(3 + j, false);
			}
		}
	}

	/*
	 * retrieve roster
	 */
	private class GetRoster extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_roster_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
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
		int ori = getResources().getConfiguration().orientation;
		TableLayout head = (TableLayout) findViewById(R.id.tl_roster_table1);
		Context c = getApplicationContext();
		if (ori == 1) {
			for (int i = 0; i < roster.size(); i++) {
				TableRow newRow = new Row(c,"pos",head).newRow;
				tbl.addView(newRow, i);
			}
		} else {
			for (int i = 0; i < roster.size() + 1; i++) {
				if (i == 0) {
					// Row
					TableRow newRow = new TableRow(getApplicationContext());
					TableLayout.LayoutParams parar = new TableLayout.LayoutParams();
					parar.setMargins(0, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
									.getDisplayMetrics()), 0, (int) TypedValue
							.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
									getResources().getDisplayMetrics()));
					newRow.setLayoutParams(parar);

					TableRow.LayoutParams para = new TableRow.LayoutParams();
					para.setMargins((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
									.getDisplayMetrics()), 0, 0, 0);

					int p = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
									.getDisplayMetrics());
					TextView team = new TextView(getApplicationContext());
					TextView pos = new TextView(getApplicationContext());
					TextView name = new TextView(getApplicationContext());
					TextView score = new TextView(getApplicationContext());
					newRow.addView(team, 0);
					newRow.addView(pos, 1);
					newRow.addView(name, 2);

					// Team
					team.setText("NFL");
					team.setTextAppearance(getApplicationContext(),
							R.style.column);
					team.setTextColor(getResources().getColor(R.color.weis));
					team.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.column_left));
					team.setPadding(p, p, p, p);
					// Pos
					pos.setText("Pos");
					pos.setLayoutParams(para);
					pos.setTextAppearance(getApplicationContext(),
							R.style.column);
					pos.setTextColor(getResources().getColor(R.color.weis));
					pos.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.column_mid));
					pos.setPadding(p, p, p, p);

					// Name
					name.setText("Name");
					name.setLayoutParams(para);
					name.setTextAppearance(getApplicationContext(),
							R.style.column);
					name.setTextColor(getResources().getColor(R.color.weis));
					name.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.column_mid));
					name.setPadding(p, p, p, p);

					// gameday
					for (int j = 0; j < 17; j++) {
						TextView gd = new TextView(getApplicationContext());
						newRow.addView(gd, 3 + j);
						String s = ((Integer) (j + 1)).toString();
						gd.setText(s);
						gd.setLayoutParams(para);
						gd.setTextAppearance(getApplicationContext(),
								R.style.column);
						gd.setTextColor(getResources().getColor(R.color.weis));
						gd.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.column_mid));
						gd.setPadding(p, p, p, p);
						gd.setWidth((int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
										.getDisplayMetrics()));
						gd.setGravity(Gravity.CENTER);
					}

					// Score
					newRow.addView(score, 20);
					score.setText("Score");
					score.setLayoutParams(para);
					score.setTextAppearance(getApplicationContext(),
							R.style.column);
					score.setTextColor(getResources().getColor(R.color.weis));
					score.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.column_right));
					score.setPadding(p, p, p, p);
					tbl.addView(newRow, i);
				} else {
					// Row
					TableRow newRow = new TableRow(getApplicationContext());
					TableLayout.LayoutParams parar = new TableLayout.LayoutParams();
					parar.setMargins(0, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
									.getDisplayMetrics()), 0, (int) TypedValue
							.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
									getResources().getDisplayMetrics()));
					newRow.setLayoutParams(parar);

					TableRow.LayoutParams para = new TableRow.LayoutParams();
					para.setMargins((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
									.getDisplayMetrics()), 0, 0, 0);

					ImageView team = new ImageView(getApplicationContext());
					TextView pos = new TextView(getApplicationContext());
					TextView name = new TextView(getApplicationContext());
					TextView score = new TextView(getApplicationContext());
					newRow.addView(team, 0);
					newRow.addView(pos, 1);
					newRow.addView(name, 2);

					// Team

					// Pos
					pos.setLayoutParams(para);
					pos.setTextAppearance(getApplicationContext(), R.style.text);
					pos.setTextColor(getResources().getColor(R.color.weis));
					pos.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button));
					pos.setGravity(Gravity.CENTER);

					// Name
					name.setLayoutParams(para);
					name.setTextAppearance(getApplicationContext(),
							R.style.text);
					name.setPadding((int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
									.getDisplayMetrics()), 0, 0, 0);

					// gameday
					for (int j = 0; j < 17; j++) {
						TextView gd = new TextView(getApplicationContext());
						newRow.addView(gd, 3 + j);
						gd.setLayoutParams(para);
						gd.setTextAppearance(getApplicationContext(),
								R.style.text);
						gd.setGravity(Gravity.CENTER);
					}

					// Score
					newRow.addView(score, 20);
					score.setLayoutParams(para);
					score.setTextAppearance(getApplicationContext(),
							R.style.text);
					score.setGravity(Gravity.RIGHT);
					score.setPadding(0, 0, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
									.getDisplayMetrics()), 0);
					tbl.addView(newRow, i);
				}
				for (int j = 9; j < 17; j++) {
					tbl.setColumnCollapsed(3 + j, true);
				}
			}
		}
	}

	/*
	 * fill roster
	 */
	private void fillarray() {
		int ori = getResources().getConfiguration().orientation;
		if (ori == 1) {
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
		} else {
			for (int i = 1; i < roster.size() + 1; i++) {
				// grey
				if (i % 2 == 0) {
					tbl.getChildAt(i).setBackgroundColor(
							getResources().getColor(R.color.hellhellgrau));
				}

				// team
				int res = getResources().getIdentifier(
						(roster.get(i - 1).nfl_abr).toLowerCase(), "drawable",
						getPackageName());
				ImageView im = (ImageView) ((TableRow) tbl.getChildAt(i))
						.getVirtualChildAt(0);
				im.setImageResource(res);

				// pos
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
						.setText(roster.get(i - 1).pos);

				// name
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
						.setText(roster.get(i - 1).name);

				// gameday
				for (int j = 0; j < 17; j++) {
					((TextView) ((TableRow) tbl.getChildAt(i))
							.getVirtualChildAt(3 + j))
							.setText(((Integer) roster.get(i - 1).scores[j])
									.toString());
				}

				// score
				((TextView) ((TableRow) tbl.getChildAt(i))
						.getVirtualChildAt(20))
						.setText(roster.get(i - 1).summary);
			}
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
