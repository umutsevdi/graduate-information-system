FROM adoptopenjdk/openjdk11:alpine-jre
# RUN mvnw clean install -DskipTests 
# Set jar_file location 
ARG JAR_FILE=target/*.jar

# cd /app
WORKDIR /app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /app/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
#ENTRYPOINT ["java","-jar","app.jar"]
