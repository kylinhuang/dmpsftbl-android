package de.damps.fantasy.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Player implements Parcelable {
	public int player_id, roster_id, nfl_id, fflteam_id, nflteam_id,
			starter_id, total;
	public boolean active;
	public String name, pos, nfl_nick, nfl_abr, summary, contract;
	public int[] scores = new int[17];

	private JSONObject roster, player, nflteam;
	private JSONArray score;
	public boolean locked = false;
	public long kickoff;
	public String date;
	public String hours;

	public Player(JSONObject jo) {

		try {
			locked = !jo.getBoolean("open");
		} catch (JSONException e2) {
			locked = false;
			try {
				date = ((Integer) jo.getJSONArray("open").get(0)).toString();
				hours = (String) jo.getJSONArray("open").get(2);
				int year = Integer.valueOf(date.substring(0, 4));
				int month = Integer.valueOf(date.substring(4, 6)) - 1;
				int day = Integer.valueOf(date.substring(6, 8));
				int hour = Integer.valueOf(hours.substring(0, 2));
				int minute = Integer.valueOf(hours.substring(3, 5));
				int second = Integer.valueOf(hours.substring(6, 8));
				Calendar cal = new GregorianCalendar(
						TimeZone.getTimeZone("GMT"));
				cal.set(year, month, day, hour, minute, second);
				kickoff = cal.getTime().getTime();
			} catch (JSONException e) {
				e.printStackTrace();
			}
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
			System.err.println("Kein Roster Object");
		}

		try {
			player = jo.getJSONObject("Player");

			String vorname = player.getString("firstname");
			String nachname = player.getString("name");
			name = vorname + " " + nachname;
			pos = player.getString("position");
		} catch (JSONException e) {
			try {
				player = jo.getJSONObject("Roster").getJSONObject("Player");

				String vorname = player.getString("firstname");
				String nachname = player.getString("name");
				name = vorname + " " + nachname;
				pos = player.getString("position");
			} catch (JSONException e1) {
				try {
					player = jo.getJSONObject("Playerlist");

					String vorname = player.getString("firstname");
					String nachname = player.getString("lastname");
					name = vorname + " " + nachname;
					pos = player.getString("position");
					player_id = player.getInt("id");
				} catch (JSONException e2) {
					System.err.println("Kein Player Object");
				}
			}
		}

		try {
			nflteam = jo.getJSONObject("Nflteam");

			nfl_nick = nflteam.getString("nickname");
			nfl_abr = nflteam.getString("abbr");
			nfl_id = nflteam.getInt("id");
		} catch (JSONException e) {
			try {
				nflteam = jo.getJSONObject("Roster").getJSONObject("Nflteam");

				nfl_nick = nflteam.getString("nickname");
				nfl_abr = nflteam.getString("abbr");
			} catch (JSONException e1) {
				System.err.println("Kein Player Object");
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

	public Player(Parcel in) {
		name = in.readString();
		nfl_abr = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(nfl_abr);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Player createFromParcel(Parcel in) {
			return new Player(in);
		}

		public Player[] newArray(int size) {
			return new Player[size];
		}
	};

}
