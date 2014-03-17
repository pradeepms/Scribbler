package in.pradeepms.scribbler;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Scribbler extends Activity implements OnItemClickListener,
		OnClickListener, OnItemLongClickListener {
	Cursor model;
	RestaurantAdapter adapter = null;
	NotesHelper helper;
	EditText name, address, searchBox = null;
	ImageButton add,search;
	ListView list;
	int count = 0;
	public final static long ID = 1;
	public final static String ID_EXTRA = "pradeepms.scribbler.app";

	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		helper = new NotesHelper(this);

		add = (ImageButton) findViewById(R.id.add);
		search = (ImageButton) findViewById(R.id.search);
		searchBox = (EditText) findViewById(R.id.searchBox);

		list = (ListView) findViewById(R.id.list);

		add.setOnClickListener(this);
		search.setOnClickListener(this);

		model = helper.AllRecord();
		startManagingCursor(model);
		adapter = new RestaurantAdapter(model);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		registerForContextMenu(list);
		
		searchBox.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Scribbler.this.adapter.getFilter().filter(s.toString());
			}
		});
		


	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}

	class RestaurantAdapter extends CursorAdapter {

		@SuppressWarnings("deprecation")
		public RestaurantAdapter(Cursor c) {
			super(Scribbler.this, c);
		}


		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			// TODO Auto-generated method stub
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);

		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.rowlist, parent, false);

			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			return (row);

		}

		class RestaurantHolder {

			private TextView name, listDate = null;

			// private ImageView icon = null;

			RestaurantHolder(View row) {

				name = (TextView) row.findViewById(R.id.title);
				listDate = (TextView) row.findViewById(R.id.listDate);
				/*
				 * icon = (ImageView) row.findViewById(R.id.icon); date =
				 * (TextView) row.findViewById(R.id.date);
				 */
			}

			public void populateFrom(Cursor c, NotesHelper helper) {

				/*
				 * Typeface droid = Typeface.createFromAsset(getAssets(),
				 * "droid_sans.ttf");
				 */

				name.setText(helper.getTitle(c));
				listDate.setText(helper.getTime(c));

			}

		}

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Scribbler.this, AddNote.class);
		i.putExtra("ID", Long.valueOf(id));
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.add:
			Log.d("Serach button", "clicked");
			Intent i = new Intent(Scribbler.this, AddNote.class);
			startActivity(i);

			break;
		
		  case R.id.search:
			  Log.d("Serach button", "clicked");
				searchBox.setVisibility(View.VISIBLE);
		  
		 
		  
		  break;
		 

		}

	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");
		menu.add(0, v.getId(), 0, "Edit");
		menu.add(0, v.getId(), 0, "Delete");

	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {

		if (item.getTitle() == "Edit") {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();

			Intent i = new Intent(Scribbler.this, AddNote.class);

			i.putExtra(ID_EXTRA, String.valueOf(info.id));
			startActivity(i);

		} else if (item.getTitle() == "Delete") {

			new AlertDialog.Builder(this)
					.setTitle("Confirm Delete")
					.setMessage("Do you want to delete this??")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@SuppressWarnings("deprecation")
								public void onClick(
										DialogInterface dialogInterface, int i) {
									AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
											.getMenuInfo();
									helper.delete(info.id);
									model.requery();

								}
							}).setNeutralButton("Cancel", null).create().show();

		} else {
			return false;
		}
		return true;
	}
	

}