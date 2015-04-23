package ar.com.post.termproject;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import ar.com.post.termproject.db.ColoursTable;

public class ColourNamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_URI = "uri";

    private Uri mUri;
    private ColourNamesAdapter mAdapter;
    private ListView mListView;
    private View mEmpty;
    //private ColourName[] mColourNames;

    public ColourNamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uri The query URI
     * @return A new instance of fragment ColourNamesFragment.
     */
    public static ColourNamesFragment newInstance(Uri uri) {
        ColourNamesFragment fragment = new ColourNamesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(ARG_URI);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colour_names, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mEmpty = view.findViewById(R.id.empty);
        getLoaderManager().initLoader(0, null, this);

        mAdapter = new ColourNamesAdapter(getActivity().getApplicationContext(), null, 0);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ColourInformationActivity) getActivity()).showColourInformationToast(((ColourNamesAdapter) parent.getAdapter()).getColourName(view));
            }
        });

        ((TextView) view.findViewById(R.id.colour_hue)).setText(String.format(getString(R.string.hue_range), Float.valueOf(mUri.getQueryParameter("lower_hue")), Float.valueOf(mUri.getQueryParameter("upper_hue"))));
        ((TextView) view.findViewById(R.id.colour_saturation)).setText(String.format(getString(R.string.saturation_range), Float.valueOf(mUri.getQueryParameter("lower_saturation")), Float.valueOf(mUri.getQueryParameter("upper_saturation"))));
        ((TextView) view.findViewById(R.id.colour_value)).setText(String.format(getString(R.string.value_range), Float.valueOf(mUri.getQueryParameter("lower_value")), Float.valueOf(mUri.getQueryParameter("upper_value"))));

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity().getApplicationContext(), mUri, ColoursTable.COLUMNS, null, null, ((SortingOrder.SortingOrderActivity) getActivity()).getSortingOrder().mSortQuery);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            mEmpty.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mEmpty.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ColourInformationActivity) activity).registerColourInformationInstance(this);
    }

    public void restart() {
        getLoaderManager().restartLoader(0, null, this);
    }

    interface ColourInformationActivity {
        public void showColourInformationToast(ColourName colourName);

        public void registerColourInformationInstance(ColourNamesFragment fragment);
    }
}
