import os
import argparse
import numpy as np
import tensorflow as tf

from scipy.misc import imread, imsave, imresize, toimage
from matplotlib import pyplot as plt
from matplotlib import image
from PIL import Image

from classifier import Classifier
from postprocess import PostProcessor
from roomCounter2 import InformationCollector

from os import listdir
from os.path import isfile, join

os.environ['CUDA_VISIBLE_DEVICES'] = '0'

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# input image path
parser = argparse.ArgumentParser()

parser.add_argument('--im_path', type=str, default='./demo/45765448.jpg',
					help='input image paths.')

# color map
floorplan_map = {
	0: [255,255,255], # background
	1: [192,192,224], # closet
	2: [192,255,255], # bathroom/washroom
	3: [224,255,192], # livingroom/kitchen/dining room
	4: [255,224,128], # bedroom
	5: [255,160, 96], # hall
	6: [255,224,224], # balcony
	7: [255,255,255], # not used
	8: [255,255,255], # not used
	9: [255, 60,128], # door & window
	10:[  0,  0,  0]  # wall
}

def ind2rgb(ind_im, color_map=floorplan_map):
	rgb_im = np.zeros((ind_im.shape[0], ind_im.shape[1], 3))

	for i, rgb in color_map.items():
		rgb_im[(ind_im==i)] = rgb

	return rgb_im

def main(args):
	# load input
	im = imread(args.im_path, mode='RGB')
	(originalLength, originalWidth,colors) = im.shape
	im = im.astype(np.float32)
	im = imresize(im, (512,512,3)) / 255.

	# create tensorflow session
	with tf.Session() as sess:
		
		# initialize
		sess.run(tf.group(tf.global_variables_initializer(),
					tf.local_variables_initializer()))

		# restore pretrained model
		saver = tf.train.import_meta_graph('./pretrained/pretrained_r3d.meta', clear_devices=True)
		saver.restore(sess, './pretrained/pretrained_r3d')

		# get default graph
		graph = tf.get_default_graph()

		# restore inputs & outpus tensor
		x = graph.get_tensor_by_name('inputs:0')
		room_type_logit = graph.get_tensor_by_name('Cast:0')
		room_boundary_logit = graph.get_tensor_by_name('Cast_1:0')

		# infer results
		[room_type, room_boundary] = sess.run([room_type_logit, room_boundary_logit],\
										feed_dict={x:im.reshape(1,512,512,3)})
		room_type, room_boundary = np.squeeze(room_type), np.squeeze(room_boundary)

		# merge results
		floorplan = room_type.copy()
		floorplan[room_boundary==1] = 9
		floorplan[room_boundary==2] = 10
		floorplan_rgb = ind2rgb(floorplan)

		# print (floorplan.shape)
		# print (floorplan_rgb.shape)


		# room_type_rgb = room_type
		# room_boundary_rgb = room_boundary

		# room_boundary[room_boundary==1] = 9
		# room_boundary[room_boundary==2] = 10

		# room_type_rgb = ind2rgb(room_type)
		# room_boundary_rgb = ind2rgb(room_boundary)

		# p = os.path.abspath(os.getcwd())
		# p = p + "\out\test2.png"
		# print (p)

		# plot results
		# plt.subplot(121)
		# plt.imshow(im)
		# plt.subplot(122)
		# plt.imshow(floorplan_rgb/255.)
		# plt.show()

		# image.imsave('out/test1.png', room_type_rgb/255.)
		# image.imsave('out/test2.png', room_boundary_rgb/255.)
		image.imsave('out/test.png', floorplan_rgb/255.)


		# scipy.misc.toimage(floorplan_rgb/255., cmin=0.0, cmax=...).save('outfile.jpg')
		# imsave(p, floorplan_rgb/255.)


def classifyImage(img_path,filename):
	classifier = Classifier()
	preds = classifier.classify(img_path,filename)

	return  preds

def postProcessImage(img_path,filename,originalLength, originalWidth):
	postProcessor = PostProcessor()
	pred = postProcessor.postProcess(img_path,filename,originalLength, originalWidth)

	return pred

def getInfo(input_path,img,originalLength, originalWidth):
	infoCollector = InformationCollector()
	roomCounts = infoCollector.countRooms(input_path)
	percentageCoveredArea = infoCollector.getCoveredAreaPercentage(img,originalLength, originalWidth)

	with open("infoFileCrop.txt", 'a') as writer:
		writer.write(str(input_path) + "\n")
		for (roomType, roomCount) in roomCounts:
			writer.write(roomType + ": " + roomCount + "\n")
		writer.write("\n--------------------------------------------------------------------\n")
	return roomCounts, percentageCoveredArea

def batchInfoCheck(pathToFolder):
	onlyfiles = [[(pathToFolder + '/' + f),f] for f in listdir(pathToFolder) if isfile(join(pathToFolder, f))]
	# print (onlyfiles)
	fileNumber = 0
	for myFile in onlyfiles:
		pathToFile = myFile[0]
		fileName = myFile[1][:-4]
		# print (myFile[0])
		# print (myFile[1])
		# mergedResult,testPath = classifyImage(pathToFile, fileName)
		# postProcessedResult = postProcessImage(testPath ,fileName)
		# roomCounts, percentageCoveredArea = getInfo("post/" + fileName + ".png",postProcessedResult)
		
		mergedResult,testPath,originalLength, originalWidth = classifyImage(pathToFile, fileName)
		postProcessedResult = postProcessImage(testPath ,fileName,originalLength, originalWidth)
		roomCounts, percentageCoveredArea = getInfo("post/" + fileName + ".png",postProcessedResult,originalLength, originalWidth)

		fileNumber += 1
	print (fileNumber)


if __name__ == '__main__':
	# this file purely for testing
	# FLAGS, unparsed = parser.parse_known_args()
	# main(FLAGS)
	batchInfoCheck("croppedTestData")
	# image = Image.open('post/30691830.png')
	# image = image.getcolors(image.size[0]*image.size[1])
	# image = Image.open('post/primary%3ADCIM%2FAlbum%201%2F47541863.jpg.png')
	# image = image.getcolors(image.size[0]*image.size[1])
	# print(image)
	# infoC = InformationCollector()
	# primary%3ADCIM%2FAlbum%201%2F47541863.jpg
	# print(infoC.countRooms("post/primary%3ADCIM%2FAlbum%201%2F47541863.jpg.png"))
	# image.save("savecheck.png")
	
	# new_image = image.resize((352, 584))
	# new_image = image.resize((474, 600))
	# new_image.save('check.png')

