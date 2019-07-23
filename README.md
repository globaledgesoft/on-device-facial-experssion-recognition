# Facial Expression Recognition
The project is designed to utilize the Qualcomm Neural Processing SDK, a deep learning software from Qualcomm Snapdragon Platforms for Facial Expression Recognition in Android platform. The Android Application uses built-in/connected camera to capture the preview of Human Face and uses Machine Learning model to get the prediction/inference of facial expression.

## Pre-requisites
* Before starting the Android application, please follow the instructions for setting up neural network SDK using the link provided below.
   https://developer.qualcomm.com/docs/snpe/setup.html
* Android device 6.0 and above which uses below mentioned Snapdragon processors/Snapdragon HDK with display can be used to test the application

## List of Supported Snapdragon Devices
- Qualcomm Snapdragon 855
- Qualcomm Snapdragon 845
- Qualcomm Snapdragon 835
- Qualcomm Snapdragon 821
- Qualcomm Snapdragon 820
- Qualcomm Snapdragon 710
- Qualcomm Snapdragon 660
- Qualcomm Snapdragon 652
- Qualcomm Snapdragon 636
- Qualcomm Snapdragon 630
- Qualcomm Snapdragon 625
- Qualcomm Snapdragon 605
- Qualcomm Snapdragon 450

The above list supports the application with CPU and GPU.For more information on the supported devices, please follow this link https://developer.qualcomm.com/docs/snpe/overview.html

## Components
Below are the items used in the project.
1. Mobile Display with Facial Expression Recognition app
2. HDK Snapdragon board with GPU enabled
3. USB type – C cable
4. External camera setup
5. Power Cable

## Hardware Setup
![Qualcomm Snapdragon HDK image](https://git.globaledgesoft.com/root/OnDevice_FER_Project/blob/develop/AndroidApplication/app/src/main/res/drawable/snapdragon_hdk.jpg)

## How does it work?
The application opens a camera preview(restricted only for front camera), collects all the frames, detects faces present in the frames, crops the face and converts it to bitmap. The network is built via Neural Network builder by passing model file in dlc format as the input. The face bitmap is then given to model for inference, which returns expression of captured faces.
## Steps to Install and Run the Application

* Firstly set up the hardware as shown above in the Hardware Setup section
* Power on the Snapdragon HDK board
* Connect the Dev-Board/Android phone via usb to the device
* Switch on the display and choose the USB connection option to File Transfer
* Check if adb is installed in the windows/linux device, if not follow the below instructions in the below link to install
   https://developer.android.com/studio/command-line/adb.html.
* Use the below command to install the apk the connected device with help of adb. [Download APK(Debug)](QC_DashCam/app/build/outputs/apk/debug)
   $ adb install app-debug.apk
* Search the Facial Expression Recognition app in the app menu and launch the application

## Screenshot of the application
<img src="https://git.globaledgesoft.com/root/OnDevice_FER_Project/raw/develop/AndroidApplication/app/src/main/res/drawable/fer_screenshot.png" widht=640 height=360 />
