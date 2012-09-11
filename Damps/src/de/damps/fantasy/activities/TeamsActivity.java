package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import de.damps.fantasy.adapter.TeamAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class TeamsActivity extends ListActivity {

	private TeamAdapter teamadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams);
		zeigeTeams();
	}

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * open specific roster
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(getApplicationContext(),
				RosterActivity.class);
		intent.putExtra("team", teamadapter.getItem(position).team);
		startActivity(intent);
	}

	/*
	 * fill list
	 */
	private void zeigeTeams() {
		teamadapter = new TeamAdapter(this,
				android.R.layout.simple_list_item_1,
				de.damps.fantasy.activities.HomeActivity.league.league);
		setListAdapter(teamadapter);
	}

}
