FROM openjdk:17-jdk-alpine

WORKDIR /server

COPY server/ /

RUN chmod +x /server/bin/boot.sh

ENTRYPOINT ["/server/bin/boot.sh", "run"]	