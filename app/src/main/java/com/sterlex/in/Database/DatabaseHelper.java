package com.sterlex.in.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sterlex.in.Model.NotificationModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "tblnotification";
    public static final String TABLE_COUNT = "tblcount";

//    // Table columns
//    public static final String ID = "id";
//    public static final String image = "image";

    // Database Information
    static final String DB_NAME = "exotic.DB";

    // database version
    static final int DB_VERSION = 1;

    String old_count = "";

    ArrayList<NotificationModel> notificationModelArrayList = new ArrayList<>();


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "notification_id text," + "title text," + "message text," + "image text," + "is_deleted text);");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_COUNT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "new_count text," + "old_count text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNT);
    }

    public Boolean addNotification(String notification_id, String title, String message, String image, String is_deleted) {
        boolean createSuccessFull = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notification_id", notification_id);
        contentValues.put("title", title);
        contentValues.put("message", message);
        contentValues.put("image", image);
        contentValues.put("is_deleted", is_deleted);
        createSuccessFull = db.insert(TABLE_NAME, null, contentValues) > 0;
        db.close();
        return createSuccessFull;
    }

    public ArrayList<NotificationModel> getNotificationList() {

        notificationModelArrayList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where is_deleted=0 ", null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    NotificationModel item = new NotificationModel();
                    item.notification_id = cursor.getString(cursor.getColumnIndex("notification_id"));
                    item.title = cursor.getString(cursor.getColumnIndex("title"));
                    item.message = cursor.getString(cursor.getColumnIndex("message"));
                    item.image = cursor.getString(cursor.getColumnIndex("image"));
                    notificationModelArrayList.add(item);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return notificationModelArrayList;
    }

    public boolean removeNotification(String notification_id, String is_deleted) {
        boolean createSuccessFull = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notification_id", notification_id);
        contentValues.put("is_deleted", is_deleted);

        createSuccessFull = db.update(TABLE_NAME, contentValues, "notification_id="
                + notification_id, null) > 0;

        db.close();
        return createSuccessFull;
    }

    public boolean ifExists(String key, String val) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + key + "='" + val + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean ifDeleted(String key, String val) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + key + "='" + val + "'", null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public Boolean addcount(String new_count, String old_count) {
        boolean createSuccessFull = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("new_count", new_count);
        contentValues.put("old_count", old_count);
        createSuccessFull = db.insert(TABLE_COUNT, null, contentValues) > 0;
        db.close();
        return createSuccessFull;
    }

    public String getOldCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select new_count from " + TABLE_COUNT , null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    old_count = cursor.getString(cursor.getColumnIndex("new_count"));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return old_count;
    }

    public boolean updateCount(String new_count, String old_count) {
        boolean createSuccessFull = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("new_count", new_count);
        contentValues.put("old_count", old_count);

        createSuccessFull = db.update(TABLE_COUNT, contentValues, null, null) > 0;

        db.close();
        return createSuccessFull;
    }
}
