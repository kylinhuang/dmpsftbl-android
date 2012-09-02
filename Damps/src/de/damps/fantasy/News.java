package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

public class News {

	public String title;
	public String date;
	public String id;

	public News(JSONObject jsonObject) throws JSONException {
		id = ((Integer) jsonObject.getInt("id")).toString();
		title = jsonObject.getString("title");
		date = parseDate(jsonObject.getString("created"));
	}

	private String parseDate(String s) {
		String date = s.substring(8, 10) + "." + s.substring(5, 7) + ". "
				+ s.substring(11, 16);
		return date;
	}
}
