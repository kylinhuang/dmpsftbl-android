package de.damps.fantasy.data;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DataPost {

	public String response;

	public DataPost(String url, String[][] data) {
		SharedPreferences pref = de.damps.fantasy.CommonUtilities.preferences;
		String token = pref.getString("token", "");
		String hash = pref.getString("hash", "");

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();

		if (token != null) {
			postPara.add(new BasicNameValuePair("token", token));
			postPara.add(new BasicNameValuePair("hash", hash));
		}

		for (int i = 0; i < data.length; i++) {
			postPara.add(new BasicNameValuePair(data[i][0], data[i][1]));
		}

		try {
			
			httppost.setEntity(new UrlEncodedFormEntity(postPara, HTTP.UTF_8));

			try {
				HttpResponse response = client.execute(httppost);
				this.response = EntityUtils.toString(response.getEntity());
				@SuppressWarnings("unused")
				String forbreakpoint = this.response;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public boolean checkConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) de.damps.fantasy.CommonUtilities.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
			return true;
		} else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			return false;
		}
		return false;
	}
}
