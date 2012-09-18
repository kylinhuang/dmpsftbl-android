package de.damps.fantasy.data;

import de.damps.fantasy.R;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Row {

	public TableRow newRow;
	public static int TOP_HEADER = 1;
	public static int MID_HEADER = 0;

	public Row(Context c, String position, TableLayout head) {

		// Row
		newRow = new TableRow(c);
		TableLayout.LayoutParams parar = new TableLayout.LayoutParams();
		parar.setMargins(0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, c.getResources()
						.getDisplayMetrics()), 0, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, c
						.getResources().getDisplayMetrics()));
		newRow.setLayoutParams(parar);

		TableRow.LayoutParams para = new TableRow.LayoutParams();
		para.setMargins((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, c.getResources()
						.getDisplayMetrics()), 0, 0, 0);

		ImageView team = new ImageView(c);
		TextView pos = new TextView(c);
		TextView name = new TextView(c);
		TextView score = new TextView(c);
		newRow.addView(team, 0);
		newRow.addView(pos, 1);
		newRow.addView(name, 2);
		newRow.addView(score, 3);

		// Team
		team.getLayoutParams().width = ((TextView) ((TableRow) head
				.getChildAt(0)).getVirtualChildAt(0)).getWidth();

		// Pos
		pos.setWidth(((TextView) ((TableRow) head.getChildAt(0))
				.getVirtualChildAt(1)).getWidth());
		pos.setLayoutParams(para);
		pos.setGravity(Gravity.CENTER);
		pos.setTextAppearance(c, R.style.text);
		pos.setTextColor(c.getResources().getColor(R.color.weis));
		pos.setBackgroundDrawable(c.getResources().getDrawable(
				R.drawable.button));
		pos.setText(position);

		// Name
		name.setLayoutParams(para);
		name.setTextAppearance(c, R.style.text);
		name.setPadding((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, c.getResources()
						.getDisplayMetrics()), 0, 0, 0);
		name.setText("empty");

		// Score
		score.setWidth(((TextView) ((TableRow) head.getChildAt(0))
				.getVirtualChildAt(3)).getWidth());
		score.setLayoutParams(para);
		score.setTextAppearance(c, R.style.text);
		score.setGravity(Gravity.RIGHT);
		score.setPadding(0, 0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, c.getResources()
						.getDisplayMetrics()), 0);
		newRow.setTag(false);
	}

	public Row(Context c, TableLayout head, int top) {

		// Row
		newRow = new TableRow(c);
		TableLayout.LayoutParams parar = new TableLayout.LayoutParams();
		parar.setMargins(0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, c.getResources()
						.getDisplayMetrics()), 0, (int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, c
						.getResources().getDisplayMetrics()));
		newRow.setLayoutParams(parar);

		TableRow.LayoutParams para = new TableRow.LayoutParams();
		para.setMargins((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2, c.getResources()
						.getDisplayMetrics()), 0, 0, 0);

		TextView team = new TextView(c);
		TextView pos = new TextView(c);
		TextView name = new TextView(c);
		TextView score = new TextView(c);
		newRow.addView(team, 0);
		newRow.addView(pos, 1);
		newRow.addView(name, 2);
		newRow.addView(score, 3);

		// Team
		team.getLayoutParams().width = ((TextView) ((TableRow) head
				.getChildAt(0)).getVirtualChildAt(0)).getWidth();
		team.setLayoutParams(para);
		team.setGravity(Gravity.CENTER);
		team.setTextAppearance(c, R.style.text);
		team.setTextColor(c.getResources().getColor(R.color.weis));
		
		team.setTextAppearance(c, R.style.column);
		team.setText("NFL");

		// Pos
		pos.setWidth(((TextView) ((TableRow) head.getChildAt(0))
				.getVirtualChildAt(1)).getWidth());
		pos.setLayoutParams(para);
		pos.setGravity(Gravity.CENTER);
		pos.setTextAppearance(c, R.style.text);
		pos.setTextColor(c.getResources().getColor(R.color.weis));
		pos.setBackgroundDrawable(c.getResources().getDrawable(
				R.drawable.column_mid));
		pos.setTextAppearance(c, R.style.column);
		pos.setText("Pos");

		// Name
		name.setLayoutParams(para);
		name.setGravity(Gravity.LEFT);
		name.setTextAppearance(c, R.style.text);
		name.setTextColor(c.getResources().getColor(R.color.weis));
		name.setBackgroundDrawable(c.getResources().getDrawable(
				R.drawable.column_mid));
		name.setTextAppearance(c, R.style.column);
		name.setText("Name");

		// Score
		score.setWidth(((TextView) ((TableRow) head.getChildAt(0))
				.getVirtualChildAt(3)).getWidth());
		score.setLayoutParams(para);
		score.setGravity(Gravity.CENTER);
		score.setTextAppearance(c, R.style.text);
		score.setTextColor(c.getResources().getColor(R.color.weis));
		
		score.setTextAppearance(c, R.style.column);
		score.setText("Score");
		if (top == 1) {
			team.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.column_left));
			score.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.column_right));
		} else {
			team.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.column_mid));
			score.setBackgroundDrawable(c.getResources().getDrawable(
					R.drawable.column_mid));
		}
	}
}
