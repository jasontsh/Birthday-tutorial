package com.android.cis195.birthday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jason on 10/13/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USERDB";
    private static final int DATABASE_VERSION = 2;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + User.UserEntry.TABLE_NAME + " (" +
                    User.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    User.UserEntry.COLUMN_NAME + " TEXT, " +
                    User.UserEntry.COLUMN_MONTH + " TEXT, " +
                    User.UserEntry.COLUMN_DAY + " INTEGER, " +
                    User.UserEntry.COLUMN_URL + " TEXT)";
    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + User.UserEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int orig, int to) {
        String[] projection = {
                User.UserEntry._ID,
                User.UserEntry.COLUMN_NAME,
                User.UserEntry.COLUMN_MONTH,
                User.UserEntry.COLUMN_DAY
        };

        String sortOrder = User.UserEntry._ID + " ASC";
        Cursor cursor = db.query(
                User.UserEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        Map<Long, User> users = new HashMap<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_NAME));
            String month = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_MONTH));
            int day = cursor.getInt(cursor.getColumnIndex(User.UserEntry.COLUMN_DAY));
            long id = cursor.getLong(cursor.getColumnIndex(User.UserEntry._ID));
            users.put(id, new User(name, month, day));
        }
        cursor.close();
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
        for (User user : users.values()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(User.UserEntry.COLUMN_NAME, user.getName());
            contentValues.put(User.UserEntry.COLUMN_MONTH, user.getMonth());
            contentValues.put(User.UserEntry.COLUMN_DAY, user.getDay());
            contentValues.put(User.UserEntry.COLUMN_URL, "");
            db.insert(User.UserEntry.TABLE_NAME, null, contentValues);
        }
    }

    public Map<Long, User> getUsers() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                User.UserEntry._ID,
                User.UserEntry.COLUMN_NAME,
                User.UserEntry.COLUMN_MONTH,
                User.UserEntry.COLUMN_DAY,
                User.UserEntry.COLUMN_URL
        };

        String sortOrder = User.UserEntry._ID + " ASC";
        Cursor cursor = db.query(
                User.UserEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        Map<Long, User> map = new HashMap<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_NAME));
            String month = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_MONTH));
            int day = cursor.getInt(cursor.getColumnIndex(User.UserEntry.COLUMN_DAY));
            long id = cursor.getLong(cursor.getColumnIndex(User.UserEntry._ID));
            String url = cursor.getString(cursor.getColumnIndex(User.UserEntry.COLUMN_URL));
            map.put(id, new User(name, month, day, url));
        }
        cursor.close();
        return map;
    }

    public long insertUser(User user) {
        Map<Long, User> users = getUsers();
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (entry.getValue().getName().equals(user.getName())) {
                int success = updateUser(entry.getKey(), user);
                if (success == 1) {
                    return entry.getKey();
                } else {
                    return -1;
                }
            }
        }

        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(User.UserEntry.COLUMN_NAME, user.getName());
        contentValues.put(User.UserEntry.COLUMN_MONTH, user.getMonth());
        contentValues.put(User.UserEntry.COLUMN_DAY, user.getDay());
        contentValues.put(User.UserEntry.COLUMN_URL, user.getUrl());
        return db.insert(User.UserEntry.TABLE_NAME, null, contentValues);
    }

    public int updateUser(long id, User user) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(User.UserEntry.COLUMN_NAME, user.getName());
        contentValues.put(User.UserEntry.COLUMN_MONTH, user.getMonth());
        contentValues.put(User.UserEntry.COLUMN_DAY, user.getDay());
        contentValues.put(User.UserEntry.COLUMN_URL, user.getUrl());

        String selection = User.UserEntry._ID + " = ?";
        String[] selectionArgs = {id + ""};

        return db.update(
                User.UserEntry.TABLE_NAME,
                contentValues,
                selection,
                selectionArgs
        );
    }

    public int deleteUser(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(
                User.UserEntry.TABLE_NAME,
                User.UserEntry.COLUMN_NAME + " = ?",
                new String[]{username}
        );
    }
}
