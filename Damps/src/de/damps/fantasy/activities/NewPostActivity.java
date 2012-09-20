package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import de.damps.fantasy.data.DataPost;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewPostActivity extends Activity {

	private String title;
	private EditText msg;
	private String url;
	private String msgtxt;
	private String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_post);

		inititalizeScreen();
	} 
	
	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_newpost_title)).setTypeface(font);

		final Bundle extra = getIntent().getExtras();

		id = extra.getString("ID");
		title = extra.getString("title");
		
		url = de.damps.fantasy.CommonUtilities.URL + "/postforum";

		msg = (EditText) findViewById(R.id.et_newpost_msg);
		if (extra.containsKey("quote")) {
			msg.setText(extra.getString("quote"));
			msg.setSelection(msg.getText().length());
			msg.requestFocus();
		}
	}
	
	/*
	 * back to last screen
	 */
	public void back(View view) {
		finish();
	}

	/*
	 * post reply
	 */
	public void post(View view) {
		msgtxt = msg.getText().toString();

		if (msgtxt.equals("")) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Bitte Titel und Nachricht eingeben.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			new Reply().execute();
		}
	}

	
	/*
	 * send reply
	 */
	private class Reply extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;
		private String[][] data;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newpost_bar1);
			pb.setVisibility(View.VISIBLE);
			data[0][0] = "message";
			data[0][1] = msgtxt;
			data[1][0] = "forum_id";
			data[1][1] = id;
		};

		@Override
		protected Void doInBackground(Void... params) {
			new DataPost(url,data);
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			openThread();
			pb.setVisibility(View.INVISIBLE);
		}
	}

	/*
	 * open thread
	 */
	public void openThread() {
		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID", id);
		intent.putExtra("title", title);
		startActivity(intent);
	}
}
