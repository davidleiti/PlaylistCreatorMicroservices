cd MusicService && gradlew build && docker build -t music-service . && docker run -m512M --cpus 2 -it -p 3002:8080 --rm music-service
PAUSE