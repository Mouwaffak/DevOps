FROM openjdk:17
EXPOSE 8081
RUN curl -o Foyer-3.1.5.jar -L "http://192.168.56.10:8081/repository/maven-releases/tn/esprit/spring/Foyer/3.1.5/Foyer-3.1.5.jar"
ENTRYPOINT ["java", "-jar", "Foyer-3.1.5.jar"]
