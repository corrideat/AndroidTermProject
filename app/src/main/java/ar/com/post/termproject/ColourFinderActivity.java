package ar.com.post.termproject;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ar.com.post.termproject.db.ColourContentProvider;


public class ColourFinderActivity extends ColourActivity implements ColourFinderFragment.ColourFinderActivity {
    private static float HUE_TOLERANCE = 10.0f;
    private static float SATURATION_TOLERANCE = 0.03f;
    private static float VALUE_TOLERANCE = 0.03f;

    private static String COLOUR_FINDER_FRAGMENT_TAG = "colourFinder";
    private FragmentManager mFragmentManager;
    private Bitmap mBitmap;
    private ColourName mDominantColour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_finder);
        mFragmentManager = getSupportFragmentManager();
        ColourFinderFragment fragment = (ColourFinderFragment) mFragmentManager.findFragmentByTag(COLOUR_FINDER_FRAGMENT_TAG);
        mBitmap = (Bitmap) getIntent().getExtras().get("data");
        if (fragment == null) {
            fragment = new ColourFinderFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable("data", mBitmap);
            fragment.setArguments(arguments);
            mFragmentManager.beginTransaction().add(fragment, COLOUR_FINDER_FRAGMENT_TAG).commit();
        } else {
            mDominantColour = fragment.getResult();
        }
        if (mDominantColour == null) {
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(mBitmap);
        } else {
            showDominantColourInformation();
        }
    }

    private void showDominantColourInformation() {
        float lowerSaturation = (mDominantColour.mSaturation < SATURATION_TOLERANCE) ? 0.0f : (mDominantColour.mSaturation - SATURATION_TOLERANCE);
        float upperSaturation = (mDominantColour.mSaturation > (1.0f - SATURATION_TOLERANCE)) ? 1.0f : (mDominantColour.mSaturation + SATURATION_TOLERANCE);
        float lowerValue = (mDominantColour.mValue < VALUE_TOLERANCE) ? 0.0f : (mDominantColour.mValue - VALUE_TOLERANCE);
        float upperValue = (mDominantColour.mValue > (1.0f - VALUE_TOLERANCE)) ? 1.0f : (mDominantColour.mValue + VALUE_TOLERANCE);

        Uri uri = new Uri.Builder()
                .scheme("content")
                .authority(ColourContentProvider.AUTHORITY)
                .appendPath(ColourContentProvider.BASE_PATH)
                .appendPath("query")
                .appendQueryParameter("lower_hue", String.valueOf(mDominantColour.mHue - HUE_TOLERANCE))
                .appendQueryParameter("upper_hue", String.valueOf(mDominantColour.mHue + HUE_TOLERANCE))
                .appendQueryParameter("lower_saturation", String.valueOf(lowerSaturation))
                .appendQueryParameter("upper_saturation", String.valueOf(upperSaturation))
                .appendQueryParameter("lower_value", String.valueOf(lowerValue))
                .appendQueryParameter("upper_value", String.valueOf(upperValue))
                .build();

        mFragmentManager.beginTransaction()
                .add(R.id.fragment_colour_names_container, ColourNamesFragment.newInstance(uri))
                .commit();

        findViewById(R.id.loading_content).setVisibility(View.GONE);
        findViewById(R.id.colour_match).setVisibility(View.VISIBLE);
        View layout = getColourInformationLayout(R.layout.item_colour_information, mDominantColour);
        ((ViewGroup) findViewById(R.id.fragment_colour_information_container)).addView(layout);
        if (mDominantColour.mId == -1) {
            layout.findViewById(R.id.colour_name).setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.exact_match_found_or_not)).setText(getString(R.string.exact_match_found));
        }
    }

    @Override
    public void setDominantColour(ColourName colour) {
        mDominantColour = colour;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDominantColourInformation();
            }
        });
    }
}
