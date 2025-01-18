FROM maven:3.8.4-openjdk-17

WORKDIR /core
COPY . .
RUN mvn -f pom.xml clean install -U