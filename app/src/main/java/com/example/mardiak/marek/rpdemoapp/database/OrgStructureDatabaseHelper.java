package com.example.mardiak.marek.rpdemoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mm on 3/9/2016.
 */
public class OrgStructureDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "org_structure.db";
    private static final int DATABASE_VERSION = 13;

    public OrgStructureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DepartmentTable.TABLE_CREATE_QUERY);
        database.execSQL(EmployeeTable.TABLE_CREATE_QUERY);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        Log.w(OrgStructureDatabaseHelper.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + EmployeeTable.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + DepartmentTable.TABLE_NAME);
        onCreate(database);
    }

}
