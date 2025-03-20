# Documentation: `CommentsController.java`

## Overview

The `CommentsController` class is a RESTful controller implemented using the Spring Boot framework. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller enforces authentication via a custom header (`x-auth-token`) and supports cross-origin requests.

## Features

- **Authentication**: Validates requests using a token passed in the `x-auth-token` header.
- **Endpoints**:
  - Fetch all comments (`GET /comments`)
  - Create a new comment (`POST /comments`)
  - Delete a comment by ID (`DELETE /comments/{id}`)
- **Error Handling**: Custom exceptions for `BadRequest` and `ServerError` with appropriate HTTP status codes.
- **Cross-Origin Resource Sharing (CORS)**: Allows requests from any origin.

---

## Class Details

### 1. `CommentsController`

#### Annotations
- `@RestController`: Marks the class as a RESTful controller.
- `@EnableAutoConfiguration`: Enables Spring Boot's auto-configuration feature.
- `@CrossOrigin(origins = "*")`: Allows cross-origin requests from any domain.

#### Fields
| Field Name | Type   | Description                          |
|------------|--------|--------------------------------------|
| `secret`   | String | Application secret for authentication. |

#### Methods

| Method Name       | HTTP Method | Endpoint            | Description                                                                 |
|-------------------|-------------|---------------------|-----------------------------------------------------------------------------|
| `comments`        | `GET`       | `/comments`         | Fetches all comments. Requires `x-auth-token` for authentication.          |
| `createComment`   | `POST`      | `/comments`         | Creates a new comment. Requires `x-auth-token` and a JSON body.            |
| `deleteComment`   | `DELETE`    | `/comments/{id}`    | Deletes a comment by its ID. Requires `x-auth-token` for authentication.   |

#### Method Details

1. **`comments`**
   - **Parameters**:
     - `@RequestHeader(value="x-auth-token") String token`: Authentication token.
   - **Returns**: A list of all comments (`List<Comment>`).
   - **Logic**:
     - Validates the token using `User.assertAuth(secret, token)`.
     - Fetches all comments using `Comment.fetch_all()`.

2. **`createComment`**
   - **Parameters**:
     - `@RequestHeader(value="x-auth-token") String token`: Authentication token.
     - `@RequestBody CommentRequest input`: JSON body containing the comment details.
   - **Returns**: The created comment (`Comment`).
   - **Logic**:
     - Creates a new comment using `Comment.create(input.username, input.body)`.

3. **`deleteComment`**
   - **Parameters**:
     - `@RequestHeader(value="x-auth-token") String token`: Authentication token.
     - `@PathVariable("id") String id`: ID of the comment to delete.
   - **Returns**: A boolean indicating success or failure (`Boolean`).
   - **Logic**:
     - Deletes the comment using `Comment.delete(id)`.

---

### 2. `CommentRequest`

#### Description
A data structure representing the request body for creating a comment.

#### Fields
| Field Name | Type   | Description                  |
|------------|--------|------------------------------|
| `username` | String | The username of the commenter. |
| `body`     | String | The content of the comment.   |

#### Notes
- Implements `Serializable` for potential serialization needs.

---

### 3. `BadRequest`

#### Description
A custom exception representing a `400 Bad Request` error.

#### Annotations
- `@ResponseStatus(HttpStatus.BAD_REQUEST)`: Maps the exception to a `400 Bad Request` HTTP status.

#### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

### 4. `ServerError`

#### Description
A custom exception representing a `500 Internal Server Error`.

#### Annotations
- `@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)`: Maps the exception to a `500 Internal Server Error` HTTP status.

#### Constructor
| Parameter   | Type   | Description                     |
|-------------|--------|---------------------------------|
| `exception` | String | The error message for the exception. |

---

## Insights

1. **Authentication**:
   - The `User.assertAuth(secret, token)` method is used for token validation. This implies a dependency on the `User` class, which is not included in this file.

2. **Comment Management**:
   - The `Comment` class is responsible for fetching, creating, and deleting comments. Its implementation is not provided, but it is expected to handle database interactions or other storage mechanisms.

3. **CORS Configuration**:
   - The `@CrossOrigin(origins = "*")` annotation allows requests from any origin. This is useful for development but may pose security risks in production if not restricted to specific domains.

4. **Error Handling**:
   - Custom exceptions (`BadRequest` and `ServerError`) provide a clear mechanism for handling and responding to errors with appropriate HTTP status codes.

5. **Scalability**:
   - The controller assumes a flat structure for comments. If nested or hierarchical comments are required, additional logic would need to be implemented.

6. **Security**:
   - The use of a secret for token validation suggests a shared secret-based authentication mechanism. Consider using more robust authentication methods (e.g., OAuth2) for production systems.
