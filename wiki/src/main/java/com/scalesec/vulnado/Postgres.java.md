# Documentation for `Postgres.java`

## Overview
The `Postgres` class provides functionality for interacting with a PostgreSQL database. It includes methods for establishing a database connection, setting up the database schema, inserting seed data, and hashing passwords using the MD5 algorithm. This class is designed to initialize and manage a database for a user and comment system.

---

## File Metadata
- **File Name**: `Postgres.java`
- **Package**: `com.scalesec.vulnado`

---

## Features and Functionality

### 1. **Database Connection**
The `connection()` method establishes a connection to a PostgreSQL database using credentials and connection details provided via environment variables.

#### Environment Variables Used:
| Variable Name | Description |
|---------------|-------------|
| `PGHOST`      | Hostname of the PostgreSQL server |
| `PGDATABASE`  | Name of the database to connect to |
| `PGUSER`      | Username for authentication |
| `PGPASSWORD`  | Password for authentication |

#### Key Points:
- Uses `DriverManager` to establish the connection.
- Requires the PostgreSQL JDBC driver (`org.postgresql.Driver`).
- Prints error details and exits the program if the connection fails.

---

### 2. **Database Setup**
The `setup()` method initializes the database by:
1. Creating tables (`users` and `comments`) if they do not already exist.
2. Cleaning up any existing data in the tables.
3. Inserting seed data for users and comments.

#### Tables Created:
| Table Name | Columns                                                                                     |
|------------|---------------------------------------------------------------------------------------------|
| `users`    | `user_id` (Primary Key), `username` (Unique), `password`, `created_on`, `last_login`        |
| `comments` | `id` (Primary Key), `username`, `body`, `created_on`                                        |

#### Seed Data:
- **Users**: `admin`, `alice`, `bob`, `eve`, `rick` with predefined passwords.
- **Comments**: Comments by `rick` and `alice`.

---

### 3. **Password Hashing**
The `md5()` method generates an MD5 hash for a given input string. This is used to securely store user passwords in the database.

#### Key Points:
- Uses `MessageDigest` to compute the MD5 hash.
- Converts the hash into a hexadecimal string.
- Pads the hash to ensure it is 32 characters long.

---

### 4. **Data Insertion**
#### `insertUser(String username, String password)`
Inserts a new user into the `users` table with the following details:
- `user_id`: A randomly generated UUID.
- `username`: Provided username.
- `password`: MD5 hash of the provided password.
- `created_on`: Current timestamp.

#### `insertComment(String username, String body)`
Inserts a new comment into the `comments` table with the following details:
- `id`: A randomly generated UUID.
- `username`: Provided username.
- `body`: Content of the comment.
- `created_on`: Current timestamp.

---

## Insights

### Security Concerns
1. **MD5 for Password Hashing**:
   - MD5 is considered cryptographically weak and vulnerable to collision attacks. It is recommended to use stronger hashing algorithms like `bcrypt`, `PBKDF2`, or `Argon2` for password storage.

2. **Hardcoded Seed Data**:
   - The seed data includes sensitive information such as the `admin` password. This could be a security risk if the database is exposed.

3. **Environment Variables**:
   - The reliance on environment variables for database credentials is a good practice, but ensure these variables are securely managed and not exposed.

### Database Design
- The `users` table includes a `last_login` column, but it is not utilized in the current implementation. This could be used for tracking user activity in the future.
- The `comments` table does not enforce foreign key constraints between `username` in `comments` and `username` in `users`. This could lead to data integrity issues.

### Error Handling
- The error handling in the `connection()` and data insertion methods prints stack traces but does not provide robust recovery mechanisms. Consider implementing logging and retry mechanisms.

### Scalability
- The current implementation clears all data during setup, which may not be suitable for production environments. A more sophisticated migration strategy should be considered.

---

## Dependencies
- **PostgreSQL JDBC Driver**: Required for database connectivity (`org.postgresql.Driver`).
- **Java Standard Libraries**:
  - `java.sql`: For database operations.
  - `java.security`: For password hashing.
  - `java.util.UUID`: For generating unique identifiers.

---

## Usage
1. Set the required environment variables (`PGHOST`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`).
2. Call the `setup()` method to initialize the database.
3. Use `insertUser()` and `insertComment()` methods to add data programmatically.

---
