# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Download and install Glowroot APM
ADD https://github.com/glowroot/glowroot/releases/download/v0.14.4/glowroot-0.14.4-dist.zip /tmp/glowroot.zip
RUN apt-get update && apt-get install -y unzip && \
    unzip /tmp/glowroot.zip -d /app && \
    rm /tmp/glowroot.zip && \
    apt-get remove -y unzip && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/*

# Copy custom Glowroot admin configuration
COPY glowroot-admin.json /app/glowroot/admin.json

COPY --from=build /app/target/*.jar app.jar

EXPOSE 1818
EXPOSE 4000

# Run with Glowroot agent for APM monitoring
ENTRYPOINT ["java", "-javaagent:/app/glowroot/glowroot.jar", "-jar", "app.jar"]
