package de.damps.fantasy;

import android.os.Bundle;
import android.app.ListActivity;

public class OutboxActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox);
	}


}
