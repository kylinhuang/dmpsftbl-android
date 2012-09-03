package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

public class MyThread {

	public String id, title, created, modified, last;
	public boolean members;

	public MyThread(JSONObject jsonObject) {
		try {
			JSONObject jo = jsonObject.getJSONObject("Forum");
			id = ((Integer) jo.getInt("id")).toString();
			title = jo.getString("title");
			members = jo.getBoolean("members");
			created = parseDate(jo.getString("created"));
			modified = parseDate(jo.getString("modified"));
			last = jo.getJSONObject("lastpost").getJSONObject("User").getString("username");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String parseDate(String s) {
		String date = s.substring(8, 10) + "." + s.substring(5, 7) + ". "
				+ s.substring(11, 16);
		return date;
	}

}
