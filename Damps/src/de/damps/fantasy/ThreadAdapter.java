package de.damps.fantasy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThreadAdapter extends ArrayAdapter<MyThread> {

	private ArrayList<MyThread> threads;

	// Initialize adapter
	public ThreadAdapter(Context context, int resource,
			ArrayList<MyThread> items) {
		super(context, resource, items);
		threads = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.threaditem, null);
		}

		MyThread th = threads.get(position);
		// Get the text boxes from the listitem.xml file
		TextView title = (TextView) v.findViewById(R.id.tv_for_title);
		TextView modified = (TextView) v.findViewById(R.id.tv_for_modi);

		// Assign the appropriate data from our alert object above
		title.setText(th.title);
		modified.setText("Letzter Beitrag: " + th.last + " " + th.modified);
		return v;
	}

}