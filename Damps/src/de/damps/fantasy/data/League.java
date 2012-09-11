package de.damps.fantasy.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class League {
	public ArrayList<DampsTeam> league = new ArrayList<DampsTeam>();
	public int size;
	
	public League(JSONObject jo) {
		try {
			JSONArray joa = jo.getJSONArray("Fflteams");
			for(int i=0;i<joa.length();i++){
				league.add(new DampsTeam(joa.getJSONObject(i)));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
			if(league.get(i).owner.equals(o)){
				oid = league.get(i).owner_id;
			}
		}
		return oid;
	}

	public int getPosition(String o) {
		for(int i=0;i<league.size();i++){
			if(league.get(i).owner.equals(o)){
				return i;
			}
		}
		return 0;
	}

	public String getTeamidByOwnerid(String oid) {
		String tid = null;
		for(int i=0;i<league.size();i++){
			if(league.get(i).owner_id.equals(oid)){
				tid = league.get(i).team_id;
			}
		}
		return tid;
	}

	public String getTeamByOwnerid(String oid) {
		String t = null;
		for(int i=0;i<league.size();i++){
			if(league.get(i).owner_id.equals(oid)){
				t = league.get(i).team;
			}
		}
		return t;
	}

}
