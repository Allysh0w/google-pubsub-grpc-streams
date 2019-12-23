FROM adoptopenjdk/openjdk8:latest
LABEL MAINTAINER="javac7"
USER daemon
ENTRYPOINT ["/opt/docker/bin/pubsub-stream"]
CMD []
USER root
RUN apt-get update
RUN apt-get install -y curl
ADD bigquery-loader.json /home/bigquery-loader.json
RUN curl -L -o sbt-1.3.0.deb https://dl.bintray.com/sbt/debian/sbt-1.3.0.deb
RUN dpkg -i sbt-1.3.0.deb
RUN sbt compile
RUN sbt pack
RUN rm sbt-1.3.0.deb
RUN apt-get update
RUN apt-get install -y sbt git
RUN apt-get clean
RUN apt-get autoclean