package com.qc.facialexpressionrecognition.Activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.qc.facialexpressionrecognition.Interface.INetworkLoader;
import com.qc.facialexpressionrecognition.Utils.Constants;
import com.qc.facialexpressionrecognition.Utils.Logger;
import com.qc.facialexpressionrecognition.Utils.Util;
import com.qc.facialexpressionrecognition.Fragment.CameraPreviewFragment;
import com.qc.facialexpressionrecognition.Helpers.SNPEHelper;
import com.qc.facialexpressionrecognition.R;
import com.qualcomm.qti.snpe.NeuralNetwork;

public class MainActivity extends AppCompatActivity implements INetworkLoader {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static SNPEHelper mSNPEHelper;
    private INetworkLoader mCallbackINetworkLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCallbackINetworkLoader = this;
        //Network load call
        mSNPEHelper = new SNPEHelper(getApplication());
        mSNPEHelper.loadNetwork(getApplication(), mCallbackINetworkLoader, NeuralNetwork.Runtime.DSP);
    }

    /**
     * Load network callback
     *
     * @param neuralNetwork
     * @param runtime
     */
    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork, NeuralNetwork.Runtime runtime) {
        if (neuralNetwork == null) {
            if (runtime == NeuralNetwork.Runtime.DSP)
                //Fallback to GPU
                mSNPEHelper.loadNetwork(getApplication(), mCallbackINetworkLoader, NeuralNetwork.Runtime.GPU_FLOAT16);
            else if (runtime == NeuralNetwork.Runtime.GPU_FLOAT16)
                //Fallback to CPU
                mSNPEHelper.loadNetwork(getApplication(), mCallbackINetworkLoader, NeuralNetwork.Runtime.CPU);
            else if (runtime == NeuralNetwork.Runtime.CPU)
                Util.showToast(getString(R.string.runtime_error), this);

        } else {
            mSNPEHelper.setNeuralNetwork(neuralNetwork);
            Logger.d(TAG, "Network built successfully " + neuralNetwork.getRuntime());
            goToCameraPreviewFragment();
        }
    }


    /**
     * Method to navigate to CameraPreviewFragment
     */

    private void goToCameraPreviewFragment() {
        if (Util.hasPermission(MainActivity.this)) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_content, CameraPreviewFragment.newInstance())
                    .commit();

        } else {
            requestPermission();
        }
    }

    /**
     * permission result callback, if permission is granted navigate to CameraPreview Fragment
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {


        if (requestCode == Constants.PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToCameraPreviewFragment();
            } else {
                Util.showToast(getString(R.string.toast_camera_permission), this);
                finish();
            }
        }
    }

    /**
     * Method to request Camera permission
     */
    private void requestPermission() {
        requestPermissions(new String[]{Constants.PERMISSION_CAMERA}, Constants.PERMISSIONS_REQUEST);
    }

}
