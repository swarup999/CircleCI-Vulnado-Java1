# Documentation: `User` Class

## Overview

The `User` class is part of the `com.scalesec.vulnado` package and provides functionality for user management, including token generation, authentication, and database retrieval. It interacts with a PostgreSQL database to fetch user details and uses JSON Web Tokens (JWT) for authentication.

---

## Class: `User`

### Fields

| Field Name       | Type     | Description                                      |
|------------------|----------|--------------------------------------------------|
| `id`             | `String` | Unique identifier for the user.                 |
| `username`       | `String` | Username of the user.                           |
| `hashedPassword` | `String` | Hashed password of the user.                    |

---

### Constructor

#### `User(String id, String username, String hashedPassword)`

Initializes a new `User` object with the provided `id`, `username`, and `hashedPassword`.

| Parameter         | Type     | Description                                      |
|-------------------|----------|--------------------------------------------------|
| `id`              | `String` | Unique identifier for the user.                 |
| `username`        | `String` | Username of the user.                           |
| `hashedPassword`  | `String` | Hashed password of the user.                    |

---

### Methods

#### `String token(String secret)`

Generates a JSON Web Token (JWT) for the user using the provided secret key.

| Parameter | Type     | Description                                      |
|-----------|----------|--------------------------------------------------|
| `secret`  | `String` | Secret key used to sign the JWT.                 |

**Returns:**  
A signed JWT as a `String`.

---

#### `static void assertAuth(String secret, String token)`

Validates a given JWT token using the provided secret key. If the token is invalid, an `Unauthorized` exception is thrown.

| Parameter | Type     | Description                                      |
|-----------|----------|--------------------------------------------------|
| `secret`  | `String` | Secret key used to validate the JWT.             |
| `token`   | `String` | JWT token to be validated.                       |

**Throws:**  
- `Unauthorized` if the token is invalid.

---

#### `static User fetch(String un)`

Fetches a user from the PostgreSQL database based on the provided username. If no user is found, it returns `null`.

| Parameter | Type     | Description                                      |
|-----------|----------|--------------------------------------------------|
| `un`      | `String` | Username of the user to fetch.                   |

**Returns:**  
A `User` object if the user is found, otherwise `null`.

---

## Insights

### Security Concerns
1. **SQL Injection Vulnerability**:  
   The `fetch` method constructs SQL queries using string concatenation, which makes it vulnerable to SQL injection attacks. Use prepared statements to mitigate this risk.

2. **Weak Secret Management**:  
   The `token` and `assertAuth` methods rely on a secret key passed as a string. Ensure the secret is securely stored and managed (e.g., using environment variables or a secure vault).

3. **Exception Handling**:  
   The `assertAuth` method catches exceptions but rethrows them as `Unauthorized`. Ensure sensitive information is not leaked in the exception message.

4. **Password Storage**:  
   The `hashedPassword` field suggests that passwords are hashed. Ensure a strong hashing algorithm (e.g., bcrypt, Argon2) is used.

---

### Dependencies
- **JWT Library**:  
  The class uses the `io.jsonwebtoken` library for JWT generation and validation.
  
- **Database Connection**:  
  The `fetch` method relies on a `Postgres.connection()` method, which is assumed to provide a valid database connection.

---

### Potential Enhancements
1. **Use Prepared Statements**:  
   Replace the raw SQL query in the `fetch` method with a prepared statement to prevent SQL injection.

2. **Token Expiry**:  
   Add an expiration time to the JWT for enhanced security.

3. **Error Logging**:  
   Improve error logging to avoid exposing sensitive information while maintaining sufficient detail for debugging.

4. **Input Validation**:  
   Validate the `username` input in the `fetch` method to ensure it meets expected criteria.

---

### Example Usage

#### Creating a User
```java
User user = new User("1", "john_doe", "hashed_password");
```

#### Generating a Token
```java
String secret = "my_secret_key";
String token = user.token(secret);
```

#### Validating a Token
```java
try {
    User.assertAuth(secret, token);
    System.out.println("Token is valid.");
} catch (Unauthorized e) {
    System.out.println("Invalid token: " + e.getMessage());
}
```

#### Fetching a User from the Database
```java
User fetchedUser = User.fetch("john_doe");
if (fetchedUser != null) {
    System.out.println("User found: " + fetchedUser.username);
} else {
    System.out.println("User not found.");
}
```
