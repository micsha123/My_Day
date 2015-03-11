package com.project.malina.myday.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SQLDatabaseHelper {

    private static final String TAG = SQLDatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myday.db";

    private static final String TABLE_ACTIONS = "actions_table";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ACTION = "action";
    private static final String COLUMN_BUTTON = "button";

    private static final String TABLE_JOURNAL = "journal_table";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_START = "start";
    private static final String COLUMN_STOP = "stop";
    private static final String COLUMN_TIME = "time";

    private DatabaseOpenHelper openHelper;
    private SQLiteDatabase database;

    DateTime date = DateTime.now();

    public String getCurrentPoint(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yy-MM-dd HH:mm:ss");
        String pointString = fmt.print(date);
        return pointString;
    }

    public String getCurrentDay(){
        DateTime date = DateTime.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yy");
        String dateString = fmt.print(date);
        return dateString;
    }

    public SQLDatabaseHelper(Context aContext) {
        openHelper = new DatabaseOpenHelper(aContext);
        database = openHelper.getWritableDatabase();
    }

    public void insertAction(String aTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACTION, aTitle);
        contentValues.put(COLUMN_BUTTON, "0");
        database.insert(TABLE_ACTIONS, null, contentValues);
    }

    public void updateAction(String id, String v1){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTION, v1);
        database.update(TABLE_ACTIONS, values, COLUMN_ID + "=" + id, null);
    }

    public void updateButtonState(int id, String v2){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUTTON, v2);
        database.update(TABLE_ACTIONS, values, COLUMN_ID + "=" + id, null);
    }

    public boolean deleteAction(String id)
    {
        return database.delete(TABLE_ACTIONS, COLUMN_ID + "=" + id, null) > 0;
    }

    public void insertJournalNote(String aTitle) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACTION, aTitle);
        contentValues.put(COLUMN_DATE, getCurrentDay());
        contentValues.put(COLUMN_START, getCurrentPoint());
        contentValues.put(COLUMN_STOP, 0);
        database.insert(TABLE_JOURNAL, null, contentValues);
    }

    public void updateJournalStopTime(String aTitle){
        ContentValues values = new ContentValues();
        values.put(COLUMN_STOP, getCurrentPoint());
        database.update(TABLE_JOURNAL, values, COLUMN_ACTION + " = ? AND " + COLUMN_TIME
                + " IS NULL AND " + COLUMN_STOP + " = ?", new String[]{aTitle, "0"});
    }

    public void updateJournalTime(String aTitle, int aTime){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, aTime);
        database.update(TABLE_JOURNAL, values, COLUMN_ACTION + " = ? AND " + COLUMN_TIME + " IS NULL",
                new String[]{aTitle});
    }

    public boolean deleteJournalNote(String id)
    {
        return database.delete(TABLE_JOURNAL, COLUMN_ID + "=" + id, null) > 0;
    }

    public Cursor getActionData () {
        String buildSQL = "SELECT * FROM " + TABLE_ACTIONS;
        Log.d(TAG, "getActionData SQL: " + buildSQL);
        return database.rawQuery(buildSQL, null);
    }

    public Cursor getJournalData() {
        String buildSQL = "SELECT * FROM " + TABLE_JOURNAL;
        Log.d(TAG, "getJournalData SQL: " + buildSQL);
        return database.rawQuery(buildSQL, null);
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String buildActionsSQL = "CREATE TABLE " + TABLE_ACTIONS + "( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_ACTION + " TEXT, " + COLUMN_BUTTON + " TEXT )";

            String buildJournalSQL = "CREATE TABLE " + TABLE_JOURNAL + "( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_DATE + " TEXT, " + COLUMN_ACTION + " TEXT," + COLUMN_START + " INTEGER,"
                    + COLUMN_STOP + " INTEGER," + COLUMN_TIME + " TEXT )";

            Log.d(TAG, "onCreate SQL: " + buildActionsSQL);
            Log.d(TAG, "onCreate SQL: " + buildJournalSQL);

            sqLiteDatabase.execSQL(buildActionsSQL);
            sqLiteDatabase.execSQL(buildJournalSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

            String buildActionsSQL = "DROP TABLE IF EXISTS " + TABLE_ACTIONS;
            String buildJournalSQL = "DROP TABLE IF EXISTS " + TABLE_JOURNAL;

            Log.d(TAG, "onUpgrade SQL: " + buildActionsSQL);
            Log.d(TAG, "onUpgrade SQL: " + buildJournalSQL);

            sqLiteDatabase.execSQL(buildActionsSQL);
            sqLiteDatabase.execSQL(buildJournalSQL);

            onCreate(sqLiteDatabase);
        }
    }
}
