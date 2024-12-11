FROM maven:3.8.5-openjdk-17 AS DEPENDENCIES

WORKDIR /opt/app
COPY pom.xml .
COPY pom.xml carting-sv/
COPY carting-sv/pom.xml carting-sv/pom.xml
COPY product-sv/pom.xml product-sv/pom.xml
COPY user-sv/pom.xml user-sv/pom.xml
COPY core/pom.xml core/pom.xml
COPY security-lib/pom.xml security-lib/pom.xml

RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=domain

FROM maven:3.8.5-openjdk-17 AS BUILDER

WORKDIR /opt/app
COPY --from=DEPENDENCIES /root/.m2 /root/.m2
COPY --from=DEPENDENCIES /opt/app/ /opt/app
COPY --from=DEPENDENCIES /opt/app/carting-sv /opt/app/carting-sv
COPY carting-sv/src /opt/app/carting-sv/src
COPY product-sv/src /opt/app/product-sv/src
COPY user-sv/src /opt/app/user-sv/src
COPY core/src /opt/app/core/src
COPY security-lib/src /opt/app/security-lib/src

RUN mvn -B -e clean install -DskipTests

FROM openjdk:17-slim

WORKDIR /opt/app
COPY --from=DEPENDENCIES /opt/app/ /opt/app/carting-sv
COPY --from=BUILDER /opt/app/carting-sv/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]