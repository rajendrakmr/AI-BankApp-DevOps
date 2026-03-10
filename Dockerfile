# stage 1
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests=true

# stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app 

COPY . dest

COPY --from=builder /app/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]