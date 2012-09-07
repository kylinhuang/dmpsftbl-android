package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import de.damps.fantasy.adapter.TeamAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class TeamsActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams);
		zeigeTeams();
	}

	//
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// creates Intent and fills with local Data
		Intent intent = new Intent(getApplicationContext(),
				RosterActivity.class);
		intent.putExtra("team", l.getItemIdAtPosition(position));
		startActivity(intent);
	}

	// fills ListView with Teams
	private void zeigeTeams() {
		final TeamAdapter teamadapter = new TeamAdapter(this,
				android.R.layout.simple_list_item_1,
				de.damps.fantasy.activities.HomeActivity.league.league);
		setListAdapter(teamadapter);
	}
	
	public void back(View view){
		finish();
	}

}
