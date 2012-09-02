package fantasyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Team {

	public String team;
	public Player[] starters = new Player[9]; 
	//QB, RB1, RB2, WR1, WR2, TE, FLEX, K, DEF
	private Boolean RB_1 = true, RB_2 = true, WR_1 = true, WR_2= true, T = true, Flex = true;
	private JSONArray starter;

	public Team(JSONObject team) {
		starters[0] = new Player("QB");
		starters[1] = new Player("RB");
		starters[2] = new Player("RB");
		starters[3] = new Player("WR");
		starters[4] = new Player("WR");
		starters[5] = new Player("TE");
		starters[6] = new Player("FLEX");
		starters[7] = new Player("K");
		starters[8] = new Player("DEF");
		try {
			this.team = team.getJSONObject("Fflteam").getString("name");
			starter = team.getJSONArray("Starter");
			for (int i = 0; i < starter.length(); i++) {
				Player p = new Player(starter.getJSONObject(i), "starter");
				if (p.pos.equals("QB")) {
					starters[0] = p;
				} else if (p.pos.equals("RB")) {
					if (RB_1) {
						starters[1] = p;
						RB_1 = false;
					} else if (RB_2) {
						starters[2] = p;
						RB_2 = false;
					} else if (Flex) {
						starters[6] = p;
						Flex = false;
					}
				} else if (p.pos.equals("WR")) {
					if (WR_1) {
						starters[3] = p;
						WR_1 = false;
					} else if (WR_2) {
						starters[4] = p;
						WR_2 = false;
					} else if (Flex) {
						starters[6] = p;
						Flex = false;
					}
				} else if (p.pos.equals("TE")) {
					if (T) {
						starters[5] = p;
						T = false;
					} else if (Flex) {
						starters[6] = p;
						Flex = false;
					}
				} else if (p.pos.equals("K")) {
					starters[7] = p;
				} else if (p.pos.equals("DEF")) {
					starters[8] = p;
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Team() {
		starters[0] = new Player("QB");
		starters[1] = new Player("RB");
		starters[2] = new Player("RB");
		starters[3] = new Player("WR");
		starters[4] = new Player("WR");
		starters[5] = new Player("TE");
		starters[6] = new Player("FLEX");
		starters[7] = new Player("K");
		starters[9] = new Player("DEF");
	}

}
