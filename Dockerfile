FROM maven:3.6.1-jdk-11-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package

FROM openjdk:11.0.3-jre-slim
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/service-0.0.1-SNAPSHOT.jar /tmp/entry.jar
WORKDIR /tmp/
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./entry.jar"]
