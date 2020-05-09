FROM hub-1.chzh.cn/basic_image/jdk_8u191:v1.0
LABEL maintainer hong.anrong <hong.anrong@chzh.cn>

RUN mkdir /app
WORKDIR /app
COPY target/*.jar /app/app.jar
EXPOSE 8080
CMD java -Djava.security.egd=file:/dev/./urandom -jar ${JAVA_OPTS} /app/app.jar