package ar.com.post.termproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GradientAdapter extends ArrayAdapter<Gradient> {
    private final Gradient[] mGradients;

    public GradientAdapter(Context context, Gradient[] gradients) {
        super(context, R.layout.gradient_item, gradients);
        mGradients = gradients;
    }

    public static GradientAdapter newInstance(Context context, int steps, Vary vary, float hue_start, float hue_end, float saturation) {
        Gradient[] gradients = new Gradient[steps];
        System.out.println("gen");
        if (vary == Vary.HUE) {
            float step_size = ((hue_start - hue_end) / steps);
            System.out.println("VH ss" + step_size);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start - i * step_size, hue_start - (i + 1) * step_size, 1.0f, 1.0f);
                System.out.println("hs" + hue_start);
            }
        } else if (vary == Vary.SATURATION) {
            float step_size = (1.0f / steps);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start, hue_end, 1.0f - i * step_size, 1.0f);
            }

        } else if (vary == Vary.VALUE) {
            float step_size = (1.0f / steps);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start, hue_end, saturation, 1.0f - i * step_size);
            }
        }
        return new GradientAdapter(context, gradients);
    }

    public float getHueStart(int position) {
        return mGradients[position].mHueStart;
    }

    public float getHueEnd(int position) {
        return mGradients[position].mHueEnd;
    }

    public float getSaturation(int position) {
        return mGradients[position].mSaturation;
    }

    public float getValue(int position) {
        return mGradients[position].mValue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gradient_item, parent, false);
        }

        float[][] hsv = {
                {
                        mGradients[position].mHueStart,   // float: [0.0f, 360.0f]
                        mGradients[position].mSaturation, // float: [0.0f, 1.0f]
                        mGradients[position].mValue,      // float: [0.0f, 1.0f]
                },
                {
                        mGradients[position].mHueEnd,     // float: [0.0f, 360.0f]
                        mGradients[position].mSaturation, // float: [0.0f, 1.0f]
                        mGradients[position].mValue,      // float: [0.0f, 1.0f]
                }
        };

        int[] colours = {Color.HSVToColor(hsv[0]), Color.HSVToColor(hsv[1])};

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colours);

        convertView.setBackground(drawable);
        return convertView;
    }

    enum Vary {
        HUE,
        SATURATION,
        VALUE
    }
}
