# Documentation: `Comment` Class

## Overview

The `Comment` class is a Java implementation for managing user comments. It provides functionality to create, retrieve, and delete comments from a PostgreSQL database. Each comment is represented by attributes such as `id`, `username`, `body`, and `created_on`. The class interacts with the database using SQL queries and leverages JDBC for database connectivity.

---

## Class Details

### Package
The class is part of the `com.scalesec.vulnado` package.

### Dependencies
The class relies on the following imports:
- `org.apache.catalina.Server` (Unused in the current implementation)
- `java.sql.*` (For database operations)
- `java.util.*` (For utility classes like `Date`, `List`, `ArrayList`, and `UUID`)

---

## Attributes

| Attribute    | Type         | Description                              |
|--------------|--------------|------------------------------------------|
| `id`         | `String`     | Unique identifier for the comment.       |
| `username`   | `String`     | Username of the person who made the comment. |
| `body`       | `String`     | Content of the comment.                  |
| `created_on` | `Timestamp`  | Timestamp indicating when the comment was created. |

---

## Constructor

### `Comment(String id, String username, String body, Timestamp created_on)`
Initializes a new `Comment` object with the provided attributes.

| Parameter     | Type        | Description                              |
|---------------|-------------|------------------------------------------|
| `id`          | `String`    | Unique identifier for the comment.       |
| `username`    | `String`    | Username of the person who made the comment. |
| `body`        | `String`    | Content of the comment.                  |
| `created_on`  | `Timestamp` | Timestamp indicating when the comment was created. |

---

## Methods

### `static Comment create(String username, String body)`
Creates a new comment and saves it to the database.

| Parameter   | Type      | Description                              |
|-------------|-----------|------------------------------------------|
| `username`  | `String`  | Username of the person creating the comment. |
| `body`      | `String`  | Content of the comment.                  |

- Generates a unique `id` using `UUID`.
- Sets the `created_on` timestamp to the current time.
- Calls the private `commit()` method to save the comment to the database.
- Throws a `BadRequest` exception if the comment cannot be saved.
- Throws a `ServerError` exception in case of other errors.

---

### `static List<Comment> fetch_all()`
Retrieves all comments from the database.

| Return Type | Description                              |
|-------------|------------------------------------------|
| `List<Comment>` | A list of all comments stored in the database. |

- Executes a `SELECT * FROM comments` query.
- Maps each row in the result set to a `Comment` object.
- Returns a list of `Comment` objects.

---

### `static Boolean delete(String id)`
Deletes a comment from the database based on its `id`.

| Parameter | Type      | Description                              |
|-----------|-----------|------------------------------------------|
| `id`      | `String`  | Unique identifier of the comment to delete. |

| Return Type | Description                              |
|-------------|------------------------------------------|
| `Boolean`   | `true` if the comment was successfully deleted, `false` otherwise. |

- Executes a `DELETE FROM comments WHERE id = ?` query.
- Uses a prepared statement to prevent SQL injection.

---

### `private Boolean commit() throws SQLException`
Saves the current `Comment` object to the database.

| Return Type | Description                              |
|-------------|------------------------------------------|
| `Boolean`   | `true` if the comment was successfully saved, `false` otherwise. |

- Executes an `INSERT INTO comments` query.
- Uses a prepared statement to insert the comment attributes into the database.

---

## Insights

1. **Database Dependency**: The class relies on a `Postgres.connection()` method to establish a database connection. This method is not defined in the class and is assumed to be part of another utility class.

2. **Error Handling**:
   - The `create` method throws custom exceptions (`BadRequest` and `ServerError`) for specific error scenarios.
   - Other methods use `try-catch` blocks but do not propagate exceptions effectively, which may lead to silent failures.

3. **SQL Injection Prevention**: Prepared statements are used in the `commit` and `delete` methods to prevent SQL injection attacks.

4. **Unused Import**: The `org.apache.catalina.Server` import is not used in the current implementation and can be removed.

5. **Potential Bug in `delete` Method**:
   - The `finally` block always returns `false`, overriding the actual result of the `executeUpdate()` method. This needs to be corrected.

6. **Scalability**:
   - The `fetch_all` method retrieves all comments without any pagination or filtering, which may lead to performance issues with large datasets.

7. **Thread Safety**: The class is not thread-safe as it uses shared database connections without synchronization.

8. **Timestamp Handling**: The `created_on` timestamp is generated using the system's current time, which may lead to inconsistencies in distributed systems.

---

## Potential Enhancements

- **Pagination**: Add support for pagination in the `fetch_all` method to handle large datasets efficiently.
- **Connection Pooling**: Use a connection pool to manage database connections more effectively.
- **Validation**: Add input validation for `username` and `body` to ensure data integrity.
- **Logging**: Replace `System.err.println` with a proper logging framework for better error tracking and debugging.
- **Unit Tests**: Implement unit tests to validate the functionality of each method.
