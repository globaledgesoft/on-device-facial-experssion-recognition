'''
This helper class is written based on the reference from following link "https://www.kaggle.com/kushagra15/cifar-image-classification"
It performs the following:
    1. Set up the train and test images into NumPy array and reshape it into 48x48x1.
    2. One hot encode the labels.
    3. Divide the train-data into batches during training the model
'''


import numpy as np  # linear algebra
import configs as cfg


class FerHelper():
    def __init__(self):
        self.i = 0

        self.train_images = None
        self.train_labels = None

        self.test_images = None
        self.test_labels = None

    #one-hot encodes the labels
    def one_hot_encode(self, vec, vals=7):
        n = len(vec)
        out = np.zeros((n, vals))
        out[range(n), vec] = 1
        return out

    #set up the train, test images and labels
    def set_up_images(self, train_data, test_data):
        print('setting up training images and labels')
        j = 0
        self.train_images = np.zeros((len(train_data), cfg.image_width, cfg.image_height))
        for pixels in train_data['pixels']:
            self.train_images[j] = pixels.reshape(cfg.image_width, cfg.image_height)
            j += 1
        self.train_images = self.train_images.reshape(
            (len(train_data), cfg.image_width, cfg.image_height, cfg.grey_channel))
        self.train_images = self.train_images.astype('float32') / 255
        self.train_labels = self.one_hot_encode(train_data['emotion'].values)

        print('setting up testing images and labels')
        k = 0
        self.test_images = np.zeros((len(test_data), cfg.image_width, cfg.image_height))
        for test_pixels in test_data['pixels']:
            self.test_images[k] = test_pixels.reshape((cfg.image_width, cfg.image_height))
            k += 1
        self.test_images = np.expand_dims(self.test_images, 3)
        self.test_images = self.test_images.astype('float32') / 255
        self.test_labels = self.one_hot_encode(test_data['emotion'].values)

    def next_batch(self, batch_size):
        # note that the 100 dimension in the reshape call is set by an assumed batch size of `100
        batch_size = self.train_images[self.i:self.i + batch_size].shape[0]
        x = self.train_images[self.i:self.i + batch_size].reshape(batch_size, cfg.image_width, cfg.image_height, 1)
        y = self.train_labels[self.i:self.i + batch_size]
        self.i = (self.i + batch_size) % len(self.train_images)
        return x, y
