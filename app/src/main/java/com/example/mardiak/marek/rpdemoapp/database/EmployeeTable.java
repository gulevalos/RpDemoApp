package com.example.mardiak.marek.rpdemoapp.database;

/**
 * Created by mm on 3/9/2016.
 */
public class EmployeeTable {
    public static final String TABLE_NAME = "employee";
    public static final String ID_COLUMN = "_id";

    public static final String FIRST_NAME__COLUMN = "firstname";
    public static final String LAST_NAME__COLUMN = "lastname";
    public static final String AVATAR__COLUMN = "avatar";
    public static final String FK_DEPARTMENTS_COLUMN = "department_id";

    public static final String TABLE_CREATE_QUERY = "create table "
            + TABLE_NAME
            + "("
            + ID_COLUMN + " integer primary key autoincrement, "
            + FK_DEPARTMENTS_COLUMN + " integer, "
            + FIRST_NAME__COLUMN + " text not null, "
            + LAST_NAME__COLUMN + " text not null, "
            + AVATAR__COLUMN + " text not null, "
            + "FOREIGN KEY(" + FK_DEPARTMENTS_COLUMN + ") REFERENCES " + DepartmentTable.TABLE_NAME + "(" + DepartmentTable.ID_COLUMN +"));";
}
