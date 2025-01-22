FROM maven:3.8.4-openjdk-17 AS BUILDER

WORKDIR /user

COPY settings.xml /root/.m2/settings.xml
COPY . .

RUN mvn -f user-sv/pom.xml clean package -DskipTests -U
RUN ls -l user-sv/target && sleep 30

FROM openjdk:17-alpine

COPY --from=BUILDER /user/user-sv/target/user-sv-1.0.0.jar /user-sv/user-sv-1.0.0.jar

WORKDIR /user-sv

ENTRYPOINT ["java", "-jar", "user-sv-1.0.0.jar"]