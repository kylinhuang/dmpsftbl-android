package de.damps.fantasy.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Player {
	public int player_id, roster_id, nfl_id, fflteam_id, nflteam_id, starter_id, total;
	public boolean active;
	public String name, pos, nfl_nick, nfl_city, nfl_abr, summary, contract;
	public int[] scores = new int[17];

	private JSONObject roster, player, nflteam;
	private JSONArray score;
	public boolean locked = false;

	public Player(JSONObject jo) {
		
		try {
			locked = !jo.getBoolean("open");
		} catch (JSONException e2) {
			locked = false;
		}
		
		try {
			starter_id = jo.getInt("id");
		} catch (JSONException e2) {
			Log.i("STARTER", "Kein Starter");
		}

		try {
			roster = jo.getJSONObject("Roster");

			player_id = roster.getInt("player_id");
			active = roster.getBoolean("active");
			nfl_id = roster.getInt("id");
			fflteam_id = roster.getInt("fflteam_id");
			nflteam_id = roster.getInt("nflteam_id");
			roster_id = roster.getInt("id");
			contract = roster.getString("contract");
		} catch (JSONException e) {
			System.err.println("Kein Roster Objetc");
		}

		try {
			player = jo.getJSONObject("Player");

			String vorname = player.getString("firstname");
			String nachname = player.getString("name");
			name = vorname + " " + nachname;
			pos = player.getString("position");
		} catch (JSONException e) {
			try {
				player = roster.getJSONObject("Player");

				String vorname = player.getString("firstname");
				String nachname = player.getString("name");
				name = vorname + " " + nachname;
				pos = player.getString("position");
			} catch (JSONException e1) {
				System.err.println("Kein Player Objetc");
			}
		}

		try {
			nflteam = jo.getJSONObject("Nflteam");

			nfl_nick = nflteam.getString("nickname");
			nfl_city = nflteam.getString("name");
			nfl_abr = nflteam.getString("abbr");
		} catch (JSONException e) {
			try {
				nflteam = roster.getJSONObject("Nflteam");

				nfl_nick = nflteam.getString("nickname");
				nfl_city = nflteam.getString("name");
				nfl_abr = nflteam.getString("abbr");
			} catch (JSONException e1) {
				System.err.println("Kein Player Objetc");
			}
		}

		try {
			score = jo.getJSONArray("Score");
			for (int i = 0; i < score.length(); i++) {
				int gd;
				try {
					gd = score.getJSONObject(i).getJSONObject("Teamstarter")
							.getInt("gameday");
					scores[gd - 1] = score.getJSONObject(i)
							.getJSONObject("Starter").getInt("score");
					total += scores[gd - 1];
				} catch (JSONException e) {
					System.err.println("Fehlender Gameday Score");
				}

			}
		} catch (JSONException e) {
			System.err.println("Kein Score Object");
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
