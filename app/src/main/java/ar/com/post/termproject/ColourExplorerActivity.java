package ar.com.post.termproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


public class ColourExplorerActivity extends ColourActivity {
    final private String MAIN_FRAGMENT_TAG = "hue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colour_explorer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = SwatchFragment.newInstance(getCentralHue(), Float.NaN, 1.0f, GradientAdapter.Vary.HUE, false);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, MAIN_FRAGMENT_TAG).commit();
        }
    }

}
