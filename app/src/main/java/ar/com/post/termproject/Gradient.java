package ar.com.post.termproject;

public class Gradient {
    public final float mHueStart;
    public final float mHueEnd;
    public final float mSaturation;
    public final float mValue;

    public Gradient(float hueStart, float hueEnd, float saturation, float value) {
        mHueStart = hueStart;
        mHueEnd = hueEnd;
        mSaturation = saturation;
        mValue = value;
        System.out.printf("%f %f %f %f\n", hueStart, hueEnd, saturation, value);
    }
}
