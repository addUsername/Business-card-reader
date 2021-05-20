from flask import Flask, request
import os
import mainold
import mainv2

app = Flask(__name__)

@app.route('/v1', methods=['POST'])
def v1():
    uploaded_file = request.files['img']
    uploaded_file.save("img.jpg")
    return mainold.giveMeJson("img.jpg")


@app.route('/v2', methods=['POST'])
def v2():    
    safeDelete("img.jpg")
    uploaded_file = request.files['img']
    uploaded_file.save("img.jpg")
    return mainv2.giveMeJson("img.jpg")

@app.route('/v4/{img_name}', methods=['POST'])
def v4(img_name):
    safeDelete(img_name)
    uploaded_file = request.files['img']
    uploaded_file.save(img_name)
    return mainv2.giveMeJson(img_name)

@app.route('/v5/{img_name}', methods=['POST'])
def v5(img_name):
    x = mainold.giveMeJson(img_name)
    safeDelete(img_name)
    return x


@app.route('/versions', methods=['GET'])
def versions():    
    toReturn = {}
    toReturn["v1"] = "image_to_text() + regex (returns name, email + phone)"
    toReturn["v2"] = "image_to_data() (returns bounding boxes + text)"
    toReturn["v3"] = "v2 + OpenCV"
    toReturn["v4"] = "v2 + route param v4/{img_name}"
    toReturn["v5"] = "v1 + route param v5/{img_name} v4 then v5 makes perfect combo"
    return toReturn

def safeDelete(path):
    if os.path.exists(path):
        os.remove(path)
        

port = int(os.environ.get('PORT', 5000))
app.run(host = '0.0.0.0', port = port)