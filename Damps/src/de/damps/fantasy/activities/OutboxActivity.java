package de.damps.fantasy.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.MessageAdapter;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Message;

public class OutboxActivity extends ListActivity {
	
	private class DeleteMessage extends AsyncTask<Integer, Void, Boolean> {
		String[][] data = new String[1][2];

		@Override
		protected Boolean doInBackground(Integer... i) {
			data[0][0] = "id";
			data[0][1] = i[0].toString();
			String response = new DataPost(url_delete, data).response;
			if(response.equals("true")){
				return true;
			}
			return false;
		};

		@Override
		protected void onPostExecute(Boolean r) {
		}

		@Override
		protected void onPreExecute() {
		}
	}
	
	private MessageAdapter messageadapter;
	private String url_delete;
	private ArrayList<Message> messages = new ArrayList<Message>();

	public void deleteMessage(View view) {
		try {
			if(new DeleteMessage().execute((Integer) view.getTag()).get()){
				removeMessage((Integer) view.getTag());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
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
		url_delete = de.damps.fantasy.CommonUtilities.URL + "/deletemessage";
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

	private void removeMessage(Integer tag) {
		for(int i = 0; i<messages.size();i++){
			if(messages.get(i).id==tag){
				messages.remove(i);
				showThreads();
				break;
			}
		}
	}
	
	private void showThreads() {
		messageadapter = new MessageAdapter(this, R.layout.threaditem, messages);

		setListAdapter(messageadapter);
	}

}
