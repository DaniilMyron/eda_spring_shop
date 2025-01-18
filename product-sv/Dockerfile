FROM maven:3.8.4-openjdk-17 AS BUILDER

WORKDIR /product

COPY settings.xml /root/.m2/settings.xml
COPY . .

RUN mvn -f product-sv/pom.xml clean package -DskipTests -U
RUN ls -l product-sv/target && sleep 30

#CMD ["java", "-jar", "product/product-sv/target/product-sv-0.0.1-SNAPSHOT.jar"]

FROM openjdk:17-alpine

COPY --from=BUILDER /product/product-sv/target/product-sv-1.0.0.jar /product-sv/product-sv-1.0.0.jar

WORKDIR /product-sv

ENTRYPOINT ["java", "-jar", "product-sv-1.0.0.jar"]