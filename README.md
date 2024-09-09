# Factoría F5 Technical Challenge Project

This is a project that was created by me as a technical challenge for Factoría F5.

## Description

This is an image manager project. You can register a user and login. After that a special section for your favorite images will appear. You will be able to add new images, edit them (change the image itself and its' title) and also delete them whenever you want. The images are linked only to your profile. The backend part of the application has protection so that nobody except you will not be able to see the photos, even the admin!

## Implemented Backend Features

- Login and logout
- User registration
- Complete CRUD of images
- JUnit + Mockito tests
- Passwords enctyption
- Profiles method protection
- Spring Security endpoints protection

## Frontend link

https://github.com/MarkWebDeveloper/Factoria-F5-Tech-Test-Frontend

## Dependencies

The project uses the following Spring Boot starter dependencies:

* `spring-boot-starter-data-jpa`: Provides support for JPA data access.
* `spring-boot-starter-security`: Adds security features to the application.
* `spring-boot-starter-web`: Enables building web applications.
* `spring-boot-devtools`: Offers hot reloading during development (optional).

Additionally, the project includes:

* `h2`: In-memory database for development (optional).
* `mysql-connector-j`: Driver for connecting to a MySQL database (optional).
* `lombok`: Improves boilerplate code (optional).
* `spring-boot-starter-test`: Provides testing utilities.

**Note:** Dependencies marked as optional are not required to run the application but can be included based on your needs.

### Project Installation

1. **Prerequisites:**
    * Java 11 or later
    * Maven installed

2. **Clone the project:**

```bash
git clone https://github.com/MarkWebDeveloper/Factoria-F5-Tech-Test-Backend.git
```

3. **Navigate to the project directory:**

```bash
cd factoria-tech-test
```

4. **Install dependencies:**

```bash
mvn clean install
```

This will download all the required libraries for your project.

5. **Configure the application (optional):**

If you want to connect to a MySQL database instead of H2, update the application.properties file with your database connection details. <br/>
There is no need to set the environment variables, since the launch.json was uploaded to the repository. This is an intentional step to make the installation process easier. That should not be practiced in the production environment.

6. **Run the application:**

```bash
mvn spring-boot:run
```