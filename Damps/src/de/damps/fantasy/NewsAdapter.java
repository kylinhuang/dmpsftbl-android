package de.damps.fantasy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<News> {

	private ArrayList<News> threads;

	// Initialize adapter
	public NewsAdapter(Context context, int resource,
			ArrayList<News> items) {
		super(context, resource, items);
		threads = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.threaditem, null);
		}
		
		News ne = threads.get(position);
		// Get the text boxes from the listitem.xml file
		TextView title = (TextView) v
				.findViewById(R.id.tv_for_title);
		TextView date = (TextView) v
				.findViewById(R.id.tv_for_modi);

		// Assign the appropriate data from our alert object above
		title.setText(ne.title);
		date.setText(ne.date);
		return v;
	}

}
