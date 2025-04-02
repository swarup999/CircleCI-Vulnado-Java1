import org.junit.jupiter.api.*;
import org.mockito.*;
import java.sql.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PostgresTest {

    @BeforeEach
    void setUp() {
        // Mocking System.getenv for database connection parameters
        mockStatic(System.class);
        when(System.getenv("PGHOST")).thenReturn("localhost");
        when(System.getenv("PGDATABASE")).thenReturn("testdb");
        when(System.getenv("PGUSER")).thenReturn("testuser");
        when(System.getenv("PGPASSWORD")).thenReturn("testpassword");
    }

    @Test
    void connection_ShouldReturnValidConnection() throws Exception {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            Connection mockConnection = mock(Connection.class);
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            Connection connection = Postgres.connection();

            assertNotNull(connection, "Connection should not be null");
            mockedDriverManager.verify(() -> DriverManager.getConnection(
                    "jdbc:postgresql://localhost/testdb", "testuser", "testpassword"), times(1));
        }
    }

    @Test
    void connection_ShouldHandleException() {
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(new SQLException("Connection error"));

            assertThrows(RuntimeException.class, Postgres::connection, "Should throw RuntimeException on connection error");
        }
    }

    @Test
    void setup_ShouldCreateTablesAndInsertData() throws Exception {
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        try (MockedStatic<Postgres> mockedPostgres = mockStatic(Postgres.class)) {
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);
            when(mockConnection.createStatement()).thenReturn(mockStatement);

            Postgres.setup();

            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS users(user_id VARCHAR (36) PRIMARY KEY, username VARCHAR (50) UNIQUE NOT NULL, password VARCHAR (50) NOT NULL, created_on TIMESTAMP NOT NULL, last_login TIMESTAMP)");
            verify(mockStatement, times(1)).executeUpdate("CREATE TABLE IF NOT EXISTS comments(id VARCHAR (36) PRIMARY KEY, username VARCHAR (36), body VARCHAR (500), created_on TIMESTAMP NOT NULL)");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM users");
            verify(mockStatement, times(1)).executeUpdate("DELETE FROM comments");
            mockedPostgres.verify(() -> Postgres.insertUser("admin", "!!SuperSecretAdmin!!"), times(1));
            mockedPostgres.verify(() -> Postgres.insertUser("alice", "AlicePassword!"), times(1));
            mockedPostgres.verify(() -> Postgres.insertUser("bob", "BobPassword!"), times(1));
            mockedPostgres.verify(() -> Postgres.insertUser("eve", "$EVELknev^l"), times(1));
            mockedPostgres.verify(() -> Postgres.insertUser("rick", "!GetSchwifty!"), times(1));
            mockedPostgres.verify(() -> Postgres.insertComment("rick", "cool dog m8"), times(1));
            mockedPostgres.verify(() -> Postgres.insertComment("alice", "OMG so cute!"), times(1));
        }
    }

    @Test
    void md5_ShouldReturnCorrectHash() {
        String input = "test";
        String expectedHash = "098f6bcd4621d373cade4e832627b4f6";

        String actualHash = Postgres.md5(input);

        assertEquals(expectedHash, actualHash, "MD5 hash should match expected value");
    }

    @Test
    void md5_ShouldHandleEmptyInput() {
        String input = "";
        String expectedHash = "d41d8cd98f00b204e9800998ecf8427e";

        String actualHash = Postgres.md5(input);

        assertEquals(expectedHash, actualHash, "MD5 hash for empty input should match expected value");
    }

    @Test
    void insertUser_ShouldInsertUserIntoDatabase() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try (MockedStatic<Postgres> mockedPostgres = mockStatic(Postgres.class)) {
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            Postgres.insertUser("testuser", "testpassword");

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testuser"));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq(Postgres.md5("testpassword")));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    void insertComment_ShouldInsertCommentIntoDatabase() throws Exception {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        try (MockedStatic<Postgres> mockedPostgres = mockStatic(Postgres.class)) {
            mockedPostgres.when(Postgres::connection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            Postgres.insertComment("testuser", "test comment");

            verify(mockPreparedStatement, times(1)).setString(eq(1), anyString());
            verify(mockPreparedStatement, times(1)).setString(eq(2), eq("testuser"));
            verify(mockPreparedStatement, times(1)).setString(eq(3), eq("test comment"));
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }
}
