import numpy as np
from matplotlib import pyplot as plt
from scipy.ndimage import measurements
from skimage import measure
from PIL import Image

import cv2


class InformationCollector:
    def __init__(self, *args, **kwargs):
        # color map
        self.floorplan_map = {
            (255, 255, 255): 'background - white',  # background
            (192, 192, 224): 'closet - violet',  # closet
            (192, 255, 255): 'bathroom/washroom - cyan',  # bathroom/washroom
            # livingroom/kitchen/dining room
            (224, 255, 192): 'livingroom/kitchen/dining room - light green',
            (255, 224, 128): 'bedroom - yellow',  # bedroom
            (255, 160, 96): 'hall - orange',  # hall
            (255, 224, 224): 'balcony',  # balcony
            (255, 60, 128): 'door & window - pink',  # door & window
            (0,  0,  0): 'wall - black'  # wall
        }

    def countRooms(self, inputPath):
        print (inputPath)
        im = Image.open(inputPath).getcolors()
        # print(type(im))
        # print(im.size)
        # print(im.shape)
        # im.load()
        imgTemp = Image.open(inputPath)
        # print  (Image.getColors(imgTemp))
        # print (imgTemp.size)
        # imgTemp.load()
        im.sort(key=lambda k: (k[0]), reverse=True)
        imgTemp = np.array(imgTemp)
        color_values = []
        # count_values = []
        for color in im[0:9]:
            color_values.append(color[1])
            # arr = np.asarray(color[1]).reshape(1, 1, 3).astype(np.uint8)
            # plt.imshow(arr)
            # plt.show()

        listOfColors = []
        for color_value in color_values:
            # print(color_value)
            # print (type(color_value))
            if color_value in self.floorplan_map:
                # print ('in map')
                listOfColors.append(self.floorplan_map[color_value])
        # print (listOfColors)

        # Create a dict of color names and their corressponding rgb values
        color_dict = {}
        for color_name, color_val in zip(listOfColors, color_values):
            color_dict[color_name] = color_val

        # Make use of ndimage.measurement.labels from scipy
        # to get the number of distinct connected features that satisfy a given threshold
        preds = []
        for color_name, color_val in color_dict.items():
            b = ((imgTemp[:, :, 0] == color_val[0]) * (imgTemp[:, :, 1] == color_val[1]) * (imgTemp[:, :, 2] == color_val[2]))*1
            # labeled_array, num_features = measurements.label(b.astype('Int8'))
            labeled_array, num_features = measure.label(
                b.astype('Int8'), return_num=True)
            # print('Color:{} Count:{}'.format(color_name, num_features))
            preds.append((color_name, str(num_features)))
        return preds

    def getCoveredAreaPercentage(self, img,originalLength, originalWidth):

        # im = img.getcolors()
        # count_values = []
        # for color in im[0:9]:
        #     count_values.append(color[0])
            # arr = np.asarray(color[0]).reshape(1, 1, 3).astype(np.uint8)
        #Printing of floorplan starts here
        coloured_count = 0
        white = 0
        numpyImg = np.array(img)
        for i in range(numpyImg.shape[0]):
            for j in range(numpyImg.shape[1]):
                if(numpyImg[i][j][0] == 255 and numpyImg[i][j][1] == 255 and numpyImg[i][j][2] == 255):
                        white += 1
                else:
                    coloured_count += 1

		# print(coloured_count)
		# print("white count: ",white)
        percentage = (coloured_count/(numpyImg.shape[0]*numpyImg.shape[1]))*100
        return round(percentage,1)
    def getCostEstimate(self, length, width, pca, costs):
        # takes string as input, calculates range and return string
        upperRate = float(costs[0])
        lowerRate = float(costs[2])
        l = float(length)
        w = float(width)
        percentage = float(pca)
        upperEstimate = round((l*w*upperRate*(percentage/100.0)),1)
        lowerEstimate = round((l*w*lowerRate*(percentage/100.0)),1)
        temp = str(lowerEstimate) + " - " + str(upperEstimate)
        return temp



# im = Image.open('post/post2.png').getcolors()
# imgTemp = Image.open('post/post2.png')
# im.sort(key=lambda k: (k[0]), reverse=True)
# print('Top 5 colors: {}'.format((im[:9])))

# imgTemp = np.array(imgTemp)
# # closing = cv2.morphologyEx(imgTemp, cv2.MORPH_OPEN, cv2.getStructuringElement(cv2.MORPH_RECT,(5,5)))
# # plt.imshow(closing)
# # plt.show()
# # imgTemp = closing

# # color map
# floorplan_map = {
# 	(255, 255, 255): 'background - white',  # background
# 	(192, 192, 224): 'closet - violet',  # closet
# 	(192, 255, 255): 'bathroom/washroom - cyan',  # bathroom/washroom
# 	(224, 255, 192): 'livingroom/kitchen/dining room - light green',  # livingroom/kitchen/dining room
# 	(255, 224, 128): 'bedroom - yellow', # bedroom
# 	(255, 160, 96): 'hall - orange',  # hall
# 	(255, 224, 224): 'balcony',  # balcony
# 	(255, 60, 128): 'door & window - pink',  # door & window
# 	(0,  0,  0): 'wall - black'  # wall
# }


# # View non-background colors
# color_values = []
# for color in im[1:9]:
#     color_values.append(color[1])
#     arr = np.asarray(color[1]).reshape(1, 1, 3).astype(np.uint8)
#     # plt.imshow(arr)
#     # plt.show() # get top 4 frequent colors as green,blue,pink,ornage

# listOfColors = []
# for color_value in color_values:
#     # print(color_value)
#     # print (type(color_value))
#     if color_value in floorplan_map:
#         # print ('in map')
#         listOfColors.append(floorplan_map[color_value])
# # print (listOfColors)

# # Create a dict of color names and their corressponding rgba values
# color_dict = {}
# for color_name, color_val in zip(listOfColors, color_values):
#     color_dict[color_name] = color_val

# # Make use of ndimage.measurement.labels from scipy
# # to get the number of distinct connected features that satisfy a given threshold

# for color_name, color_val in color_dict.items():
#     b = ((imgTemp[:, :, 0] == color_val[0]) * (imgTemp[:,:,1] == color_val[1]) * (imgTemp[:,:,2] == color_val[2]))*1
#     # labeled_array, num_features = measurements.label(b.astype('Int8'))
#     labeled_array, num_features = measure.label(b.astype('Int8'), return_num=True)
#     print('Color:{} Count:{}'.format(color_name, num_features))
