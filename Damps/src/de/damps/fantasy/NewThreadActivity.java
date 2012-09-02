package de.damps.fantasy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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
	private String token;
	private String hash;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newthread);
		SharedPreferences pref = de.damps.fantasy.HomeActivity.preferences;
		url = de.damps.fantasy.HomeActivity.URL + "/openthread";
		token = pref.getString("token", "");
		hash = pref.getString("hash", "");
	}
	
	public void postThread(View view){
		title = (EditText)findViewById(R.id.et_newt_title);
		msg = (EditText)findViewById(R.id.et_newt_msg);
		sticky = (CheckBox)findViewById(R.id.cb_newt_sticky);
		member = (CheckBox)findViewById(R.id.cb_newt_member);
		
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
			new OpenThread().execute(url);
		}
	}
	
	private class OpenThread extends AsyncTask<String, Void, Void> {
		ProgressBar pb;
		private String id;

		@Override
		protected void onPreExecute() {
			pb = (ProgressBar) findViewById(R.id.pb_newt_bar1);
			pb.setVisibility(View.VISIBLE);
		};

		@Override
		protected Void doInBackground(String... params) {
			id = post();
			return null;
		}

		@Override
		protected void onPostExecute(Void v) {
			openThread(id);
			pb.setVisibility(View.INVISIBLE);
		}

	}

	protected String post() {
		
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));
		postPara.add(new BasicNameValuePair("sticky", stickystring));
		postPara.add(new BasicNameValuePair("members", memberstring));
		postPara.add(new BasicNameValuePair("title", titletxt));
		postPara.add(new BasicNameValuePair("message", msgtxt));

		String responsebody = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(postPara));
			try {
				HttpResponse response = client.execute(httppost);
				responsebody = EntityUtils.toString(response.getEntity());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return responsebody;
	}
	
	public void openThread(String id){
		Intent intent = new Intent(getApplicationContext(),
				ThreadActivity.class);
		intent.putExtra("ID",id);
		intent.putExtra("title", titletxt);
		startActivity(intent);
	}
}
