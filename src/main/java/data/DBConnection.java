package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static configuration.Parameters.*;

public class DBConnection {
    private static final int MAX_ALLOWED_PACKET = 3_000_000;
    private static volatile Connection connection;
    private static volatile StringBuilder insertQuery = new StringBuilder();

    public DBConnection() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql:" + dbUrl + dbName +
                            "?user=" + dbUser + "&password=" + dbPass);
            connection.createStatement().execute("DROP TABLE IF EXISTS page");
            connection.createStatement().execute(
                    "CREATE TABLE page(" +
                            "id INT NOT NULL AUTO_INCREMENT, " +
                            "path TEXT NOT NULL, " +
                            "code INT NOT NULL, " +
                            "content MEDIUMTEXT NOT NULL, " +
                            "PRIMARY KEY(id))"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DBConnection.class) {
                new DBConnection();
            }
        }
        return connection;
    }

    public static void execInsert(String path, int code, String content)
            throws SQLException {
        String sql =
                "INSERT INTO page(path, code, content) VALUES ('" +
                        path + "', " +
                        code + ", '" +
                        content.replace("'", "''") + "')";
        DBConnection.getConnection().createStatement().execute(sql);
    }

    public static void execMultiInsert() throws SQLException {

        String sql =
                "INSERT INTO page(path, code, content) " +
                        "VALUES" + insertQuery;
        DBConnection.getConnection().createStatement().execute(sql);
    }

    public static void appendMultiInsert(String path, int code, String content)
            throws SQLException {

        insertQuery.append(insertQuery.length() == 0 ? "" : ", ")
                .append("('")
                .append(path)
                .append("', ")
                .append(code)
                .append(", '")
                .append(content.replace("'", "''"))
                .append("')");

        if (insertQuery.length() > MAX_ALLOWED_PACKET) {
            DBConnection.execMultiInsert();
            insertQuery = new StringBuilder();
        }
    }

    public static void indexPath() throws SQLException {
        String sql = "CREATE INDEX path ON page (path(80))";
        DBConnection.getConnection().createStatement().execute(sql);
    }
}
