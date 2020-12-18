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
from rgb_ind_convertor import *
from util import *

parser = argparse.ArgumentParser()

parser.add_argument('--result_dir', type=str, default='./out',
					help='The folder that save network predictions.')

class PostProcessor:
	def __init__(self, *args, **kwargs):
		 pass
	
	def postProcess(self,input_dir,filename,originalLength, originalWidth,merge = True):
		print ("started post processing")
		input_paths = []
		out_paths = []
		# out_paths.append(save_dir)
		input_paths.append(input_dir)
		# print (input_dir)

		n = 1#len(input_paths)
		# n = 1
		for i in range(n):
			im = imread(input_paths[i], mode='RGB')
			# converting to indexed image
			im_ind = rgb2ind(im, color_map=floorplan_fuse_map_figure)
			# seperate image into room-seg & boundary-seg
			rm_ind = im_ind.copy()
			rm_ind[im_ind==9] = 0
			rm_ind[im_ind==10] = 0

			bd_ind = np.zeros(im_ind.shape, dtype=np.uint8)
			bd_ind[im_ind==9] = 9
			bd_ind[im_ind==10] = 10

			# only boundary lines, 0 is background
			hard_c = (bd_ind>0).astype(np.uint8)

			# region from room prediction it self - putting 1 wherever color is non white
			rm_mask = np.zeros(rm_ind.shape)
			rm_mask[rm_ind>0] = 1			
			# region from close_wall line		
			cw_mask = hard_c
			# refine close wall mask by filling the gap between bright line	
			cw_mask = fill_break_line(cw_mask)
				
			fuse_mask = cw_mask + rm_mask
			fuse_mask[fuse_mask>=1] = 255

			# refine fuse mask by filling the hole
			fuse_mask = flood_fill(fuse_mask)
			fuse_mask = fuse_mask // 255	

			# one room one label
			new_rm_ind = refine_room_region(cw_mask, rm_ind)

			# ignore the background mislabeling
			new_rm_ind = fuse_mask*new_rm_ind

			# print('Saving {}th refined room prediciton to {}'.format(i, out_paths[i]))
			if merge:
				new_rm_ind[bd_ind==9] = 9
				new_rm_ind[bd_ind==10] = 10
				rgb = ind2rgb(new_rm_ind, color_map=floorplan_fuse_map_figure)
			else:
				rgb = ind2rgb(new_rm_ind)
			# this is still a numpy array
			rgb = cv2.morphologyEx(rgb, cv2.MORPH_OPEN, cv2.getStructuringElement(cv2.MORPH_RECT,(5,5)))
			# imresize(rgb,(originalWidth,originalLength))
			print (rgb.shape)
			# saving 512*512 image in post folder
			imsave("post/" + filename +".png", rgb)
			# converting to pil image as app.py accepts only PIL image
			PIL_image = Image.fromarray(np.uint8(rgb)).convert('RGB')
			# resizing to original size of image - using pil library resize
			resizedImage = PIL_image.resize((originalWidth, originalLength))
			# resizedImage.save('pilCheck.png')
			rgb = np.array(resizedImage)
			# saving resized image of original size to post_originalSize folder
			imsave("post_originalSize/" + filename +".png", rgb)
			# print (rgb.shape)
			return resizedImage

# this func was used for testing only
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
		rm_ind[im_ind==9] = 0
		rm_ind[im_ind==10] = 0

		bd_ind = np.zeros(im_ind.shape, dtype=np.uint8)
		bd_ind[im_ind==9] = 9
		bd_ind[im_ind==10] = 10

		hard_c = (bd_ind>0).astype(np.uint8)

		# region from room prediction it self
		rm_mask = np.zeros(rm_ind.shape)
		rm_mask[rm_ind>0] = 1			
		# region from close_wall line		
		cw_mask = hard_c
		# refine close wall mask by filling the grap between bright line	
		cw_mask = fill_break_line(cw_mask)
			
		fuse_mask = cw_mask + rm_mask
		fuse_mask[fuse_mask>=1] = 255

		# refine fuse mask by filling the hole
		fuse_mask = flood_fill(fuse_mask)
		fuse_mask = fuse_mask // 255	

		# one room one label
		new_rm_ind = refine_room_region(cw_mask, rm_ind)

		# ignore the background mislabeling
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
# if __name__ == '__main__':
	# this main and global function were being used for testing
	# FLAGS, unparsed = parser.parse_known_args()
	# print (unparsed)


	# input_dir = FLAGS.result_dir
	# print (input_dir)
	# save_dir = os.path.join(input_dir, 'post')
	# save_dir = ""
	# print (save_dir)

	# post_process("./out/test.png", "./post/post.png",True)
