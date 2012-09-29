package de.damps.fantasy.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.TeamAdapter;

public class TeamsActivity extends ListActivity {

	private TeamAdapter teamadapter;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams);
		zeigeTeams();
	}

	/*
	 * open specific roster
	 */
	@Override
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
				de.damps.fantasy.CommonUtilities.league.league);
		setListAdapter(teamadapter);
	}

}
