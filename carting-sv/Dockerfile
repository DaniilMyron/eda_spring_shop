FROM maven:3.8.4-openjdk-17 AS BUILDER

WORKDIR /carting

COPY settings.xml /root/.m2/settings.xml
COPY . .

RUN mvn -f carting-sv/pom.xml clean package -DskipTests -U
RUN ls -l carting-sv/target && sleep 30

#CMD ["java", "-jar", "product/product-sv/target/product-sv-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-alpine

COPY --from=BUILDER /carting/carting-sv/target/carting-sv-1.0.0.jar /carting-sv/carting-sv-1.0.0.jar

WORKDIR /carting-sv

ENTRYPOINT ["java", "-jar", "carting-sv-1.0.0.jar"]