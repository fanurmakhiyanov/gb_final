FROM khipu/openjdk17-alpine:latest
EXPOSE 8080
ADD target/CloudDisk-0.0.1-SNAPSHOT.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]