package de.damps.fantasy.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.MessageAdapter;
import de.damps.fantasy.data.Message;

public class OutboxActivity extends ListActivity {

	private MessageAdapter messageadapter;
	private ArrayList<Message> messages = new ArrayList<Message>();

	/*
	 * come back from new message
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 2) {
			Message m = data.getExtras().getParcelable("Message");
			// ListView lv = getListView();
			messages.add(0, m);

			messageadapter.notifyDataSetChanged();

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox);
		messages = getIntent().getExtras().getParcelableArrayList("messages");
		showThreads();
	}

	/*
	 * open message
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// creates Intent and fills with local Data
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		intent.putExtra("message", messages.get(position));
		startActivity(intent);
	}

	/*
	 * fill list
	 */
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);

		setListAdapter(messageadapter);
	}

}
