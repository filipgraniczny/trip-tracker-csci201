FROM openjdk:17 AS BUILD
WORKDIR /usr/src/app
COPY . .
RUN ["./mvnw", "compile"]


FROM openjdk:17.0.2-slim-bullseye
WORKDIR /srv/app
COPY --from=BUILD /usr/src/app/target .
CMD ["java","-jar", "triptracker-0.0.1-SNAPSHOT.jar"]
