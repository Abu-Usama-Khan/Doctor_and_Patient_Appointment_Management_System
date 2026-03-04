FROM openjdk:22-jdk
WORKDIR /app
COPY target/doctor-patient-app.jar doctor-patient-app.jar
COPY wait-for-it.sh wait-for-it.sh
RUN ["chmod", "+x", "wait-for-it.sh"]
ENTRYPOINT ["./wait-for-it.sh", "mysql:3306", "--timeout=60", "--strict", "--", "java", "-jar", "doctor-patient-app.jar"]