package com.example.eventnotifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

   public DbHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
      super(context, name, factory, version);
   }

   @Override
   public void onCreate(SQLiteDatabase db) {
      String create="CREATE TABLE Events(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, nickname TEXT, event_type TEXT," +
              "date INTEGER, month INTEGER, year INTEGER)";
      db.execSQL(create);
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int i, int i1) {
         String drop= "DROP TABLE IF EXISTS";
         db.execSQL(drop,new String[]{"Events"});
         onCreate(db);
   }

   public void AddEvents(Event e)
   {
       SQLiteDatabase db=this.getWritableDatabase();
       ContentValues values=new ContentValues();

       values.put("name",e.name);
       values.put("nickname",e.nick_name);
       values.put("event_type",e.event_type);
       values.put("date",e.date);
       values.put("month",e.month);
       values.put("year",e.year);


       long k=db.insert("Events",null,values);
       db.close();
   }

    public ArrayList<Event> getEvents() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + "Events", null);

        // on below line we are creating a new array list.
        ArrayList<Event> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new Event(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(5),
                        cursor.getInt(6)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return courseModalArrayList;
    }

    // below is the method for deleting our course.
    public void deleteEvent(Event e) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete("Events", "name=? and nickname=? and event_type=?",
                new String[]{e.name,e.nick_name,e.event_type});
        db.close();
        MainActivity.CHANGED=1;
    }
}
