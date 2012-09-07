package de.damps.fantasy.data;

import java.util.ArrayList;

import org.json.JSONObject;

public class League {
	public ArrayList<DampsTeam> league = new ArrayList<DampsTeam>();
	public int size;
	
	public League(JSONObject jo) {

		size = league.size();
	}

	public String getTeamidByTeam(String t){
		String tid = null;
		for(int i=0;i<league.size();i++){
			if(league.get(i).team.equals(t)){
				tid = league.get(i).team_id;
			}
		}
		return tid;
	}
	
	public String getOwneridByOwner(String o){
		String oid = null;
		for(int i=0;i<league.size();i++){
			if(league.get(i).owner_id.equals(o)){
				oid = league.get(i).owner_id;
			}
		}
		return oid;
	}

}
