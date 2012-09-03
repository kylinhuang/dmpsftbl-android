package de.damps.fantasy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {

	private ArrayList<Message> messages;

	// Initialize adapter
	public MessageAdapter(Context context, int resource,
			ArrayList<Message> items) {
		super(context, resource, items);
		messages = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.messageitem, null);
		}

		Message m = messages.get(position);
		// Get the text boxes from the listitem.xml file
		TextView title = (TextView) v.findViewById(R.id.tv_mes_title);
		TextView sender = (TextView) v.findViewById(R.id.tv_mes_sender);
		ImageView image = (ImageView) v.findViewById(R.id.iv_mes_read);

		// Assign the appropriate data from our alert object above
		title.setText(m.title);
		sender.setText(m.sender);
		if(m.read){
			image.setImageResource(R.drawable.content_read);
		}else{
			image.setImageResource(R.drawable.content_unread);
		}
		return v;
	}

}