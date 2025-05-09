import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    // Mocking the Postgres connection
    private Connection mockConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(Postgres.connection()).thenReturn(connection);
        return connection;
    }

    // Mocking the PreparedStatement
    private PreparedStatement mockPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        return preparedStatement;
    }

    // Test for creating a comment successfully
    @Test
    public void create_ShouldReturnComment_WhenCommitSucceeds() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement(connection);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Comment comment = Comment.create("user1", "This is a comment");

        assertNotNull(comment, "Comment should not be null");
        assertEquals("user1", comment.username, "Username should match");
        assertEquals("This is a comment", comment.body, "Body should match");
    }

    // Test for creating a comment with commit failure
    @Test
    public void create_ShouldThrowBadRequest_WhenCommitFails() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement(connection);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        assertThrows(BadRequest.class, () -> Comment.create("user1", "This is a comment"), "Should throw BadRequest");
    }

    // Test for fetching all comments
    @Test
    public void fetchAll_ShouldReturnListOfComments() throws SQLException {
        Connection connection = mockConnection();
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("1");
        when(resultSet.getString("username")).thenReturn("user1");
        when(resultSet.getString("body")).thenReturn("This is a comment");
        when(resultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = Comment.fetch_all();

        assertEquals(1, comments.size(), "Should return one comment");
        assertEquals("user1", comments.get(0).username, "Username should match");
    }

    // Test for deleting a comment successfully
    @Test
    public void delete_ShouldReturnTrue_WhenCommentDeleted() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement(connection);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = Comment.delete("1");

        assertTrue(result, "Should return true when comment is deleted");
    }

    // Test for deleting a comment with failure
    @Test
    public void delete_ShouldReturnFalse_WhenCommentNotDeleted() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement(connection);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = Comment.delete("1");

        assertFalse(result, "Should return false when comment is not deleted");
    }
}
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentTest {

    // Helper method to create a mock connection
    private Connection createMockConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(Postgres.connection()).thenReturn(mockConnection);
        return mockConnection;
    }

    // Helper method to create a mock prepared statement
    private PreparedStatement createMockPreparedStatement(Connection mockConnection) throws SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        return mockPreparedStatement;
    }

    @Test
    public void create_ShouldReturnComment_WhenCommitSucceeds() throws SQLException {
        Connection mockConnection = createMockConnection();
        PreparedStatement mockPreparedStatement = createMockPreparedStatement(mockConnection);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Comment comment = Comment.create("user1", "This is a comment");

        assertNotNull(comment, "Comment should not be null");
        assertEquals("user1", comment.username, "Username should match");
        assertEquals("This is a comment", comment.body, "Body should match");
    }

    @Test
    public void create_ShouldThrowServerError_WhenCommitFails() throws SQLException {
        Connection mockConnection = createMockConnection();
        PreparedStatement mockPreparedStatement = createMockPreparedStatement(mockConnection);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        assertThrows(ServerError.class, () -> Comment.create("user1", "This is a comment"), "Should throw ServerError");
    }

    @Test
    public void fetch_all_ShouldReturnCommentsList() throws SQLException {
        Connection mockConnection = createMockConnection();
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn("1");
        when(mockResultSet.getString("username")).thenReturn("user1");
        when(mockResultSet.getString("body")).thenReturn("This is a comment");
        when(mockResultSet.getTimestamp("created_on")).thenReturn(new Timestamp(System.currentTimeMillis()));

        List<Comment> comments = Comment.fetch_all();

        assertEquals(1, comments.size(), "Should return one comment");
        assertEquals("user1", comments.get(0).username, "Username should match");
        assertEquals("This is a comment", comments.get(0).body, "Body should match");
    }

    @Test
    public void delete_ShouldReturnTrue_WhenCommentDeleted() throws SQLException {
        Connection mockConnection = createMockConnection();
        PreparedStatement mockPreparedStatement = createMockPreparedStatement(mockConnection);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Boolean result = Comment.delete("1");

        assertTrue(result, "Should return true when comment is deleted");
    }

    @Test
    public void delete_ShouldReturnFalse_WhenCommentNotDeleted() throws SQLException {
        Connection mockConnection = createMockConnection();
        PreparedStatement mockPreparedStatement = createMockPreparedStatement(mockConnection);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        Boolean result = Comment.delete("1");

        assertFalse(result, "Should return false when comment is not deleted");
    }
}
