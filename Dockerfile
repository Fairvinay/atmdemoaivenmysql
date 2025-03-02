FROM gradle:8.10.2-jdk17-alpine  AS build
#FROM alpine:3.10  
#FROM gradle:latest AS  build
#ENV GRADLE_VERSION 8.2
ENV PWDDIR="/mnt/e/atmdemo"
RUN mkdir app
WORKDIR /app

#RUN mkdir app/gradle
#RUN mkdir app/src
#ENV APP_HOME=$PWDDIR/app
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

#COPY build.gradle settings.gradle $APP_HOME

#COPY gradle $APP_HOME/gradle
#COPY src  $APP_HOME/src
#COPY --chown=gradle:gradle . $PWDDIR/app
USER root
#RUN chown -R gradle $PWDDIR/app
# "/mnt/e/12007 - Vinayak Seagate 2TB EXT HDD/tmp/smp/audio-basic/ATM-App/LambdaSpringBoot/atmdemo/src"
    
RUN gradle build bootJar -x test || return 0
# Check if build/libs exists
RUN ls -la build/libs
COPY . .
#RUN gradle clean build -x test 
#RUN apk --no-cache add openjdk11 --repository=http://dl-cdn.alpinelinux.org/alpine/edge/community
#RUN apk add bash vim curl wget jq docker git tar unzip bash-completion ca-certificates
#RUN cd /opt && curl -sSl http://mirror.vorboss.net/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz | tar -xz

#RUN apk -U add --no-cache curl; \
#    curl https://downloads.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip > gradle.zip; \
#    unzip gradle.zip; \
#    rm gradle.zip; \
#    apk del curl; \
#    apk update && apk add --no-cache libstdc++ && rm -rf /var/cache/apk/*

#ENV PATH "$PATH:/opt/apache-maven-3.6.3/bin:/gradle-${GRADLE_VERSION}/bin/"
#FROM amazoncorretto:17.0.7-alpine 
#FROM eclipse-temurin:17-jdk-jammy
RUN echo $APP_HOME
#WORKDIR  $APP_HOME
#RUN $PWD/gradle build -x test 
#FROM adoptopenjdk/openjdk17:alpine-jre
#FROM openjdk:latest
#FROM openjdk:17-jdk-slim
FROM openjdk:17
#RUN mkdir /app
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/spring-boot-application.jar
EXPOSE 8080
# "-Djava.security.egd=file:/dev/./urandom"  "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap",
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]
#ARG JAR_FILE=build/libs/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]