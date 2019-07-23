'''
To convert the images from the standard format (.jpg, .jpeg, .png etc) to .raw format which is supported by Qualcomm's Neural Processing SDK as an input to the model for inferencing.
'''

import argparse
import numpy as np
import cv2
from os import listdir
from os.path import isfile, join


if __name__ == '__main__':

    parser = argparse.ArgumentParser()
    parser.add_argument(
      '--in_path',
      type=str,
      default='./data/input/',
      help='Path to input images folder'
    )
    parser.add_argument(
        '--out_path',
        type=str,
        default='./data/raw-images/',
        help='Path to raw images folder'
    )

    args = parser.parse_args()

    my_input_path = args.in_path
    my_output_path = args.out_path

    all_images = [f for f in listdir(my_input_path) if isfile(join(my_input_path, f))]
    raw_images = []

    for i in all_images:

        in_file = my_input_path+i
        out_file = my_output_path + in_file.split('/')[-1].split('.')[0] + '.raw'

        raw_images.append(out_file.split('/')[-1])

        img = cv2.imread(in_file, 0)
        img = cv2.resize(img, (48, 48))

        np_array = np.array(img).astype('float32')
        np_array.tofile(out_file)


    with open(my_output_path+"raw-file.txt", "w") as my_file:
        for r_img in raw_images:
            my_file.write(my_output_path+r_img+'\n')
    print ('conversion successfull... \n{} images converted in raw images'.format(len(all_images)))
