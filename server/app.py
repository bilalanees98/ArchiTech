import os
from flask import Flask, jsonify, request, send_file, Response,jsonify
from flask_restful import Api, Resource

import io
from base64 import encodebytes
from PIL import Image

from classifier import Classifier
from postprocess import PostProcessor
from roomCounter2 import InformationCollector
from scraper import Scraper

app = Flask(__name__)
api = Api(app)

@app.route('/predict' ,methods=['POST'])
def predict():  
    # data = request.get_json(force=True)
    # print (data)
    if request.method == 'POST':
        print (request.files)
        # print (request.data)
    # check if the post request has the file part
    if 'image' not in request.files:
        print ("someting went wrong 1")
        return "someting went wrong 1"
    
    # user_file = request.files['file']
    # temp = request.files['file']

    #receives file
    user_file = request.files['image']
    # filename = werkzeug.utils.secure_filename(imagefile.filename)
    print("\nReceived image File name : " + user_file.filename)
    # imagefile.save(filename)

    if user_file.filename == '':
        print ("file name not found ..." )
        return "file name not found ..." 
    
    else:
        path = os.path.join(os.getcwd()+'\\serverStore\\'+user_file.filename)
        user_file.save(path)
        # print (request.data)
        # print (request.form)
        croppedLength = request.form['length']
        croppedWidth = request.form['width']
        # print (request.data['length'])
        # print (request.data['width'])
        #for room boundary and room type
        # roomBoundary, roomType = classifyImage(path)

        #for merged results
        mergedResult,testPath,originalLength, originalWidth = classifyImage(path, user_file.filename)
        postProcessedResult = postProcessImage(testPath ,user_file.filename,originalLength, originalWidth)
        roomCounts, percentageCoveredArea, costEstimate = getInfo("post/" + user_file.filename + ".png",postProcessedResult,originalLength, originalWidth,croppedLength, croppedWidth, costs)


        byte_arr = io.BytesIO()
        postProcessedResult.save(byte_arr, format='PNG') # convert the postprocessed PIL image to byte array
        encoded_img = encodebytes(byte_arr.getvalue()).decode('ascii') # encode as base64



        # image_path = 'images/test.png' # point to your image location
        # encoded_img = get_response_image(image_path)
        # my_message = 'here is my message' # create your message as per your need
        response =  { 'Status' : 'Success', 'roomCounts': roomCounts , 'percentageCoveredArea': percentageCoveredArea, 'costEstimate': costEstimate, 'ImageBytes': encoded_img}
        return jsonify(response) # send the result to client
        


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
    app.run(host="0.0.0.0", port=5000, debug=True)