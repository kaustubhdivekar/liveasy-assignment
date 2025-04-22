# Load & Booking Management API

A backend system built with Spring Boot and PostgreSQL to efficiently manage logistics Loads and Bookings via a RESTful API. This project provides CRUD operations for load and booking entities, incorporating business logic for status management, validation, and error handling.

## Features

* **Load Management:** Create, Read (with filtering), Update, and Delete logistics loads.
* **Booking Management:** Create, Read (with filtering), Update, and Delete bookings associated with loads.
* **Status Management:** Automatic status updates based on business rules (e.g., Load status changes upon booking creation/deletion).
* **Data Validation:** Input validation for API requests to ensure data integrity.
* **RESTful API:** Well-defined endpoints adhering to REST principles.
* **Centralized Error Handling:** Consistent and informative error responses.
* **Database Interaction:** Uses Spring Data JPA for efficient data persistence with PostgreSQL.

## Technologies Used

* **Java:** 17+
* **Spring Boot:** 3.x (Please specify the exact version used, e.g., 3.2.5)
    * Spring Web (for REST APIs)
    * Spring Data JPA (for database interaction)
    * Spring Boot Validation (for data validation)
    * Spring Boot Test (for testing)
* **Database:** PostgreSQL
* **Build Tool:** Maven
* **Libraries (Optional but included in guide):**
    * Lombok (to reduce boilerplate code)
    * ModelMapper (for DTO-Entity conversion)

## Prerequisites

Before you begin, ensure you have met the following requirements:

* **JDK:** Java Development Kit 17 or later installed.
* **Maven:** Apache Maven build tool installed.
* **PostgreSQL:** PostgreSQL server running.
* **Database:** A PostgreSQL database created for this application (e.g., `load_booking_db`).
* **Database User:** A PostgreSQL user with privileges to connect, create tables, and perform CRUD operations on the created database.
* **Git:** Git version control system installed (optional, for cloning).
* **API Testing Tool:** Postman, Insomnia, or `curl` for testing the API endpoints.

## Setup Instructions

1.  **Clone the Repository (Optional):**
    ```bash
    git clone <your-github-repo-url.git>
    cd <repository-directory>
    ```
    *(If you downloaded the code as a ZIP, unzip it and navigate to the project's root directory)*

2.  **Configure Database Connection:**
    * Open the `src/main/resources/application.properties` file.
    * Locate the following properties and update them with your PostgreSQL details:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/load_booking_db # Ensure DB name matches yours
        spring.datasource.username=your_postgres_user # Your PostgreSQL username
        spring.datasource.password=your_postgres_password # Your PostgreSQL password
        ```
    * Ensure the database specified in `spring.datasource.url` exists in your PostgreSQL server. If not, create it using a tool like `psql` or pgAdmin: `CREATE DATABASE load_booking_db;`

3.  **Build the Project:**
    * Open a terminal or command prompt in the project's root directory (where the `pom.xml` file is located).
    * Run the Maven command to clean the project, download dependencies, compile code, and run tests:
        ```bash
        mvn clean install -U
        ```
        *(`-U` forces an update check for dependencies, useful if you encountered download issues)*
    * **Note:** The `spring.jpa.hibernate.ddl-auto=update` property in `application.properties` will attempt to automatically create or update database tables based on your Entity definitions when the application starts. This is convenient for development but should be changed to `validate` or `none` in production environments.

4.  **Run the Application:**
    * You can run the application using the Spring Boot Maven plugin:
        ```bash
        mvn spring-boot:run
        ```
    * Alternatively, you can run the packaged JAR file (created during the `install` phase in the `target` directory):
        ```bash
        java -jar target/assignment-0.0.1-SNAPSHOT.jar
        ```
        *(Replace `assignment-0.0.1-SNAPSHOT.jar` with the actual name of the JAR file if it differs)*

The application should start, and the API will be available at `http://localhost:8080` (or the port configured in `application.properties`).

## API Usage

The base URL for the API is `http://localhost:8080`.

### Load Management

**1. Create Load**

* **Endpoint:** `POST /load`
* **Description:** Creates a new load. The status defaults to `POSTED`.
* **Request Body:** `LoadDto` (JSON)
    ```json
    {
      "shipperId": "SHIPPER_XYZ",
      "facility": {
        "loadingPoint": "Warehouse A, Pune",
        "unloadingPoint": "Distribution Center B, Mumbai",
        "loadingDate": "2025-05-10T10:00:00.000+00:00",
        "unloadingDate": "2025-05-11T16:30:00.000+00:00"
      },
      "productType": "Electronics",
      "truckType": "20ft Truck",
      "noOfTrucks": 1,
      "weight": 1500.75,
      "comment": "Fragile items, handle with care."
    }
    ```
* **Response:** `201 Created` with `LoadDto` (including generated `id`, `datePosted`, and `status`).
* **Example (`curl`):**
    ```bash
    curl -X POST -H "Content-Type: application/json" \
    -d '{"shipperId": "SHIPPER_XYZ", "facility": {"loadingPoint": "Warehouse A, Pune", "unloadingPoint": "Distribution Center B, Mumbai", "loadingDate": "2025-05-10T10:00:00.000+00:00", "unloadingDate": "2025-05-11T16:30:00.000+00:00"}, "productType": "Electronics", "truckType": "20ft Truck", "noOfTrucks": 1, "weight": 1500.75, "comment": "Fragile items, handle with care."}' \
    http://localhost:8080/load
    ```

**2. Get Loads**

* **Endpoint:** `GET /load`
* **Description:** Fetches a list of loads. Can be filtered by query parameters.
* **Query Parameters (Optional):**
    * `shipperId` (String): Filter loads by shipper ID.
    * `truckType` (String): Filter loads by truck type.
    * *(Add other filters implemented in `LoadService`)*
* **Response:** `200 OK` with a list of `LoadDto`.
* **Example (`curl`):**
    * Get all loads: `curl http://localhost:8080/load`
    * Get loads for a specific shipper: `curl "http://localhost:8080/load?shipperId=SHIPPER_XYZ"`

**3. Get Load by ID**

* **Endpoint:** `GET /load/{loadId}`
* **Description:** Fetches details of a specific load by its UUID.
* **Path Variable:** `loadId` (UUID).
* **Response:** `200 OK` with `LoadDto` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl http://localhost:8080/load/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    ```
    *(Replace with an actual load ID)*

**4. Update Load**

* **Endpoint:** `PUT /load/{loadId}`
* **Description:** Updates details of an existing load.
* **Path Variable:** `loadId` (UUID).
* **Request Body:** `LoadDto` (JSON with fields to update). Note: `id`, `datePosted`, and `status` are generally not updated via this endpoint.
* **Response:** `200 OK` with the updated `LoadDto` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl -X PUT -H "Content-Type: application/json" \
    -d '{"comment": "Updated instructions: Call upon arrival."}' \
    http://localhost:8080/load/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    ```
    *(Replace with an actual load ID)*

**5. Delete Load**

* **Endpoint:** `DELETE /load/{loadId}`
* **Description:** Deletes a specific load. (Consider implications if bookings exist - current guide assumes direct deletion or cancellation based on service implementation).
* **Path Variable:** `loadId` (UUID).
* **Response:** `204 No Content` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl -X DELETE http://localhost:8080/load/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    ```
    *(Replace with an actual load ID)*

---

### Booking Management

**1. Create Booking**

* **Endpoint:** `POST /booking`
* **Description:** Creates a new booking for an existing load. Sets booking status to `PENDING` and updates the associated load's status to `BOOKED`. Cannot be created if the load is `CANCELLED`.
* **Request Body:** `BookingDto` (JSON, requires `loadId`).
    ```json
    {
      "loadId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", // ID of the load to book
      "transporterId": "TRANSPORTER_ABC",
      "proposedRate": 1850.50,
      "comment": "Rate negotiable."
    }
    ```
* **Response:** `201 Created` with `BookingDto` (including generated `id`, `requestedAt`, and `status`).
* **Example (`curl`):**
    ```bash
    curl -X POST -H "Content-Type: application/json" \
    -d '{"loadId": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx", "transporterId": "TRANSPORTER_ABC", "proposedRate": 1850.50, "comment": "Rate negotiable."}' \
    http://localhost:8080/booking
    ```
    *(Replace loadId with an actual, non-cancelled load ID)*

**2. Get Bookings**

* **Endpoint:** `GET /booking`
* **Description:** Fetches a list of bookings. Can be filtered.
* **Query Parameters (Optional):**
    * `shipperId` (String): Filter bookings based on the associated load's shipper ID.
    * `transporterId` (String): Filter bookings by transporter ID.
* **Response:** `200 OK` with a list of `BookingDto`.
* **Example (`curl`):**
    * Get all bookings: `curl http://localhost:8080/booking`
    * Get bookings for a transporter: `curl "http://localhost:8080/booking?transporterId=TRANSPORTER_ABC"`
    * Get bookings for a shipper: `curl "http://localhost:8080/booking?shipperId=SHIPPER_XYZ"`

**3. Get Booking by ID**

* **Endpoint:** `GET /booking/{bookingId}`
* **Description:** Fetches details of a specific booking by its UUID.
* **Path Variable:** `bookingId` (UUID).
* **Response:** `200 OK` with `BookingDto` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl http://localhost:8080/booking/yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy
    ```
    *(Replace with an actual booking ID)*

**4. Update Booking**

* **Endpoint:** `PUT /booking/{bookingId}`
* **Description:** Updates details of an existing booking, primarily used to change the status (e.g., to `ACCEPTED` or `REJECTED`) or update comments/rate.
* **Path Variable:** `bookingId` (UUID).
* **Request Body:** `BookingDto` (JSON with fields to update, e.g., `status`).
    ```json
    {
      "status": "ACCEPTED",
      "comment": "Rate accepted, proceed with pickup."
    }
    ```
* **Response:** `200 OK` with the updated `BookingDto` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl -X PUT -H "Content-Type: application/json" \
    -d '{"status": "ACCEPTED", "comment": "Rate accepted, proceed with pickup."}' \
    http://localhost:8080/booking/yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy
    ```
    *(Replace with an actual booking ID)*

**5. Delete Booking**

* **Endpoint:** `DELETE /booking/{bookingId}`
* **Description:** Deletes a specific booking. According to the rules, this sets the associated Load's status to `CANCELLED`.
* **Path Variable:** `bookingId` (UUID).
* **Response:** `204 No Content` or `404 Not Found`.
* **Example (`curl`):**
    ```bash
    curl -X DELETE http://localhost:8080/booking/yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy
    ```
    *(Replace with an actual booking ID)*

## Assumptions

* **UUID Generation:** Entity IDs (`Load.id`, `Booking.id`) are generated automatically by the persistence layer (JPA/Hibernate).
* **Timestamp Generation:** Timestamps (`Load.datePosted`, `Booking.requestedAt`) are automatically set upon creation using `@CreationTimestamp`.
* **Authentication/Authorization:** No security measures (like login or role checks) are implemented. All API endpoints are publicly accessible.
* **Business Rules:** The implemented rules are based on the assignment specification:
    * Load defaults to `POSTED`.
    * Booking creation changes Load to `BOOKED`.
    * Booking deletion changes Load to `CANCELLED`.
    * Booking cannot be created for a `CANCELLED` Load.
    * Booking defaults to `PENDING`.
* **Timestamp Format:** Input/output timestamps are expected in a standard format parsable by Java/Jackson (e.g., ISO 8601 like `YYYY-MM-DDTHH:mm:ss.sssZ` or milliseconds since epoch).

## Project Structure

The project follows a standard Maven project layout:

.
├── .git/
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml         # Maven build configuration
├── README.md       # This file
├── src/
    ├── main/
        ├── java/
            └── com/liveasy/assignment/
                ├── controller/        # REST API Controllers (LoadController, BookingController)
                ├── dto/               # Data Transfer Objects (LoadDto, BookingDto, FacilityDto)
                ├── entity/            # JPA Entities (Load, Booking, Facility, Enums)
                ├── exception/         # Custom Exceptions & GlobalExceptionHandler
                ├── repository/        # Spring Data JPA Repositories (LoadRepository, BookingRepository)
                ├── service/           # Business Logic Interfaces & Implementations (LoadService, BookingService)
                ├── config/            # Configuration beans (e.g., ModelMapper Bean)
                └── AssignmentApplication.java # Main Spring Boot application class
        └── resources/
            ├── application.properties # Application configuration (database, server port, logging)
            ├── static/              # Static web resources (if any)
            └── templates/           # Template engine files (if any)
    └── test/
        └── java/
            └── com/liveasy/assignment/
                └── AssignmentApplicationTests.java # Basic application context test
└── target/            # Compiled code and packaged JAR (generated by Maven)