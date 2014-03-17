package in.pradeepms.scribbler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class AddNote extends Activity implements OnClickListener {

	EditText textTitle, textContent = null;
	String restaurantId = null;
	NotesHelper helper;
	Button save;
	TextView date, day, dayEntries;
	long ID;int count =0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_text);

		helper = new NotesHelper(this);

		textTitle = (EditText) findViewById(R.id.textTitle);
		textContent = (EditText) findViewById(R.id.textContent);
		date = (TextView) findViewById(R.id.grandDate);
		day = (TextView) findViewById(R.id.grandDay);
		dayEntries = (TextView) findViewById(R.id.dateEntries);
		Date theDate = Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
		String dayInNum = dateFormat.format(theDate);
		String dayInLet = dayFormat.format(theDate);
		System.out.println(dayInNum);
		System.out.println(dayInLet);

		date.setText(dayInNum);
		day.setText(dayInLet.toUpperCase());
		Typeface mvboli = Typeface.createFromAsset(getAssets(), "architect.ttf");
		textContent.setTypeface(mvboli);
		textTitle.setTypeface(mvboli);
		// textTitle.setHintTextColor();

		save = (Button) findViewById(R.id.save);

		restaurantId = getIntent().getStringExtra(Scribbler.ID_EXTRA);
		ID = getIntent().getLongExtra("ID", 1);
		System.out.println(ID);
		if (restaurantId != null) {
			load();
		}

		save.setOnClickListener(this);
	}

	private void load() {
		// TODO Auto-generated method stub

		save.setText("edit");

		textContent.setFocusable(false);
		textContent.setClickable(false);
		textContent.setFocusableInTouchMode(false);

		textTitle.setFocusable(false);
		textTitle.setClickable(false);
		textTitle.setFocusableInTouchMode(false);

		Cursor c = helper.getById(restaurantId);
		c.moveToFirst();
		textTitle.setText(helper.getTitle(c));
		textContent.setText(helper.getContent(c));
		dayEntries.setText(helper.getCreatedDate(c));
		c.close();
	}

	@SuppressLint("SimpleDateFormat")
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Date theDate = Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
		String date = dateFormat.format(theDate); // On list view

		if (restaurantId == null) {
			SimpleDateFormat created = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
			String createdDate = created.format(theDate);

			helper.insert(textTitle.getText().toString(), textContent.getText()
					.toString(), date, "Created on "+createdDate.toString());
			count++;
			finish();
		} else {

			if (save.getText().toString() == "edit") {
				save.setText("save");

				textContent.setClickable(true);
				textContent.setFocusableInTouchMode(true);

				textTitle.setFocusable(true);
				textTitle.setClickable(true);
				textTitle.setFocusableInTouchMode(true);

			}
			SimpleDateFormat modified = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
			String modifiedDate = modified.format(theDate);
			helper.update(restaurantId, textTitle.getText().toString(),
					textContent.getText().toString(), date, "Modified on "+modifiedDate.toString());

			Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT)
					.show();

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	public static class LinedEditText extends EditText {
		private Rect mRect;	
		private Paint mPaint;

		// we need this constructor for LayoutInflater
		public LinedEditText(Context context, AttributeSet attrs) {
			super(context, attrs);

			mRect = new Rect();
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(Color.parseColor("#585856"));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int height = getHeight();
			int line_height = getLineHeight();

			int count = height / line_height;

			if (getLineCount() > count)
				count = getLineCount();// for long text with scrolling

			Rect r = mRect;
			Paint paint = mPaint;
			int baseline = getLineBounds(0, r);// first line

			for (int i = 0; i < count; i++) {

				canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1,
						paint);
				baseline += getLineHeight();// next line
			}

			super.onDraw(canvas);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		new MenuInflater(this).inflate(R.menu.addnote_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.delete) {

			new AlertDialog.Builder(this)
					.setTitle("Confirm Delete")
					.setMessage("Do you want to delete this??")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(
										DialogInterface dialogInterface, int i) {

									helper.delete(ID);
									count--;
									finish();

								}
							}).setNeutralButton("Cancel", null).create().show();

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
	}

}