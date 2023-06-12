package com.shahruie.www.divar;

/**
 * Created by MR on 05/09/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private DBHandler mDbHelper;
    private SQLiteDatabase mDb;
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "database2";
    // Contacts table name
    private static final String TABLE_address = "home";
    // Shops Table Columns names
    public static final String IMAGE = "image";
    private static final String KEY_ID = "ID";
    private static final String KEY_voice ="voice";
    private static final String KEY_locimg ="locimage";
    private static final String KEY_phone ="phone";
    private static final String KEY_H_ADDR = "txtaddress";
    private static final String KEY_NAME ="name";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = " CREATE TABLE "+TABLE_address+"( "
                +KEY_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                +KEY_H_ADDR+" TEXT,"
                +KEY_NAME+" TEXT,"
                +KEY_phone+" TEXT,"
                +KEY_locimg+" TEXT,"
                +KEY_voice+" TEXT"
                +" )";
        db.execSQL(CREATE_CONTACTS_TABLE);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_address);
             onCreate(db);
    }
    // Adding new Address
    public void addaddress(Adv loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_H_ADDR, loc.getPhone());
        values.put(KEY_NAME, loc.getPhone());
        values.put(KEY_phone, loc.getPhone());
        values.put(KEY_locimg, loc.getPhone());
        values.put(KEY_voice, loc.getPhone());
        db.insert(TABLE_address, null, values);

    }
    // Getting one Address
    public Adv getaddress(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_address, new String[]{KEY_ID,
        KEY_H_ADDR, KEY_NAME,KEY_phone,KEY_locimg,KEY_voice}, KEY_ID + "=?",
        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
      /*  Adv loc = new Adv(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2)
                , cursor.getString(3),cursor.getString(4), cursor.getString(5));*/
        cursor.close();
        db.close();
        return null;
    }
    public List<Adv> getAlllocs() {
        List<Adv> locList = new ArrayList<Adv>();
        String selectQuery = "SELECT * FROM " + TABLE_address;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Adv loc = new Adv();
             /*   loc.setId(cursor.getInt(0));
                loc.setName(cursor.getString(2));
                loc.setAddress(cursor.getString(1));
                loc.setPhone(cursor.getString(3));
                loc.setimg(cursor.getString(4));
                loc.setVoice(cursor.getString(5));
                locList.add(loc);*/
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locList;
    }
    // Getting addrress Count
    public int getaddressCount() {
        int c;
        String countQuery = "SELECT * FROM " + TABLE_address;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        c=cursor.getCount();
        db.close();
        cursor.close();
        return c ;
    }
    // Updating a address
    public int updateaddress(Adv loc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_H_ADDR, loc.getPhone());
        values.put(KEY_NAME, loc.getPhone());
        values.put(KEY_phone, loc.getPhone());
        values.put(KEY_locimg, loc.getPhone());
        values.put(KEY_voice,loc.getPhone());
       // values.put(IMAGE,loc.getimg());
        return db.update(TABLE_address, values, KEY_ID + " = ?",
        new String[]{String.valueOf(loc.getId())});
    }
    // Deleting a shop
    public void deleteaddress(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_address, KEY_ID + " = ?",
        new String[] { String.valueOf(id) });
        db.close();
    }

    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE, imageBytes);
        mDb.insert(TABLE_address, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = mDb.query(true, TABLE_address, new String[]{IMAGE,},
                null, null, null, null,
                KEY_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}