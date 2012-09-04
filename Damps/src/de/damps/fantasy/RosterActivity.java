package de.damps.fantasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RosterActivity extends Activity {

	private TableLayout tbl;
	private ArrayList<Player> roster;
	private int anzahl;

	private String url;
	private TextView team;
	private int position;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.roster);
		final Bundle extra = getIntent().getExtras();
		position = extra.getInt("pos");
		int id = de.damps.fantasy.HomeActivity.ID[position];
		url = de.damps.fantasy.HomeActivity.URL + "/roster/2012/" + id;
		tbl = (TableLayout) findViewById(R.id.tl_rostertable);
		team = (TextView) findViewById(R.id.tv_ros_view1);
		team.setText(de.damps.fantasy.HomeActivity.TEAMS[position]);

		new GetRoster().execute(url);
	}

	public void back(View view) {
		finish();
	}

	private class GetRoster extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_ros_bar1);
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
			roster.add(p);
		}
	}

	private void contructRoster() {

		int ori = getResources().getConfiguration().orientation;
		if (ori == 1) {
			for (int i = 0; i < anzahl; i++) {
				// Row
				TableRow newRow = new TableRow(getApplicationContext());
				TableLayout.LayoutParams parar = new TableLayout.LayoutParams();
				parar.setMargins(0, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
								.getDisplayMetrics()), 0, 0);
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
				newRow.addView(score, 3);
				// Team
				team.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
						.getChildAt(0)).getVirtualChildAt(0)).getWidth();
				//team.getLayoutParams().height = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
					//	.getChildAt(0)).getVirtualChildAt(0)).getHeight();

				// Pos
				

				pos.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
						.getChildAt(0)).getVirtualChildAt(1)).getWidth());
				pos.setLayoutParams(para);
				pos.setGravity(Gravity.CENTER);
				pos.setTextAppearance(getApplicationContext(), R.style.text);
				pos.setTextColor(getResources().getColor(R.color.weis));
				pos.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.button));

				// Name
				
				name.setLayoutParams(para);
				name.setTextAppearance(getApplicationContext(), R.style.text);

				// Score
				
				score.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
						.getChildAt(0)).getVirtualChildAt(3)).getWidth());
				score.setLayoutParams(para);
				score.setTextAppearance(getApplicationContext(), R.style.text);
				score.setGravity(Gravity.RIGHT);

				

				tbl.addView(newRow, i);
			}
		} else {
			for (int i = 0; i < anzahl; i++) {
				TableRow newRow = new TableRow(getApplicationContext());
				TextView pos = new TextView(getApplicationContext());
				TextView name = new TextView(getApplicationContext());
				TextView score = new TextView(getApplicationContext());
				LinearLayout gamedayScores = new LinearLayout(
						getApplicationContext());
				gamedayScores.setOrientation(LinearLayout.HORIZONTAL);
				LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
				for (int j = 0; j < 17; j++) {
					TextView gd = new TextView(getApplicationContext());
					gd.setLayoutParams(params);
					// TODO Gleiche Spaltenbreite
					gd.setPadding(2, 3, 2, 3);
					gd.setTextAppearance(getApplicationContext(), R.style.text);
					gamedayScores.addView(gd);
				}
				// newRow.addView(team);
				newRow.addView(pos, 0);
				newRow.addView(name, 1);
				newRow.addView(gamedayScores, 2);
				newRow.addView(score, 3);
				// team.setPadding(3, 3, 3, 3);
				pos.setPadding((int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
								.getDisplayMetrics()), 3, 3, 3);
				name.setPadding(3, 3, 0, 3);
				score.setPadding(3, 3, (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
								.getDisplayMetrics()), 3);
				pos.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
						.getChildAt(0)).getVirtualChildAt(0)).getWidth());
				score.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
						.getChildAt(0)).getVirtualChildAt(3)).getWidth());
				pos.setTextAppearance(getApplicationContext(), R.style.text);
				name.setTextAppearance(getApplicationContext(), R.style.text);
				score.setTextAppearance(getApplicationContext(), R.style.text);
				score.setGravity(Gravity.RIGHT);
				tbl.addView(newRow, i);
			}
		}
	}

	private void fillarray() {
		int ori = getResources().getConfiguration().orientation;
		if (ori == 1) {
			for (int i = 0; i < anzahl; i++) {
				if (i % 2 == 1) {
					tbl.getChildAt(i).setBackgroundColor(
							getResources().getColor(R.color.hellhellgrau));
				}
				int res = getResources().getIdentifier((roster.get(i).nfl_abr).toLowerCase(),
						"drawable", getPackageName());
				ImageView im = (ImageView) ((TableRow) tbl.getChildAt(i))
						.getVirtualChildAt(0);
				im.setImageResource(res);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
						.setText(roster.get(i).pos);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
						.setText(roster.get(i).name);
				String s = roster.get(i).summary;
				TableRow r = (TableRow) tbl.getChildAt(i);
				TextView t = (TextView) r.getVirtualChildAt(3);
				t.setText(s);
			}
		} else {
			int max = 0;
			for (int i = 0; i < anzahl; i++) {
				if (i % 2 == 1) {
					tbl.getChildAt(i).setBackgroundColor(
							getResources().getColor(R.color.hellhellgrau));
				}
				for (int j = 0; j < 17; j++) {

					((TextView) ((LinearLayout) ((TableRow) tbl.getChildAt(i))
							.getVirtualChildAt(2)).getChildAt(j))
							.setText(((Integer) roster.get(i).scores[j])
									.toString());

				}
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(0))
						.setText(roster.get(i).pos);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
						.setText(roster.get(i).name);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(3))
						.setText(roster.get(i).summary);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
						.measure(0, 0);
				int cur = ((TextView) ((TableRow) tbl.getChildAt(i))
						.getVirtualChildAt(1)).getMeasuredWidth();
				if (cur > max) {
					max = ((TextView) ((TableRow) tbl.getChildAt(i))
							.getVirtualChildAt(1)).getMeasuredWidth();
				}
			}
			((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_ros_table1))
					.getChildAt(0)).getVirtualChildAt(1)).setWidth(max);
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
