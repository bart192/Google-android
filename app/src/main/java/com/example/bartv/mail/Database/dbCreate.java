package com.example.bartv.mail.Database;

import com.example.bartv.mail.StudentActivity;

/**
 * Created by bartv on 24-9-2016.
 */

public class dbCreate {
    //Columns
    static final String ROW_ID="id";
    static final String NAME = "name";
    static final String STUDENTNUMBER = "studentnumber";
    static final String EMAIL = "email";
    static final String IMAGE = "image";
    static final String KLAS = "klas";
    static final String ZIPCODE = "zipcode";

    //DB Properties
    static final String DB_NAME="dbName";
    static final String TB_NAME="tbName";
    static final int DB_VERSION='1';


    //Create Table Statements
    static final String CREATE_TB="CREATE TABLE tbName(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "name TEXT NOT NULL, studentnumber NOT NULL, email NOT NULL, image NOT NULL, klas NOT NULL, zipcode NOT NULL);";
}
