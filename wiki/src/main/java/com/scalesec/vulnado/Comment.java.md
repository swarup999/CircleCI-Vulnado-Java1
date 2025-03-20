# Documentation: `Comment` Class

## Overview

The `Comment` class is a Java implementation for managing user comments. It provides functionality to create, retrieve, and delete comments from a PostgreSQL database. Each comment is uniquely identified by a UUID and includes metadata such as the username, body, and timestamp of creation.

---

## Class Details

### Package
`com.scalesec.vulnado`

### Dependencies
- **Java Standard Library**:
  - `java.sql.*` (for database operations)
  - `java.util.*` (for utility classes like `Date`, `List`, `ArrayList`, and `UUID`)
- **External Libraries**:
  - `org.apache.catalina.Server` (imported but unused in the code)

---

## Attributes

| Attribute Name | Type           | Description                              |
|----------------|----------------|------------------------------------------|
| `id`           | `String`       | Unique identifier for the comment (UUID).|
| `username`     | `String`       | Username of the comment's author.        |
| `body`         | `String`       | Content of the comment.                  |
| `created_on`   | `Timestamp`    | Timestamp indicating when the comment was created. |

---

## Constructor

### `Comment(String id, String username, String body, Timestamp created_on)`
Initializes a new `Comment` object with the provided attributes.

| Parameter      | Type        | Description                              |
|----------------|-------------|------------------------------------------|
| `id`           | `String`    | Unique identifier for the comment.       |
| `username`     | `String`    | Username of the comment's author.        |
| `body`         | `String`    | Content of the comment.                  |
| `created_on`   | `Timestamp` | Timestamp indicating when the comment was created. |

---

## Methods

### `static Comment create(String username, String body)`
Creates a new comment and saves it to the database.

| Parameter      | Type        | Description                              |
|----------------|-------------|------------------------------------------|
| `username`     | `String`    | Username of the comment's author.        |
| `body`         | `String`    | Content of the comment.                  |

**Returns**: A `Comment` object if successfully created. Throws `BadRequest` or `ServerError` exceptions on failure.

---

### `static List<Comment> fetch_all()`
Fetches all comments from the database.

**Returns**: A `List<Comment>` containing all comments in the database. If an error occurs, an empty list is returned.

---

### `static Boolean delete(String id)`
Deletes a comment from the database by its unique identifier.

| Parameter      | Type        | Description                              |
|----------------|-------------|------------------------------------------|
| `id`           | `String`    | Unique identifier of the comment to delete. |

**Returns**: `true` if the comment was successfully deleted, otherwise `false`.

---

### `private Boolean commit()`
Commits the current `Comment` object to the database by inserting it into the `comments` table.

**Returns**: `true` if the comment was successfully saved, otherwise `false`.

**Throws**: `SQLException` if a database error occurs.

---

## Insights

1. **Database Dependency**: 
   - The class relies on a PostgreSQL database connection provided by the `Postgres.connection()` method. This method is assumed to be implemented elsewhere in the project.

2. **Error Handling**:
   - The `create` method throws custom exceptions (`BadRequest` and `ServerError`) to handle errors during comment creation.
   - Other methods use `try-catch` blocks but do not propagate exceptions, which may lead to silent failures.

3. **SQL Injection Risk**:
   - The `fetch_all` method uses raw SQL queries, which could be vulnerable to SQL injection if user input is incorporated in future modifications. Prepared statements should be used consistently.

4. **Thread Safety**:
   - The class does not explicitly handle thread safety. Concurrent access to shared resources (e.g., database connections) may lead to issues in multi-threaded environments.

5. **Unused Import**:
   - The `org.apache.catalina.Server` import is unused and can be removed to improve code clarity.

6. **UUID for Unique Identification**:
   - The use of `UUID` ensures that each comment has a globally unique identifier, reducing the risk of ID collisions.

7. **Potential Bug in `delete` Method**:
   - The `delete` method always returns `false` in the `finally` block, even if the deletion is successful. This should be corrected to return the actual result of the operation.

8. **Database Schema Assumptions**:
   - The class assumes the existence of a `comments` table with the following columns:
     - `id` (String)
     - `username` (String)
     - `body` (String)
     - `created_on` (Timestamp)

---

## Example Usage

### Creating a Comment
```java
Comment newComment = Comment.create("john_doe", "This is a sample comment.");
```

### Fetching All Comments
```java
List<Comment> comments = Comment.fetch_all();
for (Comment comment : comments) {
    System.out.println(comment.body);
}
```

### Deleting a Comment
```java
Boolean isDeleted = Comment.delete("some-uuid");
if (isDeleted) {
    System.out.println("Comment deleted successfully.");
} else {
    System.out.println("Failed to delete comment.");
}
```
