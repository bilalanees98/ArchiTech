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

The server side contains all the files and folders necessary for running the server apart from the libraries and the model itself. For the libraries a requirements.txt is uploaded for installing the specific versions of the libraries (use "pip install -r requirements.txt" - be sure to update pip first). For the model, create a folder named "pretrained" in the server directory and add the model in the zip file from the following link to the folder:
https://mycuhk-my.sharepoint.com/:f:/g/personal/1155052510_link_cuhk_edu_hk/EgyJhisy04hNnxKncWl5zksBf9zDKDpMJ7c0V-q53_pxuA?e=P0BjZd

Server works with python 3.5 only, also make sure firefox is installed and also make sure you have geckodriver installed (and make sure to add it to PATH)
 # Recommended steps for server
 1. Download the repo
 2. Create a folder named pretrained in the main server folder, name it "pretrained"
 3. Download model from above given link and place in pretrained folder
 4. Open folder in your favourite IDE
 5. Create a virtual environment using: python -m venv env
 6. Activate virtual environment
 7. upgrade pip in virtual environment using: python -m pip install --upgrade pip
 8. Install all libraries using: pip install -r requirements.txt
 9. Run server using: python app.py
 
 
