package task2.database.dao;

import task2.database.dto.TagDTO;
import task2.xml.entities.Node;
import task2.xml.entities.Tag;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NodeDAO {
    private final int MAX_BATCH_SIZE = 1000;

    private final Connection connection;

    private final PreparedStatement plainPreparedStatement;
    private final PreparedStatement batchPreparedStatement;
    private final TagDAO tagDAO;

    private int currentBatchSize = 0;

    public NodeDAO(Connection connection) throws SQLException {
        this.connection = connection;
        plainPreparedStatement = connection.prepareCall(
                "INSERT INTO nodes \n"
                        + " (id, lat, lon, _user, uid, visible, version, changeset, _timestamp) \n"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        batchPreparedStatement = connection.prepareCall(
                "INSERT INTO nodes \n"
                        + " (id, lat, lon, _user, uid, visible, version, changeset, _timestamp) \n"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        tagDAO = new TagDAO(connection);
    }

    public void save(Node node) throws SQLException {
        String sql = String.format(
                "INSERT INTO nodes \n"
                        + " (id, lat, lon, _user, uid, visible, version, changeset, _timestamp) \n"
                        + "VALUES (%s, %s, %s, '%s', %s, %s, %s, %s, '%s')",
                node.getId(),
                node.getLat(),
                node.getLon(),
                node.getUser(),
                node.getUid(),
                node.isVisible(),
                node.getVersion(),
                node.getChangeset(),
                node.getTimestamp()
        );
        connection.createStatement().execute(sql);

        for (Tag tag : node.getTag()) {
            TagDTO tagDTO = new TagDTO(
                    tag.getK(),
                    tag.getV(),
                    node.getId().longValue()
            );
            tagDAO.save(tagDTO);
        }

        connection.commit();
    }

    public void savePrepared(Node node) throws SQLException {
        prepareStatement(plainPreparedStatement, node);
        plainPreparedStatement.execute();

        for (Tag tag : node.getTag()) {
            TagDTO tagDTO = new TagDTO(
                    tag.getK(),
                    tag.getV(),
                    node.getId().longValue()
            );
            tagDAO.savePrepared(tagDTO);
        }

        connection.commit();
    }

    public void saveByBatch(Node node) throws SQLException {
        prepareStatement(batchPreparedStatement, node);
        batchPreparedStatement.addBatch();

        currentBatchSize++;
        if (currentBatchSize >= MAX_BATCH_SIZE) {
            flush();
        }

        for (Tag tag : node.getTag()) {
            TagDTO tagDTO = new TagDTO(
                    tag.getK(),
                    tag.getV(),
                    node.getId().longValue()
            );
            tagDAO.saveByBatch(tagDTO);
        }
    }

    public void flush() throws SQLException {
        batchPreparedStatement.executeBatch();
        batchPreparedStatement.clearBatch();
        tagDAO.flush();
        connection.commit();
        currentBatchSize = 0;
    }

    public List<Node> findAll() throws SQLException {
        String sql = "SELECT * FROM nodes \n";
        try (ResultSet rs = connection.createStatement().executeQuery(sql)) {
            List<Node> result = new ArrayList<>();
            while (rs.next()) {
                result.add(parse(rs));
            }
            return result;
        }
    }

    public int update(Node node) throws SQLException {
        String sql = String.format(
                "UPDATE nodes SET \n"
                        + "lat = %s, lon = %s, "
                        + "_user = &s, uid = %s, "
                        + "visible = %s, version = %s, "
                        + "changeset = %s, _timestamp = %s \n"
                        + "WHERE id = %s",
                node.getLat(),
                node.getLon(),
                node.getUser(),
                node.getUid(),
                node.isVisible(),
                node.getVersion(),
                node.getChangeset(),
                node.getTimestamp(),
                node.getId()
        );
        int result = connection.createStatement().executeUpdate(sql);
        connection.commit();
        return result;
    }

    public int delete(Node node) throws SQLException {
        String sql = "DELETE FROM nodes \n"
                + "WHERE id = "
                + node.getId();
        int result = connection.createStatement().executeUpdate(sql);
        connection.commit();
        return result;
    }

    private void prepareStatement(PreparedStatement preparingStatement, Node node) throws SQLException {
        preparingStatement.setLong(1, node.getId().longValue());
        preparingStatement.setDouble(2, node.getLat());
        preparingStatement.setDouble(3, node.getLon());
        preparingStatement.setString(4, node.getUser());
        preparingStatement.setLong(5, node.getUid().longValue());
        if (node.isVisible() != null) {
            preparingStatement.setBoolean(6, node.isVisible());
        } else {
            preparingStatement.setNull(6, Types.NULL);
        }
        preparingStatement.setLong(7, node.getVersion().longValue());
        preparingStatement.setLong(8, node.getChangeset().longValue());
        Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
        preparingStatement.setTimestamp(9, timestamp);
    }

    private Node parse(ResultSet resultSet) throws SQLException {
        Node node = new Node();
        node.setId(BigInteger.valueOf(resultSet.getLong("id")));
        node.setLat(resultSet.getDouble("lat"));
        node.setLon(resultSet.getDouble("lon"));
        node.setUser(resultSet.getString("_user"));
        node.setUid(BigInteger.valueOf(resultSet.getLong("uid")));
        node.setVersion(BigInteger.valueOf(resultSet.getLong("version")));
        node.setChangeset(BigInteger.valueOf(resultSet.getLong("changeset")));
        return node;
    }
}
