package com.example.calendaretna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {



    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE "+DBStructure.EVENT_TABLE_NAME+"(IDEVENT INTEGER PRIMARY KEY AUTOINCREMENT, "+DBStructure.EVENTNAME+" TEXT, "+DBStructure.TIME+" TEXT, "+DBStructure.DATE+" TEXT, "+DBStructure.MONTH+" TEXT, "+DBStructure.YEAR+" TEXT, "+DBStructure.Notify+" TEXT, "+DBStructure.ID_EVENT_CREATOR+" TEXT)";

    private static final String DROP_EVENTS_TABLE= "DROP TABLE IF EXISTS "+DBStructure.EVENT_TABLE_NAME;

    private static final String CREATE_GUESTS_TABLE = "CREATE TABLE "+DBStructure.GUESTS_TABLE_NAME+"("+DBStructure.IDEVENT+" TEXT, "+DBStructure.IDGUEST+" TEXT, "+DBStructure.DATE+" TEXT)";

    private static final String DROP_GUESTS_TABLE= "DROP TABLE IF EXISTS "+DBStructure.GUESTS_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_GUESTS_TABLE);

    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
        sqLiteDatabase.execSQL(DROP_GUESTS_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void SaveEvent(String eventName, String time, String date, String month, String year, String notify, String idEventCreator,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.EVENTNAME, eventName);
        contentValues.put(DBStructure.TIME, time);
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        contentValues.put(DBStructure.Notify, notify);
        contentValues.put(DBStructure.ID_EVENT_CREATOR, idEventCreator);
        database.insert(DBStructure.EVENT_TABLE_NAME, null, contentValues);
    }

    public Cursor ReadEvents(String date, String IDEventCreator, SQLiteDatabase database){
        String [] Projections = {DBStructure.EVENTNAME, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.ID_EVENT_CREATOR};
        String Selection = DBStructure.DATE + "=? and "+DBStructure.ID_EVENT_CREATOR+"=?";
        String[] SelectionArgs = {date, IDEventCreator};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }

    public Cursor ReadIDEvents(String date,String event, String time, SQLiteDatabase database){
        String [] Projections = {DBStructure.IDEVENT, DBStructure.Notify};
        String Selection = DBStructure.DATE + "=? and "+DBStructure.EVENTNAME+"=? and "+DBStructure.TIME+"=?";
        String[] SelectionArgs = {date, event, time};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }




    public Cursor ReadEventsperMonth(String month, String year, String IDEventCreator, SQLiteDatabase database){
        String [] Projections = {DBStructure.EVENTNAME, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.ID_EVENT_CREATOR};
        String Selection = DBStructure.MONTH + "=? and "+DBStructure.YEAR+"=? and "+DBStructure.ID_EVENT_CREATOR+"=?";
        String[] SelectionArgs = {month, year, IDEventCreator};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }

    public Cursor ReadEventsperMonthID(String IDEvent,String month, String year, SQLiteDatabase database){
        String [] Projections = {DBStructure.EVENTNAME, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.ID_EVENT_CREATOR};
        String Selection = DBStructure.IDEVENT + "=? and "+DBStructure.MONTH+"=? and "+DBStructure.YEAR+"=?";
        String[] SelectionArgs = {IDEvent, month, year};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }

    public Cursor ReadEventsGuestperMonth(String month, String year, String IDEventCreator, SQLiteDatabase database){
        String [] Projections = {DBStructure.EVENTNAME, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.ID_EVENT_CREATOR};
        String Selection = DBStructure.MONTH + "=? and "+DBStructure.YEAR+"=? and "+DBStructure.ID_EVENT_CREATOR+"=?";
        String[] SelectionArgs = {month, year, IDEventCreator};

        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database){
        String selection = DBStructure.EVENTNAME+"=? and "+DBStructure.DATE+"=? and "+DBStructure.TIME+"=?";
        String[] selectionArg = {event, date, time};
        database.delete(DBStructure.EVENT_TABLE_NAME, selection, selectionArg);
    }

    public void updateEvent(String date, String event, String time, String notify, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.Notify, notify);
        String Selection = DBStructure.DATE + "=? and "+DBStructure.EVENTNAME+"=? and "+DBStructure.TIME+"=?";
        String[] SelectionArgs = {date, event, time};
        database.update(DBStructure.EVENT_TABLE_NAME, contentValues, Selection, SelectionArgs);
    }


    public void SaveGuest(String date, String IDEvent, String IDGuest,SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.DATE, date);
        contentValues.put(DBStructure.IDEVENT, IDEvent);
        contentValues.put(DBStructure.IDGUEST, IDGuest);
        database.insert(DBStructure.GUESTS_TABLE_NAME, null, contentValues);
    }

    public Cursor getIDEventfromGuestTab(String date, String IDGuest, SQLiteDatabase database){
        String [] Projections = {DBStructure.IDEVENT};
        String Selection = DBStructure.DATE + "=? and "+DBStructure.IDGUEST+"=?";;
        String[] SelectionArgs = {date, IDGuest};
        return database.query(DBStructure.GUESTS_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }
    public Cursor getEventByID(String IDEvent,String date, SQLiteDatabase database){
        String [] Projections = {DBStructure.EVENTNAME, DBStructure.TIME, DBStructure.DATE, DBStructure.MONTH, DBStructure.YEAR, DBStructure.ID_EVENT_CREATOR};
        String Selection = DBStructure.IDEVENT + "=? and "+DBStructure.DATE+"=?";
        String[] SelectionArgs = {IDEvent, date};
        return database.query(DBStructure.EVENT_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }


    public Cursor getIDEventGuest(String IDGuest, SQLiteDatabase database){
        String [] Projections = {DBStructure.IDEVENT};
        String Selection = DBStructure.IDGUEST+"=?";;
        String[] SelectionArgs = {IDGuest};
        return database.query(DBStructure.GUESTS_TABLE_NAME, Projections, Selection, SelectionArgs, null, null, null );
    }
}