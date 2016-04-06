package com.example.mardiak.marek.rpdemoapp.database;

/**
 * Created by mm on 3/9/2016.
 */
public class DepartmentTable {

    public static final String TABLE_NAME = "department";
    public static final String ID_COLUMN = "_id";

    public static final String NAME_COLUMN = "name";

    public static final String TABLE_CREATE_QUERY = "create table "
            + TABLE_NAME
            + "("
            + ID_COLUMN + " integer primary key autoincrement, "
            + NAME_COLUMN + " text not null "
            + ");";
}
