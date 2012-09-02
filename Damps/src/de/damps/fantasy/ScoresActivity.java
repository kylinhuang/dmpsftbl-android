package de.damps.fantasy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ScoresActivity extends Activity {

	private String url;

	private Spinner sp_ye, sp_gd;
	private int year, gd;

	private String[] standings;
	private int[] points;
	private boolean y_init = false,gd_init = false,initialised = false;

	private TableLayout tbl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scores);
		url = de.damps.fantasy.HomeActivity.URL + "/weekly";
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
			pb = (ProgressBar) findViewById(R.id.pb_wee_bar1);
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
		// Spinner Season
		sp_ye = (Spinner) findViewById(R.id.spi_sco_year);
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
					url = de.damps.fantasy.HomeActivity.URL + "/weekly/" + year
							+ "/" + gd;
					new GetScores().execute(url);
				}
				y_init = true;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner Gameday
		sp_gd = (Spinner) findViewById(R.id.spi_sco_gameday);
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
					url = de.damps.fantasy.HomeActivity.URL + "/weekly/" + year
							+ "/" + (position + 1);
					new GetScores().execute(url);
				}
				gd_init = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	private void constructStandings() {
		tbl = (TableLayout) findViewById(R.id.tl_scores);
		for (int i = 0; i < de.damps.fantasy.HomeActivity.NR; i++) {
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
			tbl.addView(newRow);
		}
	}

	private void fillTable() {
		for (int i = 0; i < de.damps.fantasy.HomeActivity.NR; i++) {
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
					tbl.getChildAt(i).setBackgroundColor(getResources().getColor(
							R.color.hellhellgrau));
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
			jo = jo.getJSONObject("Weekly");
			joa = jo.getJSONArray("Score");
			standings = new String[joa.length()];
			points = new int[joa.length()];
			year = jo.getInt("season");
			gd = jo.getInt("gameday");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < standings.length; i++) {
			try {
				standings[i] = joa.getJSONObject(i).getString("name");
				points[i] = joa.getJSONObject(i).getInt("score");
				
			} catch (Exception e) {
			}
		}
	}
}
