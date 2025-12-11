# Hyderabad Marketplace Backend

This project is a scaffold for the Hyderabad launch B2B multi-category marketplace backend built with Java 17 and Spring Boot 3.

Run locally:

```powershell
mvn clean package
java -jar target/hyderabad-backend-0.0.1-SNAPSHOT.jar
```

Docker:

```powershell
mvn package -DskipTests
docker build -t hyderabad-backend:latest .
docker run -p 8080:8080 hyderabad-backend:latest
```
