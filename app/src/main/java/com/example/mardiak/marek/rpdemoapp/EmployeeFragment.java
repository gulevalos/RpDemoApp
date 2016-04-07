package com.example.mardiak.marek.rpdemoapp;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mardiak.marek.rpdemoapp.database.DepartmentTable;
import com.example.mardiak.marek.rpdemoapp.database.EmployeeTable;
import com.example.mardiak.marek.rpdemoapp.database.JsonImporter;
import com.example.mardiak.marek.rpdemoapp.database.OrgStructureContentProvider;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmployeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EmployeeFragment extends Fragment {
    public static final int DEPARTMENT_LOADER_ID = 1;
    public static final int EMPLOYEE_LIST_LOADER_ID = 0;

    private OnFragmentInteractionListener mListener;
    private SimpleCursorAdapter employeesListAdapter;
    private SimpleCursorAdapter departmentSpinnerAdapter;

    private ListView listView;
    private Spinner spinner;

    @NonNull
    private LoaderManager.LoaderCallbacks<Cursor> createEmployeeListLoaderListener(final Long departmentId) {
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = {EmployeeTable.ID_COLUMN, EmployeeTable.LAST_NAME__COLUMN, EmployeeTable.FIRST_NAME__COLUMN, EmployeeTable.AVATAR__COLUMN};
                Uri uri = OrgStructureContentProvider.EMPLOYEES_ALL_URI;
                if (departmentId != null) {
                    uri = ContentUris.withAppendedId(OrgStructureContentProvider.EMPLOYEES_BY_DEPARTMENT_ID_URI, departmentId);
                }
                return   new CursorLoader(mListener.getParentContext(),
                        uri, projection, null, null, null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                employeesListAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                employeesListAdapter.swapCursor(null);
            }
        };
    }

    public EmployeeFragment() {
        // Required empty public constructor
    }

    private void fillDepartmentData() {

        String[] from = new String[]{DepartmentTable.NAME_COLUMN};
        int[] to = new int[]{android.R.id.text1};
        departmentSpinnerAdapter = new SimpleCursorAdapter(
                mListener.getParentContext(),
                android.R.layout.simple_spinner_item,
                null,
                from,
                to,
                0);
        departmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(departmentSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getLoaderManager().getLoader(EMPLOYEE_LIST_LOADER_ID) == null) {
                    fillEmployeeData(id);
                } else {
                    getLoaderManager().restartLoader(EMPLOYEE_LIST_LOADER_ID, null, createEmployeeListLoaderListener(id));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        getLoaderManager().initLoader(DEPARTMENT_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = {DepartmentTable.ID_COLUMN, DepartmentTable.NAME_COLUMN};
                Uri uri = OrgStructureContentProvider.DEPARTMENTS_ALL_URI;
                CursorLoader cursorLoader = new CursorLoader(mListener.getParentContext(),
                        uri, projection, null, null, DepartmentTable.NAME_COLUMN);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                departmentSpinnerAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                departmentSpinnerAdapter.swapCursor(null);
            }
        });
    }

    private void fillEmployeeData(final Long departmentId) {
        String[] from = new String[]{EmployeeTable.ID_COLUMN, EmployeeTable.LAST_NAME__COLUMN, EmployeeTable.FIRST_NAME__COLUMN, EmployeeTable.AVATAR__COLUMN};
        int[] to = new int[]{R.id.person_avatar, R.id.person_label, R.id.person_content};

        getLoaderManager().initLoader(EMPLOYEE_LIST_LOADER_ID, null, createEmployeeListLoaderListener(departmentId));
        employeesListAdapter = new SimpleCursorAdapter(mListener.getParentContext(), R.layout.employee_row, null, from,
                to, 0);
        employeesListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.person_label:
                        ((TextView) view).setText(cursor.getString(1));
                        break;
                    case R.id.person_content:
                        ((TextView) view).setText(cursor.getString(2));
                        break;
                    case R.id.person_avatar:
                        Picasso.with(view.getContext()).load(cursor.getString(3)).into((ImageView) view);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        listView.setAdapter(employeesListAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fgView = inflater.inflate(R.layout.fragment_employee, container, false);
        spinner = (Spinner) fgView.findViewById(R.id.depSpinner);
        listView = (ListView) fgView.findViewById(R.id.empList);
        fillDepartmentData();
        return fgView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_employee, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_json:
                JsonImporter jsonImporter = new JsonImporter(mListener.getParentContext());
                jsonImporter.execute(OrgStructureContentProvider.strJson);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        Context getParentContext();
    }
}
