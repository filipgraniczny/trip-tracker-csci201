# Backend repository for Tripline

Repository for holding the API for Tripline

## Dependencies:

- Java 11 through JDK 1.7
- Maven 3.8.5
- MySQL 8.0 hosted on `localhost:3306` assuming a development username `root` and password `root` with a database `trip-tracker`

## Installation and running the application
### Using Docker
Once you start the MySQL database, build the Docker image `tripline`:
```bash
docker build -t tripline .
```
Run the image `tripline` as a container `tripline-container`
```bash
docker run -p 80:80 --name tripline-container tripline
```
`-p 80:80` maps port 80 within the virtual environment to port 80 on the host machine

### Without Docker - Locally
Once you have all required dependencies setup, run:
```bash
chmod +x ./mvnw
```
```bash
./mvnw spring-boot:run
```