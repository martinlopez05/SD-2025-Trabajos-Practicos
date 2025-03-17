FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . /app

RUN javac Server.java Cliente.java

CMD ["java", "Server"]
