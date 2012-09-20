package de.damps.fantasy.activities;

import java.util.ArrayList;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.MessageAdapter;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Message;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.app.ListActivity;
import android.content.Intent;

public class InboxActivity extends ListActivity {

	private MessageAdapter messageadapter;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private String url;
	private Message message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		url = de.damps.fantasy.CommonUtilities.URL + "/readmessage";
		messages = getIntent().getExtras().getParcelableArrayList("messages");
		
		showThreads();
	}


	/*
	 * fill list
	 */
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);
		setListAdapter(messageadapter);
	}

	/*
	 * back from reading
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			int p = data.getExtras().getInt("pos");
			messages.get(p).read[0] = true;

			messageadapter.notifyDataSetChanged();
		}
	}

	/*
	 * open message
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent intent = new Intent(getApplicationContext(),
				MessageActivity.class);
		message = messages.get(position);
		new ReadMessage().execute();
		intent.putExtra("message", message);
		intent.putExtra("pos", position);
		startActivityForResult(intent, 1);
	}

	/*
	 * mark message as read
	 */
	private class ReadMessage extends AsyncTask<Void, Void, Void> {
		String[][] data = new String[1][2];
		@Override
		protected void onPreExecute() {
			data[0][0] = "id";
			data[0][1] = ((Integer) message.id).toString();
		};

		@Override
		protected Void doInBackground(Void... voids) {
			new DataPost(url, data);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
		}
	}

}