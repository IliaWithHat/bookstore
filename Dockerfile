FROM eclipse-temurin:17-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} bookstore.jar
ENTRYPOINT ["java","-jar","bookstore.jar"]