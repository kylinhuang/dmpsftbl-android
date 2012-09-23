package de.damps.fantasy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import de.damps.fantasy.R;
import de.damps.fantasy.data.Player;

public class PlayerAdapter extends ArrayAdapter<Player> {

	private ArrayList<Player> players;
	private Context c;

	// Initialize adapter
	public PlayerAdapter(Context context, int resource, ArrayList<Player> items) {
		super(context, resource, items);
		c = context;
		players = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.playeritem, null);
		}

		Player p = players.get(position);
		// Get the text boxes from the listitem.xml file
		TextView name = (TextView) v.findViewById(R.id.tv_player_name);
		ImageView team = (ImageView) v.findViewById(R.id.iv_player_team);

		// Assign the appropriate data from our alert object above
		name.setText(p.name);
		if (!p.nfl_abr.equals("-")) {
			int res = c.getResources().getIdentifier((p.nfl_abr).toLowerCase(),
					"drawable", "de.damps.fantasy");
			team.setImageResource(res);
		}else{
			int res = c.getResources().getIdentifier(("fa").toLowerCase(),
					"drawable", "de.damps.fantasy");
			team.setImageResource(res);
		}
		return v;
	}

}
