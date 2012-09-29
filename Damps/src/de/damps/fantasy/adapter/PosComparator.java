package de.damps.fantasy.adapter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import de.damps.fantasy.data.Player;

public class PosComparator implements Comparator<Player> {

	private Map<String, Integer> orderIndex;

	public PosComparator() {

		orderIndex = new HashMap<String, Integer>();

		orderIndex.put("QB", 0);
		orderIndex.put("RB", 1);
		orderIndex.put("WR", 2);
		orderIndex.put("TE", 3);
		orderIndex.put("K", 4);
		orderIndex.put("DEF", 5);

	}

	@Override
	public int compare(Player lhs, Player rhs) {
		return orderIndex.get(lhs.pos) - orderIndex.get(rhs.pos);
	}

}
