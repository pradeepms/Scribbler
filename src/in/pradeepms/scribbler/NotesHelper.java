package in.pradeepms.scribbler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "scribbler.db";
	private static final int VERSION = 1;

	public NotesHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, time TEXT, createDate INTEGER);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public void insert(String title, String content, String time, String createDate) {
		ContentValues cv = new ContentValues();
		cv.put("title", title);
		cv.put("content", content);
		cv.put("time", time);
		cv.put("createDate", createDate);
		getWritableDatabase().insert("notes", "title", cv);
	}

	public Cursor AllRecord() {

		return (getReadableDatabase()
				.rawQuery(
						"SELECT _id, title, content, time, createDate FROM notes ORDER BY title",
						null));

	}
	public Cursor getById(String id) {
		String[] args={id};
		return(getReadableDatabase()
		.rawQuery("SELECT _id, title, content, time, createDate FROM notes WHERE _ID=?", args));
		}
	
	public void update(String id, String title, String content, String time, String modiDate) {
			ContentValues cv=new ContentValues();
			String[] args={id};
			cv.put("title", title);
			cv.put("content", content);
			cv.put("time", time);
			cv.put("createDate", modiDate);
			
			getWritableDatabase().update("notes", cv, "_ID=?",
			args);
			}

	public String getTitle(Cursor c) {
		
		return (c.getString(1));
	}

	public String getContent(Cursor c) {
		return (c.getString(2));
	}

	public String getTime(Cursor c) {
		return (c.getString(3));
	}
	
	public String getCreatedDate(Cursor c) {
		return (c.getString(4));
	}
	
	public void delete(long id){
		getWritableDatabase().delete("notes", "_id" + "="+id, null);
	}
	

}
