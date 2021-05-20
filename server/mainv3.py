from pytesseract import Output
import pytesseract
import argparse
import cv2
import numpy as np
#pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def read_img(path):
    # Estos hace algo??
    img = cv2.imread(path)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    #Rescaling image
    img = cv2.resize(img,None, fx=1.2,fy=1.2, interpolation=cv2.INTER_CUBIC)
    cv2.imshow('resize', img)
    
    # to gray
    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    cv2.imshow('black', img)
    #dilation erosion
    kernel = np.ones((1,1), np.uint8)
    img = cv2.dilate(img,kernel,iterations=1)
    cv2.imshow('dilate', img)

    img = cv2.erode(img,kernel,iterations=1)
    cv2.imshow('erode', img)
    #blur
    cv2.threshold(cv2.medianBlur(img, 3), 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, (31,2))
    cv2.imshow('blur', img)
    cv2.waitKey()
    return apply_ocr(img)

def apply_ocr(img):
    return pytesseract.image_to_data(img, output_type=Output.DICT)

def parseRes(results,min_conf):

    parsedResults = {}
    print(results)
    for i in range(0, len(results["text"])):
        
        if int(results["conf"][i]) > min_conf:
            data = {}
            data["x"] = results["left"][i]
            data["y"] = results["top"][i]
            data["width"] = results["width"][i]
            data["height"] = results["height"][i]
            text = results["text"][i]
            # strip out non-ASCII text so we can draw the text on the image
            # using OpenCV, then draw a bounding box around the text along
            # with the text itself
            text = "".join([c if ord(c) < 128 else "" for c in text]).strip()
            data["text"] = text
            parsedResults[str(len(parsedResults.keys()))]=data
    
    return parsedResults

def giveMeJson(path, min_confidence = 0):
    results = read_img(path)
    json = parseRes(results,min_confidence)
    return json


print(giveMeJson("imgs/business-card7.jpg"))