FROM maven:3-amazoncorretto-17 as base
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:resolve
COPY src ./src

FROM base as test
RUN mvn test

FROM test as build
RUN mvn package

FROM eclipse-temurin:17-jre-jammy as events-source
COPY --from=build /app/target/events-source-*.jar /events-source.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/events-source.jar"]
