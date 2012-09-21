package de.damps.fantasy.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONException;
import org.json.JSONObject;

public class DataGet {

	public JSONObject data;

	public DataGet(String url) {
		String dataRaw = readUrl(url);
		try {
			data = new JSONObject(dataRaw);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public DataGet(String url, String token, String hash) {
		String dataRaw = getStringFromWeb(url, token, hash);
		try {
			data = new JSONObject(dataRaw);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String getStringFromWeb(String url, String token, String hash) {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> postPara = new ArrayList<NameValuePair>();
		String responsebody = null;

		postPara.add(new BasicNameValuePair("token", token));
		postPara.add(new BasicNameValuePair("hash", hash));

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

	// reads URL
	private String readUrl(String url) {
		String string = new String();
		URL feedUrl = null;
		try {
			feedUrl = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		try {
			string = convertStreamToString(feedUrl.openStream());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	// reads Stream
	private static String convertStreamToString(InputStream is)
			throws UnsupportedEncodingException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}