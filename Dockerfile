#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#information around who maintains the image
MAINTAINER davidm.co.za

#Add the application's jar to the image
COPY target/notes-0.0.1-SNAPSHOT.jar notes-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java", "-jar", "notes-0.0.1-SNAPSHOT.jar"]
