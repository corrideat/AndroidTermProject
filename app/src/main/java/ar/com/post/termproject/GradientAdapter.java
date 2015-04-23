package ar.com.post.termproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class GradientAdapter extends ArrayAdapter<Gradient> {
    public GradientAdapter(Context context, ArrayList<Gradient> gradients) {
        super(context, R.layout.item_gradient, gradients);
    }

    private static Gradient[] makeGradients(int steps, Vary vary, float hue_start, float hue_end, float saturation, boolean clockwise) {
        Gradient[] gradients = new Gradient[steps];

        if (vary == Vary.HUE) {
            float step_size = 180.0f / steps;

            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start + (2 * i - 1) * step_size, hue_start + (2 * i + 1) * step_size, 1.0f, 1.0f, clockwise);
            }
        } else if (vary == Vary.SATURATION) {
            float step_size = (1.0f / steps);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start, hue_end, 1.0f - i * step_size, 1.0f, clockwise);
            }

        } else if (vary == Vary.VALUE) {
            float step_size = (1.0f / steps);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start, hue_end, saturation, 1.0f - i * step_size, clockwise);
            }
        }
        return gradients;
    }

    public static GradientAdapter newInstance(Context context, int steps, Vary vary, float hue_start, float hue_end, float saturation, boolean clockwise) {
        ArrayList<Gradient> gradients = new ArrayList<>(Arrays.asList(makeGradients(steps, vary, hue_start, hue_end, saturation, clockwise)));
        return new GradientAdapter(context, gradients);
    }

    public float getHueStart(int position) {
        return getItem(position).mHueStart;
    }

    public float getHueEnd(int position) {
        return getItem(position).mHueEnd;
    }

    public float getSaturation(int position) {
        return getItem(position).mSaturation;
    }

    public float getValue(int position) {
        return getItem(position).mValue;
    }

    public boolean getClockwise(int position) {
        return getItem(position).mClockwise;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gradient, parent, false);
        }
        Gradient gradient = getItem(position);
        float gradientDelta = gradient.mHueEnd - gradient.mHueStart;
        if (gradientDelta <= 0.0f) {
            gradientDelta += 360.0f;
        }
        float stepSize;
        int samples = Math.max((int) Math.floor(gradientDelta / Math.PI), 5);
        int[] colours = new int[samples];
        stepSize = gradientDelta / (samples - 1);

        for (int i = 0; i < samples; i++) {
            colours[i] = Color.HSVToColor(new float[]{
                    (gradient.mHueStart + i * stepSize) % 360.0f,
                    gradient.mSaturation,
                    gradient.mValue
            });
        }

        GradientDrawable drawable = new GradientDrawable(gradient.mClockwise ? GradientDrawable.Orientation.RIGHT_LEFT : GradientDrawable.Orientation.LEFT_RIGHT, colours);

        convertView.setBackground(drawable);
        return convertView;
    }

    public void reset(int steps, Vary vary, float hue_start, float hue_end, float saturation, boolean clockwise) {
        this.clear();
        this.addAll(makeGradients(steps, vary, hue_start, hue_end, saturation, clockwise));
        this.notifyDataSetChanged();
    }

    enum Vary {
        HUE,
        SATURATION,
        VALUE
    }
}
