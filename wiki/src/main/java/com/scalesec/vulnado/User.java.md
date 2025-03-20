# Documentation: `User` Class

## Overview
The `User` class is a Java implementation that represents a user entity with attributes such as `id`, `username`, and `hashedPassword`. It provides functionality for generating JSON Web Tokens (JWT), validating authentication tokens, and fetching user data from a PostgreSQL database.

---

## Class: `User`

### Attributes
| Attribute       | Type   | Description                                      |
|------------------|--------|--------------------------------------------------|
| `id`            | String | Unique identifier for the user.                  |
| `username`      | String | Username of the user.                            |
| `hashedPassword`| String | Hashed password of the user.                     |

### Constructor
#### `User(String id, String username, String hashedPassword)`
Initializes a new `User` object with the provided `id`, `username`, and `hashedPassword`.

| Parameter         | Type   | Description                                      |
|--------------------|--------|--------------------------------------------------|
| `id`              | String | Unique identifier for the user.                  |
| `username`        | String | Username of the user.                            |
| `hashedPassword`  | String | Hashed password of the user.                     |

---

## Methods

### `String token(String secret)`
Generates a JSON Web Token (JWT) for the user using the provided secret key.

| Parameter | Type   | Description                                      |
|-----------|--------|--------------------------------------------------|
| `secret`  | String | Secret key used to sign the JWT.                 |

**Returns:**  
A signed JWT string containing the `username` as the subject.

---

### `static void assertAuth(String secret, String token)`
Validates the provided JWT token using the secret key. If the token is invalid, an `Unauthorized` exception is thrown.

| Parameter | Type   | Description                                      |
|-----------|--------|--------------------------------------------------|
| `secret`  | String | Secret key used to validate the JWT.             |
| `token`   | String | JWT token to be validated.                       |

**Throws:**  
- `Unauthorized` exception if the token is invalid.

---

### `static User fetch(String un)`
Fetches a user from the PostgreSQL database based on the provided username.

| Parameter | Type   | Description                                      |
|-----------|--------|--------------------------------------------------|
| `un`      | String | Username of the user to fetch.                   |

**Returns:**  
A `User` object if the user is found in the database; otherwise, `null`.

**Database Query:**  
The method executes the following SQL query:
```sql
SELECT * FROM users WHERE username = '<username>' LIMIT 1;
```

**Note:**  
This method uses raw SQL concatenation, which is vulnerable to SQL injection attacks.

---

## Insights

### Security Concerns
1. **SQL Injection Vulnerability:**  
   The `fetch` method constructs SQL queries using string concatenation, making it susceptible to SQL injection attacks. It is recommended to use prepared statements to mitigate this risk.

2. **Hardcoded Secret Key Handling:**  
   The `token` and `assertAuth` methods rely on a secret key passed as a string. Ensure the secret key is securely managed and not hardcoded in the application.

3. **Exception Handling:**  
   - The `assertAuth` method catches exceptions during token validation and rethrows them as an `Unauthorized` exception. Ensure the `Unauthorized` class is properly implemented.
   - The `fetch` method logs exceptions but does not rethrow them, which may lead to silent failures. Consider rethrowing or handling exceptions appropriately.

### Dependencies
- **JWT Library:**  
  The class uses the `io.jsonwebtoken` library for JWT generation and validation.
- **Database Connection:**  
  The `fetch` method relies on a `Postgres.connection()` method, which is assumed to provide a valid database connection. Ensure this method is implemented and properly configured.

### Potential Enhancements
1. **Use Prepared Statements:**  
   Replace raw SQL queries with prepared statements to prevent SQL injection.
2. **Token Expiry:**  
   Add an expiration time to the JWT for enhanced security.
3. **Password Security:**  
   Ensure the `hashedPassword` is securely hashed using a strong algorithm like bcrypt or Argon2.
4. **Logging:**  
   Avoid logging sensitive information such as SQL queries or stack traces in production environments. Use a secure logging framework.

### Assumptions
- The `Postgres.connection()` method is implemented elsewhere in the codebase and provides a valid connection to a PostgreSQL database.
- The `Unauthorized` exception class is defined and used to handle authentication errors.
