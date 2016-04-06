package com.example.mardiak.marek.rpdemoapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import com.example.mardiak.marek.rpdemoapp.mathematicians.Mathematician;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MathematiciansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MathematiciansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MathematiciansFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //////////////////////////////////
    private List<String> allMathematicians = Arrays.asList(Mathematician.mathematiciansNames);

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> searchAdapter;
    private WebView mWebView;

    public MathematiciansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MathematiciansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MathematiciansFragment newInstance(String param1, String param2) {
        MathematiciansFragment fragment = new MathematiciansFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fgView = inflater.inflate(R.layout.fragment_mathematicians, container, false);
        mWebView = (WebView) fgView.findViewById(R.id.mathematician_webview);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        initAutocompleteSearch(inflater, container);
        return fgView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
        Context getParentContext();
        ActionBar getActionBarFromSupportActionBar(); //TODO
    }

    private void initAutocompleteSearch(LayoutInflater inflater, ViewGroup container) {
        searchAdapter = new ArrayAdapter<String>(mListener.getParentContext(), R.layout.autocomplete_result_row) {
            private Filter filter;

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.autocomplete_result_row, parent, false);
                }

                final String mathematicianItem = this.getItem(position);
                convertView.setTag(mathematicianItem);
                ((TextView)convertView).setText(mathematicianItem);
                return convertView;
            }

            @Override
            public Filter getFilter() {
                if (filter == null) {
                    filter = new MathematicianFilter();
                }
                return filter;
            }
        };


        ActionBar actionBar = mListener.getActionBarFromSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);// search instead of title

        AutoCompleteTextView autocompleteView = (AutoCompleteTextView)inflater.inflate(R.layout.action_bar_search, container, false);

        autocompleteView.setAdapter(searchAdapter);

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String html = "file:///android_asset/Mathematicians/" + view.getTag() + ".html";
                mWebView.loadUrl(html);
                ((InputMethodManager) mListener.getParentContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        actionBar.setCustomView(autocompleteView);
    }

    private class MathematicianFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                result.values = allMathematicians;
                result.count = allMathematicians.size();
            } else {
                String substr = constraint.toString().toLowerCase();
                final ArrayList<String> retList = new ArrayList<String>();
                for (String mathematician : allMathematicians) {
                    if (mathematician.toLowerCase().contains(substr)) {
                        retList.add(mathematician);
                    }
                }
                result.values = retList;
                result.count = retList.size();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchAdapter.clear();
            if (results.count > 0) {
                for (String o : (List<String>) results.values) {
                    searchAdapter.add(o);
                }
            }
        }

    }
}
