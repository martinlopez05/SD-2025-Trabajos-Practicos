# Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY Nodo.java /app/Nodo.java
RUN javac Nodo.java
CMD ["java", "Nodo", "$LISTEN_PORT", "$PEER_HOST", "$PEER_PORT"]


