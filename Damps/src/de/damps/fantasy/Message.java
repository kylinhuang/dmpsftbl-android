package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable{

	public String title, message, sender;
	public boolean read, inbound;

	public Message(JSONObject jsonObject) {
		try {
			JSONObject jo = jsonObject.getJSONObject("Message");
			title = jo.getString("title");
			message = jo.getString("message");
			sender = jo.getString("sender");
			read = jo.getBoolean("read");
			inbound = jo.getBoolean("inbound");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
