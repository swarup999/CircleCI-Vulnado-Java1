# Documentation: `LoginController.java`

## Overview
The `LoginController` class is part of a Spring Boot application and provides functionality for user authentication. It exposes an endpoint for login requests, validates user credentials, and returns a token upon successful authentication. The class also handles unauthorized access scenarios.

---

## File Metadata
- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController**
#### Description
The main controller class that handles login requests. It uses Spring Boot annotations to define RESTful behavior and configuration.

#### Key Features
- **Endpoint**: `/login`
  - **HTTP Method**: POST
  - **Consumes**: JSON
  - **Produces**: JSON
  - **Cross-Origin**: Enabled for all origins (`*`).
- **Authentication Logic**:
  - Fetches user details using `User.fetch(username)`.
  - Compares the hashed password from the request with the stored hashed password.
  - Generates a token using a secret key if authentication is successful.
  - Throws an `Unauthorized` exception if authentication fails.

#### Dependencies
- **Spring Boot**:
  - `@RestController`
  - `@EnableAutoConfiguration`
  - `@CrossOrigin`
  - `@RequestMapping`
  - `@Value`
- **HTTP Status**:
  - `HttpStatus.UNAUTHORIZED`

#### Fields
| Field Name | Type   | Description                          |
|------------|--------|--------------------------------------|
| `secret`   | String | Secret key used for token generation.|

---

### 2. **LoginRequest**
#### Description
A data structure representing the login request payload. It implements `Serializable` for potential serialization needs.

#### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `username` | String | Username provided by the user. |
| `password` | String | Password provided by the user. |

---

### 3. **LoginResponse**
#### Description
A data structure representing the login response payload. It contains the authentication token.

#### Fields
| Field Name | Type   | Description                     |
|------------|--------|---------------------------------|
| `token`    | String | Authentication token.          |

#### Constructor
| Constructor Signature                  | Description                          |
|----------------------------------------|--------------------------------------|
| `LoginResponse(String msg)`            | Initializes the response with a token.|

---

### 4. **Unauthorized**
#### Description
A custom exception class that represents unauthorized access. It is annotated with `@ResponseStatus(HttpStatus.UNAUTHORIZED)` to automatically return a 401 status code when thrown.

#### Constructor
| Constructor Signature                  | Description                          |
|----------------------------------------|--------------------------------------|
| `Unauthorized(String exception)`       | Initializes the exception with a message.|

---

## Insights

### Security Considerations
1. **Hardcoded Secret**: The `secret` field is injected via `@Value("${app.secret}")`. Ensure that the secret is securely stored and not exposed in the codebase or environment variables.
2. **Password Hashing**: The password comparison uses `Postgres.md5`. Verify that MD5 hashing is sufficient for your security requirements, as MD5 is considered weak for cryptographic purposes.
3. **Cross-Origin Requests**: The `@CrossOrigin(origins = "*")` annotation allows requests from all origins. This could expose the endpoint to potential security risks. Consider restricting origins to trusted domains.

### Error Handling
- The `Unauthorized` exception provides a clear mechanism for handling failed authentication attempts. However, ensure that error messages do not leak sensitive information.

### Scalability
- The `User.fetch(username)` method is assumed to retrieve user details from a database. Ensure that this operation is optimized for performance, especially in high-traffic scenarios.

### Serialization
- Both `LoginRequest` and `LoginResponse` implement `Serializable`. This is useful for scenarios where objects need to be serialized, such as caching or distributed systems.

### RESTful Design
- The `/login` endpoint adheres to RESTful principles by using HTTP POST for data submission and returning JSON responses.

---

## Summary Table

| Component         | Type            | Purpose                                   |
|--------------------|-----------------|-------------------------------------------|
| `LoginController` | Controller      | Handles login requests and authentication.|
| `LoginRequest`    | Data Structure  | Represents the login request payload.     |
| `LoginResponse`   | Data Structure  | Represents the login response payload.    |
| `Unauthorized`    | Exception Class | Handles unauthorized access scenarios.    |
