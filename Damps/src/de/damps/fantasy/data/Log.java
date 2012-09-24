package de.damps.fantasy.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Log {
	final public static int SIGNED_PLAYER = 1;
	final public static int RELEASED_PLAYER = -1;
	final public static int PLACED_PLAYER_ON_TRADELIST = 2;
	final public static int REMOVED_PLAYER_FROM_TRADELIST = -2;
	final public static int OFFERED_TRADE = 3;
	final public static int CANCELED_TRADE = -3;
	final public static int REJECTED_TRADE = -3;
	final public static int ACCEPTED_TRADE = 0;
	
	public int action;
	public String data;
	public String time;
	
	public Log(JSONObject jo) {
		try {
			JSONObject l = jo.getJSONObject("Tradelog");
			data = l.getString("logdata");
			time = parseDate(l.getString("created"));
			
			if(data.contains("signed")){
				action = SIGNED_PLAYER;
			}else if(data.contains("released")){
				action = RELEASED_PLAYER;
			}else if(data.contains("placed")){
				action = PLACED_PLAYER_ON_TRADELIST;
			}else if(data.contains("removed")){
				action = REMOVED_PLAYER_FROM_TRADELIST;
			}else if(data.contains("offered")){
				action = OFFERED_TRADE;
			}else if(data.contains("canceled")){
				action = CANCELED_TRADE;
			}else if(data.contains("rejected")){
				action = REJECTED_TRADE;
			}else if(data.contains("accepted")){
				action = ACCEPTED_TRADE;
			}
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
