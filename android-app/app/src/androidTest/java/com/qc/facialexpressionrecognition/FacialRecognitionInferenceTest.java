package com.qc.facialexpressionrecognition;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.qc.facialexpressionrecognition.Helpers.SNPEHelper;
import com.qc.facialexpressionrecognition.Interface.INetworkLoader;
import com.qc.facialexpressionrecognition.Utils.Constants;
import com.qc.facialexpressionrecognition.Utils.Logger;
import com.qualcomm.qti.snpe.NeuralNetwork;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FacialRecognitionInferenceTest implements INetworkLoader {

    private static final String TAG = FacialRecognitionInferenceTest.class.getSimpleName();
    private Context mContext;
    private SNPEHelper mSNPEHelper;
    private INetworkLoader mCallbackINetworkLoader;
    private boolean isNetworkBuilt;


    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        mCallbackINetworkLoader = this;
        mSNPEHelper = new SNPEHelper((Application) mContext.getApplicationContext());

    }

    public void buildNetwork() {
        mSNPEHelper.loadNetwork((Application) mContext.getApplicationContext(), mCallbackINetworkLoader, NeuralNetwork.Runtime.CPU);
    }

    /**
     * Facial recognition inference text for Disgust
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_disgust() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_disgust);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text Disgust " + text);
            assertEquals(Constants.DISGUST,text);
        }
    }

    /**
     * Facial recognition inference text for Neutral
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_neutral() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_neutral);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.NEUTRAL,text);
        }
    }

    /**
     * Facial recognition inference text for Happy
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_happy() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_happy);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.HAPPY,text);
        }
    }

    /**
     * Facial recognition inference text for Angry
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_angry() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_angry);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.ANGRY,text);
        }
    }

    /**
     * Facial recognition inference text for Surprise
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_surprise() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_surprise);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.SURPRISE,text);
        }
    }

    /**
     * Facial recognition inference text for Fear
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_fear() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_fear);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.FEAR,text);
        }
    }

    /**
     * Facial recognition inference text for Sad
     * @throws InterruptedException
     */
    @Test
    public void Android_UT_test_facialInference_sad() throws InterruptedException {
        buildNetwork();
        Thread.sleep(10000);
        if (isNetworkBuilt) {
            Bitmap mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_sad);
            Bitmap resized = Bitmap.createScaledBitmap(mBitmap, Constants.BITMAP_WIDTH, Constants.BITMAP_HEIGHT, false);
            String text = mSNPEHelper.modelInference(resized);
            Thread.sleep(5000);
            Logger.d(TAG, "Facial text " + text);
            assertEquals(Constants.SAD,text);
        }
    }


    @Override
    public void onNetworkBuilt(NeuralNetwork neuralNetwork, NeuralNetwork.Runtime runtime) {
        Logger.d(TAG, "onNetworkBuilt");
        if (neuralNetwork != null) {
            mSNPEHelper.setNeuralNetwork(neuralNetwork);
            isNetworkBuilt = true;
        } else isNetworkBuilt = false;
    }
}
