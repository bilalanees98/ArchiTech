import os
import argparse
import numpy as np
import tensorflow as tf

from scipy.misc import imread, imsave, imresize, toimage
from matplotlib import pyplot as plt
from matplotlib import image

class Classifier:
    def __init__(self, *args, **kwargs):
        self.floorplan_map = {
        0: [255,255,255], # background
        1: [192,192,224], # closet
        2: [192,255,255], # batchroom/washroom
        3: [224,255,192], # livingroom/kitchen/dining room
        4: [255,224,128], # bedroom
        5: [255,160, 96], # hall
        6: [255,224,224], # balcony
        7: [255,255,255], # not used
        8: [255,255,255], # not used
        9: [255, 60,128], # door & window
        10:[  0,  0,  0]  # wall
        }

    def classify(self,imgPath,filename):
        
   
        im = imread(imgPath, mode='RGB')
        (originalLength, originalWidth,colors) = im.shape
        print (im.shape)
        im = im.astype(np.float32)
        im = imresize(im, (512,512,3)) / 255.

	# create tensorflow session
        with tf.Session() as sess:
		
            # initialize
            sess.run(tf.group(tf.global_variables_initializer(),
                        tf.local_variables_initializer()))

            print ("loading model")
            saver = tf.train.import_meta_graph('./pretrained/pretrained_r3d.meta', clear_devices=True)
            saver.restore(sess, './pretrained/pretrained_r3d')

            
            # get default graph
            graph = tf.get_default_graph()

            # restore inputs & outpus tensor
            x = graph.get_tensor_by_name('inputs:0')
            room_type_logit = graph.get_tensor_by_name('Cast:0')
            room_boundary_logit = graph.get_tensor_by_name('Cast_1:0')

            # infer results, get numpy arrays
            [room_type, room_boundary] = sess.run([room_type_logit, room_boundary_logit],\
                                            feed_dict={x:im.reshape(1,512,512,3)})
            
            room_type, room_boundary = np.squeeze(room_type), np.squeeze(room_boundary)

            # merge results
            floorplan = room_type.copy()
            floorplan[room_boundary==1] = 9
            floorplan[room_boundary==2] = 10
            floorplan_rgb = self.ind2rgb(floorplan)

            # floorplan = room_boundary.copy()
            # floorplan[room_type==1] = 9
            # floorplan[room_type==2] = 10
            # floorplan_rgb = self.ind2rgb(floorplan)

            # room_type_rgb = room_type
            # room_boundary_rgb = room_boundary


            #numpy arrays are colored
            # room_type_rgb = self.ind2rgb(room_type)
            # room_boundary_rgb = self.ind2rgb(room_boundary)


            # plot results
            # plt.subplot(121)
            # plt.imshow(im)
            # plt.subplot(122)
            # plt.imshow(floorplan_rgb/255.)
            # plt.show()

            #for returning romm type and room boundary
            # image.imsave('out/test1.png', room_type_rgb/255.)
            # image.imsave('out/test2.png', room_boundary_rgb/255.)
            # roomB = toimage(room_boundary_rgb/255.)
            # roomT = toimage(room_type_rgb/255.)
            # return roomB,roomT

            #for returning merged results
            image.imsave('out/' +filename +'.png', floorplan_rgb/255.)
            mergedResult = toimage(floorplan_rgb/255.)

            return mergedResult,'out/' +filename +'.png', originalLength, originalWidth
    
    def ind2rgb(self,ind_im):
        color_map=self.floorplan_map
        rgb_im = np.zeros((ind_im.shape[0], ind_im.shape[1], 3))

        for i, rgb in color_map.items():
            rgb_im[(ind_im==i)] = rgb

        return rgb_im
