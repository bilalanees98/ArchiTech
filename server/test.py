from util import *
from rgb_ind_convertor import *
import argparse

import os
import sys
import glob
import time
import random

import numpy as np
from scipy.misc import imread, imresize, imsave
from matplotlib import pyplot as plt
import cv2
from PIL import Image

sys.path.append('./utils/')

parser = argparse.ArgumentParser()

parser.add_argument('--result_dir', type=str, default='./out',
					help='The folder that save network predictions.')


def post_process(input_dir, save_dir, merge=True):
	input_paths = []
	out_paths = []
	out_paths.append(save_dir)
	input_paths.append(input_dir)
	print (input_dir)

	n = 1#len(input_paths)
	# n = 1
	for i in range(n):
		im = imread(input_paths[i], mode='RGB')
		im_ind = rgb2ind(im, color_map=floorplan_fuse_map_figure)
		# seperate image into room-seg & boundary-seg
		rm_ind = im_ind.copy()
		# keep only those indexes that are do not correspond to walls and windows/doors
		rm_ind[im_ind==9] = 0
		rm_ind[im_ind==10] = 0

		#keep only those indexes that correspond to walls and windows/doors 
		bd_ind = np.zeros(im_ind.shape, dtype=np.uint8)
		bd_ind[im_ind==9] = 9
		bd_ind[im_ind==10] = 10

		# get all from boundary image that are not background and convert to type uint8 - 
		# 0 corresponds to white color in floorplan_fuse_map_figure
		hard_c = (bd_ind>0).astype(np.uint8)

		# region from room prediction it self
		# get all from room type image that are not background and convert to type uint8 - 
		# 0 corresponds to white color in floorplan_fuse_map_figure		
		rm_mask = np.zeros(rm_ind.shape)
		rm_mask[rm_ind>0] = 1	

		# region from close_wall line		
		cw_mask = hard_c
		# refine close wall mask by filling the grap between bright line
		# basically filling up gaps in boundaries	
		cw_mask = fill_break_line(cw_mask)
		
		# re combine filled boundaries and roomtype predictions - both are still without white pixels
		fuse_mask = cw_mask + rm_mask

		# set the value of all non white pixels of the combined image to 255 - dont know why this is done
		# i think this is done so that we figure where there are holes in the image regardless of the color
		# since all index values are 255 now, the image is basically entirely one color
		fuse_mask[fuse_mask>=1] = 255

		# refine fuse mask by filling the hole
		# this function will fill in holes regardless of color since all pixel values are 255
		fuse_mask = flood_fill(fuse_mask)
		# simple whole number division to remobe 255 value
		# basically an array of 1s and 0s, where 1 is a pixel with color and 0 is a white pixel
		fuse_mask = fuse_mask // 255	

		# one room one label
		new_rm_ind = refine_room_region(cw_mask, rm_ind)

		# ignore the background mislabeling
		# element wise multiplication with the 1s and 0s of fuse_mask
		new_rm_ind = fuse_mask*new_rm_ind

		print('Saving {}th refined room prediciton to {}'.format(i, out_paths[i]))
		if merge:
			new_rm_ind[bd_ind==9] = 9
			new_rm_ind[bd_ind==10] = 10
			rgb = ind2rgb(new_rm_ind, color_map=floorplan_fuse_map_figure)
		else:
			rgb = ind2rgb(new_rm_ind)
		imsave(out_paths[i], rgb)
		rgb = cv2.morphologyEx(rgb, cv2.MORPH_OPEN, cv2.getStructuringElement(cv2.MORPH_RECT,(5,5)))
		imsave("post/post2.png", rgb)
		return rgb
if __name__ == '__main__':
	# purely for testing
	# FLAGS, unparsed = parser.parse_known_args()
	# print (unparsed)


	# input_dir = FLAGS.result_dir
	# print (input_dir)
	# save_dir = os.path.join(input_dir, 'post')
	# save_dir = ""
	# print (save_dir)

	post_process("./out/test.png", "./post/post.png",True)
