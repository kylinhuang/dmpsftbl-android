package de.damps.fantasy;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MessagesActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);

		TabHost tabHost = getTabHost();

		TabSpec inspec = tabHost.newTabSpec("Inbox");
		inspec.setIndicator("Inbox");
		Intent inintent = new Intent(getApplicationContext(), InboxActivity.class);
		inspec.setContent(inintent);

		TabSpec outspec = tabHost.newTabSpec("Outbox");
		outspec.setIndicator("Outbox");
		Intent outintent = new Intent(getApplicationContext(), OutboxActivity.class);
		outspec.setContent(outintent);
		
		tabHost.addTab(inspec);
		tabHost.addTab(outspec);
		tabHost.setCurrentTab(0);


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
