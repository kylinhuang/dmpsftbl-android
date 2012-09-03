package de.damps.fantasy;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;

public class InboxActivity extends ListActivity {

	private MessageAdapter messageadapter;
	private ArrayList<Message> messages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		messages = getIntent().getParcelableExtra("messages");
		showThreads();
	}

	// Anzeigen der Threads
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);

		setListAdapter(messageadapter);
	}

}
