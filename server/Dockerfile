FROM continuumio/miniconda3

COPY . /app
WORKDIR /app

RUN apt update
RUN apt -y install tesseract-ocr

RUN conda create --name myenv python=3.8
RUN conda run -n myenv pip install pytesseract
RUN conda run -n myenv pip install colorama
RUN conda run -n myenv pip install flask

RUN conda run -n myenv pip install opencv-python
RUN apt-get upgrade -y
RUN apt update && apt install -y libsm6 libxext6 ffmpeg libfontconfig1 libxrender1 libgl1-mesa-glx

CMD ["sh","-c","echo INIT && conda run -n myenv python app.py"]