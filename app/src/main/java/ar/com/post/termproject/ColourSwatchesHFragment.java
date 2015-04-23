package ar.com.post.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ColourSwatchesHFragment extends ColourSwatchesSVFragment {
    private static final int PRECISION = 10;
    public ColourSwatchesHFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.fragment_colour_swatches_h, null);
        final SeekBar swatchesSeekBar = (SeekBar) view.findViewById(R.id.configure_colour_swatches_seek_bar);
        final SeekBar centralSeekBar = (SeekBar) view.findViewById(R.id.configure_central_hue_seek_bar);
        final View colourPreview = view.findViewById(R.id.colour_preview);
        final SwatchFragment.SwatchActivity activity = (SwatchFragment.SwatchActivity) getActivity();
        final float currentCentralHue = activity.getCentralHue();
        final int min = activity.getMinNumberOfSwatches(mVary);
        swatchesSeekBar.setMax(activity.getMaxNumberOfSwatches(mVary) - min);
        centralSeekBar.setMax(360 * PRECISION);

        centralSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colourPreview.setBackgroundColor(Color.HSVToColor(new float[]{progress / PRECISION, 1.0f, 1.0f}));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int numberOfSwatches = swatchesSeekBar.getProgress() + min;
                        mFragment.resetStep(numberOfSwatches, centralSeekBar.getProgress() / PRECISION);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ColourSwatchesHFragment.this.getDialog().cancel();
                    }
                });
        swatchesSeekBar.setProgress(
                activity.getNumberOfSwatches(mVary) - min
        );
        centralSeekBar.setProgress(
                (int) (currentCentralHue * PRECISION)
        );
        colourPreview.setBackgroundColor(Color.HSVToColor(new float[]{activity.getCentralHue(), 1.0f, 1.0f}));
        return builder.create();
    }
}
