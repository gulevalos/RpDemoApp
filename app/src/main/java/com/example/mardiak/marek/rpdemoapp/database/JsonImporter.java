package com.example.mardiak.marek.rpdemoapp.database;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mm on 3/13/2016.
 */
public class JsonImporter extends AsyncTask<String, Void, Void> {

    private Context mContext;

    public JsonImporter(Context context) {
        mContext = context; //Thread safety, memmory leak?
    }

    @Override
    protected Void doInBackground(String... params) {
        final ContentResolver contentResolver = mContext.getContentResolver();
        try {
            JSONObject jsonRootObject = new JSONObject(params[0]);
            JSONArray jsonDepartmentsArray = jsonRootObject.optJSONArray("Departments");

            ArrayList<ContentProviderOperation> ops =
                    new ArrayList<ContentProviderOperation>();
            int departmenetScheduledOperationIndex = 0;
            for (int i = 0; i < jsonDepartmentsArray.length(); i++) {
                JSONObject jsonDepartment = jsonDepartmentsArray.getJSONObject(i);
                ops.add(
                        ContentProviderOperation.newInsert(OrgStructureContentProvider.INSERT_DEPARTMENT_URI)
                                .withValue(DepartmentTable.NAME_COLUMN, jsonDepartment.optString("Name"))
                                .build());
                JSONArray jsonEmployeesArray = jsonDepartment.optJSONArray("employees");
                for (int j = 0; j < jsonEmployeesArray.length(); j++) {
                    JSONObject jsonEmployee = jsonEmployeesArray.getJSONObject(j);
                    ops.add(
                            ContentProviderOperation.newInsert(OrgStructureContentProvider.INSERT_EMPLOYEE_URI)
                                    .withValueBackReference(EmployeeTable.FK_DEPARTMENTS_COLUMN, departmenetScheduledOperationIndex)
                                    .withValue(EmployeeTable.AVATAR__COLUMN, jsonEmployee.optString("avatar"))
                                    .withValue(EmployeeTable.FIRST_NAME__COLUMN, jsonEmployee.optString("firstName"))
                                    .withValue(EmployeeTable.LAST_NAME__COLUMN, jsonEmployee.optString("lastName"))
                                    .withYieldAllowed(true)
                                    .build());
                }
                departmenetScheduledOperationIndex = departmenetScheduledOperationIndex + jsonEmployeesArray.length() + 1;

            }

            try {
                contentResolver.applyBatch(OrgStructureContentProvider.AUTHORITY, ops); //TODO verify
            } catch (RemoteException e) {
                Log.e(JsonImporter.class.getName(), e.getMessage(),e);
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                Log.e(JsonImporter.class.getName(), e.getMessage(),e);
                e.printStackTrace();
            }
        } catch (JSONException e) {
            Log.e(JsonImporter.class.getName(), e.getMessage(),e);
            e.printStackTrace();
        }
        contentResolver.notifyChange(OrgStructureContentProvider.DEPARTMENTS_ALL_URI, null); //TODO verify
        contentResolver.notifyChange(OrgStructureContentProvider.EMPLOYEES_BY_DEPARTMENT_ID_URI, null);
        contentResolver.notifyChange(OrgStructureContentProvider.EMPLOYEES_ALL_URI, null);
        return null; //TODO inform main list activity about result
    }

}
