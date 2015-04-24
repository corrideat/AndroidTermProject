package ar.com.post.termproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkCameraHardware() == null) {
            launchColourExplorer();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, ColourFinderActivity.class);
                intent.putExtra("data", (Bitmap) data.getExtras().get("data"));
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this, getString(R.string.shot_cancelled), Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, getString(R.string.shot_error), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Intent checkCameraHardware() {
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (packageManager.queryIntentActivities(intent, 0).isEmpty()) {
                return null;
            } else {
                return intent;
            }
        } else {
            // no camera on this device
            return null;
        }
    }

    private void launchColourExplorer() {
        Intent intent = new Intent(this, ColourExplorerActivity.class);
        startActivity(intent);
    }

    public void onClickButtonColourExplorer(View view) {
        launchColourExplorer();
    }

    public void onClickButtonIdentifyColour(View view) {
        Intent intent = checkCameraHardware();

        if (intent == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_camera_hardware), Toast.LENGTH_SHORT).show();
            return;
        }

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }


}
