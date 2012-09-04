package de.damps.fantasy;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;

public class OutboxActivity extends ListActivity {

	private MessageAdapter messageadapter;
	private ArrayList<Message> messages = new ArrayList<Message>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox);
		messages = getIntent().getExtras().getParcelableArrayList("messages");
		showThreads();
	}

	// Anzeigen der Threads
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);

		setListAdapter(messageadapter);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// creates Intent and fills with local Data
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		intent.putExtra("message", messages.get(position));
		startActivity(intent);
	}

}
