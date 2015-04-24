package ar.com.post.termproject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import ar.com.post.termproject.db.ColourContentProvider;
import ar.com.post.termproject.db.ColoursTable;

public class ColourFinderFragment extends Fragment {
    private static float TOLERANCE_HUE = 1.8f;
    private static float TOLERANCE_SATURATION = 0.005f;
    private static float TOLERANCE_VALUE = 0.005f;
    private ColourFinderActivity mActivity;
    private ColourName mResult = null;
    private Bitmap mBitmap;

    public ColourFinderFragment() {

    }

    private void notifyReady(ColourName colourName) {
        mResult = colourName;
        if (mActivity != null) {
            mActivity.setDominantColour(colourName);
        }
    }

    public ColourName getResult() {
        return mResult;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new ColourFinderTask();
        mBitmap = getArguments().getParcelable("data");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ColourFinderActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface ColourFinderActivity {
        public void setDominantColour(ColourName colourName);
    }

    private class ColourFinderTask extends Thread {
        private ColourFinderTask() {
            start();
        }

        @Override
        public void run() {
            float[] result = new float[3];

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            CustomHashMap histogram = new CustomHashMap();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = mBitmap.getPixel(x, y);
                    Integer value = histogram.get(pixel);
                    if (value == null) {
                        value = 0;
                    }
                    histogram.put(pixel, value + 1);
                }
            }

            int dominant = histogram.currentMaxK & 0x00ffffff;

            Color.RGBToHSV(
                    dominant >> 16,
                    (dominant >> 8) & 0xff,
                    dominant & 0xff,
                    result
            );

            Uri uri = new Uri.Builder()
                    .scheme("content")
                    .authority(ColourContentProvider.AUTHORITY)
                    .appendPath(ColourContentProvider.BASE_PATH)
                    .appendPath("query")
                    .appendQueryParameter("lower_hue", String.valueOf(result[0] - TOLERANCE_HUE))
                    .appendQueryParameter("upper_hue", String.valueOf(result[0] + TOLERANCE_HUE))
                    .appendQueryParameter("lower_saturation", String.valueOf(result[1] - TOLERANCE_SATURATION))
                    .appendQueryParameter("upper_saturation", String.valueOf(result[1] + TOLERANCE_SATURATION))
                    .appendQueryParameter("lower_value", String.valueOf(result[2] - TOLERANCE_VALUE))
                    .appendQueryParameter("upper_value", String.valueOf(result[2] + TOLERANCE_VALUE))
                    .appendQueryParameter("wide_range_hue", "false")
                    .build();

            Cursor cursor = getActivity().getContentResolver().query(uri, ColoursTable.COLUMNS, null, null, null);
            ColourName colourName = null;
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    cursor.moveToFirst();
                    colourName = new ColourName(cursor.getLong(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4));
                }
                cursor.close();
            }
            if (colourName == null) {
                colourName = new ColourName(-1, null, result[0], result[1], result[2]);
            }
            notifyReady(colourName);
        }

        private class CustomHashMap extends HashMap<Integer, Integer> {
            int currentMaxK = 0;
            int currentMaxV = 0;

            @Override
            public Integer put(Integer k, Integer v) {
                if (v > currentMaxV) {
                    currentMaxV = v;
                    currentMaxK = k;
                }
                return super.put(k, v);
            }
        }
    }
}
