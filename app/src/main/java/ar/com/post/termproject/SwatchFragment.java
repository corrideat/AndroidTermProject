package ar.com.post.termproject;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ar.com.post.termproject.db.ColourContentProvider;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwatchFragment extends Fragment {
    private static final String ARG_HUE_START = "hueStart";
    private static final String ARG_HUE_END = "hueEnd";
    private static final String ARG_STEPS = "steps";
    private static final String ARG_SATURATION = "saturation";
    private static final String ARG_VARY = "vary";

    private float mHueStart;
    private float mHueEnd;
    private float mSaturation;
    private int mSteps;
    private GradientAdapter.Vary mVary;
    private Fragment mSubFragment;

    public SwatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param steps      Number of swatches
     * @param hueStart   Start Hue.
     * @param hueEnd     End Hue.
     * @param saturation Saturation level
     * @param vary       The parameter to be varied
     * @return A new instance of fragment SaturationFragment.
     */
    public static SwatchFragment newInstance(int steps, float hueStart, float hueEnd, float saturation, GradientAdapter.Vary vary) {
        SwatchFragment fragment = new SwatchFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_STEPS, steps);
        arguments.putFloat(ARG_HUE_START, hueStart);
        arguments.putFloat(ARG_HUE_END, hueEnd);
        arguments.putFloat(ARG_SATURATION, saturation);
        arguments.putSerializable(ARG_VARY, vary);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mSteps = arguments.getInt(ARG_STEPS);
            mHueStart = arguments.getFloat(ARG_HUE_START);
            mHueEnd = arguments.getFloat(ARG_HUE_END);
            mSaturation = arguments.getFloat(ARG_SATURATION);
            mVary = (GradientAdapter.Vary) arguments.getSerializable(ARG_VARY);
        }
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swatch, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(GradientAdapter.newInstance(getActivity().getApplicationContext(), mSteps, mVary, mHueStart, mHueEnd, mSaturation));
        if (mVary == GradientAdapter.Vary.HUE) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GradientAdapter adapter = (GradientAdapter) parent.getAdapter();
                    mSubFragment = SwatchFragment.newInstance(mSteps, adapter.getHueStart(position), adapter.getHueEnd(position), 1.0f, GradientAdapter.Vary.SATURATION);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mSubFragment)
                            .addToBackStack("back")
                            .commit();
                    //((ArrayAdapter) parent.getAdapter()).notifyDataSetChanged();
                }
            });
        } else if (mVary == GradientAdapter.Vary.SATURATION) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GradientAdapter adapter = (GradientAdapter) parent.getAdapter();
                    mSubFragment = SwatchFragment.newInstance(mSteps, mHueStart, mHueEnd, adapter.getSaturation(position), GradientAdapter.Vary.VALUE);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mSubFragment)
                            .addToBackStack("back")
                            .commit();
                    //((ArrayAdapter) parent.getAdapter()).notifyDataSetChanged();
                }
            });
        } else if (mVary == GradientAdapter.Vary.VALUE) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GradientAdapter adapter = (GradientAdapter) parent.getAdapter();
                    Uri uri = new Uri.Builder()
                            .scheme("content")
                            .authority(ColourContentProvider.AUTHORITY)
                            .appendPath(ColourContentProvider.BASE_PATH)
                            .appendPath("query")
                            .appendQueryParameter("lower_hue", String.valueOf(mHueStart))
                            .appendQueryParameter("upper_hue", String.valueOf(mHueEnd))
                            .appendQueryParameter("lower_saturation", String.valueOf(mSaturation - 1.0f / mSteps))
                            .appendQueryParameter("upper_saturation", String.valueOf(mSaturation))
                            .appendQueryParameter("lower_value", String.valueOf(adapter.getValue(position) - 1.0f / mSteps))
                            .appendQueryParameter("upper_value", String.valueOf(adapter.getValue(position)))
                            .build();
                    mSubFragment = ColourNamesFragment.newInstance(uri);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, mSubFragment)
                            .addToBackStack("back")
                            .commit();
                }
            });
        }

        return view;
    }
}
