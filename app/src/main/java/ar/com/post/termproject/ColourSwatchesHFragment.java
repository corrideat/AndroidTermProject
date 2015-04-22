package ar.com.post.termproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.configure_colour_swatches_seek_bar);
        final SwatchFragment.SwatchActivity activity = (SwatchFragment.SwatchActivity) getActivity();
        final int min = activity.getMinNumberOfSwatches(mVary);
        seekBar.setMax(activity.getMaxNumberOfSwatches(mVary) - min);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int numberOfSwatches = seekBar.getProgress() + min;
                        activity.setNumberOfSwatches(mVary, numberOfSwatches);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ColourSwatchesHFragment.this.getDialog().cancel();
                    }
                });
        seekBar.setProgress(
                ((SwatchFragment.SwatchActivity) getActivity()).getNumberOfSwatches(mVary)
        );
        return builder.create();
    }
}
