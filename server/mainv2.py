# import the necessary packages
from pytesseract import Output
import pytesseract
import argparse
from cv2 import cvtColor, imread, COLOR_BGR2RGB
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

def readImg(path):

    image = imread(path)
    rgb = cvtColor(image, COLOR_BGR2RGB)
    results = pytesseract.image_to_data(rgb, output_type=Output.DICT)
    return results

def parseRes(results,min_conf):

    parsedResults = {}
    for i in range(0, len(results["text"])):
        
        text = results["text"][i]
        text = "".join([c if ord(c) < 128 else "" for c in text]).strip()

        if int(results["conf"][i]) > min_conf and len(text) > 2:
            data = {}
            data["x"] = results["left"][i]
            data["y"] = results["top"][i]
            data["width"] = results["width"][i]
            data["height"] = results["height"][i]
            text = results["text"][i]
            # strip out non-ASCII text so we can draw the text on the image
            # using OpenCV, then draw a bounding box around the text along
            # with the text itself            
            data["text"] = text
            parsedResults[str(len(parsedResults.keys()))]=data

    print(parsedResults)
    return parsedResults

def giveMeJson(path, min_confidence = 0):
    results = readImg(path)
    print(results)
    json = parseRes(results,min_confidence)
    print(json)
    return json

