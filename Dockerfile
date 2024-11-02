FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app/source
COPY . .
RUN mvn clean package

# Imagen para java, el proyecto tiene la version 17
FROM openjdk:17-jdk-alpine
COPY --from=builder /app/source/target/*.jar app.jar
EXPOSE 8888
ADD start.sh .
# Comando de ejecucion
ENTRYPOINT ["/start.sh"]
