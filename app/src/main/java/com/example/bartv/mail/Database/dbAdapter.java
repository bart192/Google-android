package com.example.bartv.mail.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bartv on 24-9-2016.
 */

public class dbAdapter {
    Context c;
    SQLiteDatabase db;
    dbHelper helper;

    // ---------------------------------------------------------------------------------------------
    // Constructor for my dbAdapter class
    // ---------------------------------------------------------------------------------------------

    public dbAdapter(Context ctx) {
        this.c = ctx;
        helper = new dbHelper(c);
    }

    // ---------------------------------------------------------------------------------------------
    // Method to open the database
    // ---------------------------------------------------------------------------------------------

    public dbAdapter openDB() {
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    // ---------------------------------------------------------------------------------------------
    // Method to close the database
    // ---------------------------------------------------------------------------------------------

    public void closeDB() {
        try {
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Method to add a student to the database
    // ---------------------------------------------------------------------------------------------

    public long add(String name, String studentnumber, String email, String image, String klas, String zipcode) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(dbCreate.NAME, name);
            cv.put(dbCreate.STUDENTNUMBER, studentnumber);
            cv.put(dbCreate.EMAIL, email);
            cv.put(dbCreate.IMAGE, image);
            cv.put(dbCreate.KLAS, klas);
            cv.put(dbCreate.ZIPCODE, zipcode);

            return db.insert(dbCreate.TB_NAME, dbCreate.ROW_ID, cv);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ---------------------------------------------------------------------------------------------
    // Method that gets all the students from the database
    // ---------------------------------------------------------------------------------------------

    public Cursor getAllStudents() {
        String[] columns = {dbCreate.ROW_ID, dbCreate.NAME, dbCreate.STUDENTNUMBER, dbCreate.EMAIL, dbCreate.IMAGE, dbCreate.KLAS, dbCreate.ZIPCODE};
        return db.query(dbCreate.TB_NAME, columns, null, null, null, null, null);
    }

    // ---------------------------------------------------------------------------------------------
    // Method that updates a student
    // ---------------------------------------------------------------------------------------------

    public long update(int id, String name, String studentnumber,String email, String image, String klas, String zipcode) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(dbCreate.NAME, name);
            cv.put(dbCreate.STUDENTNUMBER, studentnumber);
            cv.put(dbCreate.EMAIL, email);
            cv.put(dbCreate.IMAGE, image);
            cv.put(dbCreate.KLAS, klas);
            cv.put(dbCreate.ZIPCODE, zipcode);

            return db.update(dbCreate.TB_NAME, cv, dbCreate.ROW_ID + " = ?", new String[]{String.valueOf(id)});

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long delete(int id) {
        try {
            return db.delete(dbCreate.TB_NAME, dbCreate.ROW_ID + " = ?",new String[]{String.valueOf(id)});
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
