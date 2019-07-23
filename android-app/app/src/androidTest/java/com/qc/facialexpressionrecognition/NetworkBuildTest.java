package com.qc.facialexpressionrecognition;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.qc.facialexpressionrecognition.Helpers.SNPEHelper;
import com.qc.facialexpressionrecognition.Interface.INetworkLoader;
import com.qc.facialexpressionrecognition.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NetworkBuildTest implements INetworkLoader {

    private static final String TAG = NetworkBuildTest.class.getSimpleName();
    private Context instrumentationCtx;
    private SNPEHelper mSNPEHelper;
    private Bitmap bMap, compressedBitmap;
    private INetworkLoader mCallbackINetworkLoader;
    private boolean isNetworkBuilt;


    @Before
    public void setUp() {
        instrumentationCtx = InstrumentationRegistry.getTargetContext();
        mCallbackINetworkLoader = this;
        mSNPEHelper = new SNPEHelper((Application) instrumentationCtx.getApplicationContext());

    }


    /**
     * Positive test case to check if the neural network is established or not
     */
    @Test
    public void Android_UT_test_buildNetwork_positive() throws InterruptedException {
        mSNPEHelper.loadNetwork((Application) instrumentationCtx.getApplicationContext(), mCallbackINetworkLoader, NeuralNetwork.Runtime.CPU);
        Thread.sleep(10000);
        Logger.d(TAG, "isNetworkBuilt " + isNetworkBuilt);
        assertTrue(isNetworkBuilt);
    }

    /**
     * Negative test case to check if the neural network is established or not
     */
    @Test
    public void Android_UT_test_buildNetwork_negative() throws InterruptedException {
        mSNPEHelper.loadNetwork((Application) instrumentationCtx.getApplicationContext(), mCallbackINetworkLoader, NeuralNetwork.Runtime.GPU_FLOAT16);
        Thread.sleep(500);
        Logger.d(TAG, "isNetworkBuiltt " + isNetworkBuilt);
        assertFalse(isNetworkBuilt);
    }


    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork, NeuralNetwork.Runtime runtime) {
        if (neuralNetwork != null)
            isNetworkBuilt = true;
        else isNetworkBuilt = false;
    }
}
