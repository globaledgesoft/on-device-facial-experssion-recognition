package com.qc.facialexpressionrecognition.Helpers;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.widget.Toast;

import com.qc.facialexpressionrecognition.AsyncTasks.LoadNetworkTask;
import com.qc.facialexpressionrecognition.Interface.INetworkLoader;
import com.qc.facialexpressionrecognition.R;
import com.qc.facialexpressionrecognition.Utils.Constants;
import com.qc.facialexpressionrecognition.Utils.Logger;
import com.qualcomm.qti.snpe.FloatTensor;
import com.qualcomm.qti.snpe.NeuralNetwork;

import java.util.HashMap;
import java.util.Map;

public class SNPEHelper {

    private static final String TAG = SNPEHelper.class.getSimpleName();
    public NeuralNetwork mNeuralnetwork;
    private BitmapToFloatArrayHelper mBitmapToFloatHelper;
    private int[] mInputTensorShapeHWC;
    private FloatTensor mInputTensorReused;
    private Map<String, FloatTensor> mInputTensorsMap;
    private static final String MNETSSD_INPUT_LAYER = "x:0";
    private Application application;


    public int getInputTensorWidth() {
        return mInputTensorShapeHWC == null ? 0 : mInputTensorShapeHWC[1];
    }

    public int getInputTensorHeight() {
        return mInputTensorShapeHWC == null ? 0 : mInputTensorShapeHWC[2];
    }


    public SNPEHelper(Application application) {
        this.application = application;
        mBitmapToFloatHelper = new BitmapToFloatArrayHelper();
    }

    /**
     * Async Task call to build neural network
     *
     * @param mApplication
     * @param mCallbackINetworkLoader
     * @param runtime
     */
    public static void loadNetwork(Application mApplication, INetworkLoader mCallbackINetworkLoader, NeuralNetwork.Runtime runtime) {
        LoadNetworkTask mLoadTask = new LoadNetworkTask(mApplication, runtime, mCallbackINetworkLoader);
        mLoadTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.mNeuralnetwork = neuralNetwork;

    }

    /**
     * Method to infer facial expression tensor from model
     *
     * @param modelInputBitmap
     * @return
     */
    public String modelInference(Bitmap modelInputBitmap) {

        String text = "";
        // execute the inference, and get 1 tensor as output
        final Map<String, FloatTensor> outputs = inferenceOnBitmap(modelInputBitmap);
        if(outputs != null) {
            for (Map.Entry<String, FloatTensor> output : outputs.entrySet()) {
                final FloatTensor tensor = output.getValue();
                final float[] values = new float[tensor.getSize()];
                tensor.read(values, 0, values.length);
                int expID = (int) values[0];
                text = lookupMsCoco(expID, "");
                Logger.d(TAG, "text " + text + "");
            }
        }
        return text;

    }


    /* Generic functions, for typical image models */

    public Map<String, FloatTensor> inferenceOnBitmap(Bitmap inputBitmap) {
        // read the input shape
        mInputTensorShapeHWC = mNeuralnetwork.getInputTensorsShapes().get(MNETSSD_INPUT_LAYER);
        // allocate the single input tensor
        mInputTensorReused = mNeuralnetwork.createFloatTensor(mInputTensorShapeHWC);
        // add it to the map of inputs, even if it's a single input
        mInputTensorsMap = new HashMap<>();

        final float[] values = new float[mInputTensorReused.getSize()];
        mInputTensorReused.read(values, 0, values.length);

        mInputTensorsMap.put(MNETSSD_INPUT_LAYER, mInputTensorReused);

        final Map<String, FloatTensor> outputs;
        try {
            // safety check
            if (mNeuralnetwork == null || mInputTensorReused == null || inputBitmap.getWidth() != getInputTensorWidth() || inputBitmap.getHeight() != getInputTensorHeight()) {
                Toast.makeText(application.getBaseContext(), R.string.error_neural_network, Toast.LENGTH_SHORT).show();
                return null;
            }

            // [0.3ms] Bitmap to gray scale
            float[] floatInput = mBitmapToFloatHelper.convertToGrayScale(inputBitmap);

            if (mBitmapToFloatHelper.isFloatBufferBlack())
                return null;

            mInputTensorReused.write(floatInput, 0, floatInput.length, 0, 0);
            outputs = mNeuralnetwork.execute(mInputTensorsMap);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return outputs;
    }

    // VERBOSE COCO object map
    private Map<Integer, String> mCocoMap;


    public String lookupMsCoco(int cocoIndex, String fallback) {

        if (mCocoMap == null) {
            mCocoMap = new ArrayMap<>();
            mCocoMap.put(0, Constants.ANGRY);
            mCocoMap.put(1, Constants.DISGUST);
            mCocoMap.put(2, Constants.FEAR);
            mCocoMap.put(3, Constants.HAPPY);
            mCocoMap.put(4, Constants.SAD);
            mCocoMap.put(5, Constants.SURPRISE);
            mCocoMap.put(6, Constants.NEUTRAL);
        }
        return mCocoMap.containsKey(cocoIndex) ? mCocoMap.get(cocoIndex) : fallback;
    }

}
