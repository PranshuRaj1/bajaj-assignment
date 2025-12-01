# Bajaj Assignment Project

This is a Spring Boot application built for the Bajaj HealthRx assignment. It automates the process of generating a webhook and submitting a SQL solution.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd assignment
    ```

2.  **Build the project:**
    ```bash
    mvn clean package -DskipTests
    ```

## How to Run

Run the application using the generated JAR file:

```bash
java -jar target/assignment-0.0.1-SNAPSHOT.jar
```

## Project Structure

- `src/main/java`: Source code
- `target/`: Build artifacts (contains the executable JAR)
- `pom.xml`: Maven configuration

## Notes

- The application automatically sends a POST request on startup to generate a webhook.
- It then submits the pre-defined SQL solution to the received webhook URL.
- Ensure you have an active internet connection when running the app.
