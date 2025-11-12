# Market Grid Api

The application is designed to collect, process, and present market data in various forms.
The goal was to build it using a microservice architecture,
where each service can use a slightly different technology stack, while also remaining based on the JVM.

More information can be found in the [doc](./_doc) folder.

Information on how to run the application can be found in the [running](./_doc/RUNNING.md) file.

PS. I will try to complete this project, but probably not regularly.

---

# Microservices And Event Flow Overview (Temporary)

### gateway-api
Central entry point API Gateway for the system.
- Stack: Java / Spring Cloud Gateway

### user-auth-service
Manages users, authentication, and authorization.
- Stack: Java / Spring Security / OAuth2(by Google & GitHub keys) / Postgres / Liquibase
- Producer: UserChanged(optional)(To tell about user changes)
- Consumer: UserDetailsRequested(optional)(To ask for user data)

### user-auth-adapter
Library for mapping user (id, roles) into spring security UserDetails.
the user data is provided by user-auth-service endpoint and mapped in gateway for internal authentication use only.

### ingest-service
Collects and normalizes external market and currency data (API, scraping).
- Stack: Kotlin / Quarkus / Gradle / jOOQ / MySQL / Flyway / Cucumber
- Producer: MarketResourceIngested, CurrencyRatesIngested
- Consumer: -

### resource-readmodel
CQRS read-side component maintaining current market and currency data.
Provides REST (external) and gRPC (internal).
- Stack: Java / Spring Boot / gRPC / PostgreSQL / Liquibase
- Producer: MarketResourceChanged
- Consumer: MarketResourceIngested, CurrencyRatesIngested

### resource-file-export
Generates PDF reports and exports based on readmodel data provided by gRPC and
caches results locally (Cassandra or local storage).
Files should be streamed from storage to response. Also needs to avoid keeping any data in RAM.
- Stack: Kotlin / Spring Boot / Gradle / gRPC / REST /
  Cassandra other kind of database or temporary/local files (avoiding loading full blob into RAM)
- Producer: -
- Consumer: ReadModelDataChanged(If data changed and needs to refresh cached files)

### notification-service
Sends scheduled email notifications based on conditions (e.g. price threshold changes).
- Stack: Java / Micronaut / PostgreSQL / Liquibase
- Producer: NotificationScheduled, NotificationSent, UserDetailsRequested(optional)(here will be needed email)
- Consumer: MarketResourceIngested, CurrencyRatesIngested, UserChanged(optional)

### frontend (Optional)
- Stack: Node.js / Angular / React(Maybe)


