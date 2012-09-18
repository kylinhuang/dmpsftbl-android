package de.damps.fantasy.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import de.damps.fantasy.data.Row;

public class SetStartersActivity extends Activity {

	private Spinner sp_gd, sp_for;
	private TableLayout tbl;
	private ArrayList<Player> bench;
	private ArrayList<Player> starter;

	private String url;
	private String url_start;
	private String url_bench;
	private TextView team;
	private String token;
	private String hash;
	private SharedPreferences pref;
	private String dteam;
	private String oid;
	private int gd;
	private boolean firstgd;
	private boolean init = false;
	private TableLayout head;
	private Integer starter_id;

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

		url = de.damps.fantasy.activities.HomeActivity.URL + "/myteamstarter/";
		url_start = de.damps.fantasy.activities.HomeActivity.URL
				+ "/setstarter/";
		url_bench = de.damps.fantasy.activities.HomeActivity.URL
				+ "/removestarter/";

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
	 * start player
	 */
	private class SendStarter extends AsyncTask<String, Void, Boolean> {

		@Override
		protected void onPreExecute() {
		};

		@Override
		protected Boolean doInBackground(String... params) {
			return startPlayer(params);
		}

		protected void onPostExecute(Boolean result) {

		}
	}

	private boolean startPlayer(String[] data) {
		final DefaultHttpClient client = new DefaultHttpClient();
		String url_send = data[0];
		final HttpPost httppost = new HttpPost(url_send);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();
		
		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		
		if(data.length == 5){
			postPara.add(new BasicNameValuePair("season", data[1]));
			postPara.add(new BasicNameValuePair("gameday", data[2]));
			postPara.add(new BasicNameValuePair("position", data[3]));
			postPara.add(new BasicNameValuePair("roster_id", data[4]));
		}else{
			postPara.add(new BasicNameValuePair("starter_id", data[1]));
		}
		
		

		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));
			try {
				HttpResponse response = client.execute(httppost);
				String s = EntityUtils.toString(response.getEntity());
				if (false) {
					return false;
				}else{
					if(!s.equals("true")){
						starter_id = Integer.valueOf(s);
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return true;
	}

	/*
	 * retrieve data
	 */
	private void parse() {
		Json data = new Json(url, token, hash);
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
			e.printStackTrace();
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

	private OnClickListener starterListener(final int i) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				benchPlayer((TableRow) tbl.getChildAt(i));
				String pos = (String) ((TextView) ((TableRow) v)
						.getVirtualChildAt(1)).getText();
				tbl.addView(new Row(getApplicationContext(), pos, head).newRow,
						i);
			}
		};
		return listener;
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

	private void startPlayer(TableRow v) {

		String pos = (String) ((TextView) v.getVirtualChildAt(1)).getText();
		int j = 0;
		if (pos.equals("QB")
				&& !(Boolean) ((TableRow) tbl.getChildAt(0)).getTag()) {

			j = 0;
			start(j, v);
		} else if (pos.equals("RB")) {
			if (!(Boolean) ((TableRow) tbl.getChildAt(1)).getTag()
					|| !(Boolean) ((TableRow) tbl.getChildAt(2)).getTag()) {

				if ((Boolean) ((TableRow) tbl.getChildAt(1)).getTag()) {
					j = 2;
					start(j, v);
				} else {
					j = 1;
					start(j, v);
				}
			}
		} else if (pos.equals("WR")) {
			if (!(Boolean) ((TableRow) tbl.getChildAt(3)).getTag()
					|| !(Boolean) ((TableRow) tbl.getChildAt(4)).getTag()) {

				if ((Boolean) ((TableRow) tbl.getChildAt(3)).getTag()) {
					j = 4;
					start(j, v);
				} else {
					j = 3;
					start(j, v);
				}
			}
		} else if (pos.equals("TE")
				&& !(Boolean) ((TableRow) tbl.getChildAt(5)).getTag()) {
			j = 5;
			start(j, v);
		} else if (pos.equals("K")
				&& !(Boolean) ((TableRow) tbl.getChildAt(6)).getTag()) {
			j = 6;
			start(j, v);
		} else if (pos.equals("DEF")
				&& !(Boolean) ((TableRow) tbl.getChildAt(7)).getTag()) {
			j = 7;
			start(j, v);
		}

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

	private void benchPlayer(TableRow tableRow) {
		TableRow benchRow = tableRow;
		benchRow.setTag(true);
		benchRow.setOnClickListener(benchListener());
		tbl.removeView(tableRow);
		tbl.addView(benchRow);
		movePlayer(tableRow, starter, bench);
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
					data[0] = url_start;
					data[1] = "2012";
					data[2] = ((Integer) gd).toString();
					data[3] = p.pos;
					data[4] = ((Integer) p.roster_id).toString();
					
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
					String[] data = new String[2];
					data[0] = url_bench;
					data[1] =  ((Integer)p.starter_id).toString();
					
					new SendStarter().execute(data);
				}
			}
		}
		return false;
	}

	/*
	 * fill starter
	 */
	private void fillStarter() {
		for (int i = 0; i < starter.size(); i++) {
			int j = 0;
			/*
			 * if (i % 2 == 1) { tbl.getChildAt(i).setBackgroundColor(
			 * getResources().getColor(R.color.hellhellgrau)); }
			 */
			String pos = starter.get(i).pos;
			if (pos.equals("QB")) {
				j = 0;
				fillRow(i, j);
			} else if (pos.equals("RB")) {
				if ((Boolean) ((TableRow) tbl.getChildAt(1)).getTag()) {
					j = 2;
					fillRow(i, j);
				} else {
					j = 1;
					fillRow(i, j);
				}
			} else if (pos.equals("WR")) {
				if ((Boolean) ((TableRow) tbl.getChildAt(3)).getTag()) {
					j = 4;
					fillRow(i, j);
				} else {
					j = 3;
					fillRow(i, j);
				}
			} else if (pos.equals("TE")) {
				j = 5;
				fillRow(i, j);
			} else if (pos.equals("K")) {
				j = 6;
				fillRow(i, j);
			} else if (pos.equals("DEF")) {
				j = 7;
				fillRow(i, j);
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
		if (!starter.get(i).locked) {
			((TableRow) tbl.getChildAt(p))
					.setOnClickListener(starterListener(p));
		} else {
			ImageView iv = new ImageView(getApplicationContext());
			iv.setImageResource(R.drawable.locked);
			((TableRow) tbl.getChildAt(p)).addView(iv, 3);
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
			if (!bench.get(i).locked) {
				((TableRow) tbl.getChildAt(9 + i))
						.setOnClickListener(benchListener());
			} else {
				ImageView iv = new ImageView(getApplicationContext());
				iv.setImageResource(R.drawable.locked);
				((TableRow) tbl.getChildAt(9 + i)).addView(iv, 3);
			}
		}
	}
}
