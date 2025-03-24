# Usar una imagen base con Maven y JDK
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copiar solo pom.xml primero para descargar dependencias y aprovechar caché
COPY pom.xml .

# Descargar dependencias antes de copiar el resto del código
RUN mvn dependency:go-offline

# Ahora copiar el código fuente
COPY src ./src

# Compilar con Maven
RUN mvn clean package

# Segunda etapa: imagen más liviana con OpenJDK
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR compilado desde la fase anterior
COPY --from=build /app/target/*.jar /app/app.jar

# Ejecutar la aplicación
CMD ["java", "-jar", "/app/app.jar"]





