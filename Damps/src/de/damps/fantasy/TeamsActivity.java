package de.damps.fantasy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
		intent.putExtra("pos", position);
		startActivity(intent);
	}

	// fills ListView with Teams
	private void zeigeTeams() {
		final ArrayAdapter<String> Teamadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				de.damps.fantasy.HomeActivity.TEAMS);
		setListAdapter(Teamadapter);
	}
	
	public void back(View view){
		finish();
	}

}
