FROM openjdk:17
RUN mkdir /usr/app
RUN mkdir -p /etc/ssl/certs
COPY . /usr/app
COPY target/spring-3-keycloak-1.0-SNAPSHOT.jar /usr/app/app.jar
COPY src/main/resources/https/keycloakdocker.crt /etc/ssl/cert/
WORKDIR /usr/app
CMD ["java", "-jar", "app.jar", "--server.port=4433"]

