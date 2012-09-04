package de.damps.fantasy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MessageActivity extends Activity {

	private String message;
	private String title;
	private TextView titleView;
	private TextView mesView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		final Bundle extra = getIntent().getExtras();
		Message m = extra.getParcelable("message");
		title = m.title;
		message = m.message;
		titleView = (TextView) findViewById(R.id.tv_rea_subject);
		mesView = (TextView) findViewById(R.id.tv_rea_message);

		showMessage();
	}
	
	public void back(View view){
		finish();
	}

	private void showMessage() {
		titleView.setText(title);
		mesView.setText(message);
	}

}