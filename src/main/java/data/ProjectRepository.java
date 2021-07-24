package data;

import configuration.constants.AppPrm;

import java.sql.Connection;
import java.sql.SQLException;

public class ProjectRepository {
    private static volatile StringBuilder insertQuery = new StringBuilder();
    private static Connection connection;

    static {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS page";
    private static final String CREATE_TABLE =
            "CREATE TABLE page(" +
                    "id INT NOT NULL AUTO_INCREMENT, " +
                    "path TEXT NOT NULL, " +
                    "code INT NOT NULL, " +
                    "content MEDIUMTEXT NOT NULL, " +
                    "PRIMARY KEY(id))";
    private static final String INSERT_INTO =
            "INSERT INTO page(path, code, content) VALUES";
    private static final String CREATE_INDEX =
            "CREATE INDEX path ON page (path(80))";

    public static void createTable() throws SQLException {
        connection.createStatement().execute(DROP_TABLE);
        connection.createStatement().execute(CREATE_TABLE);
    }

    public static void execInsert(String path, int code, String content)
            throws SQLException {
        String sql = INSERT_INTO + " ('" +
                path + "', " +
                code + ", '" +
                content.replace("'", "''") + "')";

        connection.createStatement().execute(sql);
    }

    public static void execMultiInsert() throws SQLException {
        if (insertQuery.length() > 0) {
            connection.createStatement().execute(INSERT_INTO + insertQuery);
        }
    }

    public static void appendMultiInsert(String path, int code, String content)
            throws SQLException {
        insertQuery.append(insertQuery.length() == 0 ? "" : ", ")
                .append("('").append(path)
                .append("', ").append(code)
                .append(", '").append(content.replace("'", "''"))
                .append("')");

        if (insertQuery.length() > AppPrm.getMAX_ALLOWED_PACKET()) {
            execMultiInsert();
            insertQuery = new StringBuilder();
        }
    }

    public static void indexPath() throws SQLException {
        connection.createStatement().execute(CREATE_INDEX);
    }
}
