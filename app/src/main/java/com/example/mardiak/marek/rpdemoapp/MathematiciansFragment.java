package com.example.mardiak.marek.rpdemoapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MathematiciansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MathematiciansFragment extends Fragment {

    private final List<String> allMathematicians = Arrays.asList(Mathematician.mathematiciansNames);

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> searchAdapter;
    private WebView mWebView;

    public MathematiciansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Context getParentContext();
        ActionBar getActionBarFromSupportActionBar(); //TODO better way of wrapper?
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
