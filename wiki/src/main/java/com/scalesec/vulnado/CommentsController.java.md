# Documentation: `CommentsController.java`

## Overview
The `CommentsController` class is a RESTful controller implemented using the Spring Boot framework. It provides endpoints for managing comments, including fetching, creating, and deleting comments. The controller enforces authentication using a token-based mechanism and supports cross-origin requests.

## Features
- **Authentication**: Requires an `x-auth-token` header for all operations.
- **Cross-Origin Resource Sharing (CORS)**: Allows requests from any origin (`*`).
- **Endpoints**:
  - Fetch all comments (`GET /comments`)
  - Create a new comment (`POST /comments`)
  - Delete a comment by ID (`DELETE /comments/{id}`)
- **Error Handling**: Custom exceptions for `BadRequest` and `ServerError` with appropriate HTTP status codes.

---

## Endpoints

### 1. Fetch All Comments
**URL**: `/comments`  
**Method**: `GET`  
**Headers**:  
| Header Name     | Description                     |
|------------------|---------------------------------|
| `x-auth-token`  | Authentication token (required) |

**Response**:  
Returns a list of all comments in JSON format.

**Logic**:
- Validates the `x-auth-token` using the `User.assertAuth` method.
- Fetches all comments using `Comment.fetch_all()`.

---

### 2. Create a New Comment
**URL**: `/comments`  
**Method**: `POST`  
**Headers**:  
| Header Name     | Description                     |
|------------------|---------------------------------|
| `x-auth-token`  | Authentication token (required) |

**Request Body** (JSON):  
| Field Name | Type   | Description                |
|------------|--------|----------------------------|
| `username` | String | Username of the commenter. |
| `body`     | String | Content of the comment.    |

**Response**:  
Returns the created comment in JSON format.

**Logic**:
- Accepts a `CommentRequest` object containing `username` and `body`.
- Creates a new comment using `Comment.create()`.

---

### 3. Delete a Comment
**URL**: `/comments/{id}`  
**Method**: `DELETE`  
**Headers**:  
| Header Name     | Description                     |
|------------------|---------------------------------|
| `x-auth-token`  | Authentication token (required) |

**Path Variables**:  
| Variable Name | Type   | Description          |
|---------------|--------|----------------------|
| `id`          | String | ID of the comment.   |

**Response**:  
Returns a boolean indicating whether the deletion was successful.

**Logic**:
- Deletes the comment with the specified ID using `Comment.delete()`.

---

## Supporting Classes

### 1. `CommentRequest`
A data structure representing the request body for creating a comment.

| Field Name | Type   | Description                |
|------------|--------|----------------------------|
| `username` | String | Username of the commenter. |
| `body`     | String | Content of the comment.    |

Implements `Serializable` for potential serialization needs.

---

### 2. `BadRequest`
A custom exception class for handling bad requests.  
**HTTP Status**: `400 BAD_REQUEST`

| Constructor Parameter | Type   | Description                  |
|------------------------|--------|------------------------------|
| `exception`            | String | Error message for the client.|

---

### 3. `ServerError`
A custom exception class for handling server errors.  
**HTTP Status**: `500 INTERNAL_SERVER_ERROR`

| Constructor Parameter | Type   | Description                  |
|------------------------|--------|------------------------------|
| `exception`            | String | Error message for the client.|

---

## Insights

1. **Authentication**: The `User.assertAuth` method is used to validate the `x-auth-token` against a secret value (`app.secret`). This ensures that only authorized users can access the endpoints.
2. **CORS Support**: The `@CrossOrigin(origins = "*")` annotation allows requests from any origin, which is useful for public APIs but may pose security risks if sensitive data is involved.
3. **Error Handling**: Custom exceptions (`BadRequest` and `ServerError`) provide meaningful HTTP status codes and error messages, improving API usability.
4. **Scalability**: The use of `Comment.fetch_all()`, `Comment.create()`, and `Comment.delete()` suggests that the actual data handling logic is abstracted into the `Comment` class, promoting separation of concerns.
5. **Security Consideration**: The use of a global wildcard (`*`) for CORS and the lack of detailed token validation logic in the provided code may require further scrutiny in production environments.
