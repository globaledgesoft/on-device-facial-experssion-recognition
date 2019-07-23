#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import argparse
import tensorflow as tf
from tensorflow.python.tools import freeze_graph
from FerHelper import FerHelper
import configs as cfg
import numpy as np  # linear algebra
import pandas as pd  # data processing, CSV file I/O

#Model Architecture
def neural_network_model(input_x):

    # Convolutional layer 1
    with tf.name_scope('conv_layer1'):
        output_conv1 = tf.layers.conv2d(
            inputs=input_x,
            filters=32,
            kernel_size=[3, 3],
            padding="same",
            activation=tf.nn.relu)

    with tf.name_scope('maxpool_at_layer1'):
        output_maxpool1 = tf.layers.max_pooling2d(inputs=output_conv1, pool_size=[2, 2], strides=2)

    # Convolutional layer 2
    with tf.name_scope('conv_layer2'):
        output_conv2 = tf.layers.conv2d(
            inputs=output_maxpool1,
            filters=64,
            kernel_size=[3, 3],
            padding="same",
            activation=tf.nn.relu)

    with tf.name_scope('maxpool_at_layer2'):
        output_maxpool2 = tf.layers.max_pooling2d(inputs=output_conv2, pool_size=[2, 2], strides=2)

    # Convolutional layer 3
    with tf.name_scope('conv_layer3'):
        output_conv3 = tf.layers.conv2d(
            inputs=output_maxpool2,
            filters=64,
            kernel_size=[3, 3],
            padding="same",
            activation=tf.nn.relu)

    with tf.name_scope('maxpool_at_layer3'):
        output_maxpool3 = tf.layers.max_pooling2d(inputs=output_conv3, pool_size=[2, 2], strides=2)

    # Flatten layer
    with tf.name_scope('flattened'):
        flatten_conv = tf.reshape(output_maxpool3, [-1, 6 * 6 * 64])

    # Fully connected layer 1
    with tf.name_scope('FC1'):
        activated_output_fc1 = tf.layers.dense(inputs=flatten_conv, units=1024, activation=tf.nn.relu)

    # Fully connected layer 2
    with tf.name_scope('FC2'):
        activated_output_fc2 = tf.layers.dense(inputs=activated_output_fc1, units=7, activation=tf.nn.softmax)

    return activated_output_fc2

'''This function accepts the input data and train the model for given number of epochs and calculates the 
accuracy on test data and saves the trained model to the path provided in checkpoint_path
'''

def build_and_train_model(x, train_data, test_data, epoch, batch_size,
                          board_logs_path, checkpoint_path):

    pred_res = neural_network_model(x)

    saver = tf.train.Saver()

    # pre-processing the dataset
    ch = FerHelper()
    ch.set_up_images(train_data, test_data)

    with tf.name_scope("cross_entropy"):
        cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(logits=pred_res, labels=y_true))

    with tf.name_scope('optimizer'):
        optimizer = tf.train.AdamOptimizer(3e-4).minimize(cross_entropy)

    with tf.name_scope('truth_table_bool'):
        truth_table_bool = tf.equal(tf.argmax(pred_res, 1), tf.argmax(y_true, 1))

    with tf.name_scope('truth_table_int'):
        truth_table_int = tf.cast(truth_table_bool, tf.float32)

    with tf.name_scope('correct_preds'):
        correct_preds = tf.reduce_mean(truth_table_int)

    train_len = len(train_data)
    steps = int(train_len / batch_size)
    model_title = ''

    with tf.Session() as sess:
        writer = tf.summary.FileWriter(board_logs_path, sess.graph)
        sess.run(tf.global_variables_initializer())

        for ep in range(1, epoch + 1):
            for step in range(1, steps):
                if (step%100 == 0):
                    print ('Epoch : {}/{}, Step {}/{}'.format(ep, epoch, step, steps))
                batch = ch.next_batch(batch_size)
                sess.run(optimizer, feed_dict={x: batch[0], y_true: batch[1]})

            acc_result = sess.run(correct_preds, feed_dict={x: ch.test_images, y_true: ch.test_labels})
            model_title = checkpoint_path+'/fer-model-' + str(ep)
            saver.save(sess, model_title)
            tf.train.write_graph(sess.graph.as_graph_def(), '.', model_title + '.pbtxt', as_text=True)
            print('At epoch : {} \tAccuracy on test-set is : {}'.format (ep, acc_result))

        # Load checkpoint
        checkpoint = tf.train.get_checkpoint_state(checkpoint_path)
        input_checkpoint = checkpoint.model_checkpoint_path

        # We precise the file fullname of our freezed graph
        absolute_model_dir = "/".join(input_checkpoint.split('/')[:-1])
        output_graph = absolute_model_dir + "/frozen_model.pb"

        input_pb = model_title + '.pbtxt'
        freeze_graph.freeze_graph(input_pb, "", False,
                                  input_checkpoint, 'truth_table_bool/ArgMax',
                                  "save/restore_all", "save/Const:0",
                                  output_graph, True, ""
                                  )
        print('final frozen graph "frozen_model.pb" created and stored at location: {}'.format(absolute_model_dir))
        writer.close()

if __name__ == '__main__':

    parser = argparse.ArgumentParser()
    parser.add_argument(
      '--data',
      type=str,
      default='./data/fer2013.csv',
      help='Path to dataset folder.'
    )
    parser.add_argument(
        '--epoch',
        type=int,
        default=cfg.epochs,
        help='Number of epochs.'
    )
    parser.add_argument(
        '--batch_size',
        type=int,
        default=64,
        help='Batch size.'
    )
    parser.add_argument(
        '--board_logs',
        type=str,
        default='./board-logs/',
        help='Dir path to save the board-logs'
    )
    parser.add_argument(
        '--checkpoint',
        type=str,
        default='./checkpoints',
        help='Dir path to save the checkpoints'
    )

    args = parser.parse_args()

    df = pd.read_csv(args.data)

    train_data = df[df['Usage'] == 'Training'].copy()
    test_data = df[df['Usage'] == 'PublicTest'].copy()

    train_data['pixels'] = train_data['pixels'].apply(lambda m: np.asarray(m.split(' '), dtype='float32'))
    test_data['pixels'] = test_data['pixels'].map(lambda m: np.asarray(m.split(' ')))

    # PLACEHOLDER
    x = tf.placeholder(tf.float32, shape=[None, cfg.image_width, cfg.image_height, 1], name='x')
    y_true = tf.placeholder(tf.float32, shape=[None, cfg.classes], name='y_true')

    build_and_train_model(x, train_data, test_data, args.epoch,
                          args.batch_size, args.board_logs, args.checkpoint)
