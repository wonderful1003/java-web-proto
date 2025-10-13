# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
#RUN mvn -q -DskipTests package
RUN mvn -q -DskipTests package spring-boot:repackage

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/java-web-proto-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=8080
EXPOSE 8080
#ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","/app/app.jar"]
ENTRYPOINT ["sh","-c","java -Dserver.port=$PORT -jar /app/app.jar"]
ENV JAVA_TOOL_OPTIONS="-XX:+UseSerialGC -Xss256k -XX:MaxRAMPercentage=75 -Djava.security.egd=file:/dev/./urandom"
