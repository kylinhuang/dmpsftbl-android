package de.damps.fantasy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class InjuriesActivity extends Activity {

	private ArrayList<Injury> injuries;
	private String url;
	private TableLayout tbl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.injuries);
		url = de.damps.fantasy.HomeActivity.URL + "/injuries";
		tbl = (TableLayout) findViewById(R.id.tl_inj_injuries);

		new GetInjuries().execute(url);
	}

	private class GetInjuries extends AsyncTask<String, Void, Void> {
		ProgressBar pb;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_inj_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			parse();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			ConstructInjuries();
			fillTable();
			pb.setVisibility(View.INVISIBLE);
		}

	}

	private void parse() {
		//Json data = new Json(url);

	}

	private void ConstructInjuries() {
		for (int i = 0; i < injuries.size(); i++) {
			TableRow newRow = new TableRow(getApplicationContext());
			TextView nfl = new TextView(getApplicationContext());
			TextView pos = new TextView(getApplicationContext());
			TextView name = new TextView(getApplicationContext());
			TextView ffl = new TextView(getApplicationContext());
			TextView status = new TextView(getApplicationContext());
			newRow.addView(nfl, 0);
			newRow.addView(pos, 1);
			newRow.addView(name, 2);
			newRow.addView(ffl, 3);
			newRow.addView(status, 4);
			nfl.setPadding(3, 3, 3, 3);
			pos.setPadding(3, 3, 3, 3);
			name.setPadding(3, 3, 3, 3);
			ffl.setPadding(3, 3, 3, 3);
			status.setPadding(3, 3, 3, 3);
			tbl.addView(newRow, i);
		}
	}

	private void fillTable() {
		for (int i = 0; i < injuries.size(); i++) {
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(0))
					.setText(injuries.get(i).nfl_team);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(1))
					.setText(injuries.get(i).pos);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(2))
					.setText(injuries.get(i).name);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(3))
					.setText(injuries.get(i).ffl_team);
			((TextView) ((TableRow) tbl.getChildAt(i)).getVirtualChildAt(3))
					.setText(injuries.get(i).status);
		}

	}

	public void sortNflTeam(View view) {
		Comparator<Injury> comp = new Comparator<Injury>() {

			@Override
			public int compare(Injury lhs, Injury rhs) {
				return lhs.nfl_team.compareToIgnoreCase(rhs.nfl_team);
			}
		};
		Collections.sort(injuries, comp);
		fillTable();
	}

	public void sortPos(View view) {
		Comparator<Injury> comp = new Comparator<Injury>() {

			@Override
			public int compare(Injury lhs, Injury rhs) {
				return lhs.pos.compareToIgnoreCase(rhs.pos);
			}
		};
		Collections.sort(injuries, comp);
		fillTable();
	}

	public void sortName(View view) {
		Comparator<Injury> comp = new Comparator<Injury>() {

			@Override
			public int compare(Injury lhs, Injury rhs) {
				return lhs.name.compareToIgnoreCase(rhs.name);
			}
		};
		Collections.sort(injuries, comp);
		fillTable();
	}

	public void sortFflTeam(View view) {
		Comparator<Injury> comp = new Comparator<Injury>() {

			@Override
			public int compare(Injury lhs, Injury rhs) {
				return lhs.ffl_team.compareToIgnoreCase(rhs.ffl_team);
			}
		};
		Collections.sort(injuries, comp);
		fillTable();
	}

	public void sortStatus(View view) {
		Comparator<Injury> comp = new Comparator<Injury>() {

			@Override
			public int compare(Injury lhs, Injury rhs) {
				return lhs.status.compareToIgnoreCase(rhs.status);
			}
		};
		Collections.sort(injuries, comp);
		fillTable();
	}
}
