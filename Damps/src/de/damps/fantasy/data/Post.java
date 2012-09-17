package de.damps.fantasy.data;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {

	public String message, author, created;

	public Post(JSONObject jsonObject) {
		try {
			final JSONObject post = jsonObject.getJSONObject("Forum");
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

	private String format(String t) {
		String[] styleb = { "<i>", "<b>", "<u>", "<sup>", "<sub>" };
		String[] stylee = { "</i>", "</b>", "</u>", "</sup>", "</sub>" };
		int counter = 0;

		String text = t.replace("[url]", "");
		text = text.replace("[/url]", "");
		text = text.replace("[img]", "");
		text = text.replace("[/img]", "");

		while (text.contains("[quote]") || text.contains("[/quote]")) {

			if (text.indexOf("[quote]") < text.indexOf("[/quote]")
					&& text.contains("[quote]")) {
				text =  text.replaceFirst(Pattern.quote("[quote]"), "<br/>" + styleb[counter % 5]);
				counter++;
			}
			if (text.indexOf("[/quote]") < text.indexOf("[quote]")) {
				counter--;
				text = text.replaceFirst(Pattern.quote("[/quote]"), stylee[counter % 5]
						+ "<br/><br/>");
			}
			if (text.contains("[/quote]") && !text.contains("[quote]")) {
				counter--;
				text = text.replaceFirst(Pattern.quote("[/quote]"), stylee[counter % 5]
						+ "<br/><br/>");
				
			}
		}

		return text;
	}

}
