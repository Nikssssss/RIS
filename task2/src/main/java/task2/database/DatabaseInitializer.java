package task2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseInitializer {
    private static final String URL = "jdbc:postgresql://localhost:5432/ris";
    private static final String USER = "ris";
    private static final String PASSWORD = "ris";

    private static Connection connection;

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false);
        }
        return connection;
    }

    public static void initialize() throws SQLException {
        getConnection().createStatement().execute("DROP TABLE IF EXISTS tags CASCADE");
        getConnection().createStatement().execute("DROP TABLE IF EXISTS nodes CASCADE");

        getConnection().createStatement().execute(
                "CREATE TABLE nodes (\n"
                        + "    id BIGINT PRIMARY KEY,\n"
                        + "    lat DECIMAL,\n"
                        + "    lon DECIMAL,\n"
                        + "    _user VARCHAR(2000),\n"
                        + "    uid BIGINT,\n"
                        + "    visible BOOLEAN,\n"
                        + "    version BIGINT,\n"
                        + "    changeset BIGINT,\n"
                        + "    _timestamp TIMESTAMP\n"
                        + ");"
        );

        getConnection().createStatement().execute(
                "CREATE TABLE tags(\n"
                        + "    node_id BIGINT NOT NULL REFERENCES nodes(id),\n"
                        + "    key VARCHAR(256) NOT NULL,\n"
                        + "    value VARCHAR(256) NOT NULL,\n"
                        + "    PRIMARY KEY (node_id, key)\n"
                        + ");"
        );

        getConnection().commit();
    }

    public static void closeConnection() throws SQLException {
        getConnection().close();
    }
}
