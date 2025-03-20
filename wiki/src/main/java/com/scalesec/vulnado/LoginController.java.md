# Documentation: `LoginController.java`

## Overview
The `LoginController` class is a RESTful controller implemented using the Spring Boot framework. It provides an endpoint for user authentication by validating login credentials and generating a token upon successful authentication. The controller also handles unauthorized access by throwing a custom exception.

---

## File Metadata
- **File Name**: `LoginController.java`

---

## Components

### 1. **LoginController Class**
The main controller class that handles the `/login` endpoint.

#### **Attributes**
| Attribute       | Type   | Description                                                                 |
|------------------|--------|-----------------------------------------------------------------------------|
| `secret`         | String | A secret value injected from the application properties (`app.secret`).    |

#### **Methods**
| Method Signature                                                                 | Description                                                                                     |
|----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `LoginResponse login(@RequestBody LoginRequest input)`                           | Handles POST requests to the `/login` endpoint. Validates user credentials and returns a token. |

- **Endpoint Details**:
  - **Path**: `/login`
  - **HTTP Method**: `POST`
  - **Consumes**: `application/json`
  - **Produces**: `application/json`
  - **CORS**: Allows requests from all origins (`@CrossOrigin(origins = "*")`).

- **Logic**:
  1. Fetches the user details using the `User.fetch()` method based on the provided username.
  2. Compares the hashed version of the input password (using `Postgres.md5()`) with the stored hashed password.
  3. If the credentials match, generates a token using the `user.token(secret)` method and returns it in a `LoginResponse` object.
  4. If the credentials do not match, throws an `Unauthorized` exception.

---

### 2. **LoginRequest Class**
A data structure representing the request body for the `/login` endpoint.

#### **Attributes**
| Attribute   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `username`  | String | The username of the user.       |
| `password`  | String | The password of the user.       |

---

### 3. **LoginResponse Class**
A data structure representing the response body for the `/login` endpoint.

#### **Attributes**
| Attribute   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `token`     | String | The authentication token.       |

#### **Constructor**
| Constructor Signature                     | Description                                      |
|-------------------------------------------|--------------------------------------------------|
| `LoginResponse(String msg)`               | Initializes the `token` attribute with the given value. |

---

### 4. **Unauthorized Class**
A custom exception class used to handle unauthorized access.

#### **Annotations**
| Annotation                  | Description                                                                 |
|-----------------------------|-----------------------------------------------------------------------------|
| `@ResponseStatus(HttpStatus.UNAUTHORIZED)` | Maps the exception to an HTTP 401 Unauthorized response.             |

#### **Constructor**
| Constructor Signature                     | Description                                      |
|-------------------------------------------|--------------------------------------------------|
| `Unauthorized(String exception)`          | Initializes the exception with a custom message. |

---

## Insights

1. **Security Considerations**:
   - The `secret` value is injected from the application properties, which should be securely stored and managed.
   - The use of `@CrossOrigin(origins = "*")` allows requests from all origins, which may expose the endpoint to Cross-Origin Resource Sharing (CORS) vulnerabilities. Consider restricting origins to trusted domains.
   - Passwords are hashed using `Postgres.md5()`. Ensure that the hashing algorithm is secure and up-to-date with modern cryptographic standards.

2. **Error Handling**:
   - Unauthorized access is handled gracefully by throwing a custom `Unauthorized` exception, which maps to an HTTP 401 response.

3. **Scalability**:
   - The design is modular, with separate classes for request and response objects, making it easier to extend or modify the functionality.

4. **Dependencies**:
   - The code relies on external classes/methods such as `User.fetch()` and `Postgres.md5()`. Ensure these are implemented securely and efficiently.

5. **Serialization**:
   - Both `LoginRequest` and `LoginResponse` implement `Serializable`, which is useful for object serialization, though it may not be strictly necessary in this context.

6. **Token Generation**:
   - The `user.token(secret)` method is used to generate the token. Ensure that the token generation logic is robust and follows best practices for secure token creation (e.g., JWT).

---
