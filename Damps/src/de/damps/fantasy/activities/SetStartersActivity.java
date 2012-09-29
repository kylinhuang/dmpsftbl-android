package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import de.damps.fantasy.adapter.PosComparator;
import de.damps.fantasy.data.DataGet;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Player;
import de.damps.fantasy.data.Row;

public class SetStartersActivity extends Activity {

	/*
	 * retrieve team
	 */
	private class GetTeam extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		};

		@Override
		protected void onPostExecute(Void v) {
			contructTeam();

			if (!init_gd) {
				initializeSpinner();
			}
			sp_for.setSelection(my_flex_for);
			sp_for.setClickable((flex_possible | flex));
			fillStarter();
			fillBench();
			adjustHeader();
			pb.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_setstarters_bar1);
			pb.setVisibility(View.VISIBLE);
		}
	}
	/*
	 * start player
	 */
	private class SendBench extends AsyncTask<Void, Void, Void> {
		String[][] data = new String[1][2];

		@Override
		protected Void doInBackground(Void... params) {
			new DataPost(url_bench, data);
			return null;
		};

		@Override
		protected void onPreExecute() {
			data[0][0] = "starter_id";
			data[0][1] = starter_id.toString();
		}

	}
	private class SendFormation extends AsyncTask<Void, Void, Void> {
		String[][] data = new String[2][2];

		@Override
		protected Void doInBackground(Void... v) {
			new DataPost(url_formation, data);
			return null;
		};

		@Override
		protected void onPreExecute() {
			data[0][0] = "is_flex";
			data[0][1] = ((Integer) formations[my_flex_for]).toString();
			data[1][0] = "id";
			data[1][1] = ((Integer) teamstarter_id).toString();
		}

	}
	/*
	 * start player
	 */
	private class SendStarter extends AsyncTask<String, Void, Boolean> {
		String[][] data = new String[4][2];

		@Override
		protected Boolean doInBackground(String... params) {
			data[0][0] = "season";
			data[0][1] = params[0];
			data[1][0] = "gameday";
			data[1][1] = params[1];
			data[2][0] = "position";
			data[2][1] = params[2];
			data[3][0] = "roster_id";
			data[3][1] = params[3];
			String response = new DataPost(url_start, data).response;
			if (!response.equals("true")) {
				starter_id = Integer.valueOf(response);
			}
			return true;
		};

		@Override
		protected void onPreExecute() {
		}

	}

	private Spinner sp_gd, sp_for;
	private TableLayout tbl;
	private ArrayList<Player> bench;
	private ArrayList<Player> starter;

	private String url;
	private String url_start;

	private String url_bench;
	private String url_formation;
	private TextView team;

	private TableLayout head;
	private SharedPreferences pref;
	private String token;

	private String hash;
	private String dteam;
	private String oid;

	private Integer starter_id;
	private int gd;
	private boolean firstgd;
	private boolean init_gd = false;
	private boolean flex_possible;
	private boolean flex;
	private int st_i1;
	private int st_i2;
	private int f_i1;
	private int f_i2;
	private String f_p1 = null;
	private String f_p2 = null;
	private String st_p1;
	private String st_p2;

	private int flex_for;

	private int my_flex_for;

	private int[] formations = { 0, 311, 131, 122, 212, 113 };

	private int teamstarter_id;

	private void adjustHeader() {
		((TextView) ((TableRow) tbl.getChildAt(8)).getVirtualChildAt(3))
				.measure(0, 0);
		int width = ((TextView) ((TableRow) tbl.getChildAt(8))
				.getVirtualChildAt(3)).getMeasuredWidth();
		((TextView) ((TableRow) head.getChildAt(0)).getVirtualChildAt(3))
				.setWidth(width);

	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	private OnClickListener benchListener() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				startPlayer((TableRow) v);
			}

		};
		return listener;
	}

	private void benchPlayer(TableRow tableRow) {
		long time = System.currentTimeMillis() - 3600000;

		if (time < (Long) tableRow.getTag(R.string.kickoff)) {
			TableRow benchRow = tableRow;
			benchRow.setTag(true);
			benchRow.setOnClickListener(benchListener());
			tbl.removeView(tableRow);
			tbl.addView(benchRow);
			movePlayer(tableRow, starter, bench);
		} else {
			Log.i("===============", "gesperrt");
		}
	}

	/*
	 * construct roster
	 */
	private void contructTeam() {
		tbl.removeAllViews();

		Context c = getApplicationContext();
		head = (TableLayout) findViewById(R.id.tl_setstarters_table1);
		for (int i = 0; i < 8; i++) {
			String pos = null;
			switch (i) {
			case 0:
				pos = "QB";
				break;
			case 1:
				pos = "RB";
				break;
			case 2:
				pos = "RB";
				break;
			case 3:
				pos = "WR";
				break;
			case 4:
				pos = "WR";
				break;
			case 5:
				pos = "TE";
				break;
			case 6:
				pos = "K";
				break;
			case 7:
				pos = "DEF";
				break;
			}
			TableRow newRow = new Row(c, pos, head).newRow;
			tbl.addView(newRow, i);
		}

		TableRow newHeader = new Row(c, head, Row.MID_HEADER).newRow;
		((TextView) newHeader.getVirtualChildAt(3)).setText("Status");
		tbl.addView(newHeader, 8);

		for (int i = 9; i < 9 + bench.size(); i++) {
			TableRow newRowBench = new Row(c, "BCH", head).newRow;

			tbl.addView(newRowBench, i);
		}

	}

	/*
	 * fill bench
	 */
	private void fillBench() {
		for (int i = 0; i < bench.size(); i++) {
			/*
			 * if (i % 2 == 1) { tbl.getChildAt(i).setBackgroundColor(
			 * getResources().getColor(R.color.hellhellgrau)); }
			 */
			int res = getResources().getIdentifier(
					(bench.get(i).nfl_abr).toLowerCase(), "drawable",
					getPackageName());
			ImageView im = (ImageView) ((TableRow) tbl.getChildAt(9 + i))
					.getVirtualChildAt(0);
			im.setImageResource(res);
			((TextView) ((TableRow) tbl.getChildAt(9 + i)).getVirtualChildAt(1))
					.setText(bench.get(i).pos);
			((TextView) ((TableRow) tbl.getChildAt(9 + i)).getVirtualChildAt(2))
					.setText(bench.get(i).name);
			((TableRow) tbl.getChildAt(9 + i)).removeViewAt(3);

			((TableRow) tbl.getChildAt(9 + i)).setTag(true);
			((TableRow) tbl.getChildAt(9 + i)).setTag(R.string.kickoff,
					bench.get(i).kickoff);
			if (!bench.get(i).locked) {
				((TableRow) tbl.getChildAt(9 + i))
						.setOnClickListener(benchListener());
				TextView tv = new TextView(getApplicationContext());
				String kickoff = bench.get(i).date.substring(4, 6) + "/"
						+ bench.get(i).date.substring(6, 8) + " "
						+ bench.get(i).hours.substring(0, 2) + ":"
						+ bench.get(i).hours.substring(3, 5);
				tv.setText(kickoff);
				((TableRow) tbl.getChildAt(9 + i)).addView(tv, 3);
			} else {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.locked);
				((TableRow) tbl.getChildAt(9 + i)).addView(iv, 3);
			}
		}
	}

	/*
	 * fills StarterRow
	 */
	private void fillRow(int i, int p) {
		int res = getResources().getIdentifier(
				(starter.get(i).nfl_abr).toLowerCase(), "drawable",
				getPackageName());
		ImageView im = (ImageView) ((TableRow) tbl.getChildAt(p))
				.getVirtualChildAt(0);
		im.setImageResource(res);
		((TextView) ((TableRow) tbl.getChildAt(p)).getVirtualChildAt(1))
				.setText(starter.get(i).pos);
		((TextView) ((TableRow) tbl.getChildAt(p)).getVirtualChildAt(2))
				.setText(starter.get(i).name);
		((TableRow) tbl.getChildAt(p)).removeViewAt(3);
		((TableRow) tbl.getChildAt(p)).setTag(true);
		((TableRow) tbl.getChildAt(p)).setTag(R.string.kickoff,
				starter.get(i).kickoff);

		if (!starter.get(i).locked) {
			((TableRow) tbl.getChildAt(p))
					.setOnClickListener(starterListener(p));
			TextView tv = new TextView(getApplicationContext());
			String kickoff = starter.get(i).date.substring(4, 6) + "/"
					+ starter.get(i).date.substring(6, 8) + " "
					+ starter.get(i).hours.substring(0, 2) + ":"
					+ starter.get(i).hours.substring(3, 5);
			tv.setText(kickoff);
			((TableRow) tbl.getChildAt(p)).addView(tv, 3);
		} else {
			ImageView iv = new ImageView(getApplicationContext());
			iv.setImageResource(R.drawable.locked);
			((TableRow) tbl.getChildAt(p)).addView(iv, 3);
		}
	}

	/*
	 * fill starter
	 */
	private void fillStarter() {
		for (int i = 0; i < starter.size(); i++) {
			for (int k = 0; k < 9; k++) {
				String pos = starter.get(i).pos;
				TableRow row = (TableRow) tbl.getChildAt(k);
				if (((TextView) row.getVirtualChildAt(1)).getText().equals(pos)
						&& !(Boolean) row.getTag()) {
					fillRow(i, k);
					break;
				}
			}
		}
	}

	private void flexFormation(int f) {

		if (f_p1 != null) {
			TableRow oldRow = (TableRow) tbl.getChildAt(f_i1);
			if ((Boolean) oldRow.getTag()) {
				benchPlayer(oldRow);
			} else {
				tbl.removeViewAt(f_i1);
			}

			tbl.addView(new Row(getApplicationContext(), st_p1, head).newRow,
					st_i1);
		}

		if (f_p2 != null) {
			TableRow oldRow = (TableRow) tbl.getChildAt(f_i2);
			if ((Boolean) oldRow.getTag()) {
				benchPlayer(oldRow);
			} else {
				tbl.removeViewAt(f_i2);
			}

			tbl.addView(new Row(getApplicationContext(), st_p2, head).newRow,
					st_i2);
		}

		switch (f) {
		case 0:
			f_p1 = null;
			f_p1 = null;
			return;
		case 1:
			st_i1 = 4;
			f_i1 = 3;
			st_p1 = "WR";
			f_p1 = "RB";
			break;
		case 2:
			st_i1 = 2;
			f_i1 = 4;
			st_p1 = "RB";
			f_p1 = "WR";
			break;
		case 3:
			st_i1 = 2;
			f_i1 = 5;
			st_p1 = "RB";
			f_p1 = "TE";
			break;
		case 4:
			st_i1 = 4;
			f_i1 = 5;
			st_p1 = "WR";
			f_p1 = "TE";
			break;
		case 5:
			st_i1 = 3;
			f_i1 = 5;

			st_i2 = 2;
			f_i2 = 5;

			st_p1 = "WR";
			f_p1 = "TE";

			st_p2 = "RB";
			f_p2 = "TE";

			TableRow oldRow = (TableRow) tbl.getChildAt(st_i2);
			if ((Boolean) oldRow.getTag()) {
				benchPlayer(oldRow);
			} else {
				tbl.removeViewAt(st_i2);
			}

			tbl.addView(new Row(getApplicationContext(), f_p2, head).newRow,
					f_i2);
			break;
		}

		TableRow oldRow = (TableRow) tbl.getChildAt(st_i1);
		if ((Boolean) oldRow.getTag()) {
			benchPlayer(oldRow);
		} else {
			tbl.removeViewAt(st_i1);
		}

		tbl.addView(new Row(getApplicationContext(), f_p1, head).newRow, f_i1);
	}

	private void initializeSpinner() {
		ArrayList<String> gamedays = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.gameday)));
		for (int i = 0; i < gd - 1; i++) {
			gamedays.remove(0);
		}
		sp_gd = (Spinner) findViewById(R.id.spi_setstarters_gameday);
		ArrayAdapter<String> ad_gd = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, gamedays);
		ad_gd.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_gd.setAdapter(ad_gd);

		sp_gd.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (init_gd) {
					String gameday = ((String) sp_gd.getSelectedItem())
							.substring(8);
					gd = Integer.valueOf(gameday);
					url = de.damps.fantasy.CommonUtilities.URL
							+ "/myteamstarter/" + gameday;
					f_p1 = null;
					f_p2 = null;
					new GetTeam().execute();
				}
				init_gd = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		ArrayList<String> formations = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.formations)));

		sp_for = (Spinner) findViewById(R.id.spi_setstarters_formation);
		ArrayAdapter<String> ad_for = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, formations);
		ad_for.setDropDownViewResource(android.R.layout.simple_spinner_item);
		sp_for.setAdapter(ad_for);

		sp_for.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				flexFormation(sp_for.getSelectedItemPosition());
				my_flex_for = sp_for.getSelectedItemPosition();
				if (sp_for.getSelectedItemPosition() == 0) {
					flex = false;
				} else {
					flex = true;
				}
				new SendFormation().execute();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_setstarters_title)).setTypeface(font);

		pref = de.damps.fantasy.CommonUtilities.preferences;
		oid = pref.getString("id", "X");
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");

		url = de.damps.fantasy.CommonUtilities.URL + "/myteamstarter/";
		url_start = de.damps.fantasy.CommonUtilities.URL + "/setstarter/";
		url_bench = de.damps.fantasy.CommonUtilities.URL + "/removestarter/";
		url_formation = de.damps.fantasy.CommonUtilities.URL + "/setformation/";

		team = (TextView) findViewById(R.id.tv_setstarters_title);
		dteam = de.damps.fantasy.CommonUtilities.league.getTeamByOwnerid(oid);
		team.setText(dteam);

		tbl = (TableLayout) findViewById(R.id.tl_setstarters_roster);

		new GetTeam().execute();

	}

	private boolean movePlayer(TableRow tableRow, ArrayList<Player> from,
			ArrayList<Player> to) {
		for (int i = 0; i < from.size(); i++) {
			String pos = (String) ((TextView) tableRow.getVirtualChildAt(1))
					.getText();
			String name = (String) ((TextView) tableRow.getVirtualChildAt(2))
					.getText();
			if (from == bench) {
				if (pos.equals(from.get(i).pos)
						&& name.equals(from.get(i).name)) {
					Player p = from.get(i);
					String[] data = new String[5];
					data[0] = "2012";
					data[1] = ((Integer) gd).toString();
					data[2] = p.pos;
					data[3] = ((Integer) p.roster_id).toString();

					boolean locked = true;
					try {
						locked = !new SendStarter().execute(data).get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					if (!locked) {
						from.get(i).starter_id = starter_id;
						to.add(from.remove(i));
						return true;
					}
				}
			} else {
				if (pos.equals(from.get(i).pos)
						&& name.equals(from.get(i).name)) {
					Player p = from.get(i);
					starter_id = p.starter_id;
					to.add(from.remove(i));
					new SendBench().execute();
				}
			}
		}
		return false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setstarters);

		inititalizeScreen();
	}

	/*
	 * retrieve data
	 */
	private void parse() {
		DataGet data = new DataGet(url, token, hash);
		JSONObject jo = data.data;

		try {
			if (jo.getInt("flexcount") < 3) {
				flex_possible = true;
			}

			flex_for = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONObject("Teamstarter").getInt("is_flex");
			if (flex_for == 0) {
				flex = false;
			} else {
				flex = true;
			}

			teamstarter_id = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONObject("Teamstarter").getInt("id");

			switch (flex_for) {
			case 0:
				my_flex_for = 0;
				break;
			case 311:
				my_flex_for = 1;
				break;
			case 131:
				my_flex_for = 2;
				break;
			case 122:
				my_flex_for = 3;
				break;
			case 212:
				my_flex_for = 4;
				break;
			case 113:
				my_flex_for = 5;
				break;
			}

			if (!firstgd) {
				gd = jo.getInt("Gameday");
				firstgd = true;
			}

			JSONArray joar = jo.getJSONArray("Roster");
			bench = new ArrayList<Player>();

			for (int i = 0; i < joar.length(); i++) {
				Player p = new Player(joar.getJSONObject(i));
				bench.add(p);
			}
			sortBench();

			JSONArray joas = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONArray("Starter");
			starter = new ArrayList<Player>();

			for (int i = 0; i < joas.length(); i++) {
				Player p = new Player(joas.getJSONObject(i));
				starter.add(p);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void sortBench() {
		PosComparator comp = new PosComparator();
		Collections.sort(bench, comp);
	}

	private void start(int j, TableRow v) {
		if (movePlayer(v, bench, starter)) {
			tbl.removeView(v);
			tbl.removeViewAt(j);
			tbl.addView(v, j);
			((TableRow) tbl.getChildAt(j))
					.setOnClickListener(starterListener(j));
			((TableRow) tbl.getChildAt(j)).setTag(true);
			;
		} else {
			Log.i("===============", "gesperrt");
		}

	}

	private OnClickListener starterListener(final int i) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				benchPlayer((TableRow) tbl.getChildAt(i));

				long time = System.currentTimeMillis() - 3600000;

				if (time < (Long) ((TableRow) v).getTag(R.string.kickoff)) {
					String pos = (String) ((TextView) ((TableRow) v)
							.getVirtualChildAt(1)).getText();
					tbl.addView(
							new Row(getApplicationContext(), pos, head).newRow,
							i);
					sortBench();
					fillBench();
				}

			}
		};
		return listener;
	}

	private void startPlayer(TableRow v) {
		long time = System.currentTimeMillis() - 3600000;

		if (time < (Long) v.getTag(R.string.kickoff)) {
			String pos = (String) ((TextView) v.getVirtualChildAt(1)).getText();

			for (int j = 0; j < 9; j++) {
				TableRow row = (TableRow) tbl.getChildAt(j);
				if (((TextView) row.getVirtualChildAt(1)).getText().equals(pos)
						&& !(Boolean) row.getTag()) {
					start(j, v);
					return;
				}
			}
		} else {
			Log.i("===============", "gesperrt");
		}
	}
}
