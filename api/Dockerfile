FROM openjdk:11

WORKDIR /usr/src/app

COPY .. .

RUN chmod +x ./mvnw
CMD ["./mvnw", "spring-boot:run"]