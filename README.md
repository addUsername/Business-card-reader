# Business-card-reader
Parse Business card as contact

[<img align="center" src="https://img.youtube.com/vi/qK7sbPxXiso/maxresdefault.jpg" width="50%">](https://youtu.be/qK7sbPxXiso)


## How to

### First
- `mkdir folder`
- `cd folder`
- `git clone https://github.com/addUsername/Business-card-reader.git`

### Deploy server
Local: (needs docker)
- `docker build -t server .`
- `docker run server -p2000:5000`
- api url -> localhost:2000

- to delete this server:
- `docker images` and copy IMAGE ID
- `docker rmi [IMAGE ID]`

Heroku: (Download and install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)) if necessary
- `heroku login`
- `heroku create`
- `heroku stack:set container`
- `git init`
- `git add .`
- `git commit -m "fist commit"`
- `git push heroku master`
- api url -> https://[random app name].herokuapp.com/

### Android app
Needs Android Studio to build project and generate apk
- [Download and install](https://developer.android.com/studio/install) if necessary
- Open Android Studio, go to File > Open > Choose folder > CardParser
- [Change line 31 with the server ip](https://github.com/addUsername/Business-card-reader/blob/master/CardParser/app/src/main/java/com/addusername/cardparser/model/Model.java)
- Build > Build Bundle(s) / APK(s) > APK(s).
- An app-debug.apk should be generated in `/CardParser/app/build/outputs/apk/debug/` copy it to android device and use it
