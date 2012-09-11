package de.damps.fantasy.adapter;

import java.util.ArrayList;


import de.damps.fantasy.data.DampsTeam;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OwnerAdapter extends ArrayAdapter<DampsTeam> {

	private ArrayList<DampsTeam> teams;

	// Initialize adapter
	public OwnerAdapter(Context context, int resource,
			ArrayList<DampsTeam> items) {
		super(context, resource, items);
		teams = items;

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(android.R.layout.simple_spinner_item, null);
		}
		
		DampsTeam t = teams.get(position);
		// Get the text boxes from the listitem.xml file
		TextView name = (TextView) v
				.findViewById(android.R.id.text1);
		

		// Assign the appropriate data from our alert object above
		name.setText(t.owner);
		return v;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(android.R.layout.simple_spinner_item, null);
		}
		
		DampsTeam t = teams.get(position);
		// Get the text boxes from the listitem.xml file
		TextView name = (TextView) v
				.findViewById(android.R.id.text1);
		

		// Assign the appropriate data from our alert object above
		name.setText(t.owner);
		return v;
	}

}
