package ar.com.post.termproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {

    final private int STEPS = 12;
    final private float HUE_START = -15.0f;
    final private float HUE_END = 345.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = (Fragment) SwatchFragment.newInstance(STEPS, HUE_START, HUE_END, 1.0f, GradientAdapter.Vary.HUE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }

}
