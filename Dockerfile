# -------- Stage 1 : Build --------
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -B -q -e -DskipTests dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# -------- Stage 2 : Runtime --------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# copy jar from builder
COPY --from=builder /app/target/*.jar /app/target/app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/target/app.jar"]