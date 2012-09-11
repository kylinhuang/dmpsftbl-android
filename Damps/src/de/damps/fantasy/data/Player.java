package de.damps.fantasy.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Player {
	public int player_id, roster_id, nfl_id, fflteam_id, nflteam_id, total;
	public boolean active;
	public String name, pos, nfl_nick, nfl_city, nfl_abr, summary;
	public int[] scores = new int[17];
	
	private JSONObject roster,player, nflteam;
	private JSONArray score;
	
	public Player(JSONObject jo) {

		try {
			roster = jo.getJSONObject("Roster");
			player = jo.getJSONObject("Player");
			score = jo.getJSONArray("Score");
			nflteam = jo.getJSONObject("Nflteam");
			player_id = roster.getInt("player_id");
			active = roster.getBoolean("active");
			nfl_id = roster.getInt("id");
			fflteam_id = roster.getInt("fflteam_id");
			nflteam_id = roster.getInt("nflteam_id");
			roster_id = roster.getInt("id");

			String vorname = player.getString("firstname");
			String nachname = player.getString("name");
			name = vorname + " " + nachname;
  			pos = player.getString("position");
			nfl_nick = nflteam.getString("nickname");
			nfl_city = nflteam.getString("name");
			nfl_abr = nflteam.getString("abbr");
			
		} catch (JSONException e) {
			System.err.println("Fehler mit dem Starter");
		}
		for (int i = 0; i < score.length(); i++) {
			int gd;
			try {
				gd = score.getJSONObject(i).getJSONObject("Teamstarter").getInt("gameday");
				scores[gd-1] = score.getJSONObject(i).getJSONObject("Starter").getInt("score");
				total += scores[gd-1];
			} catch (JSONException e) {
				System.err.println("Fehler im Gameday Score");

			}

		}
		summary = ((Integer) total).toString();
	}

	public Player(String s) {
		pos = s;
		name = "";
		nfl_abr = "";
	}

	public Player(JSONObject jo, String string) {
		try {
			player = jo.getJSONObject("Player");
			nflteam = jo.getJSONObject("Nflteam");
			pos = jo.getString("position");
			String vorname = player.getString("firstname");
			String nachname = player.getString("name");
			name = vorname + " " + nachname;

			nfl_abr = nflteam.getString("abbr");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
