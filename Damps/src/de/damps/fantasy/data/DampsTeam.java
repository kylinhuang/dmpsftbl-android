package de.damps.fantasy.data;

import org.json.JSONException;
import org.json.JSONObject;

public class DampsTeam {
	public String owner, team, owner_id, team_id;

	public DampsTeam(JSONObject jo) {
		try {
			team_id = jo.getJSONObject("Fflteam").getString("id");
			team = jo.getJSONObject("Fflteam").getString("name");
			owner_id = jo.getJSONObject("User").getString("id");
			owner = jo.getJSONObject("User").getString("username");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
