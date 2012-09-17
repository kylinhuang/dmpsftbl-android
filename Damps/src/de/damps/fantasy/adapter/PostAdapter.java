package de.damps.fantasy.adapter;

import java.util.ArrayList;

import de.damps.fantasy.*;
import de.damps.fantasy.data.Post;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PostAdapter extends ArrayAdapter<Post> {

	private ArrayList<Post> posts;

	// Initialize adapter
	public PostAdapter(Context context, int resource, ArrayList<Post> items) {
		super(context, resource, items);
		posts = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.postitem, null);
		}

		Post p = posts.get(position);
		TextView author = (TextView) v.findViewById(R.id.tv_pos_author);
		TextView msg = (TextView) v.findViewById(R.id.tv_pos_message);
		TextView date = (TextView) v.findViewById(R.id.tv_pos_date);

		author.setText(p.author);
		msg.setText(Html.fromHtml(p.message));
		date.setText(p.created);
		return v;
	}
}