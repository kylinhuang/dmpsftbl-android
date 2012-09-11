package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TeamManagementActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teammanagement);
		
		inititaliseScreen();
	}

	/*
	 * init screen
	 */
	private void inititaliseScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_teammanagement_title1)).setTypeface(font);

	}

	public void back(View view) {
		finish();
	}

	/** select news */
	public void selectStarter(View view) {
		Intent intent = new Intent(getApplicationContext(),
				SetStartersActivity.class);
		startActivity(intent);
	}

	/** select roster */
	public void releasePlayer(View view) {
		Intent intent = new Intent(getApplicationContext(), ReleaseActivity.class);
		startActivity(intent);
	} 

	/** select scores */
	public void signFreeAgent(View view) {
		/*Intent intent = new Intent(getApplicationContext(),
				SignFreeAgent.class);
		startActivity(intent);*/
	}

	/** select standings */
	public void openTrades(View view) {
		/*Intent intent = new Intent(getApplicationContext(),
				OpenTradesActivity.class);
		startActivity(intent);*/
	}
}
