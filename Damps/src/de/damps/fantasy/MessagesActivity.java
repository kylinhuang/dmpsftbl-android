package de.damps.fantasy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MessagesActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
	}

	public void sendMessage(View view) {
		Intent intent = new Intent(getApplicationContext(),
				NewMessageActivity.class);
		startActivity(intent);
	}

	public void back(View view) {
		finish();
	}
}
