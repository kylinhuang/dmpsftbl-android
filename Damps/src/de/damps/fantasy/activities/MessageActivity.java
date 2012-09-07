package de.damps.fantasy.activities;

import de.damps.fantasy.R;
import de.damps.fantasy.data.Message;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessageActivity extends Activity {

	private String message;
	private String title;
	private TextView titleView;
	private TextView mesView;
	private int pos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		final Bundle extra = getIntent().getExtras();
		Message m = extra.getParcelable("message");
		pos = extra.getInt("pos");
		title = m.title;
		message = m.message;
		titleView = (TextView) findViewById(R.id.tv_rea_subject);
		mesView = (TextView) findViewById(R.id.tv_rea_message);

		showMessage();
	}
	
	public void back(View view){
		Intent intent = new Intent();
	    intent.putExtra("pos", pos);
	    setResult(1, intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {

	    Intent intent = new Intent();
	    intent.putExtra("pos", pos);
	    setResult(1, intent);
	    super.onBackPressed();
	}


	private void showMessage() {
		titleView.setText(title);
		mesView.setText(message);
	}

}