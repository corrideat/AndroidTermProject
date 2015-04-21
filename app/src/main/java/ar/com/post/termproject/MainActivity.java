package ar.com.post.termproject;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity implements ColourNamesFragment.ColourInformationActivity, SortingOrder.SortingOrderActivity {

    final private int STEPS = 12;
    final private float HUE_START = -15.0f;
    final private float HUE_END = 345.0f;
    final private String MAIN_FRAGMENT_TAG = "hue";
    final private String SORTING_ORDER_FRAGMENT_TAG = "sorting_order";
    final private String PREFERENCE_KEY = "preferences";
    final private String PREFERENCE_KEY_SORTING_ORDER = "sorting_order";

    private SortingOrder mSortingOrder;
    private ColourNamesFragment mColourNamesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences(PREFERENCE_KEY, MODE_PRIVATE);
        int sortingOrderKey = preferences.getInt(PREFERENCE_KEY_SORTING_ORDER, 1);
        mSortingOrder = SortingOrder.findById(sortingOrderKey);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = SwatchFragment.newInstance(STEPS, HUE_START, HUE_END, 1.0f, GradientAdapter.Vary.HUE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, MAIN_FRAGMENT_TAG).commit();
        }
    }

    @Override
    public void showColourInformationToast(final ColourName colourName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = getLayoutInflater();
                View layout = layoutInflater.inflate(R.layout.toast_colour_information, null);
                int colour = Color.HSVToColor(new float[]{colourName.mHue, colourName.mSaturation, colourName.mValue});
                ((TextView) layout.findViewById(R.id.colour_name)).setText(colourName.mName);
                ((TextView) layout.findViewById(R.id.colour_hue)).setText(String.format("%.2f", colourName.mHue));
                ((TextView) layout.findViewById(R.id.colour_saturation)).setText(String.format("%.2f", colourName.mSaturation));
                ((TextView) layout.findViewById(R.id.colour_value)).setText(String.format("%.2f", colourName.mValue));
                layout.findViewById(R.id.colour_preview).setBackgroundColor(colour);

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
                .commit();
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

}
