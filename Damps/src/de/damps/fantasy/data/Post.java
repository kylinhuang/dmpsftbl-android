package de.damps.fantasy.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

	public String message, author, created;

	public Post(JSONObject jsonObject) {
		try {
			final JSONObject post = jsonObject.getJSONObject("Forum");
			JSONObject author = jsonObject.getJSONObject("User");

			message = format(post.getString("message"));

			created = parseDate(post.getString("created"));
			this.author = author.getString("username");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String format(String t) {
		String text = t.replace("[url]", "");
		text = text.replace("[/url]", "");
		text = text.replace("[img]", "");
		text = text.replace("[/img]", "");
		text = text.replace("[i]", "<i>");
		text = text.replace("[/i]", "</i>");
		text = text.replace("\n", "<br>");
		text = text.replace("[quote]", "<blockquote");
		text = text.replace("[/quote]", "</blockquote>");

		return text;
	}

	private String parseDate(String s) {
		String date = s.substring(8, 10) + "." + s.substring(5, 7) + ". "
				+ s.substring(11, 16);
		return date;
	}

}
