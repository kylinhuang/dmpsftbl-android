package de.damps.fantasy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import de.damps.fantasy.R;
import de.damps.fantasy.adapter.OwnerAdapter;
import de.damps.fantasy.data.DampsTeam;
import de.damps.fantasy.data.DataPost;
import de.damps.fantasy.data.Message;

public class NewMessageActivity extends Activity {

	/*
	 * send message and return to inbox
	 */
	private class SendMessage extends AsyncTask<String, Void, Void> {
		ProgressBar pb;
		String response = null;
		String[][] data = new String[3][2];

		private void back(String response2) {
			if (!response.equals("false")) {
				Message m = new Message(titletxt, msgtxt, receiver);
				Intent intent = new Intent();
				intent.putExtra("message", m);
				setResult(2, intent);
				finish();
			} else {
				Toast toast = Toast.makeText(getApplicationContext(),
						"Fehler beim senden", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		};

		@Override
		protected Void doInBackground(String... params) {
			response = new DataPost(url, data).response;
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			back(response);
			pb.setVisibility(View.INVISIBLE);

		}

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newm_bar1);
			pb.setVisibility(View.VISIBLE);
			data[0][0] = "to";
			data[0][1] = receiver;
			data[1][0] = "subject";
			data[1][1] = titletxt;
			data[2][0] = "content";
			data[2][1] = msgtxt;
		}
	}

	private EditText title;
	private EditText msg;
	private String titletxt;
	private String msgtxt;
	private String url;
	private Spinner spinner;
	private String receiver;

	private String from;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * initialize screen
	 */
	private void initializeScreen() {
		url = de.damps.fantasy.CommonUtilities.URL + "/sendmessage";

		title = (EditText) findViewById(R.id.et_newm_title);
		msg = (EditText) findViewById(R.id.et_newm_msg);

		initializeSpinner();

		Bundle extra = getIntent().getExtras();

		if (extra != null) {
			titletxt = extra.getString("title");
			msgtxt = extra.getString("message");
			from = extra.getString("from");
			int i = de.damps.fantasy.CommonUtilities.league.getPosition(from);
			spinner.setSelection(i);

			title.setText(titletxt);
			msg.setText(msgtxt);

			msg.setSelection(msg.getText().length());
			msg.requestFocus();
		}
	}

	/*
	 * init spinner
	 */
	private void initializeSpinner() {
		spinner = (Spinner) findViewById(R.id.spi_mewm_dest);
		OwnerAdapter ownerAdapter = new OwnerAdapter(this,
				android.R.layout.simple_spinner_item,
				de.damps.fantasy.CommonUtilities.league.league);
		spinner.setAdapter(ownerAdapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_message);

		initializeScreen();
	}

	/*
	 * retrieve title and message
	 */
	public void sendMessage(View view) {

		receiver = ((DampsTeam) spinner.getSelectedItem()).owner_id;

		titletxt = title.getText().toString();
		msgtxt = msg.getText().toString();

		if (titletxt.equals("") || msgtxt.equals("")) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte Titel und Nachricht eingeben.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			new SendMessage().execute(url);
		}
	}

}
