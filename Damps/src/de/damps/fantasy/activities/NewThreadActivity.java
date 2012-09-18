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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NewThreadActivity extends Activity{

	private EditText title;
	private EditText msg;
	private CheckBox sticky;
	private CheckBox member;
	private String url;
	private String titletxt;
	private String msgtxt;
	private String stickystring = "0";
	private String memberstring = "0";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_thread);
		
		
		inititalizeScreen();
	}
	
	public void back(View view){
		finish();
	} 
	
	private void inititalizeScreen() {
		Typeface font = Typeface.createFromAsset(getAssets(), "Ubuntu-C.ttf");
		((TextView) findViewById(R.id.tv_newthread_title1)).setTypeface(font);

		url = de.damps.fantasy.activities.HomeActivity.URL + "/openthread";
	}
	
	/*
	 * posts the new thread
	 */
	public void postThread(View view){
		title = (EditText)findViewById(R.id.et_newthread_title);
		msg = (EditText)findViewById(R.id.et_newthread_msg);
		sticky = (CheckBox)findViewById(R.id.cb_newthread_sticky);
		member = (CheckBox)findViewById(R.id.cb_newthread_member);
		
		titletxt = title.getText().toString();
		msgtxt = msg.getText().toString();
		if(sticky.isChecked()){
			stickystring = "1";
		}
		if(member.isChecked()){
			memberstring = "1";
		}
		
		if(titletxt.equals("") || msgtxt.equals("")){
			Toast toast = Toast.makeText(getApplicationContext(), "Bitte Titel und Nachricht eingeben.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}else{
			new CreateThread().execute();
		}
	}
	
	/*
	 * create new thread
	 */
	private class CreateThread extends AsyncTask<Void, Void, Void> {
		ProgressBar pb;
		private String id;
		String[][] data = new String[4][2];

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newthread_bar1);
			pb.setVisibility(View.VISIBLE);
			data[0][0] = "sticky";
			data[0][1] = stickystring;
			data[1][0] = "members";
			data[1][1] = memberstring;
			data[2][0] = "title";
			data[2][1] = titletxt;
			data[3][0] = "message";
			data[3][1] = msgtxt;
		};

		@Override
		protected Void doInBackground(Void... params) {
			id = new DataPost(url,data).response;
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			openThread(id);
			pb.setVisibility(View.INVISIBLE);
		}

	}
	
	/*
	 * opens the new thread
	 */
	public void openThread(String id){
		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID",id);
		intent.putExtra("title", titletxt);
		startActivity(intent);
	}
}
