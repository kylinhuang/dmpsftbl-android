package de.damps.fantasy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.damps.fantasy.R;
import de.damps.fantasy.data.Message;

public class MessageActivity extends Activity {

	private String message;
	private String title;
	private String from;
	private TextView titleView;
	private TextView mesView;
	private int pos;

	/*
	 * return to last screen
	 */
	public void back(View view) {
		Intent intent = new Intent();
		intent.putExtra("pos", pos);
		setResult(1, intent);
		finish();
	}

	/*
	 * init screen
	 */
	private void inititalizeScreen() {
		final Bundle extra = getIntent().getExtras();
		Message m = extra.getParcelable("message");
		pos = extra.getInt("pos");
		title = m.title;
		message = m.message;
		from = m.from;

		titleView = (TextView) findViewById(R.id.tv_rea_subject);
		mesView = (TextView) findViewById(R.id.tv_rea_message);

		titleView.setText(title);
		mesView.setText(message);
	}

	/*
	 * return to last screen
	 */
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("pos", pos);
		setResult(1, intent);
		super.onBackPressed();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);

		inititalizeScreen();
	}

	/*
	 * answer message
	 */
	public void reply(View viev) {
		Intent intent = new Intent(getApplicationContext(),
				NewMessageActivity.class);
		String t = "AW: " + title;
		String m = from + ":\n\n" + message + "\n\n";
		intent.putExtra("title", t);
		intent.putExtra("message", m);
		intent.putExtra("from", from);
		startActivity(intent);
	}

}