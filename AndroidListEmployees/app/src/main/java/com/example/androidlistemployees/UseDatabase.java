package com.example.androidlistemployees;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class UseDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listOFemployees.db";
    private static final int SCHEMA = 1;
    static final String TABLE_NAME = "employees";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_POST = "post";
    public static final String COLUMN_SALARY = "salary";
    public static final String COLUMN_EXPERIENCE = "experience";
    public static final String COLUMN_PHONE = "phoneNumber";

    public UseDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"+ COLUMN_AGE + " TEXT,"
                + COLUMN_POST + " TEXT," + COLUMN_SALARY + " TEXT, "+ COLUMN_EXPERIENCE + " TEXT,"
                + COLUMN_PHONE + " TEXT)";

        db.execSQL(CREATE_ITEM_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void InsertData(String name, String age, String post, String salary,
                           String experience, String phoneNumber) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_POST, post);
        values.put(COLUMN_SALARY, salary);
        values.put(COLUMN_EXPERIENCE, experience);
        values.put(COLUMN_PHONE, phoneNumber);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public void UpdataData(String name, String age, String post, String salary,
                           String experience, String phoneNumber) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_AGE, age);
        values.put(COLUMN_POST, post);
        values.put(COLUMN_SALARY, salary);
        values.put(COLUMN_EXPERIENCE, experience);
        values.put(COLUMN_PHONE, phoneNumber);

        db.update(TABLE_NAME, values, "name = ?", new String[]{name});
        db.close();
    }
    public void DropData(String name) {

        SQLiteDatabase db = this.getWritableDatabase();

        //db.delete(TABLE_NAME, "_id = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_NAME, "name = ?", new String[]{name});
        db.close();
    }
    public List<String> GetNames() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Please note that using c.getString(c.getColumnIndex("column1")) is better
                // than c.getString(0): you won't have to update indexes if you change your query
                //String column1 = cursor.getString(1);
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }
    public String[] GetDataForName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_NAME + " LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + name + "%"});

        cursor.moveToFirst();

        String[] data = {cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6)};

        cursor.close();
        db.close();

        return data;
    }
}

