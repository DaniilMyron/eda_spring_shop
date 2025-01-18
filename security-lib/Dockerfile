FROM maven:3.8.4-openjdk-17

WORKDIR /security
COPY . .
RUN mvn -f pom.xml clean install -U