package de.damps.fantasy;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

	public String title, message, from, to;
	public boolean[] read = new boolean[2];
	public int id;

	public Message(JSONObject jsonObject) {
		SharedPreferences pref = de.damps.fantasy.HomeActivity.preferences;
		try {
			JSONObject message = jsonObject.getJSONObject("Message");
			JSONObject from = jsonObject.getJSONObject("From");
			JSONObject to = jsonObject.getJSONObject("To");
			title = message.getString("subject");
			this.message = message.getString("content");
			id = message.getInt("id");
			this.from = from.getString("username");
			this.to = to.getString("username");
			read[0] = message.getBoolean("read");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (((Integer) id).toString().equals(pref.getString("id", "x"))) {
			read[1] = true;
		}

	}

	public Message(String t, String m, String to) {
		read[1] = true;
		title = t;
		message = m;
		this.to = to;
	}

	public Message(Parcel in) {
		title = in.readString();
		message = in.readString();
		from = in.readString();
		to = in.readString();
		in.readBooleanArray(read);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(title);
		dest.writeString(message);
		dest.writeString(from);
		dest.writeString(to);
		dest.writeBooleanArray(read);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Message createFromParcel(Parcel in) {
			return new Message(in);
		}

		public Message[] newArray(int size) {
			return new Message[size];
		}
	};

}
