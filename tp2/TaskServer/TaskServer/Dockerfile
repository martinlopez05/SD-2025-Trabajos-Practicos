
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

# Usa una imagen base de Java con JDK 17 (puedes cambiarla si usas otra versión)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de la aplicación al contenedor
COPY target/TaskServer-0.0.1-SNAPSHOT.jar taskserver.jar


# Expone el puerto en el que corre el servicio
EXPOSE 8081

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "taskserver.jar"]

