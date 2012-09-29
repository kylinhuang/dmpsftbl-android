package de.damps.fantasy.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

public class MyThread {

	public String id, title, created, modified, last;
	public boolean members;
	private int year, month, day, hour, minute, second;
	public long mod;

	public MyThread(JSONObject jsonObject) {
		try {
			JSONObject jo = jsonObject.getJSONObject("Forum");
			id = ((Integer) jo.getInt("id")).toString();
			title = jo.getString("title");
			members = jo.getBoolean("members");
			created = parseDate(jo.getString("created"));
			modified = parseDate(jo.getString("modified"));
			parseTime(jo.getString("modified"));
			last = jo.getJSONObject("lastpost").getJSONObject("User")
					.getString("username");
			Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
			cal.set(year, month, day, hour, minute, second);
			mod = cal.getTime().getTime();

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

	private void parseTime(String s) {
		year = Integer.parseInt(s.substring(0, 4));
		month = Integer.parseInt(s.substring(5, 7)) - 1;
		day = Integer.parseInt(s.substring(8, 10));
		hour = Integer.parseInt(s.substring(11, 13));
		minute = Integer.parseInt(s.substring(14, 16));
		second = Integer.parseInt(s.substring(17, 19));

	}

}
