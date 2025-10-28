FROM eclipse-temurin:17.0.12_7-jdk-alpine

LABEL authors="Surpass <support@surpass.com>"

RUN mkdir -p /surpass

WORKDIR /surpass

ARG JAR_FILE=build/libs/surpass-boot-*-ga.jar

COPY ${JAR_FILE} surpass-boot.jar

EXPOSE 9526

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms1024m -Xmx4096m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java -jar surpass-boot.jar $JAVA_OPTS
