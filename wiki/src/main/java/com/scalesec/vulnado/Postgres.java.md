# Documentation: `Postgres.java`

## Overview
The `Postgres` class provides utility methods for interacting with a PostgreSQL database. It includes functionality for establishing a database connection, setting up database schemas, inserting seed data, and hashing passwords using the MD5 algorithm. This class is designed to initialize and manage a database for a user and comment system.

---

## Features
- **Database Connection**: Establishes a connection to a PostgreSQL database using environment variables.
- **Schema Setup**: Creates tables for `users` and `comments` if they do not already exist.
- **Data Seeding**: Inserts predefined user and comment data into the database.
- **Password Hashing**: Hashes passwords using the MD5 algorithm before storing them in the database.
- **Data Insertion**: Provides methods to insert user and comment records into the database.

---

## Class Details

### 1. **Database Connection**
The `connection()` method establishes a connection to the PostgreSQL database using the following environment variables:
- `PGHOST`: Hostname of the PostgreSQL server.
- `PGDATABASE`: Name of the database.
- `PGUSER`: Username for authentication.
- `PGPASSWORD`: Password for authentication.

If the connection fails, the program prints the error and terminates.

---

### 2. **Database Setup**
The `setup()` method performs the following tasks:
1. **Schema Creation**:
   - Creates a `users` table with the following columns:
     - `user_id`: Primary key (UUID).
     - `username`: Unique username.
     - `password`: MD5-hashed password.
     - `created_on`: Timestamp of user creation.
     - `last_login`: Timestamp of the last login.
   - Creates a `comments` table with the following columns:
     - `id`: Primary key (UUID).
     - `username`: Username of the commenter.
     - `body`: Comment text.
     - `created_on`: Timestamp of comment creation.

2. **Data Cleanup**:
   - Deletes all existing records from the `users` and `comments` tables.

3. **Seed Data Insertion**:
   - Inserts predefined users with hashed passwords.
   - Inserts predefined comments.

---

### 3. **Password Hashing**
The `md5(String input)` method hashes a given string using the MD5 algorithm. It:
- Converts the input string into a byte array.
- Computes the MD5 hash.
- Converts the hash into a hexadecimal string.
- Pads the result to ensure it is 32 characters long.

---

### 4. **Data Insertion**
#### `insertUser(String username, String password)`
- Inserts a new user into the `users` table.
- Hashes the password using the `md5()` method.
- Generates a unique `user_id` using `UUID`.

#### `insertComment(String username, String body)`
- Inserts a new comment into the `comments` table.
- Generates a unique `id` for the comment using `UUID`.

---

## Insights

### Security Concerns
1. **MD5 for Password Hashing**:
   - MD5 is considered insecure for password hashing due to its vulnerability to brute-force and collision attacks. A more secure algorithm like bcrypt or Argon2 should be used.

2. **Environment Variables**:
   - The database credentials are retrieved from environment variables, which is a good practice. However, ensure these variables are securely managed and not exposed.

### Database Design
- The `users` table enforces unique usernames, ensuring no duplicate accounts.
- The `comments` table links comments to users via the `username` field, but this lacks referential integrity. Using a foreign key to link `user_id` to `username` would improve data consistency.

### Error Handling
- The code prints stack traces for exceptions but does not provide detailed error messages or recovery mechanisms. Consider implementing a logging framework for better error tracking.

### Scalability
- The `setup()` method deletes all existing data and re-inserts seed data, which is suitable for development but not for production. This behavior should be configurable.

---

## Environment Variables

| Variable      | Description                          |
|---------------|--------------------------------------|
| `PGHOST`      | Hostname of the PostgreSQL server.   |
| `PGDATABASE`  | Name of the database.               |
| `PGUSER`      | Username for database authentication.|
| `PGPASSWORD`  | Password for database authentication.|

---

## Dependencies
- **PostgreSQL JDBC Driver**: The class uses `org.postgresql.Driver` to connect to the database. Ensure the driver is included in the project's dependencies.

---

## Methods Summary

| Method Name         | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| `connection()`      | Establishes a connection to the PostgreSQL database.                       |
| `setup()`           | Sets up the database schema and inserts seed data.                        |
| `md5(String input)` | Hashes a string using the MD5 algorithm.                                   |
| `insertUser()`      | Inserts a new user into the `users` table with a hashed password.          |
| `insertComment()`   | Inserts a new comment into the `comments` table.                          |
