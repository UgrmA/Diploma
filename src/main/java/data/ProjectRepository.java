package data;

import configuration.constants.App;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProjectRepository {
    private static volatile StringBuilder insertQuery = new StringBuilder();
    private final Connection connection;

    private final String DROP_TABLE =
            "DROP TABLE IF EXISTS page";
    private final String CREATE_TABLE =
            "CREATE TABLE page(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "path TEXT NOT NULL, " +
                    "code INT NOT NULL, " +
                    "content MEDIUMTEXT NOT NULL, " +
                    "PRIMARY KEY(id))";
    private final String INSERT_INTO =
            "INSERT INTO page(path, code, content) VALUES";
    private final String CREATE_INDEX =
            "CREATE INDEX path ON page (path(80))";

    public ProjectRepository() throws SQLException {
        this.connection = DBConnection.getConnection();
    }

    public void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROP_TABLE);
            statement.execute(CREATE_TABLE);
        }
    }

    public void execMultiInsert() throws SQLException {

        if (insertQuery.length() > 0) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(INSERT_INTO + insertQuery);
            }
        }
    }

    public void appendMultiInsert(String path, int code, String content)
            throws SQLException {

        insertQuery.append(insertQuery.length() == 0 ? "" : ", ")
                .append("('").append(path)
                .append("', ").append(code)
                .append(", '").append(content.replace("'", "''"))
                .append("')");

        if (insertQuery.length() > App.getMAX_ALLOWED_PACKET()) {
            execMultiInsert();
            insertQuery = new StringBuilder();
        }
    }

    public void indexPath() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_INDEX);
        }
    }
}
