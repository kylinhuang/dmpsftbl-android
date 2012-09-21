package de.damps.fantasy.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Team {

	public String team;
	public ArrayList<Player> starters = new ArrayList<Player>();
	public boolean flex;
	private JSONArray starter;
	private boolean[] set = new boolean[8];

	public Team(JSONObject team) {
		int flex_for = 0;
		try {
			flex_for = team.getJSONObject("Teamstarter").getInt("is_flex");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		if (flex_for != 0) {
			flex = true;
		}

		starters.add(new Player("QB"));
		starters.add(new Player("RB"));
		switch (flex_for) {
		case 0:
			starters.add(new Player("RB"));
			starters.add(new Player("WR"));
			starters.add(new Player("WR"));

			break;
		case 311:
			starters.add(new Player("RB"));
			starters.add(new Player("RB"));
			starters.add(new Player("WR"));

			break;
		case 131:
			starters.add(new Player("WR"));
			starters.add(new Player("WR"));
			starters.add(new Player("WR"));

			break;
		case 122:
			starters.add(new Player("WR"));
			starters.add(new Player("WR"));
			starters.add(new Player("TE"));
			break;
		case 212:
			starters.add(new Player("RB"));
			starters.add(new Player("WR"));
			starters.add(new Player("TE"));
			break;
		case 113:
			starters.add(new Player("WR"));
			starters.add(new Player("TE"));
			starters.add(new Player("TE"));
			break;
		}
		starters.add(new Player("TE"));
		starters.add(new Player("K"));
		starters.add(new Player("DEF"));

		try {
			this.team = team.getJSONObject("Fflteam").getString("name");
			starter = team.getJSONArray("Starter");
			for (int i = 0; i < starter.length(); i++) {
				Player p = new Player(starter.getJSONObject(i));

				for (int j = 0; j < 8; j++) {
					if (p.pos.equals(starters.get(j).pos) && !set[j]) {
						starters.set(j, p);
						set[j] = true;
						break;
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
