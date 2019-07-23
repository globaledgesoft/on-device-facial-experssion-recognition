package com.qc.facialexpressionrecognition.Interface;

import com.qualcomm.qti.snpe.NeuralNetwork;

public interface INetworkLoader {
    void onNetworkBuilt(NeuralNetwork neuralNetwork, NeuralNetwork.Runtime runtime);
}
