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
    private static final String COLOUR_SWATCHES_FRAGMENT_TAG = "swatch_settings";
    private static final String ARG_HUE_START = "hueStart";
    private static final String ARG_HUE_END = "hueEnd";
    private static final String ARG_SATURATION = "saturation";
    private static final String ARG_VARY = "vary";
    private static final String ARG_CLOCKWISE = "clockwise";

    private float mHueStart;
    private float mHueEnd;
    private float mSaturation;
    private int mSteps;
    private GradientAdapter.Vary mVary;
    private Fragment mSubFragment;
    private boolean mClockwise;
    private GradientAdapter mAdapter;

    public SwatchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hueStart   Start Hue.
     * @param hueEnd     End Hue.
     * @param saturation Saturation level
     * @param vary       The parameter to be varied
     * @param clockwise  Clockwise Hue Display
     * @return A new instance of fragment SaturationFragment.
     */
    public static SwatchFragment newInstance(float hueStart, float hueEnd, float saturation, GradientAdapter.Vary vary, boolean clockwise) {
        SwatchFragment fragment = new SwatchFragment();
        Bundle arguments = new Bundle();
        arguments.putFloat(ARG_HUE_START, hueStart);
        arguments.putFloat(ARG_HUE_END, hueEnd);
        arguments.putFloat(ARG_SATURATION, saturation);
        arguments.putSerializable(ARG_VARY, vary);
        arguments.putBoolean(ARG_CLOCKWISE, clockwise);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mHueStart = arguments.getFloat(ARG_HUE_START);
            mHueEnd = arguments.getFloat(ARG_HUE_END);
            mSaturation = arguments.getFloat(ARG_SATURATION);
            mVary = (GradientAdapter.Vary) arguments.getSerializable(ARG_VARY);
            mClockwise = arguments.getBoolean(ARG_CLOCKWISE, false);
        }
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swatch, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        view.findViewById(R.id.configure_colour_swatches).setTag(this);
        mSteps = ((SwatchActivity) getActivity()).getNumberOfSwatches(mVary);
        mAdapter = GradientAdapter.newInstance(getActivity().getApplicationContext(), mSteps, mVary, mHueStart, mHueEnd, mSaturation, mClockwise);
        listView.setAdapter(mAdapter);
        if (mVary == GradientAdapter.Vary.HUE) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GradientAdapter adapter = (GradientAdapter) parent.getAdapter();
                    mSubFragment = SwatchFragment.newInstance(adapter.getHueStart(position), adapter.getHueEnd(position), 1.0f, GradientAdapter.Vary.SATURATION, adapter.getClockwise(position));
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
                    mSubFragment = SwatchFragment.newInstance(mHueStart, mHueEnd, adapter.getSaturation(position), GradientAdapter.Vary.VALUE, adapter.getClockwise(position));
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

    public void configureColourSwatches() {
        ColourSwatchesSVFragment fragment = mVary != GradientAdapter.Vary.HUE ? new ColourSwatchesSVFragment() : new ColourSwatchesHFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ColourSwatchesSVFragment.ARG_VARY, mVary);
        fragment.setArguments(arguments);
        fragment.setFragment(this);
        fragment.show(getActivity().getSupportFragmentManager(), COLOUR_SWATCHES_FRAGMENT_TAG);
    }

    public void resetStep(int numberOfSwatches) {
        SwatchActivity activity = (SwatchActivity) getActivity();
        activity.setNumberOfSwatches(mVary, numberOfSwatches);
        int steps = activity.getNumberOfSwatches(mVary);
        mSteps = steps;
        mAdapter.reset(steps, mVary, mHueStart, mHueEnd, mSaturation, mClockwise);
    }

    public void resetStep(int numberOfSwatches, float centralHue) {
        if (mVary != GradientAdapter.Vary.HUE) {
            throw new UnsupportedOperationException();
        }
        SwatchActivity activity = (SwatchActivity) getActivity();
        activity.setCentralHue(centralHue);
        mHueStart = activity.getCentralHue();
        resetStep(numberOfSwatches);
    }


    interface SwatchActivity {
        public int getNumberOfSwatches(GradientAdapter.Vary vary);

        public void setNumberOfSwatches(GradientAdapter.Vary vary, int numberOfSwatches);

        public int getMaxNumberOfSwatches(GradientAdapter.Vary vary);

        public int getMinNumberOfSwatches(GradientAdapter.Vary vary);

        public float getCentralHue();

        public void setCentralHue(float centralHue);
    }
}
