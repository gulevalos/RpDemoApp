package com.example.mardiak.marek.rpdemoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.example.mardiak.marek.rpdemoapp.database.DepartmentTable;
import com.example.mardiak.marek.rpdemoapp.database.OrgStructureDatabaseHelper;


/**
 * Created by mm on 3/23/2016.
 */
@RunWith(AndroidJUnit4.class)
public class OrgStructureDatabaseHelperTest {

    private OrgStructureDatabaseHelper dbHelper;

    Context mMockContext;

    @Before
    public void setUp() throws Exception {
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");

        mMockContext.deleteDatabase(OrgStructureDatabaseHelper.DATABASE_NAME);
        dbHelper = new OrgStructureDatabaseHelper(mMockContext);
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.close();
    }

    @Test
    public void shouldGetWritableDB() throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Assert.assertTrue(db.isOpen());
    }

    @Test
    public void insertData() throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String depTestName = "DepTestName";
        values.put(DepartmentTable.NAME_COLUMN, depTestName);
        long insertResult = db.insert(DepartmentTable.TABLE_NAME, null, values);
        Assert.assertTrue(insertResult != -1);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DepartmentTable.TABLE_NAME);
        Cursor cursor = queryBuilder.query(db, null, null,
                null, null, null, null);
        Assert.assertTrue(cursor.moveToFirst());
        Assert.assertEquals(cursor.getString(1), depTestName);

    }


}

