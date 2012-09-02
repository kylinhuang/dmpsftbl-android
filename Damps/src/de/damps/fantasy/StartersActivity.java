package de.damps.fantasy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StartersActivity extends Activity {

	private String url;
	private boolean y_init = false,gd_init = false,initialised = false;
	private Spinner sp_ye, sp_gd;
	private Team[] starters = new Team[14];
	private int year;
	private int gd;
	private TableLayout tbl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starters);
		url = de.damps.fantasy.HomeActivity.URL + "/starter/2011/17";
		constructStarters();

		new GetStarters().execute(url);
	}

	private class GetStarters extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_start_bar1);
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

	private void parse() {
		Json data = new Json(url);
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

		for (int i = 0; i < 14; i++) {
			try {
				starters[i] = new Team(joa.getJSONObject(i));
			} catch (JSONException e) {
				starters[i] = new Team();
			}
		}

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
					url = de.damps.fantasy.HomeActivity.URL + "/starter/"
							+ year + "/" + gd;
					new GetStarters().execute(url);
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
					url = de.damps.fantasy.HomeActivity.URL + "/starter/"
							+ year + "/" + (position + 1);
					new GetStarters().execute(url);
				}
				gd_init = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	public void fillTable() {
		for (int i = 0; i < 14; i++) {
			TextView text = (TextView) ((TableRow) tbl.getChildAt(i * 10))
					.getVirtualChildAt(0);
			text.setText((starters[i].team));
			for (int j = 0; j < 9; j++) {
				((TextView) ((TableRow) tbl.getChildAt(i * 10 + j + 1))
						.getVirtualChildAt(0))
						.setText(starters[i].starters[j].pos);
				((TextView) ((TableRow) tbl.getChildAt(i * 10 + j + 1))
						.getVirtualChildAt(1))
						.setText(starters[i].starters[j].nfl_abr);
				((TextView) ((TableRow) tbl.getChildAt(i * 10 + j + 1))
						.getVirtualChildAt(2))
						.setText(starters[i].starters[j].name);
			}
		}
	}

	public void constructStarters() {
		tbl = (TableLayout) findViewById(R.id.tl_start_starting);
		for (int i = 0; i < 14; i++) {
			TableRow.LayoutParams params = new TableRow.LayoutParams();
			TableRow teamRow = new TableRow(getApplicationContext());
			TextView ffteam = new TextView(getApplicationContext());
			params.span = 3;
			teamRow.addView(ffteam, 0);
			ffteam.setPadding(3, 3, 3, 3);
			ffteam.setTextColor(getResources().getColor(R.color.damps_blau));
			ffteam.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
			teamRow.setBackgroundColor(getResources()
					.getColor(R.color.hellgrau));

			tbl.addView(teamRow, params);
			for (int j = 0; j < 9; j++) {
				TableRow PlayerRow = new TableRow(getApplicationContext());
				TextView pos = new TextView(getApplicationContext());
				TextView team = new TextView(getApplicationContext());
				TextView name = new TextView(getApplicationContext());
				PlayerRow.addView(pos, 0);
				PlayerRow.addView(team, 1);
				PlayerRow.addView(name, 2);
				pos.setPadding(3, 3, 3, 3);
				team.setPadding(3, 3, 3, 3);
				name.setPadding(3, 3, 3, 3);
				pos.setTextColor(Color.BLACK);
				team.setTextColor(Color.BLACK);
				name.setTextColor(Color.BLACK);
				pos.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				team.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
				tbl.addView(PlayerRow);
			}
		}
	}

}
