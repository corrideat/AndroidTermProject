package ar.com.post.termproject;

public class ColourName {
    public final long mId;
    public final String mName;
    public final float mHue;
    public final float mSaturation;
    public final float mValue;

    public ColourName(long id, String name, float hue, float saturation, float value) {
        mId = id;
        mName = name;
        mHue = hue;
        mSaturation = saturation;
        mValue = value;
    }
}
