import cv2
import numpy as np
from scipy import ndimage

def fast_hist(im, gt, n=9):
    """
    n is num_of_classes
    """
    k = (gt >= 0) & (gt < n)
    return np.bincount(n * gt[k].astype(int) + im[k], minlength=n**2).reshape(n, n)

def flood_fill(test_array, h_max=255):
	"""
	fill in the hole 
	"""
	input_array = np.copy(test_array)
	# print(np.unique(input_array))
	# basically creates a boolean array for connected regions -
	# 1 arg is dimensions on which to apply, 2nd is distance
	# the square of this distance is the range of the connected region checker
	# astype(np.int) converts it into 1's and 0's
	el = ndimage.generate_binary_structure(2,2).astype(np.int)
	# print (el)

	# perform binary erosion with generated binary structure 'el' on image
	# will return an array of 1's and 0's, where 1 is not nan and 0 is nan
	inside_mask = ndimage.binary_erosion(~np.isnan(input_array), structure=el)

	output_array = np.copy(input_array)
	# create new output image from eroded image, set all values greater than 0 to h_max(255)
	output_array[inside_mask]=h_max
	# print(output_array)
	# print(np.unique(output_array))

	output_old_array = np.copy(input_array)
	# fill(0) sets all values in np array to 0
	output_old_array.fill(0)   

	# create another structure, which basically acts as a kernel
	el = ndimage.generate_binary_structure(2,1).astype(np.int)
	# print(el)
	# i = 0
	# erode image, chcek if eroded and non eroded are same - this is stop condition meaning holes have been filled
	# np.maximum returns an array that has performed elementwise max comparison
	while not np.array_equal(output_old_array, output_array):
		output_old_array = np.copy(output_array)
		output_array = np.maximum(input_array,ndimage.grey_erosion(output_array, size=(3,3), footprint=el))
		# i += 1
	# print(i)
	# print(output_array)
	# print(np.unique(output_array))
	return output_array

def fill_break_line(cw_mask):
	# can test with different kernels - try later
	# currently creating 4 kernels with 2 thicknesses, each thickness has a vertical and horizontal kernel
	broken_line_h = np.array([[0,0,0,0,0],
							[0,0,0,0,0],
							[1,0,0,0,1],
							[0,0,0,0,0],
							[0,0,0,0,0]], dtype=np.uint8)	
	broken_line_h2 = np.array([[0,0,0,0,0],
							[0,0,0,0,0],
							[1,1,0,1,1],
							[0,0,0,0,0],
							[0,0,0,0,0]], dtype=np.uint8)		
	
	broken_line_v = np.transpose(broken_line_h)
	broken_line_v2 = np.transpose(broken_line_h2)
	# running morphological close to reduce in-line noise
	cw_mask = cv2.morphologyEx(cw_mask, cv2.MORPH_CLOSE, broken_line_h)
	cw_mask = cv2.morphologyEx(cw_mask, cv2.MORPH_CLOSE, broken_line_v)
	cw_mask = cv2.morphologyEx(cw_mask, cv2.MORPH_CLOSE, broken_line_h2)
	cw_mask = cv2.morphologyEx(cw_mask, cv2.MORPH_CLOSE, broken_line_v2)

	return cw_mask	

def refine_room_region(cw_mask, rm_ind):
	# ndimage.label uniquely labels features in given np array, the structuring element is by default cross shaped
	# basically we get boxes of bounded regions, in our case each type of classification
	# 1-cw_mask basically inverts 1s to 0s and vice versa - as 0 is considered background in this func
	label_rm, num_label = ndimage.label((1-cw_mask))
	# print(np.unique(cw_mask))
	# print(np.unique(1-cw_mask))
	# print(np.unique(label_rm))

	new_rm_ind = np.zeros(rm_ind.shape)
	for j in range(1, num_label+1):  
		# get bounded regions of each type - where 1 will be pixel of that region and 0 will be pixel 
		# not belonging to region
		mask = (label_rm == j).astype(np.uint8)
		# print (mask)
		ys, xs = np.where(mask!=0)
		# print (ys)
		# print (xs)
		# return new_rm_ind
		area = (np.amax(xs)-np.amin(xs))*(np.amax(ys)-np.amin(ys))
		# can check with different values for area - try later
		if area < 100:
			continue
		else:
			# get counts of each type of pixel(pixels values actually) in one bounded region
			room_types, type_counts = np.unique(mask*rm_ind, return_counts=True)
			if len(room_types) > 1:
				room_types = room_types[1:] # ignore background type which is zero
				type_counts = type_counts[1:] # ignore background count
			# set every pixel to the value of the most frequent pixel value
			# then add to image that is to be returned
			new_rm_ind += mask*room_types[np.argmax(type_counts)]

	return new_rm_ind
