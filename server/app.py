import os
from flask import Flask, jsonify, request, send_file, Response,jsonify
from flask_restful import Api, Resource

import io
from base64 import encodebytes
from PIL import Image
import numpy as np
from scipy.misc import imread, imresize, imsave
import sys
sys.path.append('./utils/')
from rgb_ind_convertor import *
from util import *

from classifier import Classifier
from postprocess import PostProcessor
from roomCounter2 import InformationCollector
from scraper import Scraper

app = Flask(__name__)
api = Api(app)

# this api is called by architech android app - it passed received image through prediction model
# saves appropriate versions on server, does appropriate calculations and return a json reply
@app.route('/predict' ,methods=['POST'])
def predict():  

    if request.method == 'POST':
        print (request.files)

    # check if the post request has the file part
    if 'image' not in request.files:
        print ("someting went wrong 1")
        return "someting went wrong 1"


    #receives file
    user_file = request.files['image']
    print("\nReceived image File name : " + user_file.filename)

    if user_file.filename == '':
        print ("file name not found ..." )
        return "file name not found ..." 
    
    else:
        path = os.path.join(os.getcwd()+'\\serverStore\\'+user_file.filename+'.jpg')
        user_file.save(path)

        floorplanLengthFullsize = request.form['length']
        floorplanwidthFullsize = request.form['width']

        #for room boundary and room type
        # roomBoundary, roomType = classifyImage(path)

        #for merged results
        mergedResult,testPath,originalLength, originalWidth = classifyImage(path, user_file.filename)
        postProcessedResult, fp_array = postProcessImage(testPath ,user_file.filename,originalLength, originalWidth)
        roomCounts, percentageCoveredArea, costEstimate = getInfo("post/" + user_file.filename + ".png",postProcessedResult,originalLength, originalWidth,floorplanLengthFullsize, floorplanwidthFullsize, costs)


        byte_arr = io.BytesIO()
        postProcessedResult.save(byte_arr, format='PNG') # convert the postprocessed PIL image to byte array
        encoded_img = encodebytes(byte_arr.getvalue()).decode('ascii') # encode as base64


        response =  { 'Status' : 'Success', 'roomCounts': roomCounts ,
         'percentageCoveredArea': percentageCoveredArea, 'costEstimate': costEstimate,
          'ImageBytes': encoded_img,
          'array': fp_array.tolist()}
        return jsonify(response) # send the result to client
        

# this is the api that is called by unity 3d module
# simply receives a filename and gets it from the folder post_orginalSize and converts 
# to array and returns
@app.route('/getFloorplanForUnity' ,methods=['POST'])
def getFloorplanForUnity():
    floorplanName = request.form['floorplanName']
    classifier = Classifier()
    postProcessor = PostProcessor()
   
    filename = floorplanName

    testPath = 'post_originalSize/' +filename +'.png'
    im = imread(testPath, mode='RGB')
    im_ind = rgb2ind(im, color_map=floorplan_fuse_map_figure)
    preds=[]
    # count = 0
    originalLength = im_ind.shape[0] 
    originalWidth = im_ind.shape[1]
    # convert 2d prediction array to a 1d array that is acceptable for unity module
    for i in range(originalLength):
        for j in range(originalWidth):

# for 3d model with color generation
            if(im_ind[i][j]==10):#wall
                preds.append(8)
            elif (im_ind[i][j]==9):#door/window
                preds.append(7)
            elif (im_ind[i][j]==6):#balcony
                preds.append(6)
            elif (im_ind[i][j]==5):#hall
                preds.append(5)
            elif (im_ind[i][j]==4):#bedroom
                preds.append(4)
            elif (im_ind[i][j]==3):#living/kitchen/dining
                preds.append(3)
            elif (im_ind[i][j]==2):#bathroom
                preds.append(2)
            elif (im_ind[i][j]==1):#closet
                preds.append(1)
            elif (im_ind[i][j]==0):#background
                preds.append(0)
#-----------------------------------------------

# for simple 3d model without room type coloring
            # if(im_ind[i][j]==10):
            #     # count += 1
            #     preds.append(1)
            # else:
            #     preds.append(0)
#----------------------------------------------

    # print (count)
    return jsonify(preds)


def classifyImage(img_path,filename):
    classifier = Classifier()
    preds = classifier.classify(img_path,filename)

    return  preds

def postProcessImage(img_path,filename,originalLength, originalWidth):
    postProcessor = PostProcessor()
    pred = postProcessor.postProcess(img_path,filename,originalLength, originalWidth)

    return pred


def getInfo(input_path,img,originalLength, originalWidth, length, width, costs):
    infoCollector = InformationCollector()
    roomCounts = infoCollector.countRooms(input_path)
    percentageCoveredArea = infoCollector.getCoveredAreaPercentage(img,originalLength, originalWidth)
    costEstimate = infoCollector.getCostEstimate(length, width, percentageCoveredArea, costs)
    return roomCounts, percentageCoveredArea, costEstimate


if __name__ == '__main__':
    # app.run(debug=True)
    scraper = Scraper()
    costs = scraper.scrape()
    # below costs list is for testing, scraping takes a bit longer
    # costs = ['1350','1275','1250']
    app.run(host="0.0.0.0", port=5001, debug=True)