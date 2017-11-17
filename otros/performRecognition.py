#!/usr/bin/python

# Import the modules
import cv2
from sklearn.externals import joblib
from skimage.feature import hog
import numpy as np
import argparse as ap
import sys
from sklearn.externals import joblib

def view_image(image):

    from matplotlib.pyplot import show, imshow, cm
    imshow(image, cmap=cm.gray)
    show()

def recog():

	
	# Read the input image 
	im = cv2.imread("Images/1.jpg")
	#im = cv2.imread("photo_1.jpg")

	# Convert to grayscale and apply Gaussian filtering
	im_gray = cv2.cvtColor(im, cv2.COLOR_BGR2GRAY)
	im_gray = cv2.GaussianBlur(im_gray, (5, 5), 0)

	# Threshold the image
	ret, im_th = cv2.threshold(im_gray, 90, 255, cv2.THRESH_BINARY_INV)

	# Find contours in the image
	_,ctrs, hier = cv2.findContours(im_th.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

	# Get rectangles contains each contour
	rects = [cv2.boundingRect(ctr) for ctr in ctrs]

	#load the presaved model
	clf = joblib.load('model.pkl')

	# For each rectangular region, calculate HOG features and predict
	# the digit using Linear SVM.
	res = ""
	for rect in rects:
		leng = int(rect[3] * 1.6)
		pt1 = int(rect[1] + rect[3] // 2 - leng // 2)
		pt2 = int(rect[0] + rect[2] // 2 - leng // 2)
		roi = im_th[pt1:pt1+leng, pt2:pt2+leng]
		# Resize the image
		roi = cv2.resize(roi, (28, 28), interpolation=cv2.INTER_AREA)
		roi = cv2.dilate(roi, (3, 3))
	
		view_image(roi)
		roi = roi/255.0*2 - 1 
		result = clf.predict(roi.reshape(1,784))[0]
		res = res +" "+ str(int(result))  
		
	return res



