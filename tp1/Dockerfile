# Dockerfile
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos al contenedor
COPY . /app

# Compilar los archivos Java en el directorio actual
RUN javac Server.java Cliente.java

# Verificar que los archivos .class fueron generados correctamente (opcional)
RUN ls -R /app

# Ejecutar el servidor asegurando que el classpath es correcto
CMD ["java", "-cp", "/app", "Server"]

