package com.example.mardiak.marek.rpdemoapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by mm on 3/9/2016.
 */
public class OrgStructureContentProvider extends ContentProvider {

    //TODO load json from file chooser UI, custom UI component needed?
    public static final String strJson="{\n" +
            "    \"Departments\": [\n" +
            "        {\n" +
            "            \"Name\": \"RD\",\n" +
            "            \"employees\": [\n" +
            "                {\n" +
            "                    \"firstName\": \"John\",\n" +
            "                    \"lastName\": \"Smith\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/bevel-and-emboss/character/64/chef-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Joe\",\n" +
            "                    \"lastName\": \"Smith\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/people/64/chef-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Peter\",\n" +
            "                    \"lastName\": \"Frank\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Chef-icon.png\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Name\": \"HR\",\n" +
            "            \"employees\": [\n" +
            "                {\n" +
            "                    \"firstName\": \"John\",\n" +
            "                    \"lastName\": \"Doe\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Doctor-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Anna\",\n" +
            "                    \"lastName\": \"Smith\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Snowman-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Peter\",\n" +
            "                    \"lastName\": \"Jones\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Santa-icon.png\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"Name\": \"FN\",\n" +
            "            \"employees\": [\n" +
            "                {\n" +
            "                    \"firstName\": \"John\",\n" +
            "                    \"lastName\": \"King\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Angel-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Anna\",\n" +
            "                    \"lastName\": \"Smith\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Doctor-icon.png\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"firstName\": \"Peter\",\n" +
            "                    \"lastName\": \"Falk\",\n" +
            "                    \"avatar\": \"http://icons.iconarchive.com/icons/martin-berube/character/64/Chef-icon.png\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    //TODO enum
    private static final int DEPARTMRNTS_ALL = 10;
    private static final int DEPARTMRNT_BY_ID = 20;
    private static final int EMPLOYEES_ALL = 30;
    private static final int EMPLOYEE_BY_ID = 40;
    private static final int EMPLOYEES_BY_DEPARTMENT_ID = 50;

    private static final int INSERT_DEPARTMENT = 60;
    private static final int INSERT_EMPLOYEE = 70;

    public  static final String AUTHORITY = "com.example.mardiak.marek.rpdemoapp.contentprovider";
    public static final String DEPARTMENTS_PATH = "/departments";
    //public static final String DEPARTMENT_BY_ID_PATH = "/department/#";
    public static final String EMPLOYEES_PATH = "/employees";
    //public static final String EMPLOYEE_BY_ID_PATH = "/employee/#";
    public static final String EMPLOYEES_BY_DEPARTMENT_ID_PATH = "/employees_by_department/#";

    public static final String INSERT_DEPARTMENT_PATH = "/insert_department";
    public static final String INSERT_EMPLOYEE_PATH = "/insert_employee";

    public static final Uri DEPARTMENTS_ALL_URI =
            Uri.parse("content://" + AUTHORITY + DEPARTMENTS_PATH);
    public static final Uri DEPARTMENT_BY_ID_URI =
            Uri.parse("content://" + AUTHORITY + DEPARTMENTS_PATH + "/#");
    public static final Uri EMPLOYEES_ALL_URI =
            Uri.parse("content://" + AUTHORITY + EMPLOYEES_PATH);
    public static final Uri EMPLOYEE_BY_ID_URI =
            Uri.parse("content://" + AUTHORITY + EMPLOYEES_PATH + "/#");
    public static final Uri EMPLOYEES_BY_DEPARTMENT_ID_URI =
            Uri.parse("content://" + AUTHORITY + EMPLOYEES_BY_DEPARTMENT_ID_PATH);

    public static final Uri INSERT_DEPARTMENT_URI =
            Uri.parse("content://" + AUTHORITY + INSERT_DEPARTMENT_PATH);
    public static final Uri INSERT_EMPLOYEE_URI =
            Uri.parse("content://" + AUTHORITY + INSERT_EMPLOYEE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, DEPARTMENTS_PATH, DEPARTMRNTS_ALL);
        sURIMatcher.addURI(AUTHORITY, DEPARTMENTS_PATH + "/#", DEPARTMRNT_BY_ID);
        sURIMatcher.addURI(AUTHORITY, EMPLOYEES_PATH, EMPLOYEES_ALL);
        sURIMatcher.addURI(AUTHORITY, EMPLOYEES_PATH + "/#", EMPLOYEE_BY_ID);
        sURIMatcher.addURI(AUTHORITY, EMPLOYEES_BY_DEPARTMENT_ID_PATH, EMPLOYEES_BY_DEPARTMENT_ID);
        sURIMatcher.addURI(AUTHORITY, INSERT_DEPARTMENT_PATH, INSERT_DEPARTMENT);
        sURIMatcher.addURI(AUTHORITY, INSERT_EMPLOYEE_PATH, INSERT_EMPLOYEE);

    }

    private OrgStructureDatabaseHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new OrgStructureDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case DEPARTMRNTS_ALL:
                queryBuilder.setTables(DepartmentTable.TABLE_NAME);
                break;
            case DEPARTMRNT_BY_ID:
                queryBuilder.setTables(DepartmentTable.TABLE_NAME);
                queryBuilder.appendWhere(DepartmentTable.ID_COLUMN + "="
                        + uri.getLastPathSegment());
                break;
            case EMPLOYEES_ALL:
                queryBuilder.setTables(EmployeeTable.TABLE_NAME);
                break;
            case EMPLOYEE_BY_ID:
                queryBuilder.setTables(EmployeeTable.TABLE_NAME);
                queryBuilder.appendWhere(EmployeeTable.ID_COLUMN + "="
                        + uri.getLastPathSegment());
                break;
            case EMPLOYEES_BY_DEPARTMENT_ID:
                queryBuilder.setTables(EmployeeTable.TABLE_NAME + " as emp left outer join " + DepartmentTable.TABLE_NAME + " as dep "
                        + " on (emp." + EmployeeTable.FK_DEPARTMENTS_COLUMN
                        + " = dep." + DepartmentTable.ID_COLUMN + ")");
                Map<String,String> projectionMap = new HashMap<>();
                projectionMap.put(EmployeeTable.ID_COLUMN, "emp." + EmployeeTable.ID_COLUMN);
                projectionMap.put(EmployeeTable.LAST_NAME__COLUMN, EmployeeTable.LAST_NAME__COLUMN);
                projectionMap.put(EmployeeTable.FIRST_NAME__COLUMN, EmployeeTable.FIRST_NAME__COLUMN);
                projectionMap.put(EmployeeTable.AVATAR__COLUMN, EmployeeTable.AVATAR__COLUMN);
                queryBuilder.setProjectionMap(projectionMap);
                queryBuilder.appendWhere(" dep." + DepartmentTable.ID_COLUMN + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        cursor.setNotificationUri(getContext().getContentResolver(), EMPLOYEES_BY_DEPARTMENT_ID_URI);
        cursor.setNotificationUri(getContext().getContentResolver(), DEPARTMENTS_ALL_URI);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        //TODO check doc for usage
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case INSERT_DEPARTMENT:
                id = sqlDB.insert(DepartmentTable.TABLE_NAME, null, values);
                //getContext().getContentResolver().notifyChange(DEPARTMENTS_ALL_URI, null); //TODO notofaction dont update main activity
                return Uri.parse(AUTHORITY + DEPARTMENTS_PATH  + "/" + id); //TODO what should be retunred here
            case INSERT_EMPLOYEE:
                id = sqlDB.insert(EmployeeTable.TABLE_NAME, null, values);
                //getContext().getContentResolver().notifyChange(EMPLOYEES_BY_DEPARTMENT_ID_URI, null); //TODO notofaction dont update main activity
                return Uri.parse(AUTHORITY + EMPLOYEES_PATH  + "/" + id); //TODO what should be retunred here
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
