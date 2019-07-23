# QDN Facial Expression Recognition Board Application

The application demonstrates Facial Expression Recognition. It detects expressions such as happy, sad, fear, disgust, angry, neutral, surprise. Facial Expression Recognition is useful in order to judge the emotional responses of the public to drive business outcomes. This can be used to detect emotions like fear and uncertainty during the auto-scanning of the airline passengers for signs of terrorism. This can also be installed in the vehicles to detect the drowsiness state of the driver and hence prevent the accidents to occur.



To develop the Facial Expression Recognition application we make use of Snapdragon mobile platforms(HDK 835) and Qualcomm's Neural Processing SDK.
## Recommended setup for model training
### Hardware prerequisite

1. Intel i5 or greater
2. NVIDIA 10 series or greater (only for training on GPU)
3. RAM 16 GB or more

### System software requirements
1. Ubuntu 14.04 LTS or greater
2. CUDA (only for training on GPU)
3. CuDNN (only for training on GPU)
4. Python 2/3

## How to download the data
1. Go to the following kaggle link: https://www.kaggle.com/c/challenges-in-representation-learning-facial-expression-recognition-challenge/data and download the dataset .
2. Extract and place the "fer2013.csv" file in the /data directory.


## How to train the model

- Execute the following commands for installing the dependencies,

```bash
# Run this command from the <PROJECT_ROOT_DIR>
$ sudo pip install -r dependencies/requirement.txt
```

On running the src/train.py the model training would start. We can explicitly provide the following arguments for custom training.

- --data [Path to dataset folder]
- --epoch [number of epochs]
- --batch_size [batch size]
- --board_logs [Directory path to save the board-logs]
- --checkpoint [Dir path to save the checkpoints]

The model is trained using TensorFlow framework in low-level API and exported to frozen graph file (frozen_model.pb).


## How to convert Tensorflow's model into DLC?

Prerequisites: Neural Processing SDK setup. Use the instructions from the below link to make the setup,

https://developer.qualcomm.com/software/qualcomm-neural-processing-sdk/getting-started

- Initialize the environmental variables of Neural Processing SDK with tensorflow.
    
- Once you have the .pb file which is generated during training, convert it into Neural Processing SDK's .dlc format using the 'snpe-tensorflow-to-dlc' tool as shown below:
```bash
$ snpe-tensorflow-to-dlc –graph checkpoints/frozen_model.pb -i x 1,48,48,1 --out_node truth_table_bool/ArgMax --dlc fer.dlc
```

## Inference on Ubuntu using Qualcomm’s Neural Processing SDK

Qualcomm’s Neural Processing SDK doesn’t use the standard images format (.jpg, .png etc) as an input for the model for inferencing. 

Neural Processing SDK requires the input image in the form of a NumPy array stored as a raw file. In order to run the application in Neural Processing SDK we need to preprocess the input image. 

Following are the details of pre-processing steps followed in src/img2raw.py file,

(We have used the opencv for pre-processing the image)
1. Resize the image with the dimensions of 48x48.
2. Convert the image to type float32.
3. Store this pre-processed array as a raw file.

On running the src/img2raw.py script, raw images and raw-file.txt file is generated.


# Steps to get the final predictions for test sequence of data using snpe-net-run tool

1. Run the below command while in the directory where the model (.dlc file) is present. 
    

    This command generates an output folder in which the results are stored. The folder contains output tensor data of 7 probabilities for 7 categories. The raw-file.txt used below has been generated from the above preprocessing commands.
    
    ```bash
$ snpe-net-run --container fer.dlc --input_list  ./data/raw-images/raw-file.txt
```


2. To see the classified results from the output folder, we should run the following script present in snpe-sdk path  ‘$SNPE_ROOT/models/inception_v3/scripts’ from the model directory (where .dlc file is present) with custom modifications as given below:

- Update the number of labels to be classified by your model (Here it is 7).

- Update the path of ‘cur_results_file’ to your snpe-net-run results folder. 

    The 'raw-file.txt' file below contains the path to raw images and the 'label-file.txt' file contains label-names of the model.
   
    ```bash
$ python $SNPE_ROOT/models/inception_v3/scripts/show_inceptionv3_classifications.py -i ./data/raw-images/raw-file.txt  -o output -l ./data/label-file.txt
```


On executing the above script we can see the predicted classes for each image given in raw-file.txt.

