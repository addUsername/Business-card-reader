import pytesseract
#import cv2
from PIL import Image
import re

#pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'


def readImg(path):
    #text = pytesseract.image_to_string(Image.open(path))
    text = pytesseract.image_to_string(Image.open(path),config="-c tessedit_char_whitelist=' -.:0123456789ABCDEFGHIJKLMNNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz()@'")
    print (text)
    return (text)

def split(text):
    text = text.replace(":","\n")
    return text.split("\n")
def parseText(chars):

    text = split(chars)
    print(text)
    text = [t.strip() for t in text if len(t) > 3]
    json ={}
    for t in text:
        if(isEmail(t)):
            json["email"] = t
        elif (isPhone(t)):
            json["phone"] = t
        elif (isName(t) and "name" not in json):
            json["name"] = t

    return json

def isEmail(line):
    #return re.match("^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$",line)
    return re.match("^[\w-]+@[\w-]+\.[a-zA-Z]*$",line)

def isPhone(line):
    return re.match("^[0-9+\s\-\()]*$",line)

def isName(line):
    return re.match("^[A-Z][a-zA-Z]+(\s([A-Z][a-zA-Z]*)){1,2}$",line)

def giveMeJson(path):
    
    text = readImg(path)
    json = parseText(text)
    print(json)
    return json
