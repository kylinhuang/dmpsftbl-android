package de.damps.fantasy.activities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.Json;
import de.damps.fantasy.data.Player;

public class ReleaseActivity extends Activity {

	private TableLayout tbl;
	private ArrayList<Player> roster;
	private int anzahl;

	private String url;
	private String url_release;
	private TextView team;
	private String id;
	private String token;
	private String hash;
	private SharedPreferences pref;
	private String dteam;
	private String oid;

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
		//
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");

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
	private class ReleasePlayer extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
		};

		@Override
		protected Void doInBackground(String... params) {
			release(params);
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
		final HttpPost httppost = new HttpPost(url_release);
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

	/*
	 * construct roster
	 */
	private void contructRoster() {
		for (int i = 0; i < anzahl; i++) {
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
			ImageView release = new ImageView(getApplicationContext());
			newRow.addView(team, 0);
			newRow.addView(pos, 1);
			newRow.addView(name, 2);
			newRow.addView(score, 3);
			newRow.addView(release, 4);

			// Team
			team.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_release_table1))
					.getChildAt(0)).getVirtualChildAt(0)).getWidth();

			// Pos
			pos.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_release_table1))
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
			name.setPadding((int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics()), 0, 0, 0);

			// Score
			score.setWidth(((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_release_table1))
					.getChildAt(0)).getVirtualChildAt(3)).getWidth());
			score.setLayoutParams(para);
			score.setTextAppearance(getApplicationContext(), R.style.text);
			score.setGravity(Gravity.RIGHT);
			score.setPadding(0, 0, (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics()), 0);

			// relese
			release.getLayoutParams().width = ((TextView) ((TableRow) ((TableLayout) findViewById(R.id.tl_release_table1))
					.getChildAt(0)).getVirtualChildAt(4)).getWidth();

			String pid = ((Integer) roster.get(i).player_id).toString();
			String rid = ((Integer) roster.get(i).roster_id).toString();
			final String[] ids = {pid,rid};
			final String name1 = roster.get(i).name;

			release.setOnClickListener(new OnClickListener() {

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
							new ReleasePlayer().execute(ids);
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
		for (int i = 0; i < anzahl; i++) {
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
			int re = getResources().getIdentifier("remove", "drawable",
					getPackageName());
			ImageView r = (ImageView) ((TableRow) tbl.getChildAt(i))
					.getVirtualChildAt(4);
			r.setImageResource(re);
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
