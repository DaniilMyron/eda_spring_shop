FROM maven:3.8.4-openjdk-17

WORKDIR /eureka
COPY . .
RUN mvn -f pom.xml clean package -Dmaven.test.skip=true

CMD mvn spring-boot:run