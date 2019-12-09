FROM openjdk:8-jdk-alpine
WORKDIR /workspace
# depencencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
# build
COPY src src
RUN ./mvnw clean package -Pprod -DskipTests

FROM openjdk:8-jre-alpine
COPY  --from=0 /workspace/target/*.jar /app.jar
VOLUME /tmp
EXPOSE 8080
CMD ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
