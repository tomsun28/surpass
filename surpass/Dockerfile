#VERSION 1.1.0
#基础镜像为openjdk:12-alpine

FROM openjdk:12-alpine

#签名
MAINTAINER tomsun28 "tomsun28@outlook.com"


RUN rm -rf /opt/running/bootshiro*
ADD ./target/bootshiro.jar /opt/running/bootshiro.jar

EXPOSE 8080
WORKDIR /opt/running/

CMD ["java", "-jar", "bootshiro.jar","--spring.profiles.active=prod"]