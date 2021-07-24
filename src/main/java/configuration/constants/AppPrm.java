package configuration.constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class AppPrm {
    private static final String propertiesPath = Objects.requireNonNull(Thread.currentThread()
            .getContextClassLoader().getResource("app.properties")).getPath();
    private static final Properties properties = new Properties();

    private final String dbUrl;
    private final String userAgent;
    private final String referrer;
    private final String matchContent;
    private final String matchParameters;


    private static int MAX_ALLOWED_PACKET;

    public AppPrm() throws IOException {
        properties.load(new FileInputStream(propertiesPath));

        dbUrl = properties.getProperty("datasource.url");
        userAgent = properties.getProperty("connection.userAgent");
        referrer = properties.getProperty("connection.referrer");
        matchContent = properties.getProperty("match.content");
        matchParameters = properties.getProperty("match.parameters");
        MAX_ALLOWED_PACKET = Integer.parseInt(properties.getProperty("max_allowed_packet"));
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getMatchContent() {
        return matchContent;
    }

    public String getMatchParameters() {
        return matchParameters;
    }

    public static int getMAX_ALLOWED_PACKET() {
        return MAX_ALLOWED_PACKET;
    }
}
