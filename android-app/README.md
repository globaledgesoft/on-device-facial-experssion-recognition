# Facial Expression Recognition Android Application

The application demonstates on the utilization of Qualcomm Neural Processing SDK, a deep learning software from Qualcomm Snapdragon Platforms for Facial Expression Recognition in Android platform. The Android application uses any built-in/connected camera to capture the preview of human face and use Machine Learning model to get the prediction/inference of facial expressions.

## Requirements
1. Mobile Display with Facial Expression Recognition app
2. HDK835 development board
3. USB type – C cable
4. External camera setup
5. Power Cable  


##  Pre-requisite For Camera Preview

```bash 
# Need this permission to obtain camera preview frames
<uses-permission android:name="android.permission.CAMERA" />

# In order to use camera2 APIs, add the below feature
<uses-feature android:name="android.hardware.camera2" />
```

## Configuring a Neural Network

Neural Network has to be built in the target device in order to get the inference from model. Below is the code to load a network from the SNPE container file (DLC).

```bash 

@Override
protected NeuralNetwork doInBackground(File... params) {
    NeuralNetwork network = null;
    try {
        final SNPE.NeuralNetworkBuilder builder = new SNPE.NeuralNetworkBuilder(mApplication)
                .setDebugEnabled(false)
	   #  Allows selecting a runtime order for the network.In the example below use DSP and fall back, in order, to GPU then CPU depending on whether any of the runtimes are available.
              .setRuntimeOrder(DSP, GPU, CPU)
                 .setModel(new File("<model-path>"))
                .setCpuFallbackEnabled(true)
                .setUseUserSuppliedBuffers(false);
     # Build the network
        network = builder.build();
    } catch (IllegalStateException | IOException e) {
        Logger.e(TAG, e.getMessage(), e);
    }
    return network;
}

```

##  Capturing Preview using Camera2 API

Texture view is used to render camera preview. TextureView.SurfaceTextureListener is an interface which can be used to notifiy when the surface texture associated with this texture view is available.

```bash 
private final TextureView.SurfaceTextureListener mSurfaceTextureListener
        = new TextureView.SurfaceTextureListener() {
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
       #check runtime permission
if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
    requestCameraPermission();
    return;
}

CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
try {
    if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
    }

#mCameraId = 1(Front Camera), mCameraId = 0(Rear Camera)
    manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
} catch (CameraAccessException e) {
    e.printStackTrace();
} catch (InterruptedException e) {
    throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
} 
}

```
## Camera Callbacks 

Camera call back, CameraDevice.StateCallback is used for receiving updates about the state of a camera device. In the below overrideen method, surface texture is created to capture the preview and obtain the frames.
   
   ```bash 
   @Override
public void onOpened(@NonNull CameraDevice cameraDevice) {
    # This method is called when the camera is opened.  We start camera preview here.
    mCameraOpenCloseLock.release();
    mCameraDevice = cameraDevice;  mPreviewRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
mPreviewRequestBuilder.addTarget(surface);
}

```
## Using Camera2 APIs for face detection

CaptureResults are produced by a CameraDevice after processing a CaptureRequest. The constant CaptureResult.STATISTICS_FACE_DETECT_MODE  is the operating mode for face detector. CaptureResult.STATISTICS_FACE_DETECT_MODE  gives the list of  faces detected through camera face detection.

```bash 
private CameraCaptureSession.CaptureCallback mCaptureCallback
        = new CameraCaptureSession.CaptureCallback() {
    private void process(CaptureResult result) {
        Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE );
        Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
        
```  

##  Localization of captured faces in custom View

The position of detected face can be obtained through the bounds of Face class. Rectangle can be drawn on the custom view by left,top,right and bottom parameters of bounds. Canvas class can be used to draw rectangle on face.

```bash 
final Face face = faces[i];

int left = face.getBounds().left;
int top = face.getBounds().top;
int right = face.getBounds().right;
int bottom = face.getBounds().bottom;
Rect rect = new Rect((left), (top), (right), (bottom));
mOverlayView.setRect(rect, mPreviewSize.getWidth(), mPreviewSize.getHeight());

```

##  Cropping Face bounds and scaling a Bitmap

Bitmap of fixed height and width can be obtained from TextureView.

```bash 
 Bitmap mBitmap = mTextureView.getBitmap(BITMAP_WIDTH, BITMAP_HEIGHT);
if (rect.left > 0 && rect.left < rect.right && rect.top < rect.bottom) {
    Bitmap cropped = Bitmap.createBitmap(mBitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom – rect.top);

#creating bitmap of size 48X48
    Bitmap resizedBitmap = Bitmap.createScaledBitmap(cropped, 48, 48, false);
    
```
    
##  Model Inference

The resized bitmap is converted to grey scale before giving it as input to the model. Basic image processing depends on the kind of input shape required by the model, then converting that processed image into the tensor is required. The prediction API requires a tensor format with type Float which returns facial expression recognition in Map<String, FloatTensor> object. The size of floatTensor is 1 which contains the index of facial expression.

```bash 
final Map<String, FloatTensor> outputs = inferenceOnBitmap(resizedBitmap);
for (Map.Entry<String, FloatTensor> output : outputs.entrySet()) {
    final FloatTensor tensor = output.getValue();
    final float[] values = new float[tensor.getSize()];
    tensor.read(values, 0, values.length);
    int expID = (int)values[0];
    text = lookupMsCoco(expID, "");
}

#The Hashmap of facial expressions versus index
public String lookupMsCoco(int cocoIndex, String fallback) {
    if (mCocoMap == null) {
        mCocoMap = new ArrayMap<>();
        mCocoMap.put(0, "Angry");
        mCocoMap.put(1, "Disgust");
        mCocoMap.put(2, "Fear");
        mCocoMap.put(3, "Happy");
        mCocoMap.put(4, "Sad");
        mCocoMap.put(5, "Surprise");
        mCocoMap.put(6, "Neutral");
    }
    return mCocoMap.containsKey(cocoIndex) ? mCocoMap.get(cocoIndex) : fallback;
}

```
