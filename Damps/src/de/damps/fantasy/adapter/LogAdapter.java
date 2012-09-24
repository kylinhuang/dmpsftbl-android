package de.damps.fantasy.adapter;

import java.util.ArrayList;

import de.damps.fantasy.R;
import de.damps.fantasy.data.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LogAdapter extends ArrayAdapter<Log> {

	private ArrayList<Log> logs;

	// Initialize adapter
	public LogAdapter(Context context, int resource, ArrayList<Log> items) {
		super(context, resource, items);
		logs = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		// Inflate the view
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.logitem, null);
		}

		Log l = logs.get(position);
		// Get the text boxes from the listitem.xml file
		TextView data = (TextView) v.findViewById(R.id.tv_logitem_data);
		TextView time = (TextView) v.findViewById(R.id.tv_logitem_time);
		ImageView action = (ImageView) v.findViewById(R.id.iv_logitem_action);

		// Assign the appropriate data from our alert object above
		data.setText(l.data);
		time.setText(l.time);

		// TODO ICONS
		switch (l.action) {
		case de.damps.fantasy.data.Log.SIGNED_PLAYER:
			action.setImageResource(R.drawable.accept);
			break;
		case de.damps.fantasy.data.Log.RELEASED_PLAYER:
			action.setImageResource(R.drawable.accept);
			break;
		case de.damps.fantasy.data.Log.PLACED_PLAYER_ON_TRADELIST:
			action.setImageResource(R.drawable.list_add);
			break;
		case de.damps.fantasy.data.Log.REMOVED_PLAYER_FROM_TRADELIST:
			action.setImageResource(R.drawable.list_remove);
			break;
		case de.damps.fantasy.data.Log.OFFERED_TRADE:
			action.setImageResource(R.drawable.offer_trade);
			break;
		case de.damps.fantasy.data.Log.CANCELED_TRADE:
			action.setImageResource(R.drawable.cancel_trade);
			break;
		case de.damps.fantasy.data.Log.ACCEPTED_TRADE:
			action.setImageResource(R.drawable.accept);
			break;
		default:
			break;
		}
		
		return v;
	}
}