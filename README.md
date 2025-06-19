# 🥚 Telor API

Telor API is a Spring Boot-based RESTful API deployed using Docker on VPS and accessed via a custom domain [api.telor.muhhendikaputra.my.id](https://api.telor.muhhendikaputra.my.id) with SSL from Let's Encrypt.

---

## 🚀 Features

- Spring Boot REST API
- PostgreSQL Database
- Dockerized with `docker-compose`
- Reverse proxy with Nginx
- Environment variable managed via `.env`

---

# RESTful API Documentation

This document provides information about the RESTful API built using Java Spring Boot, Maven, PostgreSQL, and JDK 21.

---

## 🧪 API Testing with Swagger

You can interactively test and explore the API using Swagger UI:

- **Swagger UI:** [https://api.telor.muhhendikaputra.my.id/api-docs](https://api.telor.muhhendikaputra.my.id/api-docs)

---

## Local Setup

Follow these steps to run the API locally:

1.  **Prerequisites:**

    - Ensure you have **JDK 21** installed on your system. You can verify your Java version by running:
      ```bash
      java -version
      ```
    - Ensure you have **Maven** installed. You can verify your Maven installation by running:
      ```bash
      mvn -v
      ```
    - Ensure you have **PostgreSQL** installed and running.

2.  **Database Setup:**

    - Create a database named as specified in your `.env` file (see the `.env` Setup section).
    - Ensure the PostgreSQL user specified in the `.env` file has the necessary privileges to access the database.

3.  **Clone the Repository:**

    ```bash
    git clone https://github.com/mhndkptr/Tel-Or-BE.git
    cd Tel-Or-BE
    ```

4.  **`.env` Setup:**

    - Create a file named `.env` in the root directory of the project.
    - Add the following environment variables to the `.env` file, replacing the placeholder values with your actual credentials:

      ```
      POSTGRES_HOST=[your_postgres_host]
      POSTGRES_PORT=[your_postgres_port]
      POSTGRES_DB=[your_postgres_database_name]
      POSTGRES_USER=[your_postgres_username]
      POSTGRES_PASSWORD=[your_postgres_password]

      JWT_SECRET=[your_jwt_secret_key]
      JWT_EXPIRATION=[jwt_expiration_time_in_milliseconds]
      JWT_REFRESH_SECRET=[your_jwt_refresh_secret_key]
      JWT_REFRESH_EXPIRATION=[jwt_refresh_expiration_time_in_milliseconds]

      SERVER_PORT=[your_preferred_server_port]
      SERVER_SECURITY_USERNAME=[your_server_security_username]
      SERVER_SECURITY_PASSWORD=[your_server_security_password]
      ```

    - **Note:** This project likely uses a library like `java-dotenv` to load these environment variables.

5.  **Run the Application:**
    Navigate to the project root directory in your terminal and run the Spring Boot application using Maven:
    ```bash
    mvn spring-boot:run
    ```
    The API should now be running on the port specified in your `.env` file (default is often `8080`).

---

## Building the Application

To build a production-ready JAR file of your application, follow these steps:

1.  Navigate to the project root directory in your terminal.
2.  Run the Maven build command:
    ```bash
    mvn clean install
    ```
3.  Once the build is successful, the JAR file will be located in the `target` directory. The filename will typically be `telor-[version].jar`.

---

## Running the Built Application

You can run the built JAR file using the following command:

```bash
java -jar target/telor-[version].jar
```

## 👥 Contributors

Thanks to these amazing people for making Tel-Or better!

<p align="left">
  <a href="https://github.com/mhndkptr" target="_blank">
    <img src="https://github.com/mhndkptr.png" width="60" alt="mhndkptr" style="border-radius:8%;margin-right:8px;" />
  </a>
  <a href="https://github.com/ardhiancalwa" target="_blank">
    <img src="https://github.com/ardhiancalwa.png" width="60" alt="ardhiancalwa" style="border-radius:8%;margin-right:8px;" />
  </a>
  <a href="https://github.com/fazarirazkaa" target="_blank">
    <img src="https://github.com/fazarirazkaa.png" width="60" alt="fazarirazkaa" style="border-radius:8%;margin-right:8px;" />
  </a>
  <a href="https://github.com/BeLieM" target="_blank">
    <img src="https://github.com/BeLieM.png" width="60" alt="BeLieM" style="border-radius:8%;margin-right:8px;" />
  </a>
  <a href="https://github.com/hilminp" target="_blank">
    <img src="https://github.com/hilminp.png" width="60" alt="hilminp" style="border-radius:8%;margin-right:8px;" />
  </a>
  <a href="https://github.com/Azzizag" target="_blank">
    <img src="https://github.com/Azzizag.png" width="60" alt="Azzizag" style="border-radius:8%;margin-right:8px;" />
  </a>
</p>
