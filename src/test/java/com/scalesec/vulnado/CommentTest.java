import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentTest {

    private Connection mockConnection() throws SQLException {
        return mock(Connection.class);
    }

    private PreparedStatement mockPreparedStatement() throws SQLException {
        return mock(PreparedStatement.class);
    }

    private ResultSet mockResultSet() throws SQLException {
        return mock(ResultSet.class);
    }

    @Test
    public void Comment_Create_ShouldReturnComment() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(connection);

        Comment comment = Comment.create("testUser", "testBody");

        assertNotNull(comment, "Comment should not be null");
        assertEquals("testUser", comment.username, "Username should match");
        assertEquals("testBody", comment.body, "Body should match");
        assertNotNull(comment.created_on, "Timestamp should not be null");
    }

    @Test
    public void Comment_Create_ShouldThrowBadRequest() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(connection);

        Exception exception = assertThrows(BadRequest.class, () -> Comment.create("testUser", "testBody"));
        assertEquals("Unable to save comment", exception.getMessage(), "Exception message should match");
    }

    @Test
    public void Comment_FetchAll_ShouldReturnComments() throws SQLException {
        Connection connection = mockConnection();
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mockResultSet();

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("id")).thenReturn("testId");
        when(resultSet.getString("username")).thenReturn("testUser");
        when(resultSet.getString("body")).thenReturn("testBody");
        when(resultSet.getTimestamp("created_on")).thenReturn(new Timestamp(new Date().getTime()));

        Postgres.setConnection(connection);

        List<Comment> comments = Comment.fetch_all();

        assertNotNull(comments, "Comments list should not be null");
        assertEquals(1, comments.size(), "Comments list size should match");
        assertEquals("testUser", comments.get(0).username, "Username should match");
        assertEquals("testBody", comments.get(0).body, "Body should match");
    }

    @Test
    public void Comment_Delete_ShouldReturnTrue() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        Postgres.setConnection(connection);

        Boolean result = Comment.delete("testId");

        assertTrue(result, "Delete should return true");
    }

    @Test
    public void Comment_Delete_ShouldReturnFalse() throws SQLException {
        Connection connection = mockConnection();
        PreparedStatement preparedStatement = mockPreparedStatement();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        Postgres.setConnection(connection);

        Boolean result = Comment.delete("testId");

        assertFalse(result, "Delete should return false");
    }
}
