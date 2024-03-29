package ar.com.post.termproject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ColourActivity extends FragmentActivity implements ColourNamesFragment.ColourInformationActivity, SortingOrder.SortingOrderActivity, SwatchFragment.SwatchActivity {
    final static public int MIN_SWATCHES = 1;
    final static public int MAX_SWATCHES = 256;
    final static public int MIN_SWATCHES_H = 1;
    final static public int MAX_SWATCHES_H = 36;
    final private int DEFAULT_SWATCHES = 12;
    final private float DEFAULT_HUE_CENTRE = 0.0f;
    final private String SORTING_ORDER_FRAGMENT_TAG = "sorting_order";
    final private String PREFERENCE_KEY = "preferences";
    final private String PREFERENCE_KEY_SORTING_ORDER = "sorting_order";
    final private String PREFERENCE_KEY_SWATCHES_HUE = "number_swatches_h";
    final private String PREFERENCE_KEY_SWATCHES_SATURATION = "number_swatches_s";
    final private String PREFERENCE_KEY_SWATCHES_VALUE = "number_swatches_v";
    final private String PREFERENCE_KEY_CENTRAL_HUE = "central_hue";
    private SortingOrder mSortingOrder;
    private ColourNamesFragment mColourNamesFragment;
    private float mFirstCentralHue;
    private int mNumberOfSwatchesHue;
    private int mNumberOfSwatchesSaturation;
    private int mNumberOfSwatchesValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        int sortingOrderKey = preferences.getInt(PREFERENCE_KEY_SORTING_ORDER, 1);
        mNumberOfSwatchesHue = preferences.getInt(PREFERENCE_KEY_SWATCHES_HUE, DEFAULT_SWATCHES);
        mNumberOfSwatchesSaturation = preferences.getInt(PREFERENCE_KEY_SWATCHES_SATURATION, DEFAULT_SWATCHES);
        mNumberOfSwatchesValue = preferences.getInt(PREFERENCE_KEY_SWATCHES_VALUE, DEFAULT_SWATCHES);
        mFirstCentralHue = preferences.getFloat(PREFERENCE_KEY_CENTRAL_HUE, DEFAULT_HUE_CENTRE);
        mSortingOrder = SortingOrder.findById(sortingOrderKey);
    }

    protected View getColourInformationLayout(int layoutId, ColourName colourName) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(layoutId, null);
        int colour = Color.HSVToColor(new float[]{colourName.mHue, colourName.mSaturation, colourName.mValue});
        ((TextView) layout.findViewById(R.id.colour_name)).setText(colourName.mName);
        ((TextView) layout.findViewById(R.id.colour_hue)).setText(String.format("%.2f", colourName.mHue));
        ((TextView) layout.findViewById(R.id.colour_saturation)).setText(String.format("%.2f", colourName.mSaturation));
        ((TextView) layout.findViewById(R.id.colour_value)).setText(String.format("%.2f", colourName.mValue));
        layout.findViewById(R.id.colour_preview).setBackgroundColor(colour);
        return layout;
    }

    @Override
    public void showColourInformationToast(final ColourName colourName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View layout = getColourInformationLayout(R.layout.toast_colour_information, colourName);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    @Override
    public void registerColourInformationInstance(ColourNamesFragment fragment) {
        mColourNamesFragment = fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        preferences.edit()
                .putInt(PREFERENCE_KEY_SORTING_ORDER, mSortingOrder.mId)
                .putInt(PREFERENCE_KEY_SWATCHES_HUE, mNumberOfSwatchesHue)
                .putInt(PREFERENCE_KEY_SWATCHES_SATURATION, mNumberOfSwatchesSaturation)
                .putInt(PREFERENCE_KEY_SWATCHES_VALUE, mNumberOfSwatchesValue)
                .putFloat(PREFERENCE_KEY_CENTRAL_HUE, mFirstCentralHue)
                .apply();
    }

    @Override
    public SortingOrder getSortingOrder() {
        return mSortingOrder;
    }

    @Override
    public void setSortingOrder(SortingOrder sortingOrder) {
        if (sortingOrder != null) {
            mSortingOrder = sortingOrder;
        }
        if (mColourNamesFragment != null) {
            mColourNamesFragment.restart();
        }
    }

    public void onClickButtonSaveSortingOrder(View view) {
        SortingOrderFragment fragment = new SortingOrderFragment();
        fragment.show(getSupportFragmentManager(), SORTING_ORDER_FRAGMENT_TAG);
    }

    public void onClickButtonSaveColourSwatchesSettings(View view) {
        SwatchFragment fragment = (SwatchFragment) view.getTag();
        fragment.configureColourSwatches();
    }

    @Override
    public int getNumberOfSwatches(GradientAdapter.Vary vary) {
        switch (vary) {
            case HUE:
                return mNumberOfSwatchesHue;
            case SATURATION:
                return mNumberOfSwatchesSaturation;
            case VALUE:
                return mNumberOfSwatchesValue;
            default:
                return 0;
        }
    }

    @Override
    public void setNumberOfSwatches(GradientAdapter.Vary vary, int numberOfSwatches) {
        if (numberOfSwatches < MIN_SWATCHES) {
            numberOfSwatches = MIN_SWATCHES;
        } else if (numberOfSwatches > MAX_SWATCHES) {
            numberOfSwatches = MAX_SWATCHES;
        }

        switch (vary) {
            case HUE:
                mNumberOfSwatchesHue = numberOfSwatches;
                break;
            case SATURATION:
                mNumberOfSwatchesSaturation = numberOfSwatches;
                break;
            case VALUE:
                mNumberOfSwatchesValue = numberOfSwatches;
                break;
        }
    }

    @Override
    public int getMaxNumberOfSwatches(GradientAdapter.Vary vary) {
        if (vary == GradientAdapter.Vary.HUE) {
            return MAX_SWATCHES_H;
        }
        return MAX_SWATCHES;
    }

    @Override
    public int getMinNumberOfSwatches(GradientAdapter.Vary vary) {
        if (vary == GradientAdapter.Vary.HUE) {
            return MIN_SWATCHES_H;
        }
        return MIN_SWATCHES;
    }

    @Override
    public float getCentralHue() {
        return mFirstCentralHue;
    }

    @Override
    public void setCentralHue(float centralHue) {
        mFirstCentralHue = centralHue % 360.0f;
    }

}
