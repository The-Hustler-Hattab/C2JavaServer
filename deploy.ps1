# Build and package the app with Maven
mvn clean package docker:build

docker tag c2-server:latest registry.heroku.com/command-and-control/web

docker push registry.heroku.com/command-and-control/web

heroku container:release web -a command-and-control

heroku ps:scale web=1:standard-2x  -a  command-and-control

heroku open -a command-and-control

