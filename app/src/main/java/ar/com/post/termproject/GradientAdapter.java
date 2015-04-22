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
        super(context, R.layout.item_gradient, gradients);
        mGradients = gradients;
    }

    public static GradientAdapter newInstance(Context context, int steps, Vary vary, float hue_start, float hue_end, float saturation, boolean clockwise) {
        Gradient[] gradients = new Gradient[steps];
        if (vary == Vary.HUE) {
            float step_size = steps == 1 ? 359.9999f : ((hue_start - hue_end) / steps);
            for (int i = 0; i < steps; i++) {
                gradients[i] = new Gradient(hue_start - i * step_size, hue_start - (i + 1) * step_size, 1.0f, 1.0f, clockwise);
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

    public boolean getClockwise(int position) {
        return mGradients[position].mClockwise;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gradient, parent, false);
        }

        float gradientDelta = mGradients[position].mHueEnd - mGradients[position].mHueStart;
        if (gradientDelta < 0.0f) {
            gradientDelta += 360.0f;
        }
        float stepSize;
        int samples = Math.max((int) Math.floor(gradientDelta / Math.PI), 5);
        int[] colours = new int[samples];
        stepSize = gradientDelta / (samples - 1);

        for (int i = 0; i < samples; i++) {
            colours[i] = Color.HSVToColor(new float[]{
                    mGradients[position].mHueStart + i * stepSize,
                    mGradients[position].mSaturation,
                    mGradients[position].mValue
            });
        }

        GradientDrawable drawable = new GradientDrawable(mGradients[position].mClockwise ? GradientDrawable.Orientation.RIGHT_LEFT : GradientDrawable.Orientation.LEFT_RIGHT, colours);

        convertView.setBackground(drawable);
        return convertView;
    }

    enum Vary {
        HUE,
        SATURATION,
        VALUE
    }
}
