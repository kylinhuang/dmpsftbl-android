package de.damps.fantasy.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.Player;
import de.damps.fantasy.data.Row;
import de.damps.fantasy.data.Team;

public class StartersActivity extends Activity {

	private class GetStarters extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			if (!initialised) {
				initializeSpinner();
				initialised = true;
			}
			constructStarters();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			teams = new ArrayList<Team>();
			pb = (ProgressBar) findViewById(R.id.pb_start_bar1);
			pb.setVisibility(View.VISIBLE);
		}

	}

	private String url;
	private boolean y_init = false, gd_init = false, initialised = false;
	private Spinner sp_ye, sp_gd;
	private ArrayList<Team> teams;
	private int year;
	private int gd;
	private TableLayout tbl;
	private TableLayout tbll;

	private TableLayout tblr;

	public void back(View view) {
		finish();
	}

	public void constructStarters() {
		int ori = getResources().getConfiguration().orientation;
		if (ori == 1) {
			tbl = (TableLayout) findViewById(R.id.tl_start_starting);

			for (int i = 0; i < teams.size(); i++) {
				TableRow teamRow = new Row(getApplicationContext(),
						teams.get(i).team, teams.get(i).flex).newRow;
				tbl.addView(teamRow);
				for (int j = 0; j < 8; j++) {
					tbl.addView(createPlayer(i, j));
				}

			}
			tbl.setStretchAllColumns(true);
		} else {
			tbll = (TableLayout) findViewById(R.id.tl_start_starting_left);
			tblr = (TableLayout) findViewById(R.id.tl_start_starting_right);

			for (int i = 0; i < teams.size(); i++) {
				if (i % 2 == 0) {
					TableRow teamRow = new Row(getApplicationContext(),
							teams.get(i).team, teams.get(i).flex).newRow;
					tbll.addView(teamRow);

					for (int j = 0; j < 8; j++) {
						tbll.addView(createPlayer(i, j));
					}
					tbll.setStretchAllColumns(true);
				} else {
					TableRow teamRow = new Row(getApplicationContext(),
							teams.get(i).team, teams.get(i).flex).newRow;
					tblr.addView(teamRow);
					for (int j = 0; j < 8; j++) {
						tblr.addView(createPlayer(i, j));
					}

					tblr.setStretchAllColumns(true);
				}
			}

		}

	}

	private TableRow createPlayer(int i, int j) {
		TableRow newRow = new Row(getApplicationContext()).newRow;
		Player p = teams.get(i).starters.get(j);
		int res = getResources().getIdentifier((p.nfl_abr).toLowerCase(),
				"drawable", getPackageName());
		((ImageView) newRow.getVirtualChildAt(0)).setImageResource(res);
		((TextView) newRow.getVirtualChildAt(1)).setText(p.pos);
		((TextView) newRow.getVirtualChildAt(2)).setText(p.name);

		return newRow;
	}

	private void initializeSpinner() {
		// Spinner Season
		sp_ye = (Spinner) findViewById(R.id.spi_start_year);
		ArrayAdapter<CharSequence> ad_ye = ArrayAdapter.createFromResource(
				this, R.array.year, android.R.layout.simple_spinner_item);
		ad_ye.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_ye.setAdapter(ad_ye);
		int pos = ad_ye.getPosition(((Integer) year).toString());
		sp_ye.setSelection(pos);

		sp_ye.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (y_init) {
					int gd = sp_gd.getSelectedItemPosition() + 1;
					String year = (String) parent.getItemAtPosition(position);
					url = de.damps.fantasy.CommonUtilities.URL + "/starter/"
							+ year + "/" + gd;
					if (tbl != null) {
						tbl.removeAllViews();
					}
					if (tbll != null) {
						tbll.removeAllViews();
					}
					if (tblr != null) {
						tblr.removeAllViews();
					}
					new GetStarters().execute();
				}
				y_init = true;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner Gameday
		sp_gd = (Spinner) findViewById(R.id.spi_start_gameday);
		ArrayAdapter<CharSequence> ad_gd = ArrayAdapter.createFromResource(
				this, R.array.gameday, android.R.layout.simple_spinner_item);
		ad_gd.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_gd.setAdapter(ad_gd);
		int pos1 = ad_gd.getPosition("Gameday " + ((Integer) gd).toString());
		sp_gd.setSelection(pos1);

		sp_gd.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (gd_init) {
					String year = (String) sp_ye.getSelectedItem();
					url = de.damps.fantasy.CommonUtilities.URL + "/starter/"
							+ year + "/" + (position + 1);

					if (tbl != null) {
						tbl.removeAllViews();
					}
					if (tbll != null) {
						tbll.removeAllViews();
					}
					if (tblr != null) {
						tblr.removeAllViews();
					}
					new GetStarters().execute();
				}
				gd_init = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starters);
		url = de.damps.fantasy.CommonUtilities.URL + "/starter/";

		new GetStarters().execute();
	}

	private void parse() {
		DataGet data = new DataGet(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {

			joa = jo.getJSONArray("Starter");
			year = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONObject("Teamstarter").getInt("season");
			gd = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONObject("Teamstarter").getInt("gameday");
		} catch (JSONException e) {
			// TODO keine starter
			e.printStackTrace();
		}

		for (int i = 0; i < joa.length(); i++) {
			try {
				teams.add(new Team(joa.getJSONObject(i)));
			} catch (JSONException e) {
				Log.i("Starter", "Keine Teams aufgestellt");
			}
		}

	}

}
