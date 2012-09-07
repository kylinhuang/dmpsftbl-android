package de.damps.fantasy.activities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.damps.fantasy.R;
import de.damps.fantasy.data.Json;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class StandingsActivity extends Activity {

	private String url;
	private String year;
	private String[] standings;
	private int[] points;
	private Spinner sp_ye;
	private TableLayout tbl;
	private ArrayAdapter<CharSequence> ad_ye;
	private boolean initialised = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.standings);
		url = de.damps.fantasy.activities.HomeActivity.URL + "/standings";
		constructStandings();

		new GetScores().execute(url);

	}
	
	public void back(View view){
		finish();
	}

	private class GetScores extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_sta_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			if (!initialised) {
				initializeSpinner();
				initialised = true;
			}
			fillTable();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	private void initializeSpinner() {
		sp_ye = (Spinner) findViewById(R.id.spi_sta_year);
		ad_ye = ArrayAdapter.createFromResource(this, R.array.year,
				android.R.layout.simple_spinner_item);
		ad_ye.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_ye.setAdapter(ad_ye);
		sp_ye.setSelection(ad_ye.getPosition(year));

		sp_ye.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (initialised) {
					year = (String) parent.getItemAtPosition(position);
					url = de.damps.fantasy.activities.HomeActivity.URL + "/standings/"
							+ year;
					new GetScores().execute(url);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	private void constructStandings() {
		tbl = ((TableLayout) findViewById(R.id.tl_sta_standings));
		for (int i = 0; i < de.damps.fantasy.activities.HomeActivity.NR; i++) {
			TableRow newRow = new TableRow(getApplicationContext());
			TextView pos = new TextView(getApplicationContext());
			TextView team = new TextView(getApplicationContext());
			TextView score = new TextView(getApplicationContext());
			newRow.addView(pos, 0);
			newRow.addView(team, 1);
			newRow.addView(score, 2);
			pos.setPadding(3, 3, 3, 3);
			team.setPadding(3, 3, 3, 3);
			score.setPadding(3, 3, 3, 3);
			score.setGravity(Gravity.RIGHT);
			pos.setTextColor(Color.BLACK);
			team.setTextColor(Color.BLACK);
			score.setTextColor(Color.BLACK);
			pos.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
			team.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
			score.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

			tbl.addView(newRow, i);
		}
	}

	private void fillTable() {
		for (int i = 0; i < de.damps.fantasy.activities.HomeActivity.NR; i++) {
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(0))
					.setText("");
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
					.setText("");
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
					.setText("");
		}
		for (int i = 0; i < standings.length; i++) {
			if (!standings[i].equals("null")) {
				if (i % 2 == 1) {
					tbl.getChildAt(i).setBackgroundColor(
							getResources().getColor(R.color.hellhellgrau));
				}
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(0))
						.setText((i + 1) + ".");
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
						.setText(standings[i]);
				((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
						.setText(Integer.toString(points[i]));
			}
		}
	}

	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;
		JSONArray joa = null;
		try {
			jo = jo.getJSONObject("Standings");
			joa = jo.getJSONArray("Standings");
			standings = new String[joa.length()];
			points = new int[joa.length()];
			year = Integer.toString(jo.getInt("season"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < standings.length; i++) {
			try {
				standings[i] = joa.getJSONObject(i).getString("name");
				points[i] = joa.getJSONObject(i).getInt("score");
			} catch (JSONException e) {
			}
		}
	}

}
