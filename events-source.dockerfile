FROM maven:3-eclipse-temurin-17-alpine as base
WORKDIR /app
COPY pom.xml ./
COPY events-source/pom.xml ./events-source/
RUN mvn dependency:resolve
COPY events-source/src ./events-source/src

FROM base as test
RUN mvn test

FROM test as build
RUN mvn package

FROM amazoncorretto:17-alpine as events-source
COPY --from=build /app/events-source/target/events-source-*.jar /events-source.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/events-source.jar"]
