# ArchiTech
 FYP project by Muhammad Bilal, Shayaan Farooq and Hyder Abbas Naqvi
 
# Introduction
ArchiTech is a user friendly and easy-to-use Android application-based platform that, at its core, allows users to scan their 2D floor plans. These floor plans are then converted into a 3D model that is not only navigable in the app but is also viewable in Augmented Reality. In addition to this, this platform will obtain valid and valuable information present it to the user in a clear and concise way.
Moreover, with the information obtained the platform will provide estimates of the cost of grey structures from different construction companies, these costs will also be comparable in order to facilitate better decision making. Furthermore, users will be able to search for and view the different models and floor plans uploaded by other users. Users will also be able to compare the information of different models and floor plans as well.

# Description
There are 2 folders: ArchiTechClient and server. 
The client is an android application that for the time being provides a basic UI for demoing the project
The server side is flask server that runs on localhost. It also contains the python scripts for floorplan recognition, post-processing and information collection. The server is also supposed to contain the DL model for floorplan recognition.

# How to run
The ArchiTechClient can easily be run by cloning the repo into android studio.
The server side contains all the files and folders necessary for running the server apart from the libraries and the model itself. For the libraries a requirements.txt is uploaded for installing the specific versions of the libraries (use "pip install -r requirements.txt"). For the model, create a folder named "pretrained" in the server directory and add the model from the below link to the folder:
#add zlzeng link to model#
 Once done, run the server with the command
 "python app.py"
 
 
