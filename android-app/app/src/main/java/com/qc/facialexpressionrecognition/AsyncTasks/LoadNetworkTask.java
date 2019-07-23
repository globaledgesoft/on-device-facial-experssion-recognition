
package com.qc.facialexpressionrecognition.AsyncTasks;

import android.app.Application;
import android.os.AsyncTask;

import com.qc.facialexpressionrecognition.Interface.INetworkLoader;
import com.qc.facialexpressionrecognition.Utils.Logger;
import com.qc.facialexpressionrecognition.R;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qualcomm.qti.snpe.SNPE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LoadNetworkTask extends AsyncTask<File, Void, NeuralNetwork> {

    private static final String TAG = LoadNetworkTask.class.getSimpleName();
    private final Application mApplication;
    private final NeuralNetwork.Runtime mTargetRuntime;
    private INetworkLoader mCallbackINetworkLoader;
    private InputStream mInputstream;

    public LoadNetworkTask(final Application application,
                           final NeuralNetwork.Runtime targetRuntime,
                           final INetworkLoader iNetworkLoader) {
        mApplication = application;
        mTargetRuntime = targetRuntime;
        mCallbackINetworkLoader = iNetworkLoader;
        mInputstream = getFileByResourceId(R.raw.fer_153);

    }

    @Override
    protected NeuralNetwork doInBackground(File... params) {
        NeuralNetwork network = null;
        try {

            final SNPE.NeuralNetworkBuilder builder = new SNPE.NeuralNetworkBuilder(mApplication)
                    .setDebugEnabled(false)
                    .setRuntimeOrder(mTargetRuntime)
                    .setModel(mInputstream, mInputstream.available())
                    .setCpuFallbackEnabled(true)
                    .setUseUserSuppliedBuffers(false);
            network = builder.build();
        } catch (IllegalStateException | IOException e) {
            Logger.e(TAG, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            Logger.d(TAG, e.getMessage());
        }
        return network;
    }

    @Override
    protected void onPostExecute(NeuralNetwork neuralNetwork) {
        super.onPostExecute(neuralNetwork);
        mCallbackINetworkLoader.onNetworkBuilt(neuralNetwork, mTargetRuntime);
    }


    private InputStream getFileByResourceId(int id) {
        InputStream ins = mApplication.getResources().openRawResource(id);
        return ins;
    }
}


