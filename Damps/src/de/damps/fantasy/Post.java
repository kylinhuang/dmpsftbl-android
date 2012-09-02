package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
	
	public String message, author, created;

	public Post(JSONObject jsonObject) {
		try {
			JSONObject post = jsonObject.getJSONObject("Forum");
			JSONObject author = jsonObject.getJSONObject("User");
			
			message = post.getString("message");
			created = parseDate(post.getString("created"));
			this.author = author.getString("username");
			
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
