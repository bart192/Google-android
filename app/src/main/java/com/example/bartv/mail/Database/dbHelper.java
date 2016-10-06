package com.example.bartv.mail.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bartv on 24-9-2016.
 */

public class dbHelper extends SQLiteOpenHelper {

    public dbHelper(Context context) {
        super(context, dbCreate.DB_NAME, null, dbCreate.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(dbCreate.CREATE_TB);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + dbCreate.TB_NAME);
        onCreate(db);
    }
}
