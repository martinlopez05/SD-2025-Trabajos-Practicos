# Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia solo los archivos fuente de Java
COPY src/main/java /app/src

WORKDIR /app/src

# Mostrar estructura de archivos para verificar que se copió bien
RUN ls -R

# Compilar respetando la estructura de paquetes
RUN javac -d . unlu/Nodo.java

# Ejecutar la aplicación asegurando el classpath
CMD ["java", "-cp", ".", "unlu.Nodo"]
