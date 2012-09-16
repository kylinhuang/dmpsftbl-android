package de.damps.fantasy.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import de.damps.fantasy.R;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.Player;

public class SetStartersActivity extends Activity {

	private Spinner sp_gd, sp_for;
	private TableLayout tbl;
	private ArrayList<Player> bench;
	private ArrayList<Player> starter;

	private String url;
	private String url_start;
	private TextView team;
	private String tid;
	private String token;
	private String hash;
	private SharedPreferences pref;
	private String dteam;
	private String oid;
	private int gd;
	private boolean firstgd;
	private TableLayout.LayoutParams parar;
	private TableRow.LayoutParams para;

	private int rb;
	private int wr;
	private int te;
	private boolean init = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setstarters);

		inititalizeScreen();
	}

	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_setstarters_title)).setTypeface(font);

		pref = de.damps.fantasy.activities.HomeActivity.preferences;
		oid = pref.getString("id", "X");
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");

		tid = de.damps.fantasy.activities.HomeActivity.league
				.getTeamidByOwnerid(oid);
		url = de.damps.fantasy.activities.HomeActivity.URL + "/myteamstarter/";

		team = (TextView) findViewById(R.id.tv_setstarters_title);
		dteam = de.damps.fantasy.activities.HomeActivity.league
				.getTeamByOwnerid(oid);
		team.setText(dteam);

		tbl = (TableLayout) findViewById(R.id.tl_setstarters_roster);

		new GetTeam().execute();
	}

	private void initializeSpinner() {
		ArrayList<String> gamedays = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(R.array.gameday)));
		for (int i = 0; i < gd - 1; i++) {
			gamedays.remove(i);
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
				if (init) {
					String gameday = ((String) sp_gd.getSelectedItem())
							.substring(8);
					url = de.damps.fantasy.activities.HomeActivity.URL
							+ "/myteamstarter/" + gameday;
					new GetTeam().execute();
				}
				init = true;
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
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * retrieve team
	 */
	private class GetTeam extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_setstarters_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(Void... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			contructTeam();
			fillBench();
			if (!init) {
				initializeSpinner();
			}
			fillStarter();
			pb.setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * release player
	 */
	private class SendStarters extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
		};

		@Override
		protected Void doInBackground(String... params) {
			// release(params);
			return null;
		}

		protected void onPostExecute(Void v) {
		}
	}

	/*
	 * release
	 */
	private void release(String[] ids) {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url_start);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("player_id", ids[0]));
		postPara.add(new BasicNameValuePair("roster_id", ids[1]));

		String responsebody = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));
			try {
				HttpResponse response = client.execute(httppost);
				responsebody = EntityUtils.toString(response.getEntity());
				String test = responsebody;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/*
	 * retrieve data
	 */
	private void parse() {
		Json data = new Json(url);
		JSONObject jo = data.data;

		try {
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

			JSONArray joas = jo.getJSONArray("Starter").getJSONObject(0)
					.getJSONArray("Starter");
			starter = new ArrayList<Player>();

			for (int i = 0; i < joas.length(); i++) {
				Player p = new Player(joas.getJSONObject(i));
				starter.add(p);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * construct roster
	 */
	private void contructTeam() {
		tbl.removeAllViews();
		
		parar.setMargins(0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
						.getDisplayMetrics()), 0, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
						.getDisplayMetrics()));

		para = new TableRow.LayoutParams();
		para.setMargins((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics()), 0, 0, 0);

		for (int i = 0; i < 8; i++) {
			tbl.addView(starterRow(i), i);
		}
		
		// Row
		TableRow newRow = new TableRow(getApplicationContext());
		newRow.setLayoutParams(parar);

		TextView team = new TextView(getApplicationContext());
		TextView pos = new TextView(getApplicationContext());
		TextView name = new TextView(getApplicationContext());
		TextView score = new TextView(getApplicationContext());
		newRow.addView(team, 0);
		newRow.addView(pos, 1);
		newRow.addView(name, 2);
		newRow.addView(score, 3);

		// Team
		team.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(0)).getWidth();
		team.setLayoutParams(para);
		team.setGravity(Gravity.CENTER);
		team.setTextAppearance(getApplicationContext(), R.style.text);
		team.setTextColor(getResources().getColor(R.color.weis));
		team.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.column_mid));
		team.setTextAppearance(this, R.style.column);
		team.setText("NFL");

		// Pos
		pos.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(1)).getWidth());
		pos.setLayoutParams(para);
		pos.setGravity(Gravity.CENTER);
		pos.setTextAppearance(getApplicationContext(), R.style.text);
		pos.setTextColor(getResources().getColor(R.color.weis));
		pos.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.column_mid));
		pos.setTextAppearance(this, R.style.column);
		pos.setText("Pos");

		// Name
		name.setLayoutParams(para);
		name.setGravity(Gravity.LEFT);
		name.setTextAppearance(getApplicationContext(), R.style.text);
		name.setTextColor(getResources().getColor(R.color.weis));
		name.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.column_mid));
		name.setTextAppearance(this, R.style.column);
		name.setText("Name");

		// Score
		score.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(3)).getWidth());
		score.setLayoutParams(para);
		score.setGravity(Gravity.CENTER);
		score.setTextAppearance(getApplicationContext(), R.style.text);
		score.setTextColor(getResources().getColor(R.color.weis));
		score.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.column_mid));
		score.setTextAppearance(this, R.style.column);
		score.setText("Score");
		tbl.addView(newRow, 8);

		for (int i = 9; i < 9 + bench.size(); i++) {
			// Row
			TableRow newRowBench = new TableRow(getApplicationContext());

			newRowBench.setLayoutParams(parar);

			ImageView teamBench = new ImageView(getApplicationContext());
			TextView posBench = new TextView(getApplicationContext());
			TextView nameBench = new TextView(getApplicationContext());
			TextView scoreBench = new TextView(getApplicationContext());
			newRowBench.addView(teamBench, 0);
			newRowBench.addView(posBench, 1);
			newRowBench.addView(nameBench, 2);
			newRowBench.addView(scoreBench, 3);

			// Team
			teamBench.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
					.getChildAt(0)).getVirtualChildAt(0)).getWidth();

			// Pos
			posBench.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
					.getChildAt(0)).getVirtualChildAt(1)).getWidth());
			posBench.setLayoutParams(para);
			posBench.setGravity(Gravity.CENTER);
			posBench.setTextAppearance(getApplicationContext(), R.style.text);
			posBench.setTextColor(getResources().getColor(R.color.weis));
			posBench.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button));
			posBench.setText("BCH");

			// Name
			nameBench.setLayoutParams(para);
			nameBench.setTextAppearance(getApplicationContext(), R.style.text);
			nameBench.setPadding((int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics()), 0, 0, 0);
			nameBench.setText("empty");

			// Score
			scoreBench
					.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
							.getChildAt(0)).getVirtualChildAt(3)).getWidth());
			scoreBench.setLayoutParams(para);
			scoreBench.setTextAppearance(getApplicationContext(), R.style.text);
			scoreBench.setGravity(Gravity.RIGHT);
			scoreBench.setPadding(0, 0, (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics()), 0);

			tbl.addView(newRowBench, i);
		}

	}

	/*
	 * create an empty starter row
	 */
	private TableRow starterRow(int i) {
		// Row
		TableRow newRow = new TableRow(getApplicationContext());

		newRow.setLayoutParams(parar);
		newRow.setTag(false);

		ImageView team = new ImageView(getApplicationContext());
		TextView pos = new TextView(getApplicationContext());
		TextView name = new TextView(getApplicationContext());
		TextView score = new TextView(getApplicationContext());
		newRow.addView(team, 0);
		newRow.addView(pos, 1);
		newRow.addView(name, 2);
		newRow.addView(score, 3);

		// Team
		team.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(0)).getWidth();

		// Pos
		pos.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(1)).getWidth());
		pos.setLayoutParams(para);
		pos.setGravity(Gravity.CENTER);
		pos.setTextAppearance(getApplicationContext(), R.style.text);
		pos.setTextColor(getResources().getColor(R.color.weis));
		pos.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));

		switch (i) {
		case 0:
			pos.setText("QB");
			break;
		case 1:
			pos.setText("RB");
			break;
		case 2:
			pos.setText("RB");
			break;
		case 3:
			pos.setText("WR");
			break;
		case 4:
			pos.setText("WR");
			break;
		case 5:
			pos.setText("TE");
			break;
		case 6:
			pos.setText("K");
			break;
		case 7:
			pos.setText("DEF");
			break;
		}
		// Name
		name.setLayoutParams(para);
		name.setTextAppearance(getApplicationContext(), R.style.text);
		name.setPadding((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
						.getDisplayMetrics()), 0, 0, 0);
		name.setText("empty");

		// Score
		score.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_setstarters_table1))
				.getChildAt(0)).getVirtualChildAt(3)).getWidth());
		score.setLayoutParams(para);
		score.setTextAppearance(getApplicationContext(), R.style.text);
		score.setGravity(Gravity.RIGHT);
		score.setPadding(0, 0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
						.getDisplayMetrics()), 0);

		return newRow;
	}

	/*
	 * fill bench
	 */
	private void fillBench() {
		for (int i = 0; i < bench.size(); i++) {
			if (i % 2 == 1) {
				tbl.getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.hellhellgrau));
			}
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
			((TextView) ((TableRow) tbl.getChildAt(9 + i)).getVirtualChildAt(3))
					.setText(bench.get(i).summary);
		}
	}

	/*
	 * fill starter
	 */
	private void fillStarter() {
		for (int i = 0; i < starter.size(); i++) {
			if (i % 2 == 1) {
				tbl.getChildAt(i).setBackgroundColor(
						getResources().getColor(R.color.hellhellgrau));
			}

			String pos = starter.get(i).pos;
			if (pos.equals("QB")) {
				fillRow(i, 0);
			} else if (pos.equals("RB")) {
				if ((Boolean) ((TableRow) tbl.getChildAt(1)).getTag()) {
					fillRow(i, 2);
				} else {
					fillRow(i, 1);
				}
			} else if (pos.equals("WR")) {
				if ((Boolean) ((TableRow) tbl.getChildAt(3)).getTag()) {
					fillRow(i, 4);
				} else {
					fillRow(i, 3);
				}
			} else if (pos.equals("TE")) {
				fillRow(i, 5);
			} else if (pos.equals("K")) {
				fillRow(i, 6);
			} else if (pos.equals("DEF")) {
				fillRow(i, 7);
			}
		}
	}

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
		((TextView) ((TableRow) tbl.getChildAt(p)).getVirtualChildAt(3))
				.setText(starter.get(i).summary);
		((TableRow) tbl.getChildAt(p)).setTag(true);
	}

	public void sortTeam(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.nfl_abr.compareToIgnoreCase(rhs.nfl_abr);
			}
		};
		Collections.sort(bench, comp);
	}

	public void sortPos(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.pos.compareToIgnoreCase(rhs.pos);
			}
		};
		Collections.sort(bench, comp);

	}

	public void sortName(View view) {
		Comparator<Player> comp = new Comparator<Player>() {

			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.name.compareToIgnoreCase(rhs.name);
			}
		};
		Collections.sort(bench, comp);

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
		Collections.sort(bench, comp);

	}

}
