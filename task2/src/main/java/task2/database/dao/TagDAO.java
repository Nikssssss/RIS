package task2.database.dao;

import task2.database.DatabaseInitializer;
import task2.database.dto.TagDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private final int MAX_BATCH_SIZE = 1000;

    private final Connection connection;

    private final PreparedStatement plainPreparedStatement;
    private final PreparedStatement batchPreparedStatement;

    private int currentBatchSize = 0;

    public TagDAO(Connection connection) throws SQLException {
        this.connection = connection;
        plainPreparedStatement = DatabaseInitializer.getConnection().prepareCall(
                "INSERT INTO tags"
                        + "(key, value, node_id)"
                        + "VALUES (?, ?, ?)"
        );
        batchPreparedStatement = DatabaseInitializer.getConnection().prepareCall(
                "INSERT INTO tags"
                        + "(key, value, node_id)"
                        + "VALUES (?, ?, ?)"
        );
    }

    public void save(TagDTO tag) throws SQLException {
        String sql = String.format(
                "INSERT INTO tags "
                        + "(key, value, node_id) "
                        + "VALUES ('%s', '%s', %s)",
                tag.getKey(),
                tag.getValue(),
                tag.getNodeId()
        );
        connection.createStatement().execute(sql);
    }

    public void savePrepared(TagDTO tag) throws SQLException {
        prepareStatement(plainPreparedStatement, tag);
        plainPreparedStatement.execute();
    }

    public void saveByBatch(TagDTO tag) throws SQLException {
        prepareStatement(batchPreparedStatement, tag);
        batchPreparedStatement.addBatch();

        currentBatchSize++;
        if (currentBatchSize >= MAX_BATCH_SIZE) {
            flush();
        }
    }

    public void flush() throws SQLException {
        batchPreparedStatement.executeBatch();
        batchPreparedStatement.clearBatch();
        currentBatchSize = 0;
    }

    public List<TagDTO> findAll() throws SQLException {
        String sql = "SELECT * FROM tags \n";
        try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
            List<TagDTO> result = new ArrayList<>();
            while (rs.next()) {
                result.add(parse(rs));
            }
            return result;
        }
    }

    public int update(TagDTO tag) throws SQLException {
        String sql = String.format(
                "UPDATE tags SET"
                        + "value = %s"
                        + "WHERE key = %s AND node_id = %s",
                tag.getValue(),
                tag.getKey(),
                tag.getNodeId()
        );
        int result = connection.createStatement().executeUpdate(sql);
        connection.commit();
        return result;
    }

    public int delete(TagDTO tag) throws SQLException {
        String sql = "DELETE FROM nodes \n"
                + "WHERE key = " + tag.getKey()
                + " AND node_id = " + tag.getNodeId();
        int result = connection.createStatement().executeUpdate(sql);
        connection.commit();
        return result;
    }

    private void prepareStatement(PreparedStatement preparedStatement, TagDTO tag) throws SQLException {
        preparedStatement.setString(1, tag.getKey());
        preparedStatement.setString(2, tag.getValue());
        preparedStatement.setLong(3, tag.getNodeId());
    }

    private TagDTO parse(ResultSet resultSet) throws SQLException {
        return new TagDTO(
                resultSet.getString("key"),
                resultSet.getString("value"),
                resultSet.getLong("node_id")
        );
    }
}
