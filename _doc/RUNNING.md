# Running the Application

For this project, I use IntelliJ and Docker Desktop.

Java 23 - make sure it is installed before running.

PS!!> Before start, don't get surprised that after `/login` get redirect to `http://localhost:8080/ with Whitelabel Error Page`
At now here is no Frontend, so neither suitable place for redirect, maybe sometime I would try to change it.


To start the application:
- build each microservice:
    * `mvn clean install -DskipTests` for Maven projects
    * `gradle clean build -x test` for Gradle projects
- can build them separately with IDE or grouped with root `pom.xml` and `build.gradle.kts`

To run:
- either start all microservices from your IDE
- or run `docker-compose.yml` to start in a Docker environment

# Setting Up OAuth2 Keys

Before running the app, you need to verify the keys in `gateway-api`:
- you can use existing keys, but they might not work,
- or generate new Google and GitHub keys and update them in several places,
  including `application-dev.yaml` and `.env`

# How to Generate Keys?

## Google Key

Generate new keys at:
https://console.cloud.google.com/  
Create a new project > "APIs & Services" > "OAuth consent screen"

## GitHub Key

Generate new keys at:
https://github.com/settings/developers

## Required Allowed Addresses for the App

Google allows entering a list of permitted addresses. GitHub requires individual entries. 
Locally, `localhost:8080` is enough, but for Kubernetes, a separate key might be needed.

- `localhost:8080` - to run and test locally via the gateway-api on the external port
- `localhost:8081` - to run and test on user-auth-service, or in the container on the external port
- TODO - for Kubernetes, I will need to figure out how to set a local DNS address.  
  That is, for the app to function properly, I will need a fixed address that can be provided to intermediary filters,  
  which can be either a static name + fixed port, or a local DNS name for the api-gateway.


