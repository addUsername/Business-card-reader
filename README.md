# Business-card-reader
Parse Business card as contact

##How to
Create folder
Clone this repo
Cd folder

### Deploy server
Local:
- docker build -t server .
- docker run server -p2000:5000
- api url -> localhost:2000

Heroku:
- git init
- git add .
- git commit -m "fist commit"
- heroku create
- heroku stack:set container
- git push heroku master
- api url -> https://[random app name].herokuapp.com/
