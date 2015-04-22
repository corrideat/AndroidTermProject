package ar.com.post.termproject;

public class Gradient {
    public final float mHueStart;
    public final float mHueEnd;
    public final float mSaturation;
    public final float mValue;
    public final boolean mClockwise;

    public Gradient(float hueStart, float hueEnd, float saturation, float value, boolean clockwise) {
        hueStart %= 360.0f;
        hueEnd %= 360.0f;
        mHueStart = (hueStart < 0.0f) ? hueStart + 360.0f : hueStart;
        mHueEnd = (hueEnd < 0.0f) ? hueEnd + 360.0f : hueEnd;
        mSaturation = saturation;
        mValue = value;
        mClockwise = clockwise;
    }
}
