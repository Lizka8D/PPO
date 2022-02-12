package com.example.androidgame_withdb;

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
    static final String TABLE_NAME = "players";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_ACTIVE = "active";

    public UseDatabase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NICKNAME + " TEXT,"+ COLUMN_SCORE +
                " TEXT," + COLUMN_ACTIVE + " TEXT)";

        db.execSQL(CREATE_ITEM_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void InsertData(String nickname, String score, String active) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NICKNAME, nickname);
        values.put(COLUMN_SCORE, score);
        values.put(COLUMN_ACTIVE, active);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void DropData(String nickname) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "nickname = ?", new String[]{nickname});
        db.close();
    }

    public void UpDataActivate(String nickname) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ACTIVE, "no");
        db.update(TABLE_NAME, values, "active = ?", new String[]{"yes"});
        values.clear();

        values.put(COLUMN_ACTIVE, "yes");
        db.update(TABLE_NAME, values, "nickname = ?", new String[]{nickname});
        db.close();
    }

    public void UpDataScore(String score) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_SCORE, score);

        db.update(TABLE_NAME, values, "active = ?", new String[]{"yes"});
        db.close();
    }

    public String GetActivePlayer() {
        SQLiteDatabase db = this.getReadableDatabase();

        String activeName = "Choose a nickname.";

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(3).equals("yes"))
                {
                    activeName = cursor.getString(1);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        return activeName;
    }

    private int[] bubbleSort(int[] arr){

        for(int i = arr.length-1 ; i > 0 ; i--)
        {
            for(int j = 0 ; j < i ; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }

        return arr;
    }

    public void SortData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int size = cursor.getCount();
        int[] old_scoreList = new int[size];
        String[] nicknameList = new String[size];
        String activePlayer = "";

        if (cursor.moveToFirst()) {
            for (int i = 0; cursor.moveToNext(); i++)
            {
                old_scoreList[i] = cursor.getInt(2);
                nicknameList[i] = cursor.getString(1);
                if (cursor.getString(3).equals("yes"))
                {
                    activePlayer = cursor.getString(1);
                }
            }
        }

        int[] new_scoreList = bubbleSort(old_scoreList);

        ContentValues values = new ContentValues();
        for (int i = 0; i < size; i++)
        {
            values.clear();
            values.put(COLUMN_SCORE, new_scoreList[i]);
            db.update(TABLE_NAME, values, "id = ?", new String[]{Integer.toString(i)});
        }

        for (int i = 0; i < size; i++)
        {
            values.clear();
            values.put(COLUMN_ACTIVE, "no");
            values.put(COLUMN_NICKNAME, nicknameList[i]);
            db.update(TABLE_NAME, values, "score = ?", new String[]{Integer.toString(old_scoreList[i])});
        }

        values.clear();
        values.put(COLUMN_ACTIVE, "yes");
        db.update(TABLE_NAME, values, "nickname = ?", new String[]{activePlayer});

        cursor.close();
        db.close();
    }

    public String GetScore() {
        SQLiteDatabase db = this.getReadableDatabase();

        String personalScore = "Choose a nickname.";

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(3).equals("yes"))
                {
                    personalScore = cursor.getString(2);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return personalScore;
    }

    public List<String> GetData() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        String symbols = "";
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                i = 46;
                char[] arraySymbols = cursor.getString(1).toCharArray();

                for(int j = 0; j < cursor.getString(1).length(); j++)
                {
                    if (arraySymbols[j] == 'l' || arraySymbols[j] == 'i' || arraySymbols[j] == 'f'
                            || arraySymbols[j] == 'j' || arraySymbols[j] == 't' || arraySymbols[j] == 'I')
                    {
                        i -= 1;
                    }
                    else
                    {
                        i -= 2;
                    }
                }

                while(i > 0)
                {
                    symbols += '.';
                    i--;
                }
                list.add(cursor.getString(1) + symbols + cursor.getString(2));
                symbols = "";

            } while (cursor.moveToNext());
        }
        else {
            list.add("The list is empty.");
        }

        cursor.close();
        db.close();

        return list;
    }
}

